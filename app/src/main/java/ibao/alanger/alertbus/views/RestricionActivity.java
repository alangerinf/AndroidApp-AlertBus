package ibao.alanger.alertbus.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListPasajeros;
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListRestricciones;
import ibao.alanger.alertbus.models.dao.RestriccionDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.PasajeroVO;
import ibao.alanger.alertbus.models.vo.RestriccionVO;

public class RestricionActivity extends AppCompatActivity {
    static RecyclerView rViewRestricciones;
    static RViewAdapterListRestricciones rViewAdapterListRestricciones;
    static List<RestriccionVO> restriccionVOList;

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restricion);
        rViewRestricciones = findViewById(R.id.rViewRestricciones);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //  initSpruce();
            }
        };
        rViewRestricciones.setLayoutManager(linearLayoutManager);
        Bundle b = getIntent().getExtras();

        restriccionVOList = new RestriccionDAO(ctx).listByIdViaje(b.getInt("id"));

        rViewAdapterListRestricciones = new RViewAdapterListRestricciones(ctx,restriccionVOList,rViewRestricciones);
        rViewRestricciones.setAdapter(rViewAdapterListRestricciones);


    }
}
