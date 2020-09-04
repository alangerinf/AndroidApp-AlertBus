package ibao.alertbus.ecosac.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.List;

import ibao.alertbus.ecosac.helpers.UploadMaster;
import ibao.alertbus.ecosac.helpers.UploadTracking;
import ibao.alertbus.ecosac.models.dao.LoginDataDAO;
import ibao.alertbus.ecosac.models.dao.TrackingDAO;
import ibao.alertbus.ecosac.models.dao.ViajeDAO;
import ibao.alertbus.ecosac.models.vo.TrackingVO;
import ibao.alertbus.ecosac.models.vo.ViajeVO;

public class UploadService extends Service { // tracking y sincronizacioin de viajes


    final Handler handler = new Handler();
    Context ctx;

    final String TAG = UploadService.class.getSimpleName();

    public static boolean statusUpload = false;

    String CHANNEL_NOTIFICATION = "my cga";
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Activacion de Servicios";
            String description = "Servicios Sincronizacióadminn";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_NOTIFICATION, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public UploadService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ctx = this;
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_NOTIFICATION)
                .setContentTitle("")
                .setContentText("").build();

        startForeground(9999999, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess)
    {   ctx = this;
        handler.removeCallbacks(runnable);
        runnable.run();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private static int timeMilis=1000*60;

    public void setTimeMilis(int timeMilis) {
        UploadService.timeMilis = timeMilis;
    }

    public static int getTimeMilis() {
        return timeMilis;
    }

    Runnable runnable = new Runnable() {
        public void run() {
            Log.d(TAG,"runnable()");
            //sincronizacion automatica
            if (new LoginDataDAO(ctx).verficarLogueo().getIsTranquera() == 1){
                List<ViajeVO> viajeVOList = new ViajeDAO(ctx).listByStatusWaitingAtUpload();
                if(viajeVOList.size()>0){
                    Log.d(TAG,"subiendo "+viajeVOList.size()+" viajes");
                    new UploadMaster(ctx).UploadViajeFinish(viajeVOList);
                }
            }
            else {
                List<ViajeVO> viajeVOList = new ViajeDAO(ctx).listByStatusWaitingAtUpload();
                if(viajeVOList.size()>0){
                    Log.d(TAG,"subiendo "+viajeVOList.size()+" viajes");
                    new UploadMaster(ctx).UploadViajeFinish(viajeVOList);
                }
                if(new LoginDataDAO(ctx).verficarLogueo().getTypeUser()==0){// si es conductor
                    int IDVIAJE = new LoginDataDAO(ctx).verficarLogueo().getIdViaje();
                    if(IDVIAJE >0 ){ // si hay un viaje activo
                        //                  ViajeVO VIAJE = new ViajeDAO(ctx).buscarById(IDVIAJE);
                    /*
                    if(VIAJE!=null && VIAJE.getIdWeb()!=0){
                        new UploadMaster(ctx).UploadViajeProgress(VIAJE);
                    }
*/
                        if(!LocationService.isEnable){
                            Intent intent = new Intent(ctx,LocationService.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
                                getBaseContext().startForegroundService(intent);
                            }else {
                                startService(intent);
                            }
                            Log.d(TAG,"*****gpsActivado");
                        }
                        Log.d(TAG,"gpsActivado");

                    }else {
                        if(LocationService.isEnable){
                            Intent intent = new Intent(ctx,LocationService.class);
                            stopService(intent);
                            Log.d(TAG,"*****gpsDisable");
                        }
                        Log.d(TAG,"gpsDisable");
                    }

                    //subida de Traking
                    List<TrackingVO> trackingVOList = new TrackingDAO(ctx).getNoUpdates();
                    if(trackingVOList.size()>0){ //si la lista de tracking esta llena y el hay un viaje en curso
                        new UploadTracking(ctx).upload(trackingVOList);
                    }
                }
            }
            if (new LoginDataDAO(ctx).verficarLogueo().getIsTranquera() == 1)
                handler.postDelayed(runnable, 60000);
            else
                handler.postDelayed(runnable, timeMilis);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
