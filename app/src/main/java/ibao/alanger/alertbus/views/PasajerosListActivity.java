package ibao.alanger.alertbus.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.adapters.AdapterDialogMapa;
import ibao.alanger.alertbus.helpers.adapters.AdapterDialogMapa_ListPasajeros;

public class PasajerosListActivity extends Activity {

    private static WebView mWebView;
    private static Context ctx ;

    private static FloatingActionButton fAButtonShowDialogMap;

    private static FloatingActionButton fAButtonAddPasajero;

    private static Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajeros_list);

        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),MapsActivity.class);
                startActivity(i);
            }
        });



        fAButtonAddPasajero = findViewById(R.id.fAButtonAddPasajero);
        fAButtonAddPasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String[] colors = {"DNI", "Lectura QR Unico", "Lectura QR Continuo"};

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Añadir Pasajero");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                final Dialog dialogDni = new Dialog(ctx);
                                dialogDni.setContentView(R.layout.dialog_add_dni);
                                Button btnAccept = dialogDni.findViewById(R.id.btnAccept);
                                final EditText eTextDNI =dialogDni.findViewById(R.id.eTextDNI);
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(eTextDNI.getText().toString().length()!=8){
                                            Snackbar.make(view, "DNI no válido", Snackbar.LENGTH_LONG)
                                                     .setAction("Action", null).show();
                                        }else{
                                            dialogDni.dismiss();
                                        }

                                    }
                                });
                                dialogDni.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogDni.show();
                                break;

                            case 1:
                                IntentIntegrator intentIntegrator1 =new IntentIntegrator(PasajerosListActivity.this);
                                intentIntegrator1
                                        .addExtra("tittle","Lectura Unica")
                                        .setOrientationLocked(false)
                                        .setCaptureActivity(CustomScannerActivity.class)
                                        .initiateScan();
                                break;

                            case 2:
                                IntentIntegrator intentIntegrator2 =new IntentIntegrator(PasajerosListActivity.this);
                                intentIntegrator2
                                        .addExtra("tittle","Lectura Continua")
                                        .setOrientationLocked(false)
                                        .setCaptureActivity(CustomScannerActivity.class)
                                        .initiateScan();
                                break;

                        }
                    }
                });
                builder.show();

            }
        });

        this.ctx = this;

        if(getRotation(ctx).equals("horizontal")){
            declareMap();
        }else {
            fAButtonShowDialogMap = findViewById(R.id.fAButtonShowDialogMap);
            fAButtonShowDialogMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AdapterDialogMapa_ListPasajeros adapterDialogMapa = new AdapterDialogMapa_ListPasajeros(v.getContext(),-8.1329634,-79.049854,-8.125603,-79.031894);

                    adapterDialogMapa.popDialog();
                }
            });
            //Toast.makeText(ctx,"vertical",Toast.LENGTH_LONG).show();
        }

    }


        private void declareMap(){

            mWebView = (WebView) findViewById(R.id.webView);
            mWebView.addJavascriptInterface(new PasajerosListActivity.WebAppInterface(ctx), "Android");
            WebSettings ws = mWebView.getSettings();
            ws.setJavaScriptEnabled(true);

            mWebView.setWebChromeClient(new WebChromeClient());

            ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
            mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
            mWebView.setInitialScale(0);
            ws.setSupportZoom(false);
            ws.setBuiltInZoomControls(false);
            ws.setUseWideViewPort(false);
            LoadMap();

        }
        class WebAppInterface {
            Context mContext;

            /** Instantiate the interface and set the context */
            WebAppInterface(Context c) {
                mContext = c;
            }

            @JavascriptInterface
            public void js_DatosDriving(final String dist) {

            }
        }
        @SuppressLint("SetJavaScriptEnabled")
        private void LoadMap(){
            mWebView.loadUrl("file:///android_asset/www/index.html");
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(mWebView, url);
                    mWebView.loadUrl("javascript:Android.onData(initMap("+(-8.1329634)+","+(-79.049854)+","+(-8.125603)+","+(-79.031894)+"))");
                    mWebView.setVisibility(View.VISIBLE);
                }

            });
        }

    public String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                return "vertical";
            case Surface.ROTATION_90:
            default:
                return "horizontal";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
               // toast = "Cancelled from fragment";
            } else {
             //   toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
          ///  displayToast();
        }
    }
}
