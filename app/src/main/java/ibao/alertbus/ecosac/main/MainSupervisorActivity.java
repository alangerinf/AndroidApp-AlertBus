package ibao.alertbus.ecosac.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ibao.alertbus.ecosac.BuildConfig;
import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.helpers.adapters.RViewAdapterListViajesSupervisor;
import ibao.alertbus.ecosac.models.dao.LoginDataDAO;
import ibao.alertbus.ecosac.models.dao.ViajeDAO;
import ibao.alertbus.ecosac.models.vo.ViajeVO;
import ibao.alertbus.ecosac.services.UploadService;
import ibao.alertbus.ecosac.views.ActivityPreloader;
import ibao.alertbus.ecosac.views.CustomScannerActivity;

public class MainSupervisorActivity extends AppCompatActivity {


    private static PageViewModelViajesActuales liveDataViajes;
    private static Context ctx;
    private static RecyclerView rViewViajes;
  //  private static List<ViajeVO> viajeVOList;
    private static RViewAdapterListViajesSupervisor rViewAdapterListViajesSupervisor;

    private static TextView tViewSinViajes;

    //private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_supervisor);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ctx = MainSupervisorActivity.this;

       // viajeVOList = new ViajeDAO(ctx).listAll(true);
        rViewViajes = findViewById(R.id.rViewViajes);



        tViewSinViajes = findViewById(R.id.tViewSinViajes);


    }
    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        moveTaskToBack(true);

    }

/*
    Runnable runnable = new Runnable() {
        public void run() {
            if(SearchViajesService.statusActualizar){
                statusActualizar = false;
                tViewSinViajes.setVisibility(View.VISIBLE);
                actualizarData();
            }
            if(UploadService.statusUpload){
                UploadService.statusUpload = false;
                actualizarData();
            }
            handler.postDelayed(runnable, 2000);
        }
    };
*/
/*
    void actualizarData(){
        viajeVOList = new ViajeDAO(ctx).listAll();
        tViewSinViajes.setVisibility(View.INVISIBLE);
        rViewAdapterListViajesSupervisor = new RViewAdapterListViajesSupervisor(ctx,viajeVOList,rViewViajes);
        rViewViajes.setAdapter(rViewAdapterListViajesSupervisor);

        if(viajeVOList.size()>0){
            tViewSinViajes.setVisibility(View.INVISIBLE);
        }else {
            tViewSinViajes.setVisibility(View.VISIBLE);
        }
    }

    */
    void actualizarData(){


        PageViewModelViajesActuales.set(new ViajeDAO(ctx).listAll(false));

        liveDataViajes = ViewModelProviders.of(this).get(PageViewModelViajesActuales.class);

        final Observer<List<ViajeVO>> listObserver = new Observer<List<ViajeVO>>() {
            @Override
            public void onChanged(List<ViajeVO> viajeVOS) {

                rViewAdapterListViajesSupervisor = new RViewAdapterListViajesSupervisor(ctx,viajeVOS,rViewViajes);
                rViewViajes.setAdapter(rViewAdapterListViajesSupervisor);
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
    public void onResume() {
        super.onResume();
        actualizarData();
    //    handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
     //   handler.removeCallbacks(runnable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_supervisor, menu);
        return true;
    }

    public void goToScanner(){
        Intent i = new Intent(ctx, CustomScannerActivity.class);
       // i.putExtra(ActivityViaje.EXTRA_VIAJE,viajeVO);
        ctx.startActivity(i);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.limpiar) {
            new ViajeDAO(ctx).deleteByStatusSicronized();
            actualizarData();

        }

        if(id == R.id.qr) {
            goToScanner();
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
                //stopService(new Intent(getBaseContext(),SearchViajesService.class));
                stopService(new Intent(getBaseContext(),UploadService.class));
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
