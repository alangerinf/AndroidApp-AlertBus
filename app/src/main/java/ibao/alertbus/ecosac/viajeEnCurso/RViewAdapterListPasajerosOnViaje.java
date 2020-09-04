package ibao.alertbus.ecosac.viajeEnCurso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.app.AppController;
import ibao.alertbus.ecosac.models.dao.LoginDataDAO;
import ibao.alertbus.ecosac.models.dao.PasajeroDAO;
import ibao.alertbus.ecosac.models.dao.RestriccionDAO;
import ibao.alertbus.ecosac.models.vo.PasajeroVO;

import static ibao.alertbus.ecosac.utilities.Utilities.URL_BUSCARTRABAJADOR;

public class RViewAdapterListPasajerosOnViaje extends RecyclerView.Adapter<RViewAdapterListPasajerosOnViaje.ViewHolder>  {

    private Context ctx;
    private List<PasajeroVO> pasajeroVOList;

    final private String TAG = RViewAdapterListPasajerosOnViaje.class.getSimpleName();

    public RViewAdapterListPasajerosOnViaje(Context ctx, List<PasajeroVO> pasajeroVOList) {
        this.ctx = ctx;
        this.pasajeroVOList = pasajeroVOList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_viaje_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {
        final PasajeroVO pasajeroVO = pasajeroVOList.get(pos);

        //fin labels
        holder.avi_tViewName.setText(""+pasajeroVO.getName());
        holder.avi_tViewDNI.setText(""+pasajeroVO.getDni());
        holder.avi_tViewFecha.setText(""+pasajeroVO.gethSubida());
        holder.avi_tViewObservacion.setText(""+pasajeroVO.getObservacion());
        Log.d(TAG,""+pasajeroVO.gethSubida()+" "+pasajeroVO.gethBajada());
        if(pasajeroVO.gethBajada()==null || pasajeroVO.gethBajada().equals("")){// si solo tiene hora de subida
            holder.avi_tViewFecha.setTextColor(ContextCompat.getColor(ctx, R.color.customGreen));
        }else {// si ya tiene hora de bajada
            holder.avi_tViewFecha.setText(pasajeroVO.gethBajada());
            holder.avi_tViewFecha.setTextColor(ContextCompat.getColor(ctx,R.color.red));

        }
        if (pasajeroVO.getName().equals("")){//si no tiene nombre
            Log.d(TAG,"consultando trabajador"+pasajeroVO.getName());
            //consultarTrabajador(pasajeroVO,holder,pos);
        }else {
            Log.d(TAG,"Trabajador con nombre"+pasajeroVO.getName());
        }

    }

    @Override
    public int getItemCount() {
        return pasajeroVOList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        //elementos

        //fin labels
        TextView avi_tViewDNI ;
        TextView avi_tViewName ;
        TextView avi_tViewObservacion ;
        TextView avi_tViewFecha ;

        public ViewHolder(View itemView){
            super(itemView);
            avi_tViewDNI = itemView.findViewById(R.id.avi_tViewDNI);
            avi_tViewName = itemView.findViewById(R.id.avi_tViewName);
            avi_tViewObservacion = itemView.findViewById(R.id.avi_tViewObservacion);
            avi_tViewFecha = itemView.findViewById(R.id.avi_tViewFecha);

        }
    }


    public void  consultarTrabajador(PasajeroVO pasajeroVO,ViewHolder holder, final int pos){

        StringRequest sr = new StringRequest(Request.Method.POST,
                URL_BUSCARTRABAJADOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG,"RESP: "+response);

                        if(!response.isEmpty()){
                            try {
                                JSONObject main = new JSONObject(response);
                                Log.d(TAG,"flag1");
                                if(main.getInt("success")==1){
                                    Log.d(TAG,"flag2");
                                    JSONObject trabajadorJson = main.getJSONObject("trabajador");
                                    pasajeroVO.setName(trabajadorJson.getString("TRABAJADOR"));
                                    if(!trabajadorJson.getString("SUSPENSION").equalsIgnoreCase("null")){
                                        pasajeroVO.setObservacion(trabajadorJson.getString("SUSPENSION"));
                                        new RestriccionDAO(ctx).insertar(pasajeroVO.getName(),pasajeroVO.getObservacion(),pasajeroVO.getIdViaje());
                                    }
                                    new PasajeroDAO(ctx).updateNameObservacion(pasajeroVO);
                                    onBindViewHolder(holder,pos);
                                }else {
                                    pasajeroVO.setName("Sin Nombre");
                                }

                            } catch (JSONException e) {
                              //  Toast.makeText(ctx, "Error al obtener nombre del trabajador", Toast.LENGTH_LONG).show();
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
                        // Toast.makeText(ctx,"Error al conectarse, verifique su conexion con el servidor",Toast.LENGTH_LONG).show();
                        //  Toast.makeText(ctx,error.toString(),Toast.LENGTH_LONG).show();
                        Log.d("error 2",error.toString());

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();

                //informacion del usuario
                Gson gson = new Gson();
                String usuarioJson = gson.toJson(new LoginDataDAO(ctx).verficarLogueo());
                params.put("usuario",usuarioJson);
                params.put("dni",pasajeroVO.getDni());

                Log.d(TAG,"usuario:"+usuarioJson);
                Log.d(TAG,"dni:<"+pasajeroVO.getDni()+">");

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


