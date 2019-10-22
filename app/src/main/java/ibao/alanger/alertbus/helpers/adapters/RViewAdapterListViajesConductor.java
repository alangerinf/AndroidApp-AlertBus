package ibao.alanger.alertbus.helpers.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.LoginHelper;
import ibao.alanger.alertbus.main.PageViewModelViajesActuales;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.PasajeroDAO;
import ibao.alanger.alertbus.models.dao.RestriccionDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;
import ibao.alanger.alertbus.utilities.Utils;
import ibao.alanger.alertbus.viajeEnCurso.ActivityViaje;

import static android.graphics.Color.BLACK;

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

                            mensaje0.setText(ctx.getString(R.string.pregunta_comenzar_viaje));
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
                                    viajeVO.sethInicio(Utils.getFecha());
                                    new ViajeDAO(ctx).updateHoraInicio(viajeVO.getId(),viajeVO.gethInicio());
                                    PageViewModelViajesActuales.viajeToStatus1(viajeVO.getId());

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
                        Handler h = new Handler();
                        h.post(()->{
                            v.setClickable(false);
                            v.setFocusable(false);
                            goToViajeEnCurso(viajeVO);
                        });
                        h.postDelayed(()->{
                            v.setClickable(true);
                            v.setFocusable(true);
                        },500);
                        break;

                    case 2:// si ya finalizo
                        /*
                        dialogAlert.setContentView(R.layout.dialog_show_qr);



                        Button btnDialogClose2 = (Button) dialogAlert.findViewById(R.id.buton_close);
                        Button btnDialogAcept2 = (Button) dialogAlert.findViewById(R.id.buton_acept);
                        ImageView iViewDialogClose2 = (ImageView) dialogAlert.findViewById(R.id.iViewDialogClose);
                        ImageView iViewQR = (ImageView) dialogAlert.findViewById(R.id.iViewQR);

                        try {

                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
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
                                   // boolean x=(viajeDAO.toStatus3(viajeVO.getId()));

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
*/
                        break;

                    case 3:


                        dialogAlert.setContentView(R.layout.dialog_show_qr);
                        Button btnDialogClose3 = (Button) dialogAlert.findViewById(R.id.buton_close);
                        Button btnDialogAcept3 = (Button) dialogAlert.findViewById(R.id.buton_acept);
                        ImageView iViewDialogClose3 = (ImageView) dialogAlert.findViewById(R.id.iViewDialogClose);
                        ImageView iViewQR3 = (ImageView) dialogAlert.findViewById(R.id.iViewQR);
 /*
                   try {

                            MultiFormatWriter multiFormatWriter3 = new MultiFormatWriter();
                            Log.d(TAG,viajeVO.toString());
                            Log.d(TAG,"tamaño"+viajeVO.toString().length());

                           BitMatrix bitMatrix = multiFormatWriter3.encode(viajeVO.toString(), BarcodeFormat.QR_CODE,512,512);
                       int width = bitMatrix.getWidth();
                       int height = bitMatrix.getHeight();
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap =  barcodeEncoder.createBitmap(bitMatrix);
                            */



                        try {
                            ViajeVO temp = new ViajeDAO(ctx).buscarById(viajeVO.getId());

                            Log.d(TAG,"qr data:"+temp.toStringQR());

                            iViewQR3.setImageBitmap(Utils.createQRCode(temp.toStringQR(),2048));
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }


                        TextView mensaje3 = (TextView) dialogAlert.findViewById(R.id.tViewRecomendacion);

                        try{

                            iViewDialogClose3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogAlert.dismiss();
                                }
                            });

                            mensaje3.setText("Viaje ya sincronizado");
                            btnDialogClose3.setVisibility(View.GONE);
                            btnDialogAcept3.setVisibility(View.GONE);

                            dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogAlert.show();

                        }catch (Exception e){
                            Toast.makeText(ctx,e.toString(),Toast.LENGTH_LONG).show();
                            Log.d(TAG,e.toString());
                        }
                        break;

                }
            }
        });


        holder.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAlert.setContentView(R.layout.dialog_message);
                Button btnDialogClose0 = (Button) dialogAlert.findViewById(R.id.buton_close);
                Button btnDialogAcept0 = (Button) dialogAlert.findViewById(R.id.buton_acept);
                ImageView iViewDialogClose0 = (ImageView) dialogAlert.findViewById(R.id.iViewDialogClose);
                TextView mensaje0 = (TextView) dialogAlert.findViewById(R.id.tViewRecomendacion);
                mensaje0.setText(ctx.getString(R.string.pregunta_eliminar_viaje));

                try {

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

                            new ViajeDAO(ctx).deleteById(viajeVO.getId());
                            viajeVOList.remove(viajeVO);
                            notifyDataSetChanged();
                            dialogAlert.dismiss();


                        }
                    });

                    dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogAlert.show();



                }catch (Exception e){
                    Toast.makeText(ctx,TAG+e.toString(),Toast.LENGTH_LONG).show();
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
                if(new LoginDataDAO(ctx).verficarLogueo().getIdViaje()>0 && new LoginDataDAO(ctx).verficarLogueo().getIdViaje()!= viajeVO.getId()){// si no es el viaje actual

                    holder.btnEnter.setBackgroundResource(R.drawable.shape_disable_br30_b0);

                    holder.btnEnter.setClickable(false);
                    holder.btnEnter.setFocusable(false);
                }else {//si es el viaje actual
                    holder.btnEnter.setBackgroundResource(R.drawable.shape_customgreen_br30_b0);
                }

                holder.fabDelete.setClickable(false);
                holder.fabDelete.setFocusable(false);
                holder.fabDelete.setBackgroundTintList(ColorStateList.valueOf(colorDisable));
              //  holder.fabDelete.setBackgroundTintList(ColorStateList.valueOf(colorDisable));

                break;
            case 1:// si ya esta en curso
                holder.tViewStatusEnCurso.setTextColor(colorEnable);
                holder.tViewStatusFinalizado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);

                holder.btnEnter.setText("Continuar");
                holder.btnEnter.setBackgroundResource(R.drawable.shape_customgreen_br30_b0);

                holder.fabDelete.setClickable(false);
                holder.fabDelete.setFocusable(false);
                holder.fabDelete.setBackgroundTintList(ColorStateList.valueOf(colorDisable));


                break;

            case 2:// si ya finalizo
                holder.tViewStatusEnCurso.setTextColor(colorDisable);
                holder.tViewStatusFinalizado.setTextColor(colorEnable);
                holder.tViewStatusSincronizado.setTextColor(colorDisable);
                holder.btnEnter.setText("Finalizado");
                holder.btnEnter.setBackgroundResource(R.drawable.shape_disable_br30_b0);

                holder.fabDelete.setClickable(false);
                holder.fabDelete.setFocusable(false);
                holder.fabDelete.setBackgroundTintList(ColorStateList.valueOf(colorDisable));
                break;

            case 3:
                holder.btnEnter.setBackgroundResource(R.drawable.shape_customgreen_br30_b0);
                holder.tViewStatusEnCurso.setTextColor(colorDisable);
                holder.tViewStatusFinalizado.setTextColor(colorDisable);
                holder.tViewStatusSincronizado.setTextColor(colorEnable);
                holder.btnEnter.setText("Ver QR");

                holder.fabDelete.setClickable(true);
                holder.fabDelete.setFocusable(true);
                holder.fabDelete.setBackgroundTintList(ColorStateList.valueOf(colorRed));

                break;
        }

        //fin labels
        holder.tViewProveedor.setText(""+viajeVO.getProveedor());
        holder.tViewPlaca.setText(""+viajeVO.getPlaca());
        holder.tViewConductor.setText(""+viajeVO.getConductor());
        holder.tViewRuta.setText(""+viajeVO.getRuta());

        if(viajeVO.gethInicio()==null ||viajeVO.gethInicio().equals("")){// si la hora de inicio esta seteada
            holder.tViewHEmbarque.setText(""+getHour(viajeVO.gethProgramada()));
            holder.tViewDateEmbarque.setText(""+getDate(viajeVO.gethProgramada()));
        }else {
            holder.tViewHEmbarque.setText(""+getHour(viajeVO.gethInicio()));
            holder.tViewDateEmbarque.setText(""+getDate(viajeVO.gethInicio()));
        }

        if(viajeVO.gethFin()== null || viajeVO.gethFin().equals("")){ // si no tiene hora fin
            Log.d(TAG,"sin hora fin");
        }else{
            holder.tViewHDesembarque.setText(""+getHour(viajeVO.gethFin()));
            holder.tViewDateDesembarque.setText(""+getDate(viajeVO.gethFin()));

        }

        holder.tViewCapacidad.setText(""+viajeVO.getCapacidad());

    }


    public static Bitmap generateQrCode(String myCodeText) throws WriterException {
        Bitmap bmp = null;
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(myCodeText, BarcodeFormat.QR_CODE, 1024, 1024);
            int width = 1024;
            int height = 1024;
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y))
                        bmp.setPixel(x, y, Color.BLACK);
                    else
                        bmp.setPixel(x, y, Color.WHITE);
                }
            }
            Log.d("RViewAdapterList","lco");
        } catch (WriterException e) {
            Log.e("QR ERROR", ""+e.toString());
        }
        return bmp;
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
        viajeVO.setPasajeroVOList(new PasajeroDAO(ctx).listByIdViaje(viajeVO.getId()));
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

        FloatingActionButton fabDelete;


        //buttons
        Button btnEnter;

        public ViewHolder(View itemView){
            super(itemView);
            tViewProveedor = itemView.findViewById(R.id.tViewProveedor);
            tViewPlaca = itemView.findViewById(R.id.tViewPlaca);
            tViewConductor = itemView.findViewById(R.id.tViewConductor);
            tViewRuta = itemView.findViewById(R.id.tViewRuta);
            tViewHDesembarque = itemView.findViewById(R.id.tViewHDesembarque);
            tViewCapacidad = itemView.findViewById(R.id.tViewCapacidad);

            tViewStatusEnCurso = itemView.findViewById(R.id.tViewStatusEnCurso);
            tViewStatusFinalizado = itemView.findViewById(R.id.tViewStatusFinalizado);
            tViewStatusSincronizado = itemView.findViewById(R.id.tViewStatusSincronizado);

            tViewDateEmbarque= itemView.findViewById(R.id.tViewDateEmbarque);
            tViewHEmbarque = itemView.findViewById(R.id.tViewHEmbarque);
            tViewDateDesembarque= itemView.findViewById(R.id.tViewDateDesembarque);
            btnEnter = (Button) itemView.findViewById(R.id.btnEnter);

            fabDelete = itemView.findViewById(R.id.fabDelete);
        }
    }




}


