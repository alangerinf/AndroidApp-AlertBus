package ibao.alanger.alertbus.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.main.MainConductorActivity;
import ibao.alanger.alertbus.main.MainSupervisorActivity;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.services.SearchChangesViajesService;
import ibao.alanger.alertbus.services.SearchViajesService;
import ibao.alanger.alertbus.services.UploadService;


public class ActivityPreloader extends Activity {

    private static String TAG = ActivityPreloader.class.getSimpleName();
    static LottieAnimationView lAVbackground;
    static ImageView iViewLogoEmpresa;
    static TextView tViewlogo_p1;
    static TextView tViewlogo_p2;
    static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloader);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        declare();
        startAnimations();


    }

    private void declare() {
        ctx = this;
        lAVbackground = findViewById(R.id.lottie);
        iViewLogoEmpresa = findViewById(R.id.iViewLogoEmpresa);
        tViewlogo_p1 = findViewById(R.id.tViewlogo_p1);
        tViewlogo_p2 = findViewById(R.id.tViewlogo_p2);
    }

    void startAnimations(){
        final Animation animLayout =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);

        Handler handler = new Handler();
        handler.post(
                () -> {
                    lAVbackground.startAnimation(animLayout);
                    lAVbackground.setVisibility(View.VISIBLE);
                }
        );

        final Animation anim_rightFadeIn1 =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(), R.anim.right_fade_in);

        Handler handler1 = new Handler();
        handler1.post(
                () -> {
                    iViewLogoEmpresa.startAnimation(anim_rightFadeIn1);
                    iViewLogoEmpresa.setVisibility(View.VISIBLE);
                }
        );
        final Animation anim_rightFadeIn2 =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(), R.anim.top_fade_in);
        Handler handler2 = new Handler();
        handler2.postDelayed(
                () -> {
                    tViewlogo_p1.startAnimation(anim_rightFadeIn2);
                    tViewlogo_p1.setVisibility(View.VISIBLE);
                },500
        );
        final Animation anim_rightFadeIn3 =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(), R.anim.top_fade_in);
        Handler handler3 = new Handler();
        handler3.postDelayed(
                () -> {
                    tViewlogo_p2.startAnimation(anim_rightFadeIn3);
                    tViewlogo_p2.setVisibility(View.VISIBLE);
                },600
        );

        Handler handler4 = new Handler();
        handler4.postDelayed(()->
        {
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
        },2000);
    }


    void startServices(){

        Context context = getBaseContext();
        Intent service1 = new Intent(context, SearchViajesService.class);
        Intent service2 = new Intent(context, UploadService.class);
        Intent service3 = new Intent(context, SearchChangesViajesService.class);


        if (new LoginDataDAO(context).verficarLogueo()!=null) {//si esta logueado
            if(new LoginDataDAO(context).verficarLogueo().getTypeUser()==0){// si es conductor
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d(TAG,"startServices");
                    context.startForegroundService (service1);
                    Log.d(TAG,"startService1");
                    context.startForegroundService (service2);
                    Log.d(TAG,"startService2");
                    context.startForegroundService (service3);
                    Log.d(TAG,"startService3");
                }else {
                    context.startService (service1);
                    context.startService (service2);
                    context.startService (service3);
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



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}