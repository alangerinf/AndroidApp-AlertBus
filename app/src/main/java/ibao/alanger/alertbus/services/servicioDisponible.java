package ibao.alanger.alertbus.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.views.MapsActivity;

/**
 * Created by Administrador on 28/08/2017.
 */
public class servicioDisponible extends Service {

    final Handler handler = new Handler();
    Notification notification;
    NotificationManager notificationManager;
    Context ctx;

    static String TAG = servicioDisponible.class.getSimpleName();


   static public double lat =-8.1395615, lng=-79.0386577;
    static public float bearing,speed;



    LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();


    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    String CHANNEL_NOTIFICATION = "recorrido";
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

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent i, int flags, int startId) {

        ctx = this;

        //handler.removeCallbacks(runnable);
        Bundle extras = i.getExtras();
        lat = extras.getDouble("lat");
        lng = extras.getDouble("lng");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MyLocationListener mlocListener = new MyLocationListener();
        mlocListener.setMainActivity(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mlocListener);

        Intent intent = new Intent(this, MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher_round, "ver recorrido", pendingIntent).build();

        //Notification
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_NOTIFICATION)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("En camino")
                .setContentTitle("Alert Bus")
                .addAction(action)
                .build();



        startForeground(1, notification);

        return START_STICKY;
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        notificationManager.cancel(1);

    }



    /////////////gps/////////////////////
    public class MyLocationListener implements LocationListener {
        servicioDisponible servicioDisponible;
        public servicioDisponible getMainActivity() {
            return servicioDisponible;
        }
        public void setMainActivity(servicioDisponible mainActivity) {
            this.servicioDisponible = mainActivity;
        }

        @Override
        public void onLocationChanged(Location location) {
            if(location!=null){
                try {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    bearing = location.getBearing();
                    speed = location.getSpeed();
                    Log.d("hola",location.toString()+" spedd :"+location.getSpeed());
                }catch (Exception e){
                    //algo salio mal
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {





        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
            notificationManager.cancel(1);
        }
    }
    /////////////fin gps/////////////////
}
