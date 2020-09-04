package ibao.alertbus.ecosac.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.widget.Toast;

import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.models.dao.TrackingDAO;
import ibao.alertbus.ecosac.views.MapsActivity;

/**
 * Created by Administrador on 28/08/2017.
 */
public class LocationService extends Service {

    final Handler handler = new Handler();
    Notification notification;

    Context ctx;


    public  static boolean isEnable = false;

    static String TAG = LocationService.class.getSimpleName();


    static public double lat = -8.1395615, lng = -79.0386577;
    static public float bearing, speed;
    static public Location location;
    static public MyLocationListener mlocListener;

    LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();


    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    String CHANNEL_NOTIFICATION = "recorrido";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Activacion de Servicios";
            String description = "Servicios Sincronizacióadminn";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_NOTIFICATION, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent i, int flags, int startId) {

        isEnable = true;

        ctx = this;

        //handler.removeCallbacks(runnable);
        Bundle extras = i.getExtras();
        if (extras != null) {
            lat = extras.getDouble("lat");
            lng = extras.getDouble("lng");
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        String bestProvider = locationManager.getBestProvider(criteria, true);

        int time = 1000*20;// 180 sec

        mlocListener = new MyLocationListener();
        mlocListener.setMainActivity(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }

       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, 0, mlocListener);

        /*
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
*/


        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {
            // First get location from Network Provider
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(bestProvider, time, 0, mlocListener);
                Log.d("Network", "Network");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                    }
                }
            }
            //get the location by gps
            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(bestProvider, time, 0, mlocListener);
                   // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, 0, mlocListener);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    }
                }
            }
        }

        Intent intent = new Intent(this, MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher_round, "ver recorrido", pendingIntent).build();

        //Notification
        createNotificationChannel();
        notification = new NotificationCompat.Builder(this, CHANNEL_NOTIFICATION)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("En camino")
                .setContentTitle("Alert Bus")
                .addAction(action)
                .build();


        startForeground(1, notification);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        locationManager.removeUpdates(mlocListener);
        Log.d(TAG,"onDestroy");

        isEnable=false;
        stopForeground(true);
        super.onDestroy();
    }


    /////////////gps/////////////////////
    public class MyLocationListener implements LocationListener {
        LocationService LocationService;

        public LocationService getMainActivity() {
            return LocationService;
        }

        public void setMainActivity(LocationService mainActivity) {
            this.LocationService = mainActivity;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                try {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    bearing = location.getBearing();
                    speed = location.getSpeed();
                    Log.d("hola", location.toString() + " spedd :" + location.getSpeed());
                    new TrackingDAO(ctx).saveNewLocation(String.valueOf(lat), String.valueOf(lng), String.valueOf(bearing), String.valueOf(speed));

                } catch (Exception e) {
                    Toast.makeText(ctx,"error de localizacion",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {


        }

        @Override
        public void onProviderEnabled(String provider) {


        }

        @Override
        public void onProviderDisabled(String provider) {


        }
    }

}
