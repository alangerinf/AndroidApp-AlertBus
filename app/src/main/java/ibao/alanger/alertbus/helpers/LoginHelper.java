package ibao.alanger.alertbus.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ibao.alanger.alertbus.app.AppController;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.vo.LoginDataVO;
import ibao.alanger.alertbus.services.SearchViajesService;
import ibao.alanger.alertbus.services.UploadService;
import ibao.alanger.alertbus.views.MainConductorActivity;
import ibao.alanger.alertbus.views.MainSupervisorActivity;

import static ibao.alanger.alertbus.utilities.Utilities.URL_AUTENTIFICATION;


public class LoginHelper {

    public static String POST_USER = "usuario";
    public static String POST_PASS = "password";


    private String data_id = "idSupervisor";
    private String data_name = "name";
    private String data_typeUser = "typeUser";

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
    public void intentoLogueo(final String user, final String pass){
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

                            Log.d(TAG,"resp:"+response);
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
                                    loginDataVO.setId(temp.getInt(data_id));
                                    loginDataVO.setName(temp.getString(data_name));
                                    loginDataVO.setUser(user);
                                    loginDataVO.setPassword(pass);

                                    if(temp.getString(data_typeUser).equals("C")){
                                        loginDataVO.setTypeUser(0);// seteadoc omo conductor
                                    }else {// seteadoc omo Vigitante .---> bota letra "V"
                                        loginDataVO.setTypeUser(1);
                                    }


                                    loginDataVO.setLastName("");
                                    /***

                                     GUARDAMDP USARIO
                                     */
                                    new LoginDataDAO(ctx).guardarUsuarioNuevo(loginDataVO);

                                    if( verificarLogueo() != null){
                                        LoginDataVO u = verificarLogueo();
                                        if(u!=null){
                                            startServices();
                                            Intent intent;
                                            if(u.getTypeUser()==0){//si  es conductor
                                                intent = new Intent(ctx, MainConductorActivity.class);//para hacer testing cambiar segun requiera
                                            }else {// si es supervisor
                                                intent = new Intent(ctx, MainSupervisorActivity.class);//para hacer testing cambiar segun requiera
                                            }

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
                        Log.d(TAG,"ERROR: "+error.toString());
                        Toast.makeText(ctx,"Error conectando con el servidor"+URL_AUTENTIFICATION+ error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(POST_USER, user);
                params.put(POST_PASS, pass);
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
    void  startServices(){
        Intent service1 = new Intent(ctx, SearchViajesService.class);
        Intent service2 = new Intent(ctx, UploadService.class);

        if (new LoginDataDAO(ctx).verficarLogueo()!=null) {//si esta logueado

            if(new LoginDataDAO(ctx).verficarLogueo().getTypeUser()==0){// si es conductor

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ctx.startForegroundService (service1);
                    ctx.startForegroundService (service2);

                }else {
                    ctx.startService (service1);
                    ctx.startService (service2);

                }
            }else {// si es supervisor ==1
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ctx.startForegroundService (service2);// upload

                }else {
                    ctx.startService (service2);//upload
                }
            }

        }
    }

}
