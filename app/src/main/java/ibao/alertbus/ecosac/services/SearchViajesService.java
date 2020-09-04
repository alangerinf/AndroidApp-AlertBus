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

import ibao.alertbus.ecosac.helpers.DownloadNewViajes;
import ibao.alertbus.ecosac.main.MainConductorActivity;

public class SearchViajesService extends Service {


    final Handler handler = new Handler();
    Context ctx;

    private static String TAG  = SearchViajesService.class.getSimpleName();

    public static boolean statusActualizar = false;
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

    public SearchViajesService() {
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
    {
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
        SearchViajesService.timeMilis = timeMilis;
    }

    public static int getTimeMilis() {
        return timeMilis;
    }

    int restriccion2 = 0;
    boolean buscar = true;
    Runnable runnable = new Runnable() {
        public void run() {
            Log.d(TAG,"buscando viajes");
            new DownloadNewViajes(ctx).SearchNews();
            if (buscar) new DownloadNewViajes(ctx).getTrabajadorRestricciones();
            restriccion2 += timeMilis;
            if (restriccion2 >= 3600000) {
                MainConductorActivity.isSaving = false; buscar = true; restriccion2 = 0;} else buscar = false;
            handler.postDelayed(runnable, timeMilis);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
