package ibao.alanger.alertbus.views;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
import ibao.alanger.alertbus.helpers.adapters.RViewAdapterListRestricciones;
import ibao.alanger.alertbus.models.dao.RestriccionDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.PasajeroVO;
import ibao.alanger.alertbus.models.vo.RestriccionVO;
import ibao.alanger.alertbus.models.vo.ViajeVO;

import static ibao.alanger.alertbus.utilities.Utilities.URL_SELECT_RESTRICCIONES;
import static ibao.alanger.alertbus.utilities.Utilities.URL_SELECT_TRABAJADORES;

public class RestricionActivity extends AppCompatActivity {
    static RecyclerView rViewRestricciones;
    static RViewAdapterListRestricciones rViewAdapterListRestricciones;


    static ViajeVO VIAJE;

    static String TAG = "TAG"+RestricionActivity.class.getSimpleName();

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restricion);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        VIAJE = new ViajeDAO(ctx).buscarById(b.getInt("id"));
        consultarRestricciones(VIAJE.getIdWeb());

    }

    private void cargarData() {

        rViewAdapterListRestricciones = new RViewAdapterListRestricciones(ctx,VIAJE.getRestriccionVOList(),rViewRestricciones);
        rViewRestricciones.setAdapter(rViewAdapterListRestricciones);
    }


    public void consultarRestricciones(int idViaje){
        Log.d(TAG,"consultarRestricciones()");

        VIAJE.getRestriccionVOList().clear();


        Log.d(TAG,URL_SELECT_RESTRICCIONES);
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_SELECT_RESTRICCIONES,
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


                                        RestriccionVO restriccionVO = new RestriccionVO();

                                        restriccionVO.setName(temp.getString("name"));
                                        restriccionVO.setDesc(temp.getString("descripcion"));

                                        Log.d(TAG,"añadiendo:"+restriccionVO.getName());

                                        VIAJE.getRestriccionVOList().add(restriccionVO);

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
