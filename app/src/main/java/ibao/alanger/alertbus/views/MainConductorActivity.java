package ibao.alanger.alertbus.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import ibao.alanger.alertbus.BuildConfig;
import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListViajesConductor;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.services.SearchViajesService;
import ibao.alanger.alertbus.services.UploadService;

import static ibao.alanger.alertbus.services.SearchViajesService.statusActualizar;

public class MainConductorActivity extends AppCompatActivity {


    private static Context ctx;
    private static RecyclerView rViewViajes;
    private static List<ViajeVO> viajeVOList;
    private static RViewAdapterListViajesConductor rViewAdapterListViajesConductor;
    private static Handler handler = new Handler();

    private static TextView tViewSinViajes;

    private static String TAG = MainConductorActivity.class.getSimpleName();

    private static int REQUEST_PERMISION_GPS =555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_conductor);

        ctx = MainConductorActivity.this;

        startService(new Intent(ctx, SearchViajesService.class));

        viajeVOList = new ViajeDAO(ctx).listAll();
        rViewViajes = findViewById(R.id.rViewViajes);

        tViewSinViajes = findViewById(R.id.tViewSinViajes);



        rViewAdapterListViajesConductor = new RViewAdapterListViajesConductor(ctx,viajeVOList,rViewViajes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //  initSpruce();
            }
        };

        rViewViajes.setLayoutManager(linearLayoutManager);
        rViewViajes.setAdapter(rViewAdapterListViajesConductor);



        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISION_GPS);
        } else {




        }
        if(viajeVOList.size()>0){
            tViewSinViajes.setVisibility(View.INVISIBLE);
           // Toast.makeText(ctx,"lisa mayor a 0",Toast.LENGTH_LONG).show();
        }else {
            //Toast.makeText(ctx,"lis= 0",Toast.LENGTH_LONG).show();
            tViewSinViajes.setVisibility(View.VISIBLE);
        }

    }

    Runnable runnable = new Runnable() {
        public void run() {
            if(statusActualizar){
                statusActualizar = false;
                tViewSinViajes.setVisibility(View.VISIBLE);

                actualizarData();
            }
            if(UploadService.statusUpload){
                UploadService.statusUpload = false;
                actualizarData();
            }
            handler.postDelayed(runnable, 1000);
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        actualizarData();
        handler.post(runnable);
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        moveTaskToBack(true);

    }

    void actualizarData(){
        viajeVOList = new ViajeDAO(ctx).listAll();

        rViewAdapterListViajesConductor = new RViewAdapterListViajesConductor(ctx,viajeVOList,rViewViajes);
        rViewViajes.setAdapter(rViewAdapterListViajesConductor);
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
            startActivity(new Intent(ctx,PasajerosListActivity.class));
        }

        if(id == R.id.version) {
            try {
                Toast.makeText(getBaseContext(),"Versión "+ BuildConfig.VERSION_NAME+" code."+BuildConfig.VERSION_CODE,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
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
