package ibao.alanger.alertbus.helpers.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.views.DetailActivity;
import ibao.alanger.alertbus.views.RestricionActivity;

public class RViewAdapterListViajes extends RecyclerView.Adapter<RViewAdapterListViajes.ViewHolder>  {

    private Context ctx;
    private List<ViajeVO> viajeVOList;
    private RecyclerView recyclerView;

    private Dialog dialogAlert;
    final private String TAG = RViewAdapterListViajes.class.getSimpleName();

    public RViewAdapterListViajes(Context ctx, List<ViajeVO> viajeVOList, RecyclerView recyclerView) {
        this.ctx = ctx;
        this.viajeVOList = viajeVOList;
        this.recyclerView = recyclerView;
        dialogAlert = new Dialog(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_viaje_detail,viewGroup,false);
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

        holder.fAButtonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, DetailActivity.class);
                intent.putExtra("status", viajeVO.getStatus());
                intent.putExtra("id", viajeVO.getId());
                ctx.startActivity(intent);
            }
        });

        holder.fAButtonCheckDirectly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialogAlert.setContentView(R.layout.dialog_danger);
                Button btnDialogClose = (Button) dialogAlert.findViewById(R.id.buton_close);
                Button btnDialogAcept = (Button) dialogAlert.findViewById(R.id.buton_acept);
                ImageView iViewDialogClose = (ImageView) dialogAlert.findViewById(R.id.iViewDialogClose);
                TextView mensaje = (TextView) dialogAlert.findViewById(R.id.tViewRecomendacion);

                try{

                    mensaje.setText("¿Esta seguro que quiere CONFIRMAR el viaje?");
                    iViewDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogAlert.dismiss();
                        }
                    });
                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialogAlert.dismiss();

                        }
                    });

                    btnDialogAcept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ViajeDAO viajeDAO = new ViajeDAO(ctx);
                            boolean x=(viajeDAO.toStatus1(viajeVO.getId()));


                                viajeVO.setStatus(1);
                                recyclerView.getAdapter().notifyDataSetChanged();

                            dialogAlert.dismiss();
                        }
                    });

                    dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogAlert.show();

                }catch (Exception e){
                    Toast.makeText(ctx,e.toString(),Toast.LENGTH_LONG).show();
                    Log.d(TAG,e.toString());
                }
            }
        });

        holder.fAButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlert.setContentView(R.layout.dialog_danger);
                Button btnDialogClose = (Button) dialogAlert.findViewById(R.id.buton_close);
                Button btnDialogAcept = (Button) dialogAlert.findViewById(R.id.buton_acept);
                ImageView iViewDialogClose = (ImageView) dialogAlert.findViewById(R.id.iViewDialogClose);
                TextView mensaje = (TextView) dialogAlert.findViewById(R.id.tViewRecomendacion);

                try{

                    mensaje.setText("¿Desea ELIMINAR el viaje?");
                    iViewDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogAlert.dismiss();
                        }
                    });
                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialogAlert.dismiss();

                        }
                    });

                    btnDialogAcept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            ViajeDAO viajeDAO = new ViajeDAO(ctx);
                            boolean x=(viajeDAO.deleteById(viajeVO.getId()));

                            if(!x){
                                Toast.makeText(ctx,"Error al Eliminar",Toast.LENGTH_SHORT).show();
                            }else{

                                viajeVOList.remove(pos);
                                recyclerView.getAdapter().notifyDataSetChanged();

                            }
                            dialogAlert.dismiss();
                        }
                    });

                    dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogAlert.show();

                }catch (Exception e){
                    Toast.makeText(ctx,e.toString(),Toast.LENGTH_LONG).show();
                    Log.d(TAG,e.toString());
                }
            }
        });

        holder.fAButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAlert.setContentView(R.layout.dialog_danger);
                Button btnDialogClose = (Button) dialogAlert.findViewById(R.id.buton_close);
                Button btnDialogAcept = (Button) dialogAlert.findViewById(R.id.buton_acept);
                ImageView iViewDialogClose = (ImageView) dialogAlert.findViewById(R.id.iViewDialogClose);
                TextView mensaje = (TextView) dialogAlert.findViewById(R.id.tViewRecomendacion);
                try{
                    if(viajeVO.getStatus()==0){
                        mensaje.setText("¿Usted va ha sincronizar un viaje sin verlo?");
                    }else {
                            mensaje.setText("Usted va ha sincronizar una confirmación de viaje\n¿Desea Continuar?");
                    }

                    iViewDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogAlert.dismiss();
                        }
                    });
                    btnDialogClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialogAlert.dismiss();

                        }
                    });

                    btnDialogAcept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            ViajeDAO viajeDAO = new ViajeDAO(ctx);
                            boolean x=(viajeDAO.toStatus2(viajeVO.getId()));

                            if(!x){
                                Toast.makeText(ctx,"Error al cambiar a estado 2",Toast.LENGTH_SHORT).show();
                            }else{
                                viajeVO.setStatus(2);
                                recyclerView.getAdapter().notifyDataSetChanged();

                            }
                            dialogAlert.dismiss();
                        }
                    });

                    dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogAlert.show();

                }catch (Exception e){
                    Toast.makeText(ctx,e.toString(),Toast.LENGTH_LONG).show();
                    Log.d(TAG,e.toString());
                }
            }
        });

        switch (viajeVO.getStatus()){
            case 0:
                holder.tViewSatusRecibido.setTextColor(colorEnable);
                holder.tViewStatusVerificado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);


                holder.fAButtonDelete.setClickable(false);
                holder.fAButtonDelete.setBackgroundTintList(ColorStateList.valueOf(colorDisable));
                break;

            case 1:
                holder.tViewSatusRecibido.setTextColor(colorDisable);
                holder.tViewStatusVerificado.setTextColor(colorEnable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);


                holder.fAButtonDelete.setClickable(false);
                holder.fAButtonDelete.setBackgroundTintList(ColorStateList.valueOf(colorDisable));

                holder.fAButtonCheckDirectly.setClickable(false);
                holder.fAButtonCheckDirectly.setBackgroundTintList(ColorStateList.valueOf(colorDisable));


                break;

            case 2:
                holder.tViewSatusRecibido.setTextColor(colorDisable);
                holder.tViewStatusVerificado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorEnable);


                holder.fAButtonCheckDirectly.setClickable(false);
                holder.fAButtonCheckDirectly.setBackgroundTintList(ColorStateList.valueOf(colorDisable));


                break;
        }
        //fin labels
        holder.tViewProveedor.setText(""+viajeVO.getProveedor());
        holder.tViewPlaca.setText(""+viajeVO.getPlaca());
        holder.tViewConductor.setText(""+viajeVO.getConductor());
        holder.tViewRuta.setText(""+viajeVO.getRuta());
        holder.tViewHInicio.setText(""+viajeVO.gethInicio());
        holder.tViewHFin.setText(""+viajeVO.gethFin());
        holder.tViewNPasajeros.setText(""+viajeVO.getNumPasajeros());
        Log.d(TAG,"tViewCapacidad: "+viajeVO.getCapacidad());
        float porc = (float) (((1.0)*(viajeVO.getNumPasajeros())/viajeVO.getCapacidad())*100);
        holder.tViewPorCapacidad.setText(""+getFloatFormateado(porc)+"%");


        if(viajeVO.getNumRestricciones()==0){
            holder.tViewRestriccion.setText("SIN RESTRICCIONES");
            holder.fAButtonRestriccion.setClickable(false);
            holder.fAButtonRestriccion.setBackgroundTintList(ColorStateList.valueOf(colorDisable));
        }else {
            holder.tViewRestriccion.setText(""+viajeVO.getNumRestricciones()+" RESTRICCIONES");
            holder.tViewRestriccion.setTextColor(colorRed);

            holder.fAButtonRestriccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ctx, RestricionActivity.class);
                    intent.putExtra("status", viajeVO.getStatus());
                    intent.putExtra("id", viajeVO.getId());
                    ctx.startActivity(intent);


                }
            });

        }






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
        TextView tViewProveedor ;
        TextView tViewPlaca ;
        TextView tViewConductor ;
        TextView tViewRuta ;
        TextView tViewHInicio ;
        TextView tViewHFin ;
        TextView tViewNPasajeros ;
        TextView tViewPorCapacidad;
        TextView tViewRestriccion;

        //status
        TextView tViewSatusRecibido;
        TextView tViewStatusVerificado;
        TextView tViewStatusSincronizado;


        //buttons
        FloatingActionButton fAButtonReview;
        FloatingActionButton fAButtonUpload;
        FloatingActionButton fAButtonDelete;
        FloatingActionButton fAButtonRestriccion;
        FloatingActionButton fAButtonCheckDirectly;

        public ViewHolder(View itemView){
            super(itemView);
            tViewProveedor = itemView.findViewById(R.id.tViewProveedor);
            tViewPlaca = itemView.findViewById(R.id.tViewPlaca);
            tViewConductor = itemView.findViewById(R.id.tViewConductor);
            tViewRuta = itemView.findViewById(R.id.tViewRuta);
            tViewHInicio = itemView.findViewById(R.id.tViewHInicio);
            tViewHFin = itemView.findViewById(R.id.tViewHFin);
            tViewNPasajeros = itemView.findViewById(R.id.tViewNPasajeros);
            tViewPorCapacidad = itemView.findViewById(R.id.tViewPorCapacidad);
            tViewRestriccion = itemView.findViewById(R.id.tViewRestriccion);
            tViewSatusRecibido = itemView.findViewById(R.id.tViewSatusRecibido);
            tViewStatusVerificado = itemView.findViewById(R.id.tViewStatusVerificado);
            tViewStatusSincronizado = itemView.findViewById(R.id.tViewStatusSincronizado);

            fAButtonReview = itemView.findViewById(R.id.fAButtonReview);
            fAButtonUpload = itemView.findViewById(R.id.fAButtonUpload);
            fAButtonDelete = itemView.findViewById(R.id.fAButtonDelete);
            fAButtonRestriccion =itemView.findViewById(R.id.fAButtonRestriccion);
            fAButtonCheckDirectly=itemView.findViewById(R.id.fAButtonCheckDirectly);
        }
    }




}


