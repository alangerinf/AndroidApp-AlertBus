package ibao.alanger.alertbus.viajeEnCurso;

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
import ibao.alanger.alertbus.models.vo.RestriccionVO;

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
        final PasajeroVO restriccionVO = pasajeroVOList.get(pos);

        //fin labels
        holder.avi_tViewName.setText(""+restriccionVO.getName());
        holder.avi_tViewDNI.setText(""+restriccionVO.getDni());
        holder.avi_tViewFecha.setText(""+restriccionVO.gethSubida());
        holder.avi_tViewObservacion.setText(""+restriccionVO.getObservacion());

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





}


