package ibao.alanger.alertbus.views;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.Random;

import ibao.alanger.alertbus.R;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class CustomScannerActivity extends AppCompatActivity implements
        DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    private ViewfinderView viewfinderView;

    private static FloatingActionButton fAButtonLinterna;

    private static boolean statusLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);


        statusLight=false;
        fAButtonLinterna = findViewById(R.id.fAButtonLinterna);

        Bundle b = getIntent().getExtras();

        setTitle(b.getString("tittle"));




        fAButtonLinterna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusLight){
                    statusLight = false;
                 //   Toast.makeText(getBaseContext(),"apagando",Toast.LENGTH_SHORT).show();
                    fAButtonLinterna.setImageResource(R.drawable.ic_light_white_off);
                    barcodeScannerView.setTorchOff();

                }else {
                    statusLight = true;
                   // Toast.makeText(getBaseContext(), "encendiendo", Toast.LENGTH_SHORT).show();
                    fAButtonLinterna.setImageResource(R.drawable.ic_highlight_white_on);
                    barcodeScannerView.setTorchOn();
                }
            }
        });


        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);


        viewfinderView = (ViewfinderView) findViewById(R.id.zxing_viewfinder_view);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...


        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        changeMaskColor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {

    }

    public void changeMaskColor(View view) {
        Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //viewfinderView.setMaskColor(color);
    }

    @Override
    public void onTorchOn() {
       // switchFlashlightButton.setText("Apagar Linterna");
    }

    @Override
    public void onTorchOff() {
       // switchFlashlightButton.setText("Encender Linterna");
    }
}
