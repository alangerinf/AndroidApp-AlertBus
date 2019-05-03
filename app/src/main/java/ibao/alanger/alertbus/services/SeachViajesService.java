package ibao.alanger.alertbus.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class SeachViajesService extends Service {
    public SeachViajesService() {
    }

    @Override
    public void onCreate(){

    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess)
    {

        return START_STICKY;
    }

    @Override
    public void onDestroy(){

    }


    private static int timeMilis=1000*5;

    public void setTimeMilis(int timeMilis) {
        SeachViajesService.timeMilis = timeMilis;
    }

    void buscarNuevos(){
        Handler hander = new Handler();
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                consultaNuevos();
                buscarNuevos();
            }
        },timeMilis
        );

    }

    private void consultaNuevos() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
