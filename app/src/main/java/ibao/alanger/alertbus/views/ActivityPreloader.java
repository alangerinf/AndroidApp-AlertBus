package ibao.alanger.alertbus.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.services.LocationService;
import ibao.alanger.alertbus.services.SearchViajesService;
import ibao.alanger.alertbus.services.UploadService;
import ibao.alanger.alertbus.utilities.Utils;


public class ActivityPreloader extends Activity {

    private static String TAG = ActivityPreloader.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloader);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


/*
        try {

        String tst = "[{\"capacidad\":45,\"comentario\":\"\",\"conductor\":\"QUIROZ NUNEZ, DORITA\",\"hConfirmado\":\"2019-09-24 13:04:14\",\"hFin\":\"2019-09-24 13:06:40\",\"hInicio\":\"2019-09-24 13:04:14\",\"hProgramada\":\"2019-09-24 14:00:00\",\"id\":1,\"idWeb\":0,\"numPasajeros\":0,\"numRestricciones\":0,\"pasajeroVOList\":[{\"dni\":\"45931220\",\"hBajada\":\"\",\"hSubida\":\"2019-09-24 13:05:18\",\"id\":19,\"idViaje\":1,\"name\":\"\",\"observacion\":\"\"},{\"dni\":\"70773577\",\"hBajada\":\"\",\"hSubida\":\"2019-09-24 13:04:52\",\"id\":18,\"idViaje\":1,\"name\":\"\",\"observacion\":\"\"},{\"dni\":\"70156888\",\"hBajada\":\"\",\"hSubida\":\"2019-09-24 13:04:41\",\"id\":17,\"idViaje\":1,\"name\":\"\",\"observacion\":\"\"},{\"dni\":\"77086608\",\"hBajada\":\"2019-09-24 13:06:39\",\"hSubida\":\"2019-09-24 13:04:31\",\"id\":16,\"idViaje\":1,\"name\":\"\",\"observacion\":\"\"},{\"dni\":\"47766453\",\"hBajada\":\"2019-09-24 13:06:39\",\"hSubida\":\"2019-09-24 13:04:24\",\"id\":15,\"idViaje\":1,\"name\":\"\",\"observacion\":\"\"}],\"placa\":\"XYZ-123\",\"proveedor\":\"Transporte El Sol\",\"restriccionVOList\":[{\"desc\":\"\",\"id\":3,\"idViaje\":1,\"name\":\"null\"}],\"ruta\":\"TRUJILLO - CHAO\",\"status\":2}]";
            Log.d(TAG,"Enc 0: " + (tst));
        System.out.println("Enc 0: " + (tst));
        // byte[] stg1 = Utils.compress(tst);
        //  String codificado = new String(stg1, "ISO-8859-1");
       //     stg1 = codificado.getBytes("ISO-8859-1");

        String comprimido = Utils.compress(tst);
        Log.d(TAG,"comprimido:"+comprimido);
        Log.d(TAG,"descomprimido:"+Utils.decompress(comprimido));

        //String dec1 = null;
        //dec1 = Utils.decompress(stg1);

     //   System.out.println("unzip: " +(dec1));
     //   Log.d(TAG,"unzip: " + (dec1));
        } catch (IOException e) {
            Log.d(TAG,e.toString());
            e.printStackTrace();
        }

*/
        final Resources res = getResources();
        final int colorProgress = res.getColor(R.color.primaryLight);

        ((ProgressBar)findViewById(R.id.progressBar))
                .getIndeterminateDrawable()

                .setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                //verificamos si tenemos internet
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {// si tiene internet
                    if (new LoginDataDAO(getBaseContext()).verficarLogueo()!=null) {//si esta logueado
                        startServices();
                        openMain();
                        finish();
                    } else {
                        openLogin();
                        finish();
                    }
                } else {// sino  tiene internet
                    if (new LoginDataDAO(getBaseContext()).verficarLogueo()!=null) {//si esta logueado
                        startServices();
                        openMain();
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(),"Conectese a internet porfavor",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

            }
        }, 500);
    }

    void startServices(){

        Context context = getBaseContext();
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

    void  openMain(){
        Intent intent;
        if(new LoginDataDAO(this).verficarLogueo().getTypeUser()==0){//si  es conductor
            intent = new Intent(this, MainConductorActivity.class);//para hacer testing cambiar segun requiera
        }else {// si es supervisor
            intent = new Intent(this, MainSupervisorActivity.class);//para hacer testing cambiar segun requiera
        }
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    void  openLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}