package com.ibao.alanger.agroq.helpers.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibao.alanger.agroq.R;
import com.ibao.alanger.agroq.models.dao.DespachoDAO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.DespachoVO;
import com.ibao.alanger.agroq.views.ActivityProceso;
import com.ibao.alanger.agroq.views.ActivityUpload;

import java.util.ArrayList;
import java.util.List;

public class AdapterListDespacho extends BaseAdapter {

    private Context ctx;
    private List<DespachoVO> produccionVOList;
    private ListView listView;

    public AdapterListDespacho(Context ctx, List<Object> despachoVOList, ListView lView) {
        this.ctx = ctx;
        this.produccionVOList = new ArrayList<>();
        for(Object o: despachoVOList){
            this.produccionVOList.add((DespachoVO) o);
        }

        this.listView = lView;
    }

    @Override
    public int getCount() {
        return produccionVOList.size();
    }

    @Override
    public DespachoVO getItem(int position) {
        return produccionVOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return produccionVOList.get(position).getId();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        final LayoutInflater inflater = LayoutInflater.from(ctx);
        v = inflater.inflate(R.layout.item_view_registro_despacho,null);
        final DespachoVO item = getItem(position);

        //labels
        final TextView tViewFechaInspeccion = v.findViewById(R.id.tViewFechaRecepcion);
        final TextView tViewFechaCarga = v.findViewById(R.id.tViewFechaCarga);
        final TextView tViewFechaLlegada = v.findViewById(R.id.tViewFechaLlegada);
        final TextView tViewFechaSalida = v.findViewById(R.id.tViewFechaSalida);

        final TextView tViewPlanta = v.findViewById(R.id.tViewNFrutos);
        final TextView tViewCultivo = v.findViewById(R.id.tViewCultivo);
        final TextView tViewOrigen = v.findViewById(R.id.tViewOrigen);
        final TextView tViewCliente = v.findViewById(R.id.tViewCliente);

        final TextView tViewControlador = v.findViewById(R.id.tViewControlador);
        final TextView tViewNPallets = v.findViewById(R.id.tViewNPallets);
        final TextView tViewNCajasPallet = v.findViewById(R.id.tViewNCajasPallet);
        final TextView tViewTotalCajas = v.findViewById(R.id.tViewTotalCajas);
        final TextView tViewPesoTotal = v.findViewById(R.id.tViewPesoTotal);


        //fin labels
        final ConstraintLayout cLayoutTotal = v.findViewById(R.id.cLayoutTotal);
        //bottons
        final FloatingActionButton fAButtonEdit = v.findViewById(R.id.fAButtonResult);
        final FloatingActionButton fAButtonDelete = v.findViewById(R.id.fAButtonDelete);
        final FloatingActionButton fAButtonUpload = v.findViewById(R.id.fAButtonUpload);
        //fin bottons
        final TextView tViewEditando = v.findViewById(R.id.tViewEditando);

        if(getItem(position).isEditing()){
            tViewEditando.setVisibility(View.VISIBLE);
            fAButtonUpload.setVisibility(View.INVISIBLE);
        }

        fAButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ActivityUpload.class);
                intent.putExtra(ctx.getString(R.string.idProceso),getItem(position).getId());
                ctx.startActivity(intent);
            }
        });

        fAButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ActivityProceso.class);
                    intent.putExtra(ctx.getString(R.string.isEditable), true);
                    intent.putExtra(ctx.getString(R.string.idProceso),  getItem(position).getId());
                ctx.startActivity(intent);
            }
        });


        fAButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogClose = new Dialog(ctx);
                dialogClose.setContentView(R.layout.dialog_danger);
                Button btnDialogClose = (Button) dialogClose.findViewById(R.id.buton_close);
                Button btnDialogAcept = (Button) dialogClose.findViewById(R.id.buton_acept);
                ImageView iViewDialogClose = (ImageView) dialogClose.findViewById(R.id.iViewDialogClose);
                TextView mensaje = (TextView) dialogClose.findViewById(R.id.tViewRecomendacion);

                mensaje.setText("Esta a punto de eliminar un Despacho.\n¿DESEA CONTINUAR?");
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

                        boolean x=new DespachoDAO(ctx).deleteById(item.getId());
                        if(!x){
                            Toast.makeText(ctx,"Error al eliminar",Toast.LENGTH_SHORT).show();
                        }else{
                            produccionVOList.remove(position);
                            cLayoutTotal.startAnimation(animClose);
                            Handler handler = new Handler();
                            handler.postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            AdapterListDespacho.super.notifyDataSetChanged();

                                        }
                                    },200
                            );
                        }
                        dialogClose.dismiss();
                    }
                });

                dialogClose.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogClose.show();
            }
        });

        String def = ctx.getString(R.string.tres_puntos);

        DespachoVO PROCESO = item;

        tViewFechaInspeccion.setText((((DespachoVO) PROCESO).getFechaInspeccion()==null?def: ((DespachoVO) PROCESO).getFechaInspeccion()));
        tViewPlanta.setText(((DespachoVO) PROCESO).getNamePlanta()==null?def: ((DespachoVO) PROCESO).getNamePlanta());
        tViewCultivo.setText((((DespachoVO) PROCESO).getNameCultivo()==null?def: ((DespachoVO) PROCESO).getNameCultivo()));

        tViewFechaCarga.setText( ((DespachoVO) PROCESO).getFechaCarga()==null?def: ((DespachoVO) PROCESO).getFechaCarga());
        tViewFechaLlegada.setText( ((DespachoVO) PROCESO).getFechaLlegada()==null?def: ((DespachoVO) PROCESO).getFechaLlegada());
        tViewFechaSalida.setText( ((DespachoVO) PROCESO).getFechaSalida()==null?def: ((DespachoVO) PROCESO).getFechaSalida());

        tViewOrigen.setText( ((DespachoVO) PROCESO).getOrigen()==null?def: ((DespachoVO) PROCESO).getOrigen());
        tViewCliente.setText( ((DespachoVO) PROCESO).getCliente()==null?def: ((DespachoVO) PROCESO).getCliente());
        tViewControlador.setText( ((DespachoVO) PROCESO).getNameControlador()==null?def: ((DespachoVO) PROCESO).getNameControlador());
        tViewNPallets.setText(""+ ((DespachoVO) PROCESO).getnPallets());
        tViewNCajasPallet.setText(""+ ((DespachoVO) PROCESO).getnCajasPallet());
        tViewTotalCajas.setText(""+ ((DespachoVO) PROCESO).getnPallets()*((DespachoVO) PROCESO).getnCajasPallet() );
        tViewPesoTotal.setText( ""+((DespachoVO) PROCESO).getnPallets()*((DespachoVO) PROCESO).getnCajasPallet() * ((DespachoVO) PROCESO).getPesoCaja() );

        return v;
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        //listView.setDividerHeight(params.height);
    }


}
