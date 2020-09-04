package ibao.alertbus.ecosac.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ibao.alertbus.ecosac.models.dao.LoginDataDAO;

public class AutoArranqueBroadcast extends BroadcastReceiver{

    Context ctx;
    public static boolean isOpening = false;
    @Override
    public void onReceive(Context context, Intent intent) {

        ctx = context;
        Intent service1 = new Intent(context, SearchViajesService.class);
        Intent service2 = new Intent(context, UploadService.class);
        Intent service3 = new Intent(context, SearchChangesViajesService.class);

        if (new LoginDataDAO(context).verficarLogueo()!=null) {//si esta logueado
            isOpening = false;
            switch (new LoginDataDAO(context).verficarLogueo().getTypeUser()){
                case 0:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService (service1);
                        context.startForegroundService (service2);
                        context.startForegroundService (service3);

                    }else {
                        context.startService (service1);
                        context.startService (service2);
                        context.startService (service3);

                    }
                    break;
                case 1:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService (service2);// upload

                    }else {
                        context.startService (service2);//upload
                    }
                    break;
            }
        }
    }

}
