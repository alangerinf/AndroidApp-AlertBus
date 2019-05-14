package ibao.alanger.alertbus.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import ibao.alanger.alertbus.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ActivityEscanerQR extends AppCompatActivity implements ZXingScannerView.ResultHandler{

        ZXingScannerView QRScanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner_qr);
        scan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        QRScanner.stopCamera();
    }
    private void scan(){
        QRScanner = new ZXingScannerView(getApplicationContext());
        setContentView(QRScanner);
        QRScanner.setResultHandler(this);
        QRScanner.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
       // Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
       // returnIntent.putExtra(ActivityEvaluacion.QR_RESULT,result.getText());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        QRScanner.stopCamera();
        finish();
    }
}
