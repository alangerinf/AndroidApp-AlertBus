package ibao.alanger.alertbus.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.List;

import ibao.alanger.alertbus.helpers.DownloadNewViajes;
import ibao.alanger.alertbus.helpers.UploadMaster;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.ViajeVO;

public class UploadService extends Service {


    final Handler handler = new Handler();
    Context ctx;

    public static boolean statusUpload = false;


    public UploadService() {
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

    private static int timeMilis=1000*2;

    public void setTimeMilis(int timeMilis) {
        UploadService.timeMilis = timeMilis;
    }

    public static int getTimeMilis() {
        return timeMilis;
    }

    Runnable runnable = new Runnable() {
        public void run() {
            List<ViajeVO> viajeVOList = new ViajeDAO(ctx).listByStatus1();
            new UploadMaster(ctx).UploadViaje(viajeVOList);
            //handler.postDelayed(runnable, timeMilis);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
