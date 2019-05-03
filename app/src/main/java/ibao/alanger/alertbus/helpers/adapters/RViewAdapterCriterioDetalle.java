package com.ibao.alanger.agroq.helpers.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibao.alanger.agroq.R;
import com.ibao.alanger.agroq.models.dao.CalibreDAO;
import com.ibao.alanger.agroq.models.dao.CriterioDetalleDAO;
import com.ibao.alanger.agroq.models.dao.EvaluacionDetalleDAO;
import com.ibao.alanger.agroq.models.dao.FotoDAO;
import com.ibao.alanger.agroq.models.dao.MuestraDAO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.CriterioDetalleVO;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.EvaluacionDetalleVO;
import com.ibao.alanger.agroq.views.ActivityEvaluacionDetalle;
import com.ibao.alanger.agroq.views.ActivityPhotoGallery;

import java.util.List;

public class RViewAdapterCriterioDetalle extends RecyclerView.Adapter<RViewAdapterCriterioDetalle.ViewHolder> {


    public  static EvaluacionDetalleVO evaluacionDetalleVO;
    private Context ctx;
    private List<CriterioDetalleVO> criterioDetalleVOList;
    private RecyclerView lView;
    private TextView tViewCantidadTotal;
    private TextView tViewPorcentajeTotal;
    private TextView tView_total;
    private TextView tView_twoPoints;
    private int nFrutos;
    private boolean isEditable;


    String TAG = RViewAdapterCriterioDetalle.class.getSimpleName();

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //elementos

        EditText eTextEntero ;
        EditText eTextDecimal;
        EditText eTextTexto ;
        ImageView iViewFoto ;
        TextView tViewPorcentajeCri ;
        ImageView iViewCarita ;
        TextView tViewSPorcentajeCri ;
        TextView tviewNumFotos ;
        TextView tViewNameCri;
        public ViewHolder(View itemView){
            super(itemView);

            EditText _1 = itemView.findViewById(R.id.eTextEntero);
            EditText _2 = itemView.findViewById(R.id.eTextDecimal);
            tviewNumFotos = itemView.findViewById(R.id.tViewNfotos);
            iViewFoto = itemView.findViewById(R.id.iViewFoto);
            tViewPorcentajeCri = itemView.findViewById(R.id.tViewPorcentajeCri);
            iViewCarita = itemView.findViewById(R.id.iViewCarita);
            tViewSPorcentajeCri = itemView.findViewById(R.id.tViewSPorcentajeCri);
            tViewNameCri = itemView.findViewById(R.id.tViewNameCri);
            if(evaluacionDetalleVO.getIdEvaluacion()==7||evaluacionDetalleVO.getIdEvaluacion()==8){
                _1.setVisibility(View.INVISIBLE);
                _2.setVisibility(View.INVISIBLE);
                _1.setFocusable(false);
                _2.setFocusable(false);
                _1 = itemView.findViewById(R.id.eTextEntero2);
                _2 = itemView.findViewById(R.id.eTextDecimal2);
            }

            eTextEntero = _1;
            eTextDecimal = _2;
            eTextTexto = itemView.findViewById(R.id.eTextTexto);

        }

    }

    public RViewAdapterCriterioDetalle(Context ctx, List<CriterioDetalleVO> criterioDetalleVOList, RecyclerView lView, boolean isEditable,TextView tViewCantidadTotal, TextView tViewPorcentaje, TextView tView_total,TextView tView_twoPoints) {
        try{
            this.ctx = ctx;
            this.criterioDetalleVOList = criterioDetalleVOList;
            this.lView = lView;
            this.isEditable = isEditable;
            this.tViewCantidadTotal= tViewCantidadTotal;
            this.tViewPorcentajeTotal = tViewPorcentaje;
            this.tView_total = tView_total;
            this.tView_twoPoints = tView_twoPoints;

            this.evaluacionDetalleVO = new EvaluacionDetalleDAO(ctx).consultarByid(
                    criterioDetalleVOList.size()>0?
                            criterioDetalleVOList.get(0).getIdEvaluacionDetalle():
                            0
            );
            this.nFrutos = (new MuestraDAO(ctx).consultarById(evaluacionDetalleVO.getIdMuestra())).getCantidad();
        }catch (Exception e){
            Toast.makeText(ctx,"error->",Toast.LENGTH_LONG).show();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_criteriodetalle,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public CriterioDetalleVO getItem(int position) {
        return criterioDetalleVOList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int posCri) {

        holder.tViewPorcentajeCri.setText(""+getItem(posCri).getPorcentaje()+"%");

        holder.tViewNameCri.setText(getItem(posCri).getNameCriterio());

        try{
            int i = new FotoDAO(ctx).contarFotos(getItem(posCri).getId());

            if(i>0 && i<=9){
                holder.tviewNumFotos.setVisibility(View.VISIBLE);
                holder.tviewNumFotos.setText(String.valueOf(i));
            }
            if(i>9){
                holder.tviewNumFotos.setVisibility(View.VISIBLE);
                holder.tviewNumFotos.setText(String.valueOf(9)+"+");
            }
            if(i==0){
                holder.tviewNumFotos.setVisibility(View.INVISIBLE);
                holder.tviewNumFotos.setText(String.valueOf(0));
            }
        }catch (Exception e){
            Log.d(TAG,"error conteo nfotos:"+e.toString());
        }

        switch (getItem(posCri).getCalificacion()){
            case 0:
                holder.iViewCarita.setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.iViewCarita.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_good_24dp));
                break;
            case 2:
                holder.iViewCarita.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_regular));
                break;
            case 3:
                holder.iViewCarita.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_mal));
                break;
        }

        if(getItem(posCri).isFotografiableCriterio())
        {
            holder.iViewFoto.setVisibility(View.VISIBLE);
            holder.iViewFoto.setClickable(true);

            holder.iViewFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //tomarFoto();
                    //Toast.makeText(ctx,"Tomar Fotografia",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ctx, ActivityPhotoGallery.class);
                    intent.putExtra("idCriterioDetalle",getItem(posCri).getId());
                    intent.putExtra("value",getItem(posCri).getValor());
                    intent.putExtra("name",getItem(posCri).getNameCriterio());
                    intent.putExtra("isEditable",isEditable);
                    intent.putExtra("fechaHora",getItem(posCri).getPorcentaje());

                    ((Activity) ctx).startActivityForResult(intent, ActivityEvaluacionDetalle.REQUEST_FOTO);

                }
            });



        }else {
            holder.iViewFoto.setVisibility(View.INVISIBLE);
            holder.iViewFoto.setClickable(false);
        }


        if(new CalibreDAO(ctx).consultarTolCalibreByidCriterioDetalle(getItem(posCri).getId())!=null){
            holder.tViewPorcentajeCri.setVisibility(View.INVISIBLE);
            tView_total.setVisibility(View.INVISIBLE);
            tView_twoPoints.setVisibility(View.INVISIBLE);
            tViewCantidadTotal.setVisibility(View.INVISIBLE);
            tViewPorcentajeTotal.setVisibility(View.INVISIBLE);
        }

        if(getItem(posCri).getTipoDatoCriterio()!=null){

            switch (criterioDetalleVOList.get(posCri).getTipoDatoCriterio()){
                case "ENTERO":
                    holder.eTextEntero.setVisibility(View.VISIBLE);
                    holder.eTextDecimal.setVisibility(View.INVISIBLE);
                    holder.eTextTexto.setVisibility(View.INVISIBLE);
                    holder.eTextEntero.setText(getItem(posCri).getValor());

                    holder.eTextEntero.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                // Toast.makeText(getBaseContext(),"guardando "+eTextEntero.getText().toString(),Toast.LENGTH_SHORT).show();
                                if((holder.eTextEntero.getText().toString().equals(""))){
                                    if(new CriterioDetalleDAO(ctx).editValor((int)getItemId(posCri),"")
                                    ){
                                        //   Toast.makeText(ctx,"se puedo cambiar el valor a 0", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(ctx,"no se puedo cambiar el valor a 0", Toast.LENGTH_LONG).show();
                                    }

                                }else {
                                    if(new CriterioDetalleDAO(ctx).editValor((int)getItemId(posCri),String.valueOf(Integer.valueOf(holder.eTextEntero.getText().toString())))
                                    ){
                                        // Toast.makeText(ctx,"se puedo cambiar el valor "+String.valueOf(Integer.valueOf(eTextEntero.getText().toString())), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(ctx,"no se puedo cambiar el valor "+String.valueOf(Integer.valueOf(holder.eTextEntero.getText().toString())), Toast.LENGTH_LONG).show();
                                    }
                                }
                                criterioDetalleVOList.set(posCri, new CriterioDetalleDAO(ctx).consultarByid(getItem(posCri).getId()));


                                float porCri= getItem(posCri).getPorcentaje();

                                holder.tViewPorcentajeCri.setText(""+porCri+"%");


                                switch (getItem(posCri).getCalificacion()){
                                    case 0:
                                        holder.iViewCarita.setVisibility(View.INVISIBLE);
                                        break;
                                    case 1:
                                        holder.iViewCarita.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_good_24dp));
                                        break;
                                    case 2:
                                        holder.iViewCarita.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_regular));
                                        break;
                                    case 3:
                                        holder.iViewCarita.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_mal));
                                        break;
                                }
                                //

                                Log.d(TAG,"cons1");
                                evaluacionDetalleVO =
                                        new EvaluacionDetalleDAO(ctx).consultarByid(evaluacionDetalleVO.getId());



                                if(evaluacionDetalleVO.isMatSec()){
                                    tView_total.setVisibility(View.INVISIBLE);
                                    tView_twoPoints.setVisibility(View.INVISIBLE);
                                    tViewCantidadTotal.setVisibility(View.INVISIBLE);
                                    tViewPorcentajeTotal.setVisibility(View.INVISIBLE);
                                    //   holder.tViewCantidadTotal.setText(""+evaluacionDetalleVOList.get(posEva).getCantidad()+"%");
                                }else {
                                    tViewPorcentajeTotal.setText(""+evaluacionDetalleVO.getPorcentaje()+"%");
                                    tViewCantidadTotal.setText(""+evaluacionDetalleVO.getCantidad());
                                }
                            }
                        }
                    });

                    break;
                case "TEXTO":
                    holder.eTextTexto.setVisibility(View.VISIBLE);
                    holder.eTextDecimal.setVisibility(View.INVISIBLE);
                    holder.eTextEntero.setVisibility(View.INVISIBLE);
                    Log.d(TAG,"val texto-->"+getItem(posCri).getValor());
                    holder.eTextTexto.setText(getItem(posCri).getValor());
                    holder.eTextTexto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                // Toast.makeText(getBaseContext(),"guardando "+eTextEntero.getText().toString(),Toast.LENGTH_SHORT).show();
                                if((holder.eTextTexto.getText().toString().equals(""))){
                                    if(new CriterioDetalleDAO(ctx).editValor((int)getItemId(posCri),"")
                                    ){
                                        //   Toast.makeText(ctx,"se puedo cambiar el valor a 0", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(ctx,"no se puede cambiar el valor a 0", Toast.LENGTH_LONG).show();
                                    }

                                }else {
                                    if(new CriterioDetalleDAO(ctx).editValor((int)getItemId(posCri),(holder.eTextTexto.getText().toString()))
                                    ){
                                        //  Toast.makeText(ctx,"se puedo cambiar el valor "+eTextTexto.getText().toString(), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(ctx,"no se puede cambiar el valor "+String.valueOf(holder.eTextTexto.getText().toString()), Toast.LENGTH_LONG).show();
                                    }
                                }


                                Log.d(TAG,"cons1");
                                evaluacionDetalleVO =
                                        new EvaluacionDetalleDAO(ctx).consultarByid(evaluacionDetalleVO.getId());



                                if(evaluacionDetalleVO.isMatSec()|| evaluacionDetalleVO.getIdEvaluacion()==7||evaluacionDetalleVO.getIdEvaluacion()==8){
                                    tView_total.setVisibility(View.INVISIBLE);
                                    tView_twoPoints.setVisibility(View.INVISIBLE);
                                    tViewCantidadTotal.setVisibility(View.INVISIBLE);
                                    tViewPorcentajeTotal.setVisibility(View.INVISIBLE);
                                    //   holder.tViewCantidadTotal.setText(""+evaluacionDetalleVOList.get(posEva).getCantidad()+"%");
                                }else {
                                    tViewPorcentajeTotal.setText(""+evaluacionDetalleVO.getPorcentaje()+"%");
                                    tViewCantidadTotal.setText(""+evaluacionDetalleVO.getCantidad());
                                }
                            }
                        }
                    });

                    break;
                case "PORCENTAJE":
                    holder.tViewPorcentajeCri.setVisibility(View.INVISIBLE);
                    holder.tViewSPorcentajeCri.setVisibility(View.VISIBLE);

                case "DECIMAL":
                default:
                    holder.eTextDecimal.setVisibility(View.VISIBLE);
                    holder.eTextEntero.setVisibility(View.INVISIBLE);
                    holder.eTextTexto.setVisibility(View.INVISIBLE);

                    holder.eTextDecimal.setText(getItem(posCri).getValor());

                    holder.eTextDecimal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                // Toast.makeText(getBaseContext(),"guardando "+eTextEntero.getText().toString(),Toast.LENGTH_SHORT).show();
                                if((holder.eTextDecimal.getText().toString().equals(""))){
                                    if(new CriterioDetalleDAO(ctx).editValor((int)getItemId(posCri),"")
                                    ){
                                        //  Toast.makeText(ctx,"se puedo cambiar el valor a 0", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(ctx,"no se puedo cambiar el valor a 0", Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    if(new CriterioDetalleDAO(ctx).editValor((int)getItemId(posCri),String.valueOf(Float.valueOf(holder.eTextDecimal.getText().toString())))
                                    ){
                                        //Toast.makeText(ctx,"se puedo cambiar el valor "+String.valueOf(Float.valueOf(eTextDecimal.getText().toString())), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(ctx,"no se puedo cambiar el valor "+String.valueOf(Float.valueOf(holder.eTextDecimal.getText().toString())), Toast.LENGTH_LONG).show();
                                    }
                                }
                                criterioDetalleVOList.set(posCri, new CriterioDetalleDAO(ctx).consultarByid(getItem(posCri).getId()));


                                holder.tViewPorcentajeCri.setText(""+getItem(posCri).getPorcentaje()+"%");

                                switch (getItem(posCri).getCalificacion()){
                                    case 0:
                                        holder.iViewCarita.setVisibility(View.INVISIBLE);
                                        break;
                                    case 1:
                                        holder.iViewCarita.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_good_24dp));
                                        break;
                                    case 2:
                                        holder.iViewCarita.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_regular));
                                        break;
                                    case 3:
                                        holder.iViewCarita.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_mal));
                                        break;
                                }
                                Log.d(TAG,"cons2");
                                evaluacionDetalleVO =
                                        new EvaluacionDetalleDAO(ctx).consultarByid(evaluacionDetalleVO.getId());


                                if(evaluacionDetalleVO.isMatSec()|| evaluacionDetalleVO.getIdEvaluacion()==7||evaluacionDetalleVO.getIdEvaluacion()==8){

                                    tView_total.setVisibility(View.INVISIBLE);
                                    tView_twoPoints.setVisibility(View.INVISIBLE);
                                    tViewCantidadTotal.setVisibility(View.INVISIBLE);
                                    tViewPorcentajeTotal.setVisibility(View.INVISIBLE);
                                    //holder.tViewCantidadTotal.setText(""+evaluacionDetalleVOList.get(posEva).getCantidad()+"%");
                                }else {
                                    tViewPorcentajeTotal.setText(""+evaluacionDetalleVO.getPorcentaje()+"%");
                                    tViewCantidadTotal.setText(""+evaluacionDetalleVO.getCantidad());
                                }
                            }
                        }
                    });



            }
        }else {
            holder.eTextDecimal.setVisibility(View.INVISIBLE);
            holder.eTextEntero.setVisibility(View.INVISIBLE);
        }

        //escribir el total al iniciar
        if(evaluacionDetalleVO.getIdEvaluacion()==3||evaluacionDetalleVO.isMatSec()|| evaluacionDetalleVO.getIdEvaluacion()==7||evaluacionDetalleVO.getIdEvaluacion()==8){
            tView_total.setVisibility(View.INVISIBLE);
            tView_twoPoints.setVisibility(View.INVISIBLE);
            tViewCantidadTotal.setVisibility(View.INVISIBLE);
            tViewPorcentajeTotal.setVisibility(View.INVISIBLE);
            // holder.tViewCantidadTotal.setText(""+evaluacionDetalleVOList.get(posEva).getCantidad()+"%");
        }else {
            tViewPorcentajeTotal.setText(""+evaluacionDetalleVO.getPorcentaje()+"%");
            tViewCantidadTotal.setText(""+evaluacionDetalleVO.getCantidad());
        }

    }

    public long getItemId(int posCri){
        return criterioDetalleVOList.get(posCri).getId();
    }
    @Override
    public int getItemCount() {
        return criterioDetalleVOList.size();
    }

}
