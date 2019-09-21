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
        Intent service1 = new Intent(context, SearchViajesService.class);
        Intent service2 = new Intent(context, UploadService.class);

        if (new LoginDataDAO(context).verficarLogueo()!=null) {//si esta logueado

            if(new LoginDataDAO(context).verficarLogueo().getTypeUser()==0){// si es conductor

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService (service1);
                    context.startForegroundService (service2);

                }else {
                    context.startService (service1);
                    context.startService (service2);

                }
            }else {// si es supervisor ==1
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService (service2);// upload

                }else {
                    context.startService (service2);//upload
                }
            }





        }
    }

}
