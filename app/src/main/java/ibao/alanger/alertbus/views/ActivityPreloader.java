package ibao.alanger.alertbus.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.services.SearchViajesService;
import ibao.alanger.alertbus.services.UploadService;


public class ActivityPreloader extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloader);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    void  startServices(){
        Intent intent = new Intent(this, SearchViajesService.class);
        startService(intent);

        intent = new Intent(this, UploadService.class);
        startService(intent);
    }


    void  openMain(){
        Intent intent = new Intent(this, MainSupervisorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    void  openLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}