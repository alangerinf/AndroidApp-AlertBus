package ibao.alanger.alertbus.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.adapters.AdapterDialogMapa;
import ibao.alanger.alertbus.helpers.adapters.AdapterDialogMapa_ListPasajeros;

public class PasajerosListActivity extends Activity {

    private static WebView mWebView;
    private static Context ctx ;

    private static FloatingActionButton fAButtonShowDialogMap;

    private static AdapterDialogMapa_ListPasajeros adapterDialogMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajeros_list);

        FloatingActionButton fab = findViewById(R.id.fAButtonAddPasajero);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
}
