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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibao.alanger.alertbus.app.AppController;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.TrackingDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.TrackingVO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.services.UploadService;

import static ibao.alanger.alertbus.utilities.Utilities.URL_UPLOAD_CONFIRMARVIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.URL_UPLOAD_TRACKING;

public class UploadTracking {

    Context ctx;
    String TAG = UploadTracking.class.getSimpleName();
    public static int status;


    public UploadTracking(Context ctx)
    {
        status=0;
        this.ctx = ctx;
    }

    public void upload(final List<TrackingVO> trackingVOList){

        status=1;
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_UPLOAD_TRACKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG,"RESP: "+response);
                        status=2;

                        if(!response.isEmpty()){
                                Log.d(TAG,"flag1");
                                if(response.equals("1")){
                                    Log.d(TAG,"flag2");
                                    for(TrackingVO track: trackingVOList){
                                        Log.d(TAG,"borrnado");
                                        new TrackingDAO(ctx).setUploadStatus(track.getDateTime());
                                    }
                                    UploadService.statusUpload=true;
                                    status=3;
                                }else {
                                    Toast.makeText(ctx,"error al subir",Toast.LENGTH_LONG).show();
                                }
                                status=3;//SE TERMINO SIN FOTOS


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

                //tracking
                gson = new Gson();
                String trackingJson = gson.toJson(
                        trackingVOList,
                        new TypeToken<ArrayList<ViajeVO>>() {}.getType());
                params.put("tracking",trackingJson);

                Log.d(TAG,"usuario:"+usuarioJson);
                Log.d(TAG,"tracking:"+trackingJson);
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