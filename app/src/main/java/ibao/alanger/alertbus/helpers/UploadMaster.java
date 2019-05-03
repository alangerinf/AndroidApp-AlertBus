package com.ibao.alanger.agroq.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ibao.alanger.agroq.app.AppController;
import com.ibao.alanger.agroq.models.dao.DescarteDAO;
import com.ibao.alanger.agroq.models.dao.DespachoDAO;
import com.ibao.alanger.agroq.models.dao.FotoDAO;
import com.ibao.alanger.agroq.models.dao.LoginDataDAO;
import com.ibao.alanger.agroq.models.dao.ProduccionDAO;
import com.ibao.alanger.agroq.models.dao.RecepcionDAO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.CriterioDetalleVO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.DescarteVO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.DespachoVO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.EvaluacionDetalleVO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.FotoVO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.MuestraVO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.ProduccionVO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.RecepcionVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.ibao.alanger.agroq.utilities.Utilities.URL_UPLOAD_MASTER_DESCARTE;
import static com.ibao.alanger.agroq.utilities.Utilities.URL_UPLOAD_MASTER_DESPACHO;
import static com.ibao.alanger.agroq.utilities.Utilities.URL_UPLOAD_MASTER_PRODUCCION;
import static com.ibao.alanger.agroq.utilities.Utilities.URL_UPLOAD_MASTER_RECEPCION;

public class UploadMaster {

    Context ctx;
    ProgressDialog progress;
    String TAG = UploadMaster.class.getSimpleName();
    public static int status;

    public UploadMaster(Context ctx)
    {
        status=0;
        this.ctx = ctx;
    }

    public void UploadRecepcion(final List<RecepcionVO> recepcionVOList, final List<MuestraVO> muestraVOList, final List<EvaluacionDetalleVO> evaluacionDetalleVOList, final List<CriterioDetalleVO> criterioDetalleVOList, final List<FotoVO> FotosUp){
//        progress = ProgressDialog.show(ctx, "", "Loading...");
        //progress.setCancelable(false);
        //progress.setMessage("Intentando descargar Configuracion Criterio");
        //progress.show();

        status=1;
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_UPLOAD_MASTER_RECEPCION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  progress.dismiss();
                        Log.d("respuestaServidor",response);
                        status=2;
/*
                        if(response.charAt(0)!='{'){
                            int i = response.indexOf("success");
                            if(i!=-1){
                                response = response.substring(i-2);
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                            }
                        }

                        */
                        for(RecepcionVO re: recepcionVOList){
                            new RecepcionDAO(ctx).clearTableUpload(re.getId());
                        }

                        //new RecepcionDAO(ctx).clearTableUpload();
                        //new EvaluacionDAO(ctx).clearTableUpload();
                        //new MuestrasDAO(ctx).clearTableUpload();
                        //new RecomendacionDAO(ctx).clearTableUpload();

                        if(!response.isEmpty()){
                            try {
                                JSONObject main = new JSONObject(response);
                                Log.d("asd ","flag1");
                                if(main.getInt("success")==1){
                                    Log.d("asd ","flag2");
                                    JSONArray datosFotos = main.getJSONArray("data");
                                    Log.d("datosFotos",datosFotos.toString());
                                    for(int i=0;i<datosFotos.length();i++){
                                        //Toast.makeText(ctx,""+datosFotos.getJSONObject(i).getInt("idInspeccionEvidencia"),Toast.LENGTH_SHORT).show();
                                        UploadFotos.status=0;
                                        JSONObject foto = new JSONObject(datosFotos.get(i).toString());
                                        Log.d(TAG,"idFoto:"+foto
                                                .getInt("idFoto"));
                                        Log.d(TAG,"idMuestraEvidencia:"+foto
                                                .getInt("idMuestraEvidencia"));



                                        String bitmap = "";
                                        try{
                                            FotoVO fotoTemp = new FotoDAO(ctx).consultarById_Upload(
                                                    foto
                                                            .getInt("idFoto"));
                                            bitmap = fotoTemp.getStringBitmap();
                                        }catch (Exception e){
                                            bitmap = "no Encontrado";
                                        }

                                        try{
                                            int idInspeccionEvidencia = foto.getInt("idMuestraEvidencia");
                                                    new UploadFotos(ctx).Upload(
                                                            foto.getInt("idFoto")
                                                            ,idInspeccionEvidencia
                                                            ,bitmap
                                                            ,i
                                                            ,datosFotos.length());
                                            Log.d("datosfotos"+i,foto.toString());
                                        }catch (Exception e){
                                            Log.d("errorFotos",e.toString());
                                        }
                                    }
                                    new FotoDAO(ctx).clearTableUpload();
                                    status=3;
                                   // Toast.makeText(ctx,datosFotos.toString(),Toast.LENGTH_LONG).show();
                                    Log.d("datosfotos",datosFotos.toString());
                                }
                                status=3;//SE TERMINO SIN FOTOS

                            } catch (JSONException e) {
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos JSON, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                                Log.d(TAG,"flag1");
                                Log.d(TAG, e.toString());
                                Log.d(TAG, response);
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
                params.put("usuario",usuarioJson);

                //recepciones
                gson = new Gson();
                List<RecepcionVO> recepcionVOS = recepcionVOList;
                String RecepcionesJson = gson.toJson(
                        recepcionVOS,
                        new TypeToken<ArrayList<RecepcionVO>>() {}.getType());
                params.put("recepciones",RecepcionesJson);

                //muestras
                gson = new Gson();
                List<MuestraVO> muestraVOS = muestraVOList;
                String muestrasJson = gson.toJson(
                        muestraVOS,
                        new TypeToken<ArrayList<MuestraVO>>() {}.getType());
                params.put("muestras",muestrasJson);

                //evaluacionesDetalle
                gson = new Gson();
                List<EvaluacionDetalleVO> evaluacionDetalleVOS = evaluacionDetalleVOList;
                String evalucionesDetalleJson = gson.toJson(
                        evaluacionDetalleVOS,
                        new TypeToken<ArrayList<EvaluacionDetalleVO>>() {}.getType());
                params.put("evalucionesDetalle",evalucionesDetalleJson);


                //Criterios Detalle
                gson = new Gson();
                List<CriterioDetalleVO> criterioDetalleVOS = criterioDetalleVOList;
                String criteriosDetalleJson = gson.toJson(
                        criterioDetalleVOS,
                        new TypeToken<ArrayList<CriterioDetalleVO>>() {}.getType());
                params.put("criteriosDetalle",criteriosDetalleJson);

                //fotos
                gson = new Gson();
                List<FotoVO> fotos = FotosUp;
                String fotosJson = gson.toJson(
                        fotos,
                        new TypeToken<ArrayList<FotoVO>>() {}.getType());

                params.put("fotos",fotosJson);


                Log.d(TAG,"usuario:"+usuarioJson);
                Log.d(TAG,"recepciones:"+RecepcionesJson);
                Log.d(TAG,"muestras:"+muestrasJson);
                Log.d(TAG,"evalucionesDetalle:"+evalucionesDetalleJson);
                Log.d(TAG,"criteriosDetalle:"+criteriosDetalleJson);
                Log.d(TAG,"fotos:"+fotosJson);


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



    public void UploadProduccion(final List<ProduccionVO> produccionVOList, final List<MuestraVO> muestraVOList, final List<EvaluacionDetalleVO> evaluacionDetalleVOList, final List<CriterioDetalleVO> criterioDetalleVOList, final List<FotoVO> FotosUp){
//        progress = ProgressDialog.show(ctx, "", "Loading...");
        //progress.setCancelable(false);
        //progress.setMessage("Intentando descargar Configuracion Criterio");
        //progress.show();

        status=1;
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_UPLOAD_MASTER_PRODUCCION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progress.dismiss();
                        Log.d("respuestaServidor",response);
                        status=2;
/*
                        if(response.charAt(0)!='{'){
                            int i = response.indexOf("success");
                            if(i!=-1){
                                response = response.substring(i-2);
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                            }
                        }

                        */

                        for(ProduccionVO pro: produccionVOList){
                            new ProduccionDAO(ctx).clearTableUpload(pro.getId());
                        }

                        //new ProduccionDAO(ctx).clearTableUpload();
                        //new RecepcionDAO(ctx).clearTableUpload();
                        //new EvaluacionDAO(ctx).clearTableUpload();
                        //new MuestrasDAO(ctx).clearTableUpload();
                        //new RecomendacionDAO(ctx).clearTableUpload();

                        if(!response.isEmpty()){
                            try {
                                JSONObject main = new JSONObject(response);
                                Log.d("asd ","flag1");
                                if(main.getInt("success")==1){
                                    Log.d("asd ","flag2");
                                    JSONArray datosFotos = main.getJSONArray("data");
                                    Log.d("datosFotos",datosFotos.toString());
                                    for(int i=0;i<datosFotos.length();i++){
                                        //Toast.makeText(ctx,""+datosFotos.getJSONObject(i).getInt("idInspeccionEvidencia"),Toast.LENGTH_SHORT).show();
                                        UploadFotos.status=0;
                                        JSONObject foto = new JSONObject(datosFotos.get(i).toString());
                                        Log.d(TAG,"idFoto:"+foto
                                                .getInt("idFoto"));
                                        Log.d(TAG,"idMuestraEvidencia:"+foto
                                                .getInt("idMuestraEvidencia"));

                                        String bitmap = "";
                                        try{
                                            FotoVO fotoTemp = new FotoDAO(ctx).consultarById_Upload(
                                                    foto
                                                            .getInt("idFoto"));
                                            bitmap = fotoTemp.getStringBitmap();
                                        }catch (Exception e){
                                            bitmap = "no Encontrado";
                                        }

                                        try{
                                            int idInspeccionEvidencia = foto.getInt("idMuestraEvidencia");
                                            new UploadFotos(ctx).Upload(
                                                    foto.getInt("idFoto")
                                                    ,idInspeccionEvidencia
                                                    ,bitmap
                                                    ,i
                                                    ,datosFotos.length());
                                            Log.d("datosfotos"+i,foto.toString());
                                        }catch (Exception e){
                                            Log.d("errorFotos",e.toString());
                                        }
                                    }
                                    new FotoDAO(ctx).clearTableUpload();
                                    status=3;
                                    // Toast.makeText(ctx,datosFotos.toString(),Toast.LENGTH_LONG).show();
                                    Log.d("datosfotos",datosFotos.toString());
                                }
                                status=3;//SE TERMINO SIN FOTOS

                            } catch (JSONException e) {
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos JSON, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                                Log.d(TAG,"flag1");
                                Log.d(TAG, e.toString());
                                Log.d(TAG, response);
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
                params.put("usuario",usuarioJson);

                //recepciones
                gson = new Gson();
                List<ProduccionVO> produccionVOS = produccionVOList;
                String ProduccionesJson = gson.toJson(
                        produccionVOS,
                        new TypeToken<ArrayList<ProduccionVO>>() {}.getType());
                params.put("producciones",ProduccionesJson);

                //muestras
                gson = new Gson();
                List<MuestraVO> muestraVOS = muestraVOList;
                String muestrasJson = gson.toJson(
                        muestraVOS,
                        new TypeToken<ArrayList<MuestraVO>>() {}.getType());
                params.put("muestras",muestrasJson);

                //evaluacionesDetalle
                gson = new Gson();
                List<EvaluacionDetalleVO> evaluacionDetalleVOS = evaluacionDetalleVOList;
                String evalucionesDetalleJson = gson.toJson(
                        evaluacionDetalleVOS,
                        new TypeToken<ArrayList<EvaluacionDetalleVO>>() {}.getType());
                params.put("evalucionesDetalle",evalucionesDetalleJson);


                //Criterios Detalle
                gson = new Gson();
                List<CriterioDetalleVO> criterioDetalleVOS = criterioDetalleVOList;
                String criteriosDetalleJson = gson.toJson(
                        criterioDetalleVOS,
                        new TypeToken<ArrayList<CriterioDetalleVO>>() {}.getType());
                params.put("criteriosDetalle",criteriosDetalleJson);

                //fotos
                gson = new Gson();
                List<FotoVO> fotos = FotosUp;
                String fotosJson = gson.toJson(
                        fotos,
                        new TypeToken<ArrayList<FotoVO>>() {}.getType());

                params.put("fotos",fotosJson);


                Log.d(TAG,"usuario:"+usuarioJson);
                Log.d(TAG,"producciones:"+ProduccionesJson);
                Log.d(TAG,"muestras:"+muestrasJson);
                Log.d(TAG,"evalucionesDetalle:"+evalucionesDetalleJson);
                Log.d(TAG,"criteriosDetalle:"+criteriosDetalleJson);
                Log.d(TAG,"fotos:"+fotosJson);


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


    public void UploadDespacho(final List<DespachoVO> despachoVOList, final List<MuestraVO> muestraVOList, final List<EvaluacionDetalleVO> evaluacionDetalleVOList, final List<CriterioDetalleVO> criterioDetalleVOList, final List<FotoVO> FotosUp){
//        progress = ProgressDialog.show(ctx, "", "Loading...");
        //progress.setCancelable(false);
        //progress.setMessage("Intentando descargar Configuracion Criterio");
        //progress.show();

        status=1;
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_UPLOAD_MASTER_DESPACHO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progress.dismiss();
                        Log.d(TAG,"respuesta servidor :"+response);
                        status=2;
/*
                        if(response.charAt(0)!='{'){
                            int i = response.indexOf("success");
                            if(i!=-1){
                                response = response.substring(i-2);
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                            }
                        }

                        */

                        for(DespachoVO desp: despachoVOList){
                            new DespachoDAO(ctx).clearTableUpload(desp.getId());
                        }

                        //new RecepcionDAO(ctx).clearTableUpload();
                        //new EvaluacionDAO(ctx).clearTableUpload();
                        //new MuestrasDAO(ctx).clearTableUpload();
                        //new RecomendacionDAO(ctx).clearTableUpload();

                        if(!response.isEmpty()){
                            try {
                                JSONObject main = new JSONObject(response);
                                Log.d("asd ","flag1");
                                if(main.getInt("success")==1){
                                    Log.d("asd ","flag2");
                                    JSONArray datosFotos = main.getJSONArray("data");
                                    Log.d("datosFotos",datosFotos.toString());
                                    for(int i=0;i<datosFotos.length();i++){
                                        //Toast.makeText(ctx,""+datosFotos.getJSONObject(i).getInt("idInspeccionEvidencia"),Toast.LENGTH_SHORT).show();
                                        UploadFotos.status=0;
                                        JSONObject foto = new JSONObject(datosFotos.get(i).toString());
                                        Log.d(TAG,"idFoto:"+foto
                                                .getInt("idFoto"));
                                        Log.d(TAG,"idMuestraEvidencia:"+foto
                                                .getInt("idMuestraEvidencia"));

                                        String bitmap = "";
                                        try{
                                            FotoVO fotoTemp = new FotoDAO(ctx).consultarById_Upload(
                                                    foto
                                                            .getInt("idFoto"));
                                            bitmap = fotoTemp.getStringBitmap();
                                        }catch (Exception e){
                                            bitmap = "no Encontrado";
                                        }

                                        try{
                                            int idInspeccionEvidencia = foto.getInt("idMuestraEvidencia");
                                            new UploadFotos(ctx).Upload(
                                                    foto.getInt("idFoto")
                                                    ,idInspeccionEvidencia
                                                    ,bitmap
                                                    ,i
                                                    ,datosFotos.length());
                                            Log.d("datosfotos"+i,foto.toString());
                                        }catch (Exception e){
                                            Log.d("errorFotos",e.toString());
                                        }
                                    }
                                    new FotoDAO(ctx).clearTableUpload();
                                    status=3;
                                    // Toast.makeText(ctx,datosFotos.toString(),Toast.LENGTH_LONG).show();
                                    Log.d("datosfotos",datosFotos.toString());
                                }
                                status=3;//SE TERMINO SIN FOTOS

                            } catch (JSONException e) {
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos JSON, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                                Log.d(TAG,"flag1");
                                Log.d(TAG, e.toString());
                                Log.d(TAG, response);
                                status = -1;
                            }
                        }else{
                            Log.d(TAG,"No se recibio respuesta del Servidor");
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
                params.put("usuario",usuarioJson);

                //recepciones
                gson = new Gson();
                List<DespachoVO> despachoVOS = despachoVOList;
                String DespachoJson = gson.toJson(
                        despachoVOS,
                        new TypeToken<ArrayList<DespachoVO>>() {}.getType());
                params.put("despachos",DespachoJson);

                //muestras
                gson = new Gson();
                List<MuestraVO> muestraVOS = muestraVOList;
                String muestrasJson = gson.toJson(
                        muestraVOS,
                        new TypeToken<ArrayList<MuestraVO>>() {}.getType());
                params.put("muestras",muestrasJson);

                //evaluacionesDetalle
                gson = new Gson();
                List<EvaluacionDetalleVO> evaluacionDetalleVOS = evaluacionDetalleVOList;
                String evalucionesDetalleJson = gson.toJson(
                        evaluacionDetalleVOS,
                        new TypeToken<ArrayList<EvaluacionDetalleVO>>() {}.getType());
                params.put("evalucionesDetalle",evalucionesDetalleJson);


                //Criterios Detalle
                gson = new Gson();
                List<CriterioDetalleVO> criterioDetalleVOS = criterioDetalleVOList;
                String criteriosDetalleJson = gson.toJson(
                        criterioDetalleVOS,
                        new TypeToken<ArrayList<CriterioDetalleVO>>() {}.getType());
                params.put("criteriosDetalle",criteriosDetalleJson);

                //fotos
                gson = new Gson();
                List<FotoVO> fotos = FotosUp;
                String fotosJson = gson.toJson(
                        fotos,
                        new TypeToken<ArrayList<FotoVO>>() {}.getType());

                params.put("fotos",fotosJson);


                Log.d(TAG,"usuario:"+usuarioJson);
                Log.d(TAG,"despachos:"+DespachoJson);
                Log.d(TAG,"muestras:"+muestrasJson);
                Log.d(TAG,"evalucionesDetalle:"+evalucionesDetalleJson);
                Log.d(TAG,"criteriosDetalle:"+criteriosDetalleJson);
                Log.d(TAG,"fotos:"+fotosJson);


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




    public void UploadDescarte(final List<DescarteVO> descarteVOList, final List<MuestraVO> muestraVOList, final List<EvaluacionDetalleVO> evaluacionDetalleVOList, final List<CriterioDetalleVO> criterioDetalleVOList, final List<FotoVO> FotosUp){
//        progress = ProgressDialog.show(ctx, "", "Loading...");
        //progress.setCancelable(false);
        //progress.setMessage("Intentando descargar Configuracion Criterio");
        //progress.show();

        status=1;
        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_UPLOAD_MASTER_DESCARTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progress.dismiss();
                        Log.d("respuestaServidor",response);
                        status=2;
/*
                        if(response.charAt(0)!='{'){
                            int i = response.indexOf("success");
                            if(i!=-1){
                                response = response.substring(i-2);
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                            }
                        }

                        */
                        for(DescarteVO desc: descarteVOList){
                            new DescarteDAO(ctx).clearTableUpload(desc.getId());
                        }
                        //new RecepcionDAO(ctx).clearTableUpload();
                        //new EvaluacionDAO(ctx).clearTableUpload();
                        //new MuestrasDAO(ctx).clearTableUpload();
                        //new RecomendacionDAO(ctx).clearTableUpload();

                        if(!response.isEmpty()){
                            try {
                                JSONObject main = new JSONObject(response);
                                Log.d(TAG,"flag1");
                                if(main.getInt("success")==1){
                                    Log.d("asd ","flag2");
                                    JSONArray datosFotos = main.getJSONArray("data");
                                    Log.d("datosFotos",datosFotos.toString());
                                    for(int i=0;i<datosFotos.length();i++){
                                        //Toast.makeText(ctx,""+datosFotos.getJSONObject(i).getInt("idInspeccionEvidencia"),Toast.LENGTH_SHORT).show();
                                        UploadFotos.status=0;
                                        JSONObject foto = new JSONObject(datosFotos.get(i).toString());
                                        Log.d(TAG,"idFoto:"+foto
                                                .getInt("idFoto"));
                                        Log.d(TAG,"idMuestraEvidencia:"+foto
                                                .getInt("idMuestraEvidencia"));
                                        String bitmap = "";
                                        try{
                                            FotoVO fotoTemp = new FotoDAO(ctx).consultarById_Upload(
                                                    foto
                                                            .getInt("idFoto"));
                                            bitmap = fotoTemp.getStringBitmap();
                                        }catch (Exception e){
                                            bitmap = "no Encontrado";
                                        }

                                        try{
                                            int idInspeccionEvidencia = foto.getInt("idMuestraEvidencia");
                                            new UploadFotos(ctx).Upload(
                                                    foto.getInt("idFoto")
                                                    ,idInspeccionEvidencia
                                                    ,bitmap
                                                    ,i
                                                    ,datosFotos.length());
                                            Log.d("datosfotos"+i,foto.toString());
                                        }catch (Exception e){
                                            Log.d("errorFotos",e.toString());
                                        }
                                    }
                                    new FotoDAO(ctx).clearTableUpload();
                                    status=3;
                                    // Toast.makeText(ctx,datosFotos.toString(),Toast.LENGTH_LONG).show();
                                    Log.d("datosfotos",datosFotos.toString());
                                }
                                status=3;//SE TERMINO SIN FOTOS

                            } catch (JSONException e) {
                                Toast.makeText(ctx, "Puede que no se hallan sincronizar sus datos JSON, por favor de aviso al administrador de la  aplicación", Toast.LENGTH_LONG).show();
                                Log.d(TAG,"flag1");
                                Log.d(TAG, e.toString());
                                Log.d(TAG, response);
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
                params.put("usuario",usuarioJson);

                //recepciones
                gson = new Gson();
                List<DescarteVO> descarteVOS = descarteVOList;
                String DescarteJson = gson.toJson(
                        descarteVOS,
                        new TypeToken<ArrayList<DescarteVO>>() {}.getType());
                params.put("descartes",DescarteJson);

                //muestras
                gson = new Gson();
                List<MuestraVO> muestraVOS = muestraVOList;
                String muestrasJson = gson.toJson(
                        muestraVOS,
                        new TypeToken<ArrayList<MuestraVO>>() {}.getType());
                params.put("muestras",muestrasJson);

                //evaluacionesDetalle
                gson = new Gson();
                List<EvaluacionDetalleVO> evaluacionDetalleVOS = evaluacionDetalleVOList;
                String evalucionesDetalleJson = gson.toJson(
                        evaluacionDetalleVOS,
                        new TypeToken<ArrayList<EvaluacionDetalleVO>>() {}.getType());
                params.put("evalucionesDetalle",evalucionesDetalleJson);


                //Criterios Detalle
                gson = new Gson();
                List<CriterioDetalleVO> criterioDetalleVOS = criterioDetalleVOList;
                String criteriosDetalleJson = gson.toJson(
                        criterioDetalleVOS,
                        new TypeToken<ArrayList<CriterioDetalleVO>>() {}.getType());
                params.put("criteriosDetalle",criteriosDetalleJson);

                //fotos
                gson = new Gson();
                List<FotoVO> fotos = FotosUp;
                String fotosJson = gson.toJson(
                        fotos,
                        new TypeToken<ArrayList<FotoVO>>() {}.getType());

                params.put("fotos",fotosJson);


                Log.d(TAG,"usuario:"+usuarioJson);
                Log.d(TAG,"descartes:"+DescarteJson);
                Log.d(TAG,"muestras:"+muestrasJson);
                Log.d(TAG,"evalucionesDetalle:"+evalucionesDetalleJson);
                Log.d(TAG,"criteriosDetalle:"+criteriosDetalleJson);
                Log.d(TAG,"fotos:"+fotosJson);


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




    private class UploadFileAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                String sourceFileUri = "/mnt/sdcard/abc.png";

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);

                if (sourceFile.isFile()) {

                    try {
                        String upLoadServerUri = "http://website.com/abc.php?";

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", sourceFileUri);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                                + sourceFileUri + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);

                        // Responses from the server (code and message)
                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn
                                .getResponseMessage();

                        if (serverResponseCode == 200) {

                            // messageText.setText(msg);
                            //Toast.makeText(ctx, "File Upload Complete.",
                            //      Toast.LENGTH_SHORT).show();

                            // recursiveDelete(mDirectory1);

                        }

                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {

                        // dialog.dismiss();
                        e.printStackTrace();

                    }
                    // dialog.dismiss();

                } // End else block


            } catch (Exception ex) {
                // dialog.dismiss();

                ex.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}