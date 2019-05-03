package com.ibao.alanger.agroq.helpers.downloaders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ibao.alanger.agroq.ConexionSQLiteHelper;
import com.ibao.alanger.agroq.app.AppController;
import com.ibao.alanger.agroq.models.dao.CalibreDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ibao.alanger.agroq.ConexionSQLiteHelper.VERSION_DB;
import static com.ibao.alanger.agroq.utilities.Utilities.DATABASE_NAME;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_ID;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_NAME;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_PESOMAX;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_PESOMIN;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_IDTIPOCALIBRE;
import static com.ibao.alanger.agroq.utilities.Utilities.URL_DOWNLOAD_TABLE_CALIBRE;

public class DownloaderCalibre {

    Context ctx;
 //   ProgressDialog progress;
    public static int status;
    public DownloaderCalibre(Context ctx){
        status=0;
        this.ctx = ctx;
    }

    public void download(){
        status=1;
   //     progress = new ProgressDialog(ctx);
   //     progress.setCancelable(false);
   //     progress.setMessage("Intentando descargar Empresas");
   //     progress.show();
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_DOWNLOAD_TABLE_CALIBRE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
     //                   progress.dismiss();
                        try {
                            JSONArray main = new JSONArray(response);
                            if(main.length()>0){
                                new CalibreDAO(ctx).clearTableUpload();
                                status=2;
                            }
                            String insert = "INSERT INTO " +
                                    TABLE_CALIBRE+
                                    "("+
                                    TABLE_CALIBRE_COL_ID+","+
                                    TABLE_CALIBRE_COL_NAME+","+
                                    TABLE_CALIBRE_COL_PESOMIN+","+
                                    TABLE_CALIBRE_COL_PESOMAX+","+
                                    TABLE_CALIBRE_COL_IDTIPOCALIBRE +""+
                                    ")"+
                                    "VALUES ";
                            for(int i=0;i<main.length();i++){
                                JSONObject data = new JSONObject(main.get(i).toString());
                                int id = data.getInt("id");
                                String nombre = /*String.valueOf(id)+"-"+*/data.getString("nombre");
                                String pesoMin = /*String.valueOf(id)+"-"+*/ data.getString("pesoMin");
                                String pesoMax = /*String.valueOf(id)+"-"+*/data.getString("pesoMax");
                                int idTipo = /*String.valueOf(id)+"-"+*/data.getInt("idTipoCalibre");

                                Log.d("CALIBRE DOWN","fila "+i+" : "+id+" "+nombre);
                                /*
                                if(new ZonaDAO(ctx).insertarZona(id,nombre)){

                                    Log.d("ZONADOWN","logro insertar"+id);
                                }
                                */
                                insert=insert+"("+id+",\""+nombre+"\""+",\""+pesoMin+"\",\""+pesoMax+"\", "+idTipo+")";
                                if(i%1000==0&& i>0){
                                    try{
                                        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB );
                                        SQLiteDatabase db = conn.getWritableDatabase();
                                        db.execSQL(insert);
                                        db.close();
                                        conn.close();
                                        insert = "INSERT INTO " +
                                                TABLE_CALIBRE+
                                                "("+
                                                TABLE_CALIBRE_COL_ID+","+
                                                TABLE_CALIBRE_COL_NAME+","+
                                                TABLE_CALIBRE_COL_PESOMIN+","+
                                                TABLE_CALIBRE_COL_PESOMAX+","+
                                                TABLE_CALIBRE_COL_IDTIPOCALIBRE +""+
                                                ")"+
                                                "VALUES ";
                                    }catch (Exception e){
                                        Log.d("errorCR",e.toString());
                                    }
                                }else {
                                    if(main.length()-1!=i ){
                                        insert=insert+",";
                                    }
                                }
                            }
                            try{
                                ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB );
                                SQLiteDatabase db = conn.getWritableDatabase();
                                db.execSQL(insert);
                                db.close();
                                conn.close();
                            }catch (Exception e){
                                Log.d("errorCR",e.toString());
                            }
                        status=3;
                        } catch (JSONException e) {
                            Log.d("calibreDOWN ",e.toString());
                            status=-1;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
       //                 progress.dismiss();
                        Toast.makeText(ctx,"Error conectando con el servidor",Toast.LENGTH_LONG).show();
                        status=-2;
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
/*
                UsuarioVO temp = new LoginHelper(ctx).verificarLogueo();
                params.put("id",String.valueOf(temp.getId()));
                params.put("idInspector",String.valueOf(temp.getCodigo()));
*/
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
