package ibao.alanger.alertbus.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import ibao.alanger.alertbus.models.dao.LoginDataDAO;

public class AutoArranqueBroadcast extends BroadcastReceiver{




    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {

        ctx = context;

        if (new LoginDataDAO(context).verficarLogueo()!=null) {//si esta logueado

            Intent service1 = new Intent(context, SearchViajesService.class);
            Intent service2 = new Intent(context, UploadService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService (service1);
                context.startForegroundService (service2);

            }else {
                context.startService (service1);
                context.startService (service2);
            }

        }
    }

}
