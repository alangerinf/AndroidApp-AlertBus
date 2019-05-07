package ibao.alanger.alertbus.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import ibao.alanger.alertbus.helpers.DownloadNewViajes;

public class SeachViajesService extends Service {


    final Handler handler = new Handler();
    Context ctx;
    public SeachViajesService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
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

    private static int timeMilis=1000*5;

    public void setTimeMilis(int timeMilis) {
        SeachViajesService.timeMilis = timeMilis;
    }

    public static int getTimeMilis() {
        return timeMilis;
    }

    Runnable runnable = new Runnable() {
        public void run() {

            new DownloadNewViajes(ctx).SearchNews();
           // handler.postDelayed(runnable, timeMilis);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
