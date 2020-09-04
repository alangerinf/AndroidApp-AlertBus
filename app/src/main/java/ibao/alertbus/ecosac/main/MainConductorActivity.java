package ibao.alertbus.ecosac.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ibao.alertbus.ecosac.BuildConfig;
import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.helpers.adapters.RViewAdapterListViajesConductor;
import ibao.alertbus.ecosac.models.dao.LoginDataDAO;
import ibao.alertbus.ecosac.models.dao.ViajeDAO;
import ibao.alertbus.ecosac.models.vo.LoginDataVO;
import ibao.alertbus.ecosac.models.vo.ViajeVO;
import ibao.alertbus.ecosac.services.AutoArranqueBroadcast;
import ibao.alertbus.ecosac.services.SearchChangesViajesService;
import ibao.alertbus.ecosac.services.SearchViajesService;
import ibao.alertbus.ecosac.services.UploadService;
import ibao.alertbus.ecosac.views.ActivityPreloader;

public class MainConductorActivity extends AppCompatActivity {


    private static final int REQUEST_PERMISION_CAMERA = 2;
    private static PageViewModelViajesActuales liveDataViajes;

    private static Context ctx;
    private static RecyclerView rViewViajes;
    public static RViewAdapterListViajesConductor rViewAdapterListViajesConductor;
  //  private static Handler handler;

    private static TextView tViewSinViajes;

    private static String TAG = MainConductorActivity.class.getSimpleName();

    private static int REQUEST_PERMISION_GPS =555;

    IntentIntegrator qrScan;

    Button btnqrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_conductor);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ctx = MainConductorActivity.this;
     //   handler= new Handler();

        rViewViajes = findViewById(R.id.rViewViajes);
        tViewSinViajes = findViewById(R.id.tViewSinViajes);
        btnqrScan = findViewById(R.id.btnqrScan);
        btnqrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan = new IntentIntegrator(MainConductorActivity.this);
                qrScan.setCameraId(0);  // Use a specific camera of the device
                qrScan.setOrientationLocked(false);
                qrScan.setBeepEnabled(true);
                qrScan.setPrompt("Apunta tu teléfono hacia el código QR para escanearlo");
                qrScan.initiateScan();
            }
        });
       // rViewAdapterListViajesConductor = new RViewAdapterListViajesConductor(ctx,viajeVOList,rViewViajes);
       // rViewViajes.setAdapter(rViewAdapterListViajesConductor);

        actualizarData();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISION_GPS);
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISION_CAMERA);
            } else {


            }

        }


        /*
        if(viajeVOList.size()>0){
            tViewSinViajes.setVisibility(View.INVISIBLE);
           // Toast.makeText(ctx,"lisa mayor a 0",Toast.LENGTH_LONG).show();
        }else {
            //Toast.makeText(ctx,"lis= 0",Toast.LENGTH_LONG).show();
            tViewSinViajes.setVisibility(View.VISIBLE);
        }
        */
        if (!AutoArranqueBroadcast.isOpening){
            AutoArranqueBroadcast.isOpening = true;
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setMessage("Actualizando datos...");
            progressDialog.show();
            handler.post(waitInsert);
        }

    }
    public static boolean isSaving = false;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    int waitTime = 0;
    Runnable waitInsert = new Runnable() {
        @Override
        public void run() {
            if (isSaving){
                isSaving = false;
                progressDialog.dismiss();
                handler.postDelayed(waitInsert, 60*60*1000);
            }
            else {
                Log.d("ada", "Esperando Inserccion");
                handler.postDelayed(waitInsert, 1000);
                waitTime++;
                if (waitTime == 5 && !isSaving){
                    isSaving = true;
                }
            }
        }
    };

/*
    Runnable runnable = new Runnable() {
        public void run() {
            Log.d(TAG,"runnable() run...");
            if(statusActualizar){
                statusActualizar = false;
                tViewSinViajes.setVisibility(View.VISIBLE);
                actualizarData();
            }
            if(UploadService.statusUpload){
                UploadService.statusUpload = false;
                actualizarData();
            }
            handler.postDelayed(()->{
                run();
            }, 3000);
        }
    };
*/
    final int SCAN_CAMERA = 100;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                //Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(result.getContents());
                    ViajeVO viajeVO = new ViajeVO();
                    viajeVO.setEmpresa(obj.getString("empresa"));
                    viajeVO.setCapacidad(0);
                    viajeVO.setComentario(obj.getString("comentario"));
                    viajeVO.sethInicio(obj.getString("hFin"));
                    viajeVO.setRuta(obj.getString("ruta"));
                    viajeVO.setId(Integer.parseInt(obj.getString("id")));
                    viajeVO.setPlaca("");
                    viajeVO.setConductor(new LoginDataVO().getUsuario());
                    viajeVO.setProveedor("");
                    viajeVO.sethProgramada(obj.getString("hProgramada"));
                    viajeVO.setStatus(0);
                    new ViajeDAO(this).insertar(viajeVO);
                    actualizarData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //setting values to textviews

                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK){
                actualizarData();
            }
        }
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        moveTaskToBack(true);

    }

    void actualizarData(){


        PageViewModelViajesActuales.set(new ViajeDAO(ctx).listAll(false));

        liveDataViajes = ViewModelProviders.of(this).get(PageViewModelViajesActuales.class);

        final Observer<List<ViajeVO>> listObserver = new Observer<List<ViajeVO>>() {
            @Override
            public void onChanged(List<ViajeVO> viajeVOS) {

                rViewAdapterListViajesConductor = new RViewAdapterListViajesConductor(ctx,viajeVOS,rViewViajes);
                rViewViajes.setAdapter(rViewAdapterListViajesConductor);
                if(viajeVOS.size()>0){
                    tViewSinViajes.setVisibility(View.INVISIBLE);
                }else {
                    tViewSinViajes.setVisibility(View.VISIBLE);
                }

            }
        };

        liveDataViajes.getViajeVOList().observe(this,listObserver);



        /*
        viajeVOList = new ViajeDAO(ctx).listAll();

        rViewAdapterListViajesConductor = new RViewAdapterListViajesConductor(ctx,viajeVOList,rViewViajes);
        rViewViajes.setAdapter(rViewAdapterListViajesConductor);
        if(viajeVOList.size()>0){
            tViewSinViajes.setVisibility(View.INVISIBLE);
        }else {
            tViewSinViajes.setVisibility(View.VISIBLE);
        }

        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.limpiar) {
            new ViajeDAO(ctx).deleteByStatusSicronized();
            actualizarData();
        }

        if(id == R.id.trasbordo) {
            btnqrScan.performClick();
        }

        if(id == R.id.version) {
            try {
                Toast.makeText(getBaseContext(),"Versión "+ BuildConfig.VERSION_NAME+" code."+BuildConfig.VERSION_CODE,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }

        if (id == R.id.logout) {
            if(new ViajeDAO(ctx).listByStatusNoFinished().size()>0){//si faltan sincronizar
                Snackbar.make(rViewViajes, "Todos los viajes deben estar sincronizados", Snackbar.LENGTH_LONG).show();
            }else {

                new LoginDataDAO(getBaseContext()).borrarTable();
                new ViajeDAO(getBaseContext()).clearTableUpload();
                Intent intent = new Intent(getBaseContext(), ActivityPreloader.class);
                startActivity(intent);
                stopService(new Intent(getBaseContext(),SearchViajesService.class));
                stopService(new Intent(getBaseContext(),UploadService.class));
                stopService(new Intent(getBaseContext(), SearchChangesViajesService.class));
                handler.removeCallbacks(waitInsert);
                AutoArranqueBroadcast.isOpening = false;
                progressDialog.dismiss();
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISION_GPS) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_PERMISION_CAMERA);
                } else {


                }
                /**
                 * ACTIVADO PERMISO HABILITAR TODAS  LAS OPCIONES DE LOCACLIZACION
                 */

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(TAG, "Permiso denegado");
            }
        }
        if (requestCode == REQUEST_PERMISION_CAMERA) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido
                /**
                 * ACTIVADO PERMISO HABILITAR TODAS  LAS OPCIONES DE LOCACLIZACION
                 */

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(TAG, "Permiso denegado");
            }
        }
    }

}
