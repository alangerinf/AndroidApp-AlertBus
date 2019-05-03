package com.ibao.alanger.agroq.helpers.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibao.alanger.agroq.R;
import com.ibao.alanger.agroq.models.dao.EvaluacionDetalleDAO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.EvaluacionDetalleVO;
import com.ibao.alanger.agroq.views.ActivityEvaluacionDetalle;
import com.ibao.alanger.agroq.views.ActivityMuestra;

import java.util.List;

public class RViewAdapterEvaluacionDetalle_new extends RecyclerView.Adapter<RViewAdapterEvaluacionDetalle_new.ViewHolder> {

    private Context ctx;
    private List<EvaluacionDetalleVO> evaluacionDetalleVOList;
    private RecyclerView lView;
    public static Boolean finalizadoEva;
    public static Boolean finalizadoCri;
    private boolean isEditable;
    private static BaseAdapter baseAdapter;
    private static EditText eTextNFrutos ;

    public static final int RESULT_REQUEST_CRIDE=500;

    String TAG = RViewAdapterEvaluacionDetalle_new.class.getSimpleName();

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //elementos

        //fin labels
        ConstraintLayout cLayoutTotal ;
        //bottons

        FloatingActionButton fAButtonDelete;
        TextView tViewNameEvaluacion ;
        TextView tViewCantidadTotal ;
        TextView tViewPorcentajeTotal;
        TextView tView_total;
        TextView tView_twoPoints;


        public ViewHolder(View itemView){
            super(itemView);
            cLayoutTotal = itemView.findViewById(R.id.cLayoutComplete);

            fAButtonDelete = itemView.findViewById(R.id.fAButtonDelete);
            tViewNameEvaluacion=itemView.findViewById(R.id.tViewNameCri);
            tViewCantidadTotal= itemView.findViewById(R.id.tViewCantidadTotal);
            tViewPorcentajeTotal = itemView.findViewById(R.id.tViewPorcentaje);
            tView_total = itemView.findViewById(R.id.tView_total);
            tView_twoPoints = itemView.findViewById(R.id.tView_twoPoints);
        }

    }

    public RViewAdapterEvaluacionDetalle_new(Context ctx, List<EvaluacionDetalleVO> evaluacionDetalleVOList, RecyclerView lView, boolean isEditable, EditText eTextNFrutos) {
        this.ctx = ctx;
        this.evaluacionDetalleVOList = evaluacionDetalleVOList;
        this.lView = lView;
        this.isEditable = isEditable;
        this.eTextNFrutos = eTextNFrutos;
        this.finalizadoEva = false;
        this.finalizadoCri = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        progress.show();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_evaluaciondetalle_new,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public EvaluacionDetalleVO getItem(int position) {
        return evaluacionDetalleVOList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int posEva) {

                holder.cLayoutTotal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ctx, ActivityEvaluacionDetalle.class);
                        i.putExtra("idEvaDe",getItem(posEva).getId());
                        i.putExtra("isEditable",isEditable);
                        ((Activity) ctx).startActivityForResult(i, ActivityMuestra.REQUEST_CRITERIODETALLE);
                    }
                });

                holder.tViewNameEvaluacion.setText(getItem(posEva).getNameEvaluacion());
                holder.fAButtonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog dialogClose = new Dialog(ctx);
                        dialogClose.setContentView(R.layout.dialog_danger);
                        Button btnDialogClose = (Button) dialogClose.findViewById(R.id.buton_close);
                        Button btnDialogAcept = (Button) dialogClose.findViewById(R.id.buton_acept);
                        ImageView iViewDialogClose = (ImageView) dialogClose.findViewById(R.id.iViewDialogClose);
                        TextView mensaje = (TextView) dialogClose.findViewById(R.id.tViewRecomendacion);


                        if(evaluacionDetalleVOList.get(posEva).getIdEvaluacion()==7 || evaluacionDetalleVOList.get(posEva).getIdEvaluacion()==8){
                            holder.tView_total.setVisibility(View.INVISIBLE);
                            holder.tView_twoPoints.setVisibility(View.INVISIBLE);
                            holder.tViewCantidadTotal.setVisibility(View.INVISIBLE);
                            holder.tViewPorcentajeTotal.setVisibility(View.INVISIBLE);
                        }
                        mensaje.setText("Esta a punto de eliminar una Evaluación. \n ¿DESEA CONTINUAR? ");
                        iViewDialogClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogClose.dismiss();
                            }
                        });
                        btnDialogClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogClose.dismiss();

                            }
                        });

                        btnDialogAcept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final Animation animClose =
                                        android.view.animation.AnimationUtils.loadAnimation(ctx, R.anim.zoom_back_out_item);

                                boolean x=new EvaluacionDetalleDAO(ctx).deleteById(evaluacionDetalleVOList.get(posEva).getId());

                                if(!x){
                                    Toast.makeText(ctx,"Error al eliminar",Toast.LENGTH_SHORT).show();
                                }else{
                                    evaluacionDetalleVOList.remove(posEva);
                                    if(evaluacionDetalleVOList.size()>0){
                                        Log.d(TAG,"LISTA  DE EVALUACIONES NO VACIA "+evaluacionDetalleVOList.size());
                                        eTextNFrutos.setFocusable(false);
                                        eTextNFrutos.setClickable(false);
                                        eTextNFrutos.setFocusableInTouchMode(false);
                                    }else {
                                        Log.d(TAG,"LISTA  DE EVALUACIONES VACIA "+evaluacionDetalleVOList.size());
                                        eTextNFrutos.setFocusable(true);
                                        eTextNFrutos.setClickable(true);
                                        eTextNFrutos.setFocusableInTouchMode(true);
                                    }
                                    //holder.cLayoutTotal.startAnimation(animClose);
                                    Handler handler = new Handler();
                                    handler.postDelayed(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    //progress.show();
                                                    RViewAdapterEvaluacionDetalle_new.super.notifyDataSetChanged();
                                                }
                                            },10
                                    );
                                }
                                dialogClose.dismiss();
                            }
                        });

                        dialogClose.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogClose.show();
                    }
                });



        if(evaluacionDetalleVOList.get(posEva).getIdEvaluacion()==3||evaluacionDetalleVOList.get(posEva).isMatSec()|| evaluacionDetalleVOList.get(posEva).getIdEvaluacion()==7|| evaluacionDetalleVOList.get(posEva).getIdEvaluacion()==8){
            holder.tView_total.setVisibility(View.INVISIBLE);
            holder.tView_twoPoints.setVisibility(View.INVISIBLE);
            holder.tViewCantidadTotal.setVisibility(View.INVISIBLE);
            holder.tViewPorcentajeTotal.setVisibility(View.INVISIBLE);
            // holder.tViewCantidadTotal.setText(""+evaluacionDetalleVOList.get(posEva).getCantidad()+"%");
        }else {
            holder.tViewPorcentajeTotal.setText(""+evaluacionDetalleVOList.get(posEva).getPorcentaje()+"%");
            holder.tViewCantidadTotal.setText(""+evaluacionDetalleVOList.get(posEva).getCantidad());
        }



    }

    @Override
    public int getItemCount() {
        return evaluacionDetalleVOList.size();
    }

}
