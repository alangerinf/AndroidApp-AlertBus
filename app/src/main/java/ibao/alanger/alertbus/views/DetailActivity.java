package ibao.alanger.alertbus.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListPasajeros;
import ibao.alanger.alertbus.models.dao.PasajeroDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.PasajeroVO;
import ibao.alanger.alertbus.models.vo.ViajeVO;

public class DetailActivity extends AppCompatActivity {

    static EditText eTextSearch;

    static FloatingActionButton fAButtonClearText;

    static FloatingActionButton fAButtonComent;
    static FloatingActionButton fABurronAlerts;

    static ViajeVO VIAJE;


    static RecyclerView rViewPasajeros;
    static RViewAdapterListPasajeros rViewAdapterListPasajeros;
    static List<PasajeroVO> pasajeroVOListAll;

    static List<PasajeroVO> pasajeroVOListFiltrado;

    String TAG = this.getClass().getSimpleName();

    Context ctx = this;
    private static Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        eTextSearch = findViewById(R.id.eTextSearch);

        fAButtonClearText = findViewById(R.id.fAButtonClearText);
        fAButtonComent = findViewById(R.id.fAButtonComent);


        rViewPasajeros = findViewById(R.id.rViewPasajeros);

        pasajeroVOListAll = new ArrayList<>();
        pasajeroVOListFiltrado = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //  initSpruce();
            }
        };
        rViewPasajeros.setLayoutManager(linearLayoutManager);
        Bundle b = getIntent().getExtras();

        VIAJE = new ViajeDAO(ctx).buscarById(b.getInt("id"));

        pasajeroVOListAll = (new PasajeroDAO(ctx).listByIdViaje(VIAJE.getId()));

        new ViajeDAO(ctx).toStatus1(VIAJE.getId());

        pasajeroVOListFiltrado = pasajeroVOListAll;

        rViewAdapterListPasajeros = new RViewAdapterListPasajeros(ctx,pasajeroVOListFiltrado,rViewPasajeros);
        rViewPasajeros.setAdapter(rViewAdapterListPasajeros);

        fAButtonClearText.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                eTextSearch.setText("");
                fAButtonClearText.setVisibility(View.INVISIBLE);
            }
        });

        eTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void afterTextChanged(Editable s) {
                String text = eTextSearch.getText().toString();
                if(text.length()>0){
                    buscarTrabajadores(text);
                    Log.d("PRUEBA","VISIBLE" );
                    fAButtonClearText.setVisibility(View.VISIBLE);
                    rViewAdapterListPasajeros = new RViewAdapterListPasajeros(ctx,pasajeroVOListFiltrado,rViewPasajeros);
                    rViewPasajeros.setAdapter(rViewAdapterListPasajeros);
                }else {
                    pasajeroVOListFiltrado = pasajeroVOListAll;
                    rViewAdapterListPasajeros = new RViewAdapterListPasajeros(ctx,pasajeroVOListFiltrado,rViewPasajeros);
                    rViewPasajeros.setAdapter(rViewAdapterListPasajeros);
                    Log.d("PRUEBA","INVISIBLE" );
                    fAButtonClearText.setVisibility(View.INVISIBLE);
                }

            }


        });

        if(VIAJE.getComentario() == null || VIAJE.getComentario().isEmpty()){
            Log.d(TAG,"COMENTARIO NULL");
            fAButtonComent.setImageResource(R.drawable.ic_mode_comment_white_24dp);
        }else {
            Log.d(TAG,"CON COMENTARIO NO NULL");
            fAButtonComent.setImageResource(R.drawable.ic_comment_white_24dp);
        }

    }

    public void showComent(View view) {
        //final String coment = new VisitaDAO(getBaseContext()).buscarById((long) visita.getId()).getComentario();
        dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.dialog_coment);
        Button btnAccept = (Button) dialog.findViewById(R.id.btnAccept);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        final EditText eTextcoment = (EditText) dialog.findViewById(R.id.eTextComent);

        eTextcoment.setText(VIAJE.getComentario());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (isEditable) {
                    new MuestraDAO(getBaseContext()).editComentarioById(MUESTRA.getId(), eTextcoment.getText().toString());
                }*/
                dialog.dismiss();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new ViajeDAO(getBaseContext()).editComentarioById(VIAJE.getId(), eTextcoment.getText().toString());
                    VIAJE.setComentario(eTextcoment.getText().toString());
                    if(VIAJE.getComentario() == null || VIAJE.getComentario().isEmpty()){
                        Log.d(TAG,"COMENTARIO NULL");
                        fAButtonComent.setImageResource(R.drawable.ic_mode_comment_white_24dp);
                    }else {
                        Log.d(TAG,"CON COMENTARIO NO NULL");
                        fAButtonComent.setImageResource(R.drawable.ic_comment_white_24dp);
                    }

                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void buscarTrabajadores(String text){
        pasajeroVOListFiltrado= new ArrayList<>();
        for(PasajeroVO p : pasajeroVOListAll){
            if(compareString(p.getName().toLowerCase(),text.toLowerCase())>0){
                pasajeroVOListFiltrado.add(p);
            }else {
                if( compareString(p.getDni(),text)>0){
                    pasajeroVOListFiltrado.add(p);

                }else {
                    if( compareString(p.getObservacion().toLowerCase(),text.toLowerCase())>0){
                        pasajeroVOListFiltrado.add(p);

                    }
                }
            }
        }
    }

    private int compareString( String sTexto, String sTextoBuscado){
        int contador = 0;
        while (sTexto.indexOf(sTextoBuscado) > -1) {
            sTexto = sTexto.substring(sTexto.indexOf(
                    sTextoBuscado)+sTextoBuscado.length(),sTexto.length());
            contador++;
        }

        return  (contador);
    }


}
