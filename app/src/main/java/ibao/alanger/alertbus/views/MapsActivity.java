package ibao.alanger.alertbus.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.services.servicioDisponible;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.CancelableCallback {

    SharedPreferences pref;

    private GoogleMap mMap;
    private Context ctx;
    LocationManager locationManager;
    private Marker marker;
    boolean creadoMap = false, mapaCreado = false, primeraUbicacion = false;

    float bearing;
    String tk;
    final Handler handler = new Handler();

    ListView lv;


    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_maps);
        ctx = this;

        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        pref = getApplicationContext().getSharedPreferences("hello", MODE_PRIVATE);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            showGPSDisabledAlert();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Obtener drawer



    }


    
    private void showGPSDisabledAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("EL GPS está deshabilitado, ¿Desea habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Habilitar GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        lat = -18.0194071;
        lng = -70.2851854;
        bearing = 0.0f;
        posicionarMarker();

        VerificandoPermiso();


    }

    //validando permisos de acceso a mylocation
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    VerificandoPermiso();
                } else {
                    finish();

                }
                return;
            }

        }
    }

    private void posicionarMarker() {
    //Toast.makeText(ctx,""+lat+lng,Toast.LENGTH_SHORT).show();
        if (!mapaCreado) {
            mapaCreado = true;
            LatLng posicion = new LatLng(lat, lng);

            if (marker == null) {
                try {
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(posicion)
                            .title(""+lat+","+lng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bus3)));
                } catch (Exception e) {

                }
            } else {
                try {
                    marker.setPosition(posicion);
                } catch (Exception e) {
                    //algo salio mal
                }
            }

            if (creadoMap) {
                try {
                    float myZoom = mMap.getCameraPosition().zoom;

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(myZoom).bearing(bearing).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.animateCamera(cameraUpdate, 500, this);
                } catch (Exception e) {
                    //algo salio mal
                }

            } else {
                try {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(posicion).zoom(17.0f).bearing(bearing).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.animateCamera(cameraUpdate, 1, this);
                    creadoMap = true;
                } catch (Exception e) {
                    //algo salio mal
                }
            }
        }
    }

    private void VerificandoPermiso() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //si el permiso no está habilitado, solicitamos el permiso
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        runnable.run();
    }

    Runnable runnableMap = new Runnable() {
        @Override
        public void run() {
            lat = servicioDisponible.lat;
            lng = servicioDisponible.lng;
            bearing = servicioDisponible.bearing;

            posicionarMarker();

            handler.postDelayed(runnableMap, 1000);
        }
    };


    double lat, lng;

    Runnable runnable = new Runnable() {
        public void run() {
            if(!primeraUbicacion) {
                try {
                    Location loc = mMap.getMyLocation();
                    if(loc!=null) {
                        lat = loc.getLatitude();
                        lng = loc.getLongitude();
                        bearing = loc.getBearing();


                        primeraUbicacion = true;
                        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(false);

                        posicionarMarker();


                        Intent sv = new Intent(ctx, servicioDisponible.class);
                        sv.putExtra("lat",lat);
                        sv.putExtra("lng",lng);
                        startService(sv);

                        runnableMap.run();
                    }
                }catch (Exception e){
                    //algo salio mal
                }

                handler.postDelayed(runnable, 100);
            }else{
                handler.removeCallbacks(runnable);
            }
        }
    };

    @Override
    public void onFinish() {
        mapaCreado = false;
    }

    @Override
    public void onCancel() {
        mapaCreado = false;
    }

}
