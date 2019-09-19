package ibao.alanger.alertbus.helpers.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.viajeEnCurso.ActivityViaje;

public class RViewAdapterListViajesConductor extends RecyclerView.Adapter<RViewAdapterListViajesConductor.ViewHolder>  {

    private Context ctx;
    private List<ViajeVO> viajeVOList;
    private RecyclerView recyclerView;

    private Dialog dialogAlert;
    final private String TAG = RViewAdapterListViajesConductor.class.getSimpleName();

    public RViewAdapterListViajesConductor(Context ctx, List<ViajeVO> viajeVOList, RecyclerView recyclerView) {
        this.ctx = ctx;
        this.viajeVOList = viajeVOList;
        this.recyclerView = recyclerView;
        this.dialogAlert = new Dialog(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_viajeconductor_detail,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {
        final ViajeVO viajeVO = viajeVOList.get(pos);
        int colorDisable = ContextCompat.getColor(ctx, R.color.materialGrey400);
        int colorEnable = ContextCompat.getColor(ctx, R.color.customGreen);
        int colorRed = ContextCompat.getColor(ctx, R.color.redAccent700);



        holder.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                switch (viajeVO.getStatus()){

                    case 0:// si recien llego el viaje
                        dialogAlert.setContentView(R.layout.dialog_message);
                        Button btnDialogClose0 = (Button) dialogAlert.findViewById(R.id.buton_close);
                        Button btnDialogAcept0 = (Button) dialogAlert.findViewById(R.id.buton_acept);
                        ImageView iViewDialogClose0 = (ImageView) dialogAlert.findViewById(R.id.iViewDialogClose);
                        TextView mensaje0 = (TextView) dialogAlert.findViewById(R.id.tViewRecomendacion);

                        try{

                            mensaje0.setText("¿Esta seguro que desea comenzar el Viaje?");
                            iViewDialogClose0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogAlert.dismiss();
                                }
                            });
                            btnDialogClose0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogAlert.dismiss();

                                }
                            });

                            btnDialogAcept0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ViajeDAO viajeDAO = new ViajeDAO(ctx);
                                    boolean x=(viajeDAO.toStatus1(viajeVO.getId()));

                                    new LoginDataDAO(ctx).uploadIdViaje(viajeVO.getId());

                                    viajeVO.setStatus(1);/// cambiando estado a en curso
                                    recyclerView.getAdapter().notifyDataSetChanged();

                                    dialogAlert.dismiss();

                                    goToViajeEnCurso(viajeVO);

                                }
                            });

                            dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogAlert.show();

                        }catch (Exception e){
                            Toast.makeText(ctx,e.toString(),Toast.LENGTH_LONG).show();
                            Log.d(TAG,e.toString());
                        }
                        break;
                    case 1:// si ya esta en curso
                        goToViajeEnCurso(viajeVO);
                        break;

                    case 2:// si ya finalizo
                        dialogAlert.setContentView(R.layout.dialog_show_qr);
                        Button btnDialogClose2 = (Button) dialogAlert.findViewById(R.id.buton_close);
                        Button btnDialogAcept2 = (Button) dialogAlert.findViewById(R.id.buton_acept);
                        ImageView iViewDialogClose2 = (ImageView) dialogAlert.findViewById(R.id.iViewDialogClose);
                        ImageView iViewQR = (ImageView) dialogAlert.findViewById(R.id.iViewQR);

                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(viajeVO.toString(), BarcodeFormat.QR_CODE,400,400);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap =  barcodeEncoder.createBitmap(bitMatrix);
                            iViewQR.setImageBitmap(bitmap);
                        }catch (WriterException e){
                            e.printStackTrace();
                        }


                        TextView mensaje2 = (TextView) dialogAlert.findViewById(R.id.tViewRecomendacion);

                        try{

                            iViewDialogClose2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogAlert.dismiss();
                                }
                            });
                            btnDialogClose2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogAlert.dismiss();

                                }
                            });

                            btnDialogAcept2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ViajeDAO viajeDAO = new ViajeDAO(ctx);
                                    boolean x=(viajeDAO.toStatus3(viajeVO.getId()));

                                    viajeVO.setStatus(3);/// cambiando estado a en curso
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
                        break;

                    case 3:


                        break;
                }



            }
        });

        switch (viajeVO.getStatus()){

            case 0:// si recien llego el viaje
                holder.tViewStatusEnCurso.setTextColor(colorDisable);
                holder.tViewStatusFinalizado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);
                holder.btnEnter.setText("Comenzar Viaje");
                holder.btnEnter.setClickable(true);
                holder.btnEnter.setFocusable(true);
                if(new LoginDataDAO(ctx).verficarLogueo().getIdViaje()>0 && new LoginDataDAO(ctx).verficarLogueo().getIdViaje()!= viajeVO.getId()){
                    holder.btnEnter.setBackgroundColor(colorDisable);
                    holder.btnEnter.setClickable(false);
                    holder.btnEnter.setFocusable(false);
                }else {
                    holder.btnEnter.setBackgroundResource(R.drawable.shape_customgreen_br30_b0);
                }
                break;
            case 1:// si ya esta en curso
                holder.tViewStatusEnCurso.setTextColor(colorEnable);
                holder.tViewStatusFinalizado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);
                holder.btnEnter.setText("Continuar");
                break;

            case 2:// si ya finalizo
                holder.tViewStatusEnCurso.setTextColor(colorDisable);
                holder.tViewStatusFinalizado.setTextColor(colorEnable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);
                holder.btnEnter.setText("Sincronizar");
                break;

            case 3:
                holder.tViewStatusEnCurso.setTextColor(colorDisable);
                holder.tViewStatusFinalizado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorEnable);
                holder.btnEnter.setText("Sincronizado");
                holder.btnEnter.setBackgroundColor(colorDisable);

                break;
        }

        //fin labels
        holder.tViewProveedor.setText(""+viajeVO.getProveedor());
        holder.tViewPlaca.setText(""+viajeVO.getPlaca());
        holder.tViewConductor.setText(""+viajeVO.getConductor());
        holder.tViewRuta.setText(""+viajeVO.getRuta());
        holder.tViewHEmbarque.setText(""+getHour(viajeVO.gethInicio()));
        if(!viajeVO.gethFin().equals("")){
            holder.tViewHDesembarque.setText(""+getHour(viajeVO.gethFin()));
            holder.tViewDateDesembarque.setText(""+getDate(viajeVO.gethFin()));
        }

        holder.tViewCapacidad.setText(""+viajeVO.getCapacidad());

        holder.tViewDateEmbarque.setText(""+getDate(viajeVO.gethInicio()));



    }

    String getDate(String dateTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// replace with your start date string
        Date d = null;
        try {
            d = df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar gc = new GregorianCalendar();
        gc.setTime(d);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate=dateFormat.format(d);
        return formattedDate;

    }

    String getHour(String dateTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// replace with your start date string
        Date d = null;
        try {
            d = df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar gc = new GregorianCalendar();
        gc.setTime(d);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedDate=dateFormat.format(d);
        return formattedDate;


    }


    public float getFloatFormateado(float n){
        return ((int)(n*10))/10.0f;
    }


    public void goToViajeEnCurso(ViajeVO viajeVO){
        Intent i = new Intent(ctx, ActivityViaje.class);
        i.putExtra(ActivityViaje.EXTRA_VIAJE,viajeVO);
        ctx.startActivity(i);
    }


    @Override
    public int getItemCount() {
        return viajeVOList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        //elementos

        //fin labels


        TextView tViewHEmbarque;
        TextView tViewHDesembarque;

        TextView tViewProveedor ;
        TextView tViewPlaca ;
        TextView tViewConductor ;
        TextView tViewRuta ;
        TextView tViewCapacidad;

        TextView tViewDateDesembarque;
        TextView tViewDateEmbarque;


        //status
        TextView tViewStatusEnCurso;
        TextView tViewStatusFinalizado;
        TextView tViewStatusSincronizado;


        //buttons
        Button btnEnter;

        public ViewHolder(View itemView){
            super(itemView);
            tViewProveedor = itemView.findViewById(R.id.tViewProveedor);
            tViewPlaca = itemView.findViewById(R.id.tViewPlaca);
            tViewConductor = itemView.findViewById(R.id.tViewConductor);
            tViewRuta = itemView.findViewById(R.id.tViewRuta);
            tViewHEmbarque = itemView.findViewById(R.id.tViewHEmbarque);
            tViewHDesembarque = itemView.findViewById(R.id.tViewHDesembarque);
            tViewCapacidad = itemView.findViewById(R.id.tViewCapacidad);

            tViewStatusEnCurso = itemView.findViewById(R.id.tViewStatusEnCurso);
            tViewStatusFinalizado = itemView.findViewById(R.id.tViewStatusFinalizado);
            tViewStatusSincronizado = itemView.findViewById(R.id.tViewStatusSincronizado);

            tViewDateEmbarque= itemView.findViewById(R.id.tViewDateEmbarque);
            tViewDateDesembarque= itemView.findViewById(R.id.tViewDateDesembarque);
            btnEnter = (Button) itemView.findViewById(R.id.btnEnter);
        }
    }




}


