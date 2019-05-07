package ibao.alanger.alertbus.views;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ibao.alanger.alertbus.BuildConfig;
import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.DownloadNewViajes;
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListViajes;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.services.SearchViajesService;

import static ibao.alanger.alertbus.services.SearchViajesService.statusActualizar;

public class MainActivity extends AppCompatActivity {


    Context ctx = this;
    private static RecyclerView rViewViajes;
    private static List<ViajeVO> viajeVOList;
    private static RViewAdapterListViajes rViewAdapterListViajes;

    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viajeVOList = new ViajeDAO(ctx).listAll();
        rViewViajes = findViewById(R.id.rViewViajes);
        rViewAdapterListViajes = new RViewAdapterListViajes(ctx,viajeVOList,rViewViajes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //  initSpruce();
            }
        };

        rViewViajes.setLayoutManager(linearLayoutManager);
        rViewViajes.setAdapter(rViewAdapterListViajes);
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
                actualizarData();
            }
             handler.postDelayed(runnable, 1000);
        }
    };




    void actualizarData(){
        viajeVOList = new ViajeDAO(ctx).listAll();
        rViewAdapterListViajes = new RViewAdapterListViajes(ctx,viajeVOList,rViewViajes);
        rViewViajes.setAdapter(rViewAdapterListViajes);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.version) {
            try {
                Toast.makeText(getBaseContext(),"Versión "+ BuildConfig.VERSION_NAME+" code."+BuildConfig.VERSION_CODE,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.logout) {

            Toast.makeText(getBaseContext(), "Cerrando Sesión...", Toast.LENGTH_LONG).show();
            new LoginDataDAO(getBaseContext()).borrarTable();
            Intent intent = new Intent(getBaseContext(), ActivityPreloader.class);
            startActivity(intent);
            //finish();
        }
        if (id == R.id.actualizar) {

            Intent intent = new Intent(this, SearchViajesService.class);
            startService(intent);
            //finish();
        }

        return super.onOptionsItemSelected(item);
    }
}