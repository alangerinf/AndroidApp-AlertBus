package com.ibao.alanger.agroq.helpers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;


import com.ibao.alanger.agroq.R;

import com.ibao.alanger.agroq.models.vo.entitiesInternal.EvaluacionDetalleVO;

import java.util.List;

public class RViewAdapterListEvaluacionDetalleISDefecto extends RecyclerView.Adapter<RViewAdapterListEvaluacionDetalleISDefecto.ViewHolder>  {

    private Context ctx;
    private List<EvaluacionDetalleVO> evaluacionDetalleVOList;
    private RecyclerView recyclerView;
    private boolean isEditable;

    final private String TAG = RViewAdapterListEvaluacionDetalleISDefecto.class.getSimpleName();

    public RViewAdapterListEvaluacionDetalleISDefecto(Context ctx, List<EvaluacionDetalleVO> evaluacionDetalleVOList, RecyclerView recyclerView, boolean isEditable) {
        this.ctx = ctx;
        this.evaluacionDetalleVOList = evaluacionDetalleVOList;
        this.recyclerView = recyclerView;
        this.isEditable = isEditable;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_evaluaciondetalle_resultmuestra,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        final EvaluacionDetalleVO EVA = evaluacionDetalleVOList.get(pos);
        holder.tViewPorcentaje.setText(""+EVA.getPorcentaje()+"%");
        holder.tViewNameEvaluacion.setText(EVA.getNameEvaluacion());

        switch (EVA.getCalificacion()){
            case 0:
                holder.iViewCarita.setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.iViewCarita.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_good_24dp));
                break;
            case 2:
              holder.iViewCarita.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_regular));
                break;
            case 3:
            holder.iViewCarita.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_mal));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return evaluacionDetalleVOList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        //elementos

        //fin labels
        TextView tViewNameEvaluacion ;
        //bottons
        TextView tViewPorcentaje;
        ImageView iViewCarita;



        public ViewHolder(View itemView){
            super(itemView);
            tViewPorcentaje = itemView.findViewById(R.id.tViewPorcentaje);
            iViewCarita = itemView.findViewById(R.id.iViewCarita);
            tViewNameEvaluacion=itemView.findViewById(R.id.tViewNameCri);
        }
    }





}


