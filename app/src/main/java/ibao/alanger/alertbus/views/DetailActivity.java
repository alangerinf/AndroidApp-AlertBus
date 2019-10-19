package ibao.alanger.alertbus.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.app.AppController;
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListPasajeros;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.PasajeroDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.PasajeroVO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.services.UploadService;

import static ibao.alanger.alertbus.utilities.Utilities.URL_CHECK_VIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.URL_SELECT_TRABAJADORES;
import static ibao.alanger.alertbus.utilities.Utilities.URL_UPLOAD_VIAJE;

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
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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


        consultarPasajeros(VIAJE.getIdWeb());




    }



    private void cargarData(){
        pasajeroVOListAll = (VIAJE.getPasajeroVOList());

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



    public void consultarPasajeros(int idViaje){
        Log.d(TAG,"consultarPasajeros()");


        Log.d(TAG,URL_SELECT_TRABAJADORES);
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_SELECT_TRABAJADORES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG,"RESP: "+response);

                        if(!response.isEmpty()){
                            try {
                                JSONObject main = new JSONObject(response);
                                Log.d(TAG,"flag1");
                                if(main.getInt("success")==1){

                                    JSONArray datos = main.getJSONArray("data");

                                    for(int i=0;i<datos.length();i++){

                                        JSONObject temp = datos.getJSONObject(i);

                                        /*
                                        'dni'=> $row['NRO_DOCUMENTO'],
                                        'name'=> $row['TRABAJADOR'],
                                        'hIngreso'=> $row['HORA_INGRESO'],
                                        'restriccion'=> $row['RESTRICCION']
                                        */


                                        PasajeroVO pasajeroVO = new PasajeroVO();

                                        pasajeroVO.setDni(temp.getString("dni"));
                                        pasajeroVO.setName(temp.getString("name"));
                                        pasajeroVO.sethSubida(temp.getString("hIngreso"));
                                        pasajeroVO.setObservacion(temp.getString("restriccion")=="null"?"":temp.getString("restriccion"));

                                        Log.d(TAG,"añadiendo:"+pasajeroVO.getName());

                                        VIAJE.getPasajeroVOList().add(pasajeroVO);

                                    }
                                }

                                cargarData();
                            } catch (JSONException e) {
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos JSON, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                                Log.d(TAG, e.toString());
                            }
                        }else{
                            Toast.makeText(ctx, "No se recibio respuesta del Servidor", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                         Toast.makeText(ctx,"Error al conectarse, verifique su conexion con el servidor",Toast.LENGTH_LONG).show();
                        //  Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
                        Log.d("error 2",error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();

                params.put("idViaje",""+idViaje);

                Log.d(TAG,"idViaje:"+idViaje);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(sr);
    }



}
