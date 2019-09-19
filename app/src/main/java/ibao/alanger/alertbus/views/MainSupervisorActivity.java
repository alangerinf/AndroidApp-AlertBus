package ibao.alanger.alertbus.views;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import ibao.alanger.alertbus.BuildConfig;
import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListViajesSupervisor;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.services.SearchViajesService;
import ibao.alanger.alertbus.services.UploadService;

import static ibao.alanger.alertbus.services.SearchViajesService.statusActualizar;

public class MainSupervisorActivity extends AppCompatActivity {


    private static Context ctx;
    private static RecyclerView rViewViajes;
    private static List<ViajeVO> viajeVOList;
    private static RViewAdapterListViajesSupervisor rViewAdapterListViajesSupervisor;

    private static TextView tViewSinViajes;

    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_supervisor);

        ctx = MainSupervisorActivity.this;

        viajeVOList = new ViajeDAO(ctx).listAll();
        rViewViajes = findViewById(R.id.rViewViajes);



        rViewAdapterListViajesSupervisor = new RViewAdapterListViajesSupervisor(ctx,viajeVOList,rViewViajes);


        rViewViajes.setAdapter(rViewAdapterListViajesSupervisor);

        tViewSinViajes = findViewById(R.id.tViewSinViajes);

        if(viajeVOList.size()>0){
            tViewSinViajes.setVisibility(View.INVISIBLE);
        }else {
            tViewSinViajes.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        moveTaskToBack(true);

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


    void actualizarData(){
        viajeVOList = new ViajeDAO(ctx).listAll();
        tViewSinViajes.setVisibility(View.INVISIBLE);
        rViewAdapterListViajesSupervisor = new RViewAdapterListViajesSupervisor(ctx,viajeVOList,rViewViajes);
        rViewViajes.setAdapter(rViewAdapterListViajesSupervisor);
    }


    @Override
    public void onResume() {
        super.onResume();
        actualizarData();
        handler.post(runnable);
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
                Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Espere a que se sincronizen los Viajes", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else {

                new LoginDataDAO(getBaseContext()).borrarTable();
                new ViajeDAO(getBaseContext()).clearTableUpload();
                Intent intent = new Intent(getBaseContext(), ActivityPreloader.class);
                startActivity(intent);
                stopService(new Intent(getBaseContext(),SearchViajesService.class));
                stopService(new Intent(getBaseContext(),UploadService.class));
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
