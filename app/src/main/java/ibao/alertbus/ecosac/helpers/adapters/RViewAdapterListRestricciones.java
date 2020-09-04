package ibao.alertbus.ecosac.helpers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.models.vo.RestriccionVO;

public class RViewAdapterListRestricciones extends RecyclerView.Adapter<RViewAdapterListRestricciones.ViewHolder>  {

    private Context ctx;
    private List<RestriccionVO> restriccionVOList;
    private RecyclerView recyclerView;

    final private String TAG = RViewAdapterListRestricciones.class.getSimpleName();

    public RViewAdapterListRestricciones(Context ctx, List<RestriccionVO> restriccionVOList, RecyclerView recyclerView) {
        this.ctx = ctx;
        this.restriccionVOList = restriccionVOList;
        this.recyclerView = recyclerView;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_restriccion,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {
        final RestriccionVO restriccionVO = restriccionVOList.get(pos);

        //fin labels
        holder.tViewName.setText(""+restriccionVO.getName());
        holder.tViewDescripcion.setText(""+restriccionVO.getDesc());

    }


    @Override
    public int getItemCount() {
        return restriccionVOList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        //elementos

        //fin labels
        TextView tViewName ;
        TextView tViewDescripcion ;
        public ViewHolder(View itemView){
            super(itemView);
            tViewName = itemView.findViewById(R.id.tViewName);
            tViewDescripcion = itemView.findViewById(R.id.tViewDescripcion);

        }
    }





}


