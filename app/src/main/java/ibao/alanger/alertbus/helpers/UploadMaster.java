package ibao.alanger.alertbus.helpers;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibao.alanger.alertbus.app.AppController;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.services.UploadService;


import static ibao.alanger.alertbus.utilities.Utilities.URL_UPLOAD_CONFIRMARVIAJE;

public class UploadMaster {

    Context ctx;
    String TAG = UploadMaster.class.getSimpleName();
    public static int status;


    public UploadMaster(Context ctx)
    {
        status=0;
        this.ctx = ctx;
    }

    public void UploadViaje(final List<ViajeVO> viajeVOList){

        String url;
        if(new LoginDataDAO(ctx).verficarLogueo().getTypeUser()==0){// si es conductor
            url = URL_UPLOAD_CONFIRMARVIAJE;//sincronizar viaje
        }else {//si es supervisor ==1
            url = URL_UPLOAD_CONFIRMARVIAJE;//confirmar viaje por supervisor
        }

        status=1;
        StringRequest sr = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG,"RESP: "+response);
                        status=2;

                        if(!response.isEmpty()){
                            try {
                                JSONObject main = new JSONObject(response);
                                Log.d(TAG,"flag1");
                                if(main.getInt("success")==1){

                                    JSONArray datos = main.getJSONArray("data");

                                    for(int i=0;i<datos.length();i++){

                                        JSONObject viaje = datos.getJSONObject(i);

                                        int idPlanificacion = viaje.getInt("idPlanificacion");
                                        int idViaje = viaje.getInt("idViaje");

                                        new ViajeDAO(ctx).toStatus3(idViaje,idPlanificacion);// este es el estado final para el conductor

                                    }

                                    UploadService.statusUpload=true;
                                    status=3;
                                }
                                status=3;//SE TERMINO SIN FOTOS

                            } catch (JSONException e) {
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos JSON, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                                Log.d(TAG, e.toString());
                                status = -1;
                            }
                        }else{
                            Toast.makeText(ctx, "No se recibio respuesta del Servidor", Toast.LENGTH_LONG).show();
                            status=-1;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                       // Toast.makeText(ctx,"Error al conectarse, verifique su conexion con el servidor",Toast.LENGTH_LONG).show();
                      //  Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
                        Log.d("error 2",error.toString());
                        status=-2;
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();

                //informacion del usuario
                Gson gson = new Gson();
                String usuarioJson = gson.toJson(new LoginDataDAO(ctx).verficarLogueo());
                params.put("usuario",usuarioJson);

                //viajes
                gson = new Gson();
                List<ViajeVO> viajeVOS = viajeVOList;
                String viajesJson = gson.toJson(
                        viajeVOS,
                        new TypeToken<ArrayList<ViajeVO>>() {}.getType());
                params.put("viajes",viajesJson);


                Log.d(TAG,"usuario:"+usuarioJson);
                Log.d(TAG,"viajes:"+viajesJson);

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