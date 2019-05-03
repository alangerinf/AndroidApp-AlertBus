package com.ibao.alanger.agroq.helpers.adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibao.alanger.agroq.R;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.FotoVO;

import java.io.File;
import java.util.List;

public class RViewAdapterFotos extends RecyclerView.Adapter<RViewAdapterFotos.ViewHolder> {

    private List<FotoVO> listFotos;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context ctx;
    private ImageView lienzo;
    private Boolean isEditable;
    public static int idFotoFocus;
    private ImageView btnDelete;

    // data is passed into the constructor
    public RViewAdapterFotos(Context context, List<FotoVO> listFotos, ImageView lienzo, Boolean isEditable, int idFotoFocus , ImageView btnDelete) {
        this.mInflater = LayoutInflater.from(context);
        this.listFotos = listFotos;
        this.ctx=context;
        this.lienzo = lienzo;
        this.isEditable = isEditable;
        this.idFotoFocus = idFotoFocus;
        this.btnDelete = btnDelete;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.foto_itemeditor_list_view, parent, false);
        return new ViewHolder(view);


    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                FotoVO fotoVO = listFotos.get(position);

                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(listFotos.get(position).getPath());
                    bitmap = Bitmap.createScaledBitmap(bitmap, 60, 80, true);
                    holder.iViewFoto.setImageBitmap(bitmap);
                }catch (Exception e){
                    holder.iViewFoto.setImageResource(R.drawable.ic_error_outline_50dp);
                    Log.d("MyRecyclerView xd",e.toString());
                }


            }
        }, 1);

        holder.iViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isEditable){
                            btnDelete.setVisibility(View.VISIBLE);
                        }else {
                            btnDelete.setVisibility(View.INVISIBLE);
                        }
                        //Toast.makeText(ctx,"seleccionado"+position,Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeFile(listFotos.get(position).getPath());
                        idFotoFocus = listFotos.get(position).getId();
                        int rotate = 0;
                        try {
                            File imageFile = new File(listFotos.get(position).getPath());
                            ExifInterface exif = new ExifInterface(
                                    imageFile.getAbsolutePath());
                            int orientation = exif.getAttributeInt(
                                    ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_NORMAL);

                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotate = 270;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotate = 180;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotate = 90;
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Matrix matrix = new Matrix();
                            matrix.postRotate(rotate);
                            matrix.postScale(0.5f,0.5f);
                            //double scale = bitmap.getWidth()/(bitmap.getHeight()*1.0);
                            bitmap = Bitmap.createBitmap(bitmap , 0, 0, (bitmap.getWidth()),  (bitmap.getHeight()), matrix, true);

                            lienzo.setImageBitmap(bitmap);
                        }catch (Exception e){
                            lienzo.setImageResource(R.drawable.ic_error_outline_50dp);
                            Log.d("MyRecyclerView xd",e.toString());
                        }


                    }
                });


            }
        });




   //     holder.tViewFechaHora.setText(listFotos.get(position).getFechaHora());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return listFotos.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iViewFoto;
        TextView tViewFechaHora;

        ViewHolder(View itemView) {
            super(itemView);
            iViewFoto = (ImageView) itemView.findViewById(R.id.foto_imagen);
          //  tViewFechaHora = itemView.findViewById(R.id.foto_fechahora);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    // convenience method for getting data at click position
    public FotoVO getItem(int position) {
        return listFotos.get(position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}