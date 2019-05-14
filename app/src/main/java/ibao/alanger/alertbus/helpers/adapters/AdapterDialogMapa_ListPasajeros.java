package ibao.alanger.alertbus.helpers.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import ibao.alanger.alertbus.R;

public class AdapterDialogMapa_ListPasajeros {

    private Dialog dialogMap;
    private Context ctx;
    private static String TAG = AdapterDialogMapa_ListPasajeros.class.getSimpleName();

    Button btnAccept;
    WebView mWebView;

    Double latIni, lonIni, latFin, lonFin;
    public AdapterDialogMapa_ListPasajeros(Context ctx, Double latIni, Double lonIni, Double latFin, Double lonFin) {
        this.dialogMap = new Dialog(ctx);
        this.ctx = ctx;
        this.latIni = latIni;
        this.lonIni = lonIni;
        this.latFin = latFin;
        this.lonFin = lonFin;


        declare();
    }

    private void declare(){
        dialogMap.setContentView(R.layout.dialog_map_viaje_listpasajeros);
     //   btnCancel = (Button) dialogMap.findViewById(R.id.btnCancel);
     //   btnAccept = (Button) dialogMap.findViewById(R.id.btnAccept);

        mWebView = (WebView) dialogMap.findViewById(R.id.webView);
        mWebView.addJavascriptInterface(new WebAppInterface(ctx), "Android");
        WebSettings ws = mWebView.getSettings();
        ws.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());

        ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        mWebView.setInitialScale(0);
        ws.setSupportZoom(false);
        ws.setBuiltInZoomControls(false);
        ws.setUseWideViewPort(false);

        btnAccept = dialogMap.findViewById(R.id.btnAccept);

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
                mWebView.loadUrl("javascript:Android.onData(initMap("+latIni+","+lonIni+","+latIni+","+lonFin+"))");
                mWebView.setVisibility(View.VISIBLE);
            }

        });
    }

    public void popDialog(){

        try{

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialogMap.dismiss();
                }
            });

           dialogMap.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           dialogMap.show();
            LoadMap();
        }catch (Exception e){
            Toast.makeText(ctx,e.toString(),Toast.LENGTH_LONG).show();
            Log.d(TAG,e.toString());
        }


    }

}
