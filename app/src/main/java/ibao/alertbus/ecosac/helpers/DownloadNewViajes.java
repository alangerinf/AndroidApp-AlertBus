package ibao.alertbus.ecosac.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.app.AppController;
import ibao.alertbus.ecosac.main.PageViewModelViajesActuales;
import ibao.alertbus.ecosac.models.dao.LoginDataDAO;
import ibao.alertbus.ecosac.models.dao.RestriccionDAO;
import ibao.alertbus.ecosac.models.dao.ViajeDAO;
import ibao.alertbus.ecosac.models.vo.ViajeVO;
import ibao.alertbus.ecosac.services.SearchViajesService;
import ibao.alertbus.ecosac.main.MainConductorActivity;

import static ibao.alertbus.ecosac.utilities.Utilities.URL_BUSCARNUEVOS;
import static ibao.alertbus.ecosac.utilities.Utilities.URL_BUSCARRESTRICCIONES;

public class DownloadNewViajes {

    Context ctx;
    String TAG = DownloadNewViajes.class.getSimpleName();

    private static String HEADER_USUARIO = "usuario";
    public static int status;

    static String CHANNEL_NOTIFICATION = "1992";
    static int  COUNT_NOTIFICATION = 0;

    public DownloadNewViajes(Context ctx)
    {
        status=0;
        this.ctx = ctx;
    }

    public void SearchNews(){
        Log.d(TAG,"SearchNews()");

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
                                         *            "id": "1",
                                         *             "horaInicio": "2019-09-18 20:20:00",
                                         *             "empresa": "IBAO PERU",
                                         *             "proveedor": "Transporte El Sol",
                                         *             "conductor": "QUIROZ NUNEZ, DORITA",
                                         *             "tipoVehiculo": "Bus",
                                         *             "placa": "XYZ-123",
                                         *             "ruta": "TRUJILLO - CHAO",
                                         *             "tipoTarifa": "UNICA",
                                         *             "capacidad": 45,
                                         *             "restricciones": "LICENCIA VENCIDA EL 18/09/2019,SOAT VENCIDO EL 18/09/2019"
                                         *         }
                                          */
                                        ViajeVO viajeVO = new ViajeVO();
                                        viajeVO.setId(viaje.getInt("id"));
                                        viajeVO.sethProgramada(viaje.getString("horaInicio"));//hora programada
                                        viajeVO.setEmpresa(viaje.getString("empresa"));
                                        viajeVO.setProveedor(viaje.getString("proveedor"));
                                        viajeVO.setConductor(viaje.getString("conductor"));
                                        viajeVO.setPlaca(viaje.getString("placa"));
                                        viajeVO.setRuta(viaje.getString("ruta"));
                                        viajeVO.setCapacidad(viaje.getInt("capacidad"));

                                        String restStr = viaje.getString("restricciones");

                                        if(!(restStr==null ||restStr.equalsIgnoreCase("null") || restStr.equals(""))){
                                            String [] restList = restStr.split(",");
                                            Log.d(TAG,"restircciones:"+restStr);
                                            for(String r : restList){
                                                Log.d(TAG,r);
                                                new RestriccionDAO(ctx).insertar(r,"",viajeVO.getId());
                                            }
                                            viajeVO.getRestriccionVOList().addAll(new RestriccionDAO(ctx).listByIdViaje(viajeVO.getId()));
                                        }


//                                        viajeVO.setNumPasajeros(viaje.getInt("totalTrabajadores"));

//                                        viajeVO.sethFin(viaje.getString("horaFin"));

                                        if(new ViajeDAO(ctx).insertar(
                                                viajeVO
                                        )){
                                            PageViewModelViajesActuales.addViaje(viajeVO);
                                        }


                                        //Notification
                                        // Create an explicit intent for an Activity in your app
                                        Intent intent = new Intent(ctx, MainConductorActivity.class);


                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);

                                        createNotificationChannel();

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_NOTIFICATION)
                                                .setSmallIcon(R.drawable.ic_comment_white_24dp)
                                                .setContentTitle("Nuevo viaje de "+viajeVO.getProveedor())
                                                .setContentText("Ruta: "+viajeVO.getRuta()+", para las "+viajeVO.gethProgramada()+" Horas!")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                // Set the intent that will fire when the user taps the notification
                                                .setContentIntent(pendingIntent)
                                                .setAutoCancel(true);
                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);

                                        notificationManager.notify(COUNT_NOTIFICATION, builder.build());
                                        COUNT_NOTIFICATION++;
                                    }
                                    /*
                                    JSONArray dataPasajeros = main.getJSONArray("pasajeros");
                                    for(int i=0;i<dataPasajeros.length();i++){
                                        JSONObject pasajero = new JSONObject(dataPasajeros.get(i).toString());

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
                                */
                                    /*
                                    JSONArray dataRestrinccion = main.getJSONArray("restricciones");
                                    for(int i=0;i<dataRestrinccion.length();i++){
                                        JSONObject restriccion = new JSONObject(dataRestrinccion.get(i).toString());

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
                                */
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
                      //  Toast.makeText(ctx,"Error al conectarse, verifique su conexion con el servidor",Toast.LENGTH_LONG).show();
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
        sr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 40, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(sr);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "titulo xd";
            String description = "descripcion del canal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_NOTIFICATION, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void getTrabajadorRestricciones() {
        final RequestQueue queue = Volley.newRequestQueue(ctx);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_BUSCARRESTRICCIONES,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String $response) {
                        // response
                        Log.d("Response", $response);

                        JSONObject $string_json = null;
                        try {
                            $string_json = new JSONObject($response);
                            JSONArray $data = $string_json.getJSONArray("data");
                            new RestriccionDAO(ctx).deleteAll();
                            if ($data.length() > 0) {
                                for (int i = 0; i < $data.length(); i++) {
                                    JSONObject $reg = $data.getJSONObject(i);
                                    new RestriccionDAO(ctx).insertar2($reg.getString("RFID"), $reg.getString("NRO_DOCUMENTO"), $reg.getString("TIPO_RESTRICCION"));
                                }
                                MainConductorActivity.isSaving = true;
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("query", "EXEC AlertBus_MOVIL_SPU_Blacklist");
                return params;
            }
        };
        queue.add(postRequest);
    }
}