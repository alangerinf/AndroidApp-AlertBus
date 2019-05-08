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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ibao.alanger.alertbus.app.AppController;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.PasajeroDAO;
import ibao.alanger.alertbus.models.dao.RestriccionDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.PasajeroVO;
import ibao.alanger.alertbus.models.vo.RestriccionVO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.services.SearchViajesService;

import static ibao.alanger.alertbus.utilities.Utilities.URL_BUSCARNUEVOS;

public class DownloadNewViajes {

    Context ctx;
    String TAG = DownloadNewViajes.class.getSimpleName();

    private static String HEADER_USUARIO = "usuario";
    public static int status;

    public DownloadNewViajes(Context ctx)
    {
        status=0;
        this.ctx = ctx;
    }

    public void SearchNews(){

        status=1;
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_BUSCARNUEVOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG,"RES SERVER:"+response);
                        status=2;

                        if(!response.isEmpty()){
                            try {
                                JSONObject main = new JSONObject(response);

                                if(main.getInt("success")==1){
                                    Log.d("asd ","flag2");
                                    JSONArray dataViajes = main.getJSONArray("viajes");

                                    if(0<dataViajes.length()){
                                        SearchViajesService.statusActualizar = true;
                                    }

                                    for(int i=0;i<dataViajes.length();i++){

                                        JSONObject viaje = new JSONObject(dataViajes.get(i).toString());
                                        /***
                                         * {
                                         *             "id": "1",
                                         *             "proveedor": "Transporte El Sol",
                                         *             "ruta": "TRUJILLO - CHAO",
                                         *             "conductor": "QUIROZ NUNEZ, DORITA",
                                         *             "placa": "XYZ-123",
                                         *             "capacidadTeorica": 45,
                                         *             "totalTrabajadores": 4,
                                         *             "horaInicio": "2019-05-03 10:00:00",
                                         *             "horaFin": "2019-05-03 12:00:00"
                                         *         }
                                          */
                                        ViajeVO viajeVO = new ViajeVO();
                                        viajeVO.setId(viaje.getInt("id"));
                                        viajeVO.setProveedor(viaje.getString("proveedor"));
                                        viajeVO.setRuta(viaje.getString("ruta"));
                                        viajeVO.setConductor(viaje.getString("conductor"));
                                        viajeVO.setPlaca(viaje.getString("placa"));
                                        viajeVO.setNumpasajeros(viaje.getInt("totalTrabajadores"));
                                        viajeVO.setCapacidad(viaje.getInt("capacidadTeorica"));
                                        viajeVO.sethInicio(viaje.getString("horaInicio"));
                                        viajeVO.sethFin(viaje.getString("horaFin"));

                                        new ViajeDAO(ctx).insertar(
                                                viajeVO.getId(),
                                                viajeVO.getProveedor(),
                                                viajeVO.getPlaca(),
                                                viajeVO.getConductor(),
                                                viajeVO.getRuta(),
                                                viajeVO.gethInicio(),
                                                viajeVO.gethFin(),
                                                viajeVO.getNumpasajeros(),
                                                viajeVO.getCapacidad()
                                                );
                                    }
                                    JSONArray dataPasajeros = main.getJSONArray("pasajeros");
                                    for(int i=0;i<dataPasajeros.length();i++){
                                        JSONObject pasajero = new JSONObject(dataPasajeros.get(i).toString());
                                        /***
                                         *{
                                         *             "id": "1",
                                         *             "idViaje": "1",
                                         *             "nroDocumento": "19201830",
                                         *             "trabajador": "QUIROZ ASTACIO, LUIS",
                                         *             "inicioLectura": "2019-05-03 10:01:00",
                                         *             "observacion": "DESCANSO VACACIONAL"
                                         *}
                                         */
                                        PasajeroVO pasajeroVO = new PasajeroVO();
                                        pasajeroVO.setIdViaje(pasajero.getInt("idViaje"));
                                        pasajeroVO.setName(pasajero.getString("trabajador"));
                                        pasajeroVO.setDni(pasajero.getString("nroDocumento"));
                                        pasajeroVO.sethSubida(pasajero.getString("inicioLectura"));
                                        pasajeroVO.setObservacion(pasajero.getString("observacion"));

                                        new PasajeroDAO(ctx).insertar(
                                                pasajeroVO.getDni(),
                                                pasajeroVO.getName(),
                                                pasajeroVO.getIdViaje(),
                                                pasajeroVO.gethSubida(),
                                                pasajeroVO.getObservacion()
                                        );
                                    }
                                    JSONArray dataRestrinccion = main.getJSONArray("restricciones");
                                    for(int i=0;i<dataRestrinccion.length();i++){
                                        JSONObject restriccion = new JSONObject(dataPasajeros.get(i).toString());
                                        /***
                                         *{"id":"2",
                                         * "nombre":"Persona",
                                         * "descripcion":"LICENCIA VENCIDA"}
                                         */
                                        RestriccionVO restriccionVO = new RestriccionVO();
                                        restriccionVO.setName(restriccion.getString("nombre"));
                                        restriccionVO.setDesc(restriccion.getString("descripcion"));
                                        restriccionVO.setIdViaje(restriccion.getInt("id"));

                                        new RestriccionDAO(ctx).insertar(
                                                restriccionVO.getName(),
                                                restriccionVO.getDesc(),
                                                restriccionVO.getIdViaje()
                                        );
                                    }
                                    status=3;
                                }
                                status=3;//SE TERMINO SIN FOTOS

                            } catch (JSONException e) {
                             //   Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos JSON, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ctx,"Error al conectarse, verifique su conexion con el servidor",Toast.LENGTH_LONG).show();
                        Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
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
                params.put(HEADER_USUARIO,usuarioJson);
                Log.d(TAG,HEADER_USUARIO+":"+usuarioJson);
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