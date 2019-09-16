package ibao.alanger.alertbus.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import ibao.alanger.alertbus.helpers.DownloadNewViajes;

public class SearchViajesService extends Service {


    final Handler handler = new Handler();
    Context ctx;

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

    private static int timeMilis=1000*10;

    public void setTimeMilis(int timeMilis) {
        SearchViajesService.timeMilis = timeMilis;
    }

    public static int getTimeMilis() {
        return timeMilis;
    }

    Runnable runnable = new Runnable() {
        public void run() {

            new DownloadNewViajes(ctx).SearchNews();
            handler.postDelayed(runnable, timeMilis);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
