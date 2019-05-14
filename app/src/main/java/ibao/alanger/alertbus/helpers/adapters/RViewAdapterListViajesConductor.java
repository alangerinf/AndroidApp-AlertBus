package ibao.alanger.alertbus.helpers.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.models.vo.ViajeVO;

public class RViewAdapterListViajesConductor extends RecyclerView.Adapter<RViewAdapterListViajesConductor.ViewHolder>  {

    private Context ctx;
    private List<ViajeVO> viajeVOList;
    private RecyclerView recyclerView;

    private Dialog dialogAlert;
    final private String TAG = RViewAdapterListViajesConductor.class.getSimpleName();

    public RViewAdapterListViajesConductor(Context ctx, List<ViajeVO> viajeVOList, RecyclerView recyclerView) {
        this.ctx = ctx;
        this.viajeVOList = viajeVOList;
        this.recyclerView = recyclerView;
        dialogAlert = new Dialog(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_viajeconductor_detail,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {
        final ViajeVO viajeVO = viajeVOList.get(pos);
        int colorDisable = ContextCompat.getColor(ctx, R.color.materialGrey400);
        int colorEnable = ContextCompat.getColor(ctx, R.color.customGreen);
        int colorRed = ContextCompat.getColor(ctx, R.color.redAccent700);


        holder.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterDialogMapa adapterDialogMapa = new AdapterDialogMapa(v.getContext(),-8.1329634,-79.049854,-8.125603,-79.031894);

                adapterDialogMapa.popDialog();

            }
        });

        switch (viajeVO.getStatus()){
            case 0:
                holder.tViewStatusEnCurso.setTextColor(colorEnable);
                holder.tViewStatusFinalizado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);

                break;

            case 1:
                holder.tViewStatusEnCurso.setTextColor(colorDisable);
                holder.tViewStatusFinalizado.setTextColor(colorEnable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);


                break;

            case 2:
                holder.tViewStatusEnCurso.setTextColor(colorDisable);
                holder.tViewStatusFinalizado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorEnable);



                break;
        }

        //fin labels
        holder.tViewProveedor.setText(""+viajeVO.getProveedor());
        holder.tViewPlaca.setText(""+viajeVO.getPlaca());
        holder.tViewConductor.setText(""+viajeVO.getConductor());
        holder.tViewRuta.setText(""+viajeVO.getRuta());
        holder.tViewHEmbarque.setText(""+getHour(viajeVO.gethInicio()));
        holder.tViewHDesembarque.setText(""+getHour(viajeVO.gethFin()));
        holder.tViewCapacidad.setText(""+viajeVO.getCapacidad());

        holder.tViewDateEmbarque.setText(""+getDate(viajeVO.gethInicio()));
        holder.tViewDateDesembarque.setText(""+getDate(viajeVO.gethFin()));


    }

    String getDate(String dateTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// replace with your start date string
        Date d = null;
        try {
            d = df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar gc = new GregorianCalendar();
        gc.setTime(d);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate=dateFormat.format(d);
        return formattedDate;

    }

    String getHour(String dateTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// replace with your start date string
        Date d = null;
        try {
            d = df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar gc = new GregorianCalendar();
        gc.setTime(d);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedDate=dateFormat.format(d);
        return formattedDate;


    }


    public float getFloatFormateado(float n){
        return ((int)(n*10))/10.0f;
    }


    @Override
    public int getItemCount() {
        return viajeVOList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        //elementos

        //fin labels


        TextView tViewHEmbarque;
        TextView tViewHDesembarque;

        TextView tViewProveedor ;
        TextView tViewPlaca ;
        TextView tViewConductor ;
        TextView tViewRuta ;
        TextView tViewCapacidad;

        TextView tViewDateDesembarque;
        TextView tViewDateEmbarque;


        //status
        TextView tViewStatusEnCurso;
        TextView tViewStatusFinalizado;
        TextView tViewStatusSincronizado;


        //buttons
        Button btnEnter;

        public ViewHolder(View itemView){
            super(itemView);
            tViewProveedor = itemView.findViewById(R.id.tViewProveedor);
            tViewPlaca = itemView.findViewById(R.id.tViewPlaca);
            tViewConductor = itemView.findViewById(R.id.tViewConductor);
            tViewRuta = itemView.findViewById(R.id.tViewRuta);
            tViewHEmbarque = itemView.findViewById(R.id.tViewHEmbarque);
            tViewHDesembarque = itemView.findViewById(R.id.tViewHDesembarque);
            tViewCapacidad = itemView.findViewById(R.id.tViewCapacidad);

            tViewStatusEnCurso = itemView.findViewById(R.id.tViewStatusEnCurso);
            tViewStatusFinalizado = itemView.findViewById(R.id.tViewStatusFinalizado);
            tViewStatusSincronizado = itemView.findViewById(R.id.tViewStatusSincronizado);

            tViewDateEmbarque= itemView.findViewById(R.id.tViewDateEmbarque);
            tViewDateDesembarque= itemView.findViewById(R.id.tViewDateDesembarque);
            btnEnter = (Button) itemView.findViewById(R.id.btnEnter);
        }
    }




}


