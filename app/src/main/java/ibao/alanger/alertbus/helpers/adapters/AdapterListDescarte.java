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
import com.ibao.alanger.agroq.models.dao.DescarteDAO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.DescarteVO;
import com.ibao.alanger.agroq.views.ActivityProceso;
import com.ibao.alanger.agroq.views.ActivityUpload;

import java.util.ArrayList;
import java.util.List;

public class AdapterListDescarte extends BaseAdapter {

    private Context ctx;
    private List<DescarteVO> produccionVOList;
    private ListView listView;

    public AdapterListDescarte(Context ctx, List<Object> produccionVOList, ListView lView) {
        this.ctx = ctx;
        this.produccionVOList = new ArrayList<>();
        for(Object o: produccionVOList){
            this.produccionVOList.add((DescarteVO) o);
        }

        this.listView = lView;
    }

    @Override
    public int getCount() {
        return produccionVOList.size();
    }

    @Override
    public DescarteVO getItem(int position) {
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
        v = inflater.inflate(R.layout.item_view_registro_descarte,null);
        final DescarteVO item = getItem(position);

        //labels
        final TextView tViewFechaRecepcion = v.findViewById(R.id.tViewFechaRecepcion);
        final TextView tViewPlanta = v.findViewById(R.id.tViewNFrutos);
        final TextView tViewNOrdenProceso = v.findViewById(R.id.tViewNOrdenProceso);

        final TextView tViewProductor = v.findViewById(R.id.tViewProductor);
        final TextView tViewVariedad = v.findViewById(R.id.tViewCultivo);
        final TextView tViewNUnidades = v.findViewById(R.id.tViewUnidades);
        final TextView tViewKilos = v.findViewById(R.id.tViewPesoTotal);

        final TextView tViewTipoEnvase = v.findViewById(R.id.tViewControlador);



        //fin labels
        final ConstraintLayout cLayoutTotal = v.findViewById(R.id.cLayoutTotal);
        //bottons
        final FloatingActionButton fAButtonEdit = v.findViewById(R.id.fAButtonResult);
        final FloatingActionButton fAButtonUpload = v.findViewById(R.id.fAButtonUpload);
        final FloatingActionButton fAButtonDelete = v.findViewById(R.id.fAButtonDelete);
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
                mensaje.setText("Esta a punto de eliminar un Descarte.\n¿DESEA CONTINUAR?");
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

                        boolean x=new DescarteDAO(ctx).deleteById(item.getId());
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
                                            AdapterListDescarte.super.notifyDataSetChanged();

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

        tViewFechaRecepcion.setText(item.getFechaHora()==null?def:item.getFechaHora());
        tViewPlanta.setText(item.getNamePlanta()==null?def:item.getNamePlanta());
        tViewNOrdenProceso.setText(item.getnOrdenProceso()==null?def:item.getnOrdenProceso());

        tViewProductor.setText(item.getNameEmpresa()==null?def:item.getNameEmpresa());
        tViewVariedad.setText((item.getNameCultivo()==null?def: item.getNameCultivo())+" - "+(item.getNameVariedad()==null?def: item.getNameVariedad()));
        tViewNUnidades.setText(item.getUnidades()==null?def:item.getUnidades());
        tViewKilos.setText(item.getKilos()==null?def:item.getKilos());
        tViewTipoEnvase.setText(item.getNameEnvase()==null?def:item.getNameEnvase());
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
