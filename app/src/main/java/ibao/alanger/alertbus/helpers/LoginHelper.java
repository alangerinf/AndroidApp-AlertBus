package com.ibao.alanger.agroq.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ibao.alanger.agroq.app.AppController;
import com.ibao.alanger.agroq.models.dao.LoginDataDAO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.LoginDataVO;
import com.ibao.alanger.agroq.views.ActivityPostloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ibao.alanger.agroq.utilities.Utilities.URL_AUTENTIFICATION;


public class LoginHelper {

    public static String POST_USER = "usuario";
    public static String POST_PASSWORD = "password";
    public static String POST_IDCULTIVO = "idCultivo";
    public static String POST_IDPLANTA = "idPlanta";

    static String TAG = "login Helper";

    Context ctx;
    ProgressDialog progress;

    public LoginHelper(Context ctx){
        this.ctx = ctx;
    }

    public LoginDataVO verificarLogueo(){
        LoginDataVO  temp = new LoginDataDAO(ctx).verficarLogueo();
        return temp;
    }
    public void intentoLogueo(final String user, final String pass,final int idPlanta,final int idCultivo){
        progress = new ProgressDialog(ctx);
        progress.setCancelable(false);
        progress.setMessage("Comprobando Credenciales");
        progress.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_AUTENTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            new LoginDataDAO(ctx).borrarTable();
                            JSONObject data = new JSONObject(response);
                            int success = data.getInt("success");
                            if (success == 1) {
                                Log.d(TAG,"success1");
                                Log.d(TAG,user);
                                Log.d(TAG,pass);
                                Log.d(TAG,response);
                                JSONArray main = data.getJSONArray("login");
                                for(int i=0;i<main.length();i++){
                                    JSONObject temp = new JSONObject(main.get(i).toString());
                                    LoginDataVO loginDataVO = new LoginDataVO();
                                    loginDataVO.setId(temp.getInt("id"));
                                    loginDataVO.setUser(user);
                                    loginDataVO.setPassword(pass);
                                    loginDataVO.setIdPlanta(idPlanta);
                                    loginDataVO.setIdCultivo(idCultivo);
                                    loginDataVO.setListIdTipoProcesos(temp.getString("idTipoProceso"));
                                    loginDataVO.setName(temp.getString("evaluador"));
                                    loginDataVO.setLastName("");
                                    /***

                                     GUARDAMDP USARIO
                                     */
                                    new LoginDataDAO(ctx).guardarUsuarioNuevo(loginDataVO);

                                    if( verificarLogueo() != null){
                                        LoginDataVO u = verificarLogueo();
                                        if(u!=null){
                                            //Toast.makeText(ctx,"Espere un momento...",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ctx, ActivityPostloader.class);
                                            ctx.startActivity(intent);
                                        }else {
                                            Toast.makeText(ctx,"Error de Base de Datos Interna",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }else{
                                Toast.makeText(ctx,"Verifique su Usuario y/o Contraseña",Toast.LENGTH_LONG).show();
                                Log.d(TAG,"success0");
                                Log.d(TAG,user);
                                Log.d(TAG,pass);
                            }

                        } catch (JSONException e) {
                            Log.d(TAG,e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(ctx,"Error conectando con el servidor",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(POST_USER, user);
                params.put(POST_PASSWORD, pass);
                params.put(POST_IDPLANTA, String.valueOf(idPlanta));
                params.put(POST_IDCULTIVO, String.valueOf(idCultivo));
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
