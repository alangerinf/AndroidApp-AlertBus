package ibao.alanger.alertbus.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ibao.alanger.alertbus.BuildConfig;
import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListViajesConductor;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.services.SearchChangesViajesService;
import ibao.alanger.alertbus.services.SearchViajesService;
import ibao.alanger.alertbus.services.UploadService;
import ibao.alanger.alertbus.views.ActivityPreloader;

import static ibao.alanger.alertbus.services.SearchViajesService.statusActualizar;

public class MainConductorActivity extends AppCompatActivity {



    private static PageViewModelViajesActuales liveDataViajes;

    private static Context ctx;
    private static RecyclerView rViewViajes;
    private static RViewAdapterListViajesConductor rViewAdapterListViajesConductor;
  //  private static Handler handler;

    private static TextView tViewSinViajes;

    private static String TAG = MainConductorActivity.class.getSimpleName();

    private static int REQUEST_PERMISION_GPS =555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_conductor);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ctx = MainConductorActivity.this;
     //   handler= new Handler();

        rViewViajes = findViewById(R.id.rViewViajes);
        tViewSinViajes = findViewById(R.id.tViewSinViajes);

       // rViewAdapterListViajesConductor = new RViewAdapterListViajesConductor(ctx,viajeVOList,rViewViajes);
       // rViewViajes.setAdapter(rViewAdapterListViajesConductor);

        actualizarData();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISION_GPS);
        } else {


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

    }

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

    @Override
    public void onResume() {
        super.onResume();

        //handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //handler.removeCallbacks(runnable);
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
           // actualizarData();
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
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISION_GPS) {
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
