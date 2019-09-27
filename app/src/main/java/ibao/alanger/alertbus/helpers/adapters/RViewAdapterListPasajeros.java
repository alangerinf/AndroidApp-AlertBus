package ibao.alanger.alertbus.helpers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.models.vo.PasajeroVO;

public class RViewAdapterListPasajeros extends RecyclerView.Adapter<RViewAdapterListPasajeros.ViewHolder>  {

    private Context ctx;
    private List<PasajeroVO> pasajeroVOList;
    private RecyclerView recyclerView;

    final private String TAG = RViewAdapterListPasajeros.class.getSimpleName();

    public RViewAdapterListPasajeros(Context ctx, List<PasajeroVO> pasajeroVOList, RecyclerView recyclerView) {
        this.ctx = ctx;
        this.pasajeroVOList = pasajeroVOList;
        this.recyclerView = recyclerView;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_person,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {
        PasajeroVO pasajeroVO = pasajeroVOList.get(pos);

        //fin labels
        holder.tViewName.setText(""+pasajeroVO.getName());
        holder.tViewDNI.setText(""+pasajeroVO.getDni());
        holder.tViewObservacion.setText("*"+pasajeroVO.getObservacion());
        holder.tViewFecha.setText(""+pasajeroVO.gethSubida());

        if(pasajeroVO.getObservacion().isEmpty()){
            holder.tViewObservacion.setHeight(0);
        }


    }

    public float getFloatFormateado(float n){
        return ((int)(n*10))/10.0f;
    }


    @Override
    public int getItemCount() {
        return pasajeroVOList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        //elementos

        //fin labels
        TextView tViewName ;
        TextView tViewDNI ;
        TextView tViewObservacion;
        TextView tViewFecha;
        public ViewHolder(View itemView){
            super(itemView);
            tViewName = itemView.findViewById(R.id.tViewName);
            tViewDNI = itemView.findViewById(R.id.tViewDNI);
            tViewObservacion = itemView.findViewById(R.id.tViewObservacion);
            tViewFecha = itemView.findViewById(R.id.tViewFecha);

        }
    }




}


