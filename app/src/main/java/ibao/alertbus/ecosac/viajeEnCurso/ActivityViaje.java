package ibao.alertbus.ecosac.viajeEnCurso;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ibao.alertbus.ecosac.BuildConfig;
import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.main.MainConductorActivity;
import ibao.alertbus.ecosac.main.PageViewModelViajesActuales;
import ibao.alertbus.ecosac.models.dao.LoginDataDAO;
import ibao.alertbus.ecosac.models.dao.PasajeroDAO;
import ibao.alertbus.ecosac.models.dao.ViajeDAO;
import ibao.alertbus.ecosac.models.vo.PasajeroVO;
import ibao.alertbus.ecosac.models.vo.ViajeVO;



import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;


import static android.Manifest.permission.CAMERA;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class ActivityViaje extends AppCompatActivity implements
        DecoratedBarcodeView.TorchListener {

    private String TAG = ActivityViaje.class.getSimpleName();

    private static PendingIntent permissionIntent;
    private static Physicaloid mPhysicaloid; // initialising library
    public static final String INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".USB_PERMISSION";

    private DecoratedBarcodeView barcodeScannerView;

    private static FloatingActionButton fAButtonShowDialogMap;

    private ViewfinderView viewfinderView;

    private static FloatingActionButton fAButtonLinterna,fAButtonSwitch;
    private static TextView tViewRFID;
    private static ScrollView scrollViewRFID;

    private static boolean statusLight;

    public static final String EXTRA_VIAJE = "extra_VIAJE";

    private PageViewModelViaje pageViewModel;

    private static View root ;

    private Context ctx = ActivityViaje.this;

    private ViajeVO VIAJEVO;

    TextView tViewHEmbarque;
    TextView tViewDateEmbarque;

    EditText txtNumero;
    private static String RFID="";

    @Override
    public void onResume() {
        barcodeScannerView.resume();
        validarPermisos();
        super.onResume();
    }


    private final BroadcastReceiver usbReceiverDetached = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    if(mPhysicaloid.close()) {
                        mPhysicaloid.clearReadListener();
                        scrollViewRFID.setVisibility(View.GONE);
                        handler.removeCallbacks(runSearchRFID);
                        Toast.makeText(ctx,"Dispositivo Desconectado",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ctx,"No se logro Desconectar",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    Runnable runSearchRFID = new Runnable() {
        @Override
        public void run() {

            int numLines = tViewRFID.getLineCount();

            if(numLines>1){
                int start = tViewRFID.getLayout().getLineStart(numLines-2);
                int end = tViewRFID.getLayout().getLineEnd(numLines-2);
                String strRFID = tViewRFID.getText().toString().substring(start, end-1);

                if(strRFID.length()>=10){
                    handler.post(()->{
                        insertarRFID(strRFID);
                        tViewRFID.setText(tViewRFID.getText().toString().substring(end));
                    })  ;
                }
            }
            handler.postDelayed(runSearchRFID,200);
        }
    };

    private final BroadcastReceiver usbReceiverAttached = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {

                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    Toast.makeText(ctx,device.getDeviceName(),Toast.LENGTH_SHORT).show();

                    manager.requestPermission(device, permissionIntent);
                }else {
                    Toast.makeText(ctx,"No se logro Conectar",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(ctx,"No se logro Conectar",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final BroadcastReceiver usbReceiverPermissionUSB = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //Toast.makeText(ctx,action,Toast.LENGTH_SHORT).show();

            if (INTENT_ACTION_GRANT_USB.equals(action)) {
                synchronized (ctx) {

                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            //call method to set up device communication
                            Toast.makeText(ctx,"Permiso Aceptado",Toast.LENGTH_SHORT).show();
                            try{
                                mPhysicaloid = new Physicaloid(ctx);
                                if(mPhysicaloid.open()) {
                                    scrollViewRFID.setVisibility(View.VISIBLE);

                                    handler.post(
                                            runSearchRFID
                                    );
                                    mPhysicaloid.setBaudrate(9600);

                                    Toast.makeText(ctx,"Escuchando Puerto Serial",Toast.LENGTH_LONG).show();
                                    // setEnabledUi(true);
                                    mPhysicaloid.clearReadListener();
                                    mPhysicaloid.addReadListener(new ReadLisener() {

                                        @Override
                                        public void onRead(int size) {
                                            byte[] buf = new byte[size];
                                            mPhysicaloid.read(buf, size);

                                            String temporal = ""+RFID;

                                            temporal = temporal+new String(buf);

                                            String finalTemporal = temporal;
                                            handler.post(
                                                    ()->{
                                                        tViewRFID.append(finalTemporal);
                                                        scrollViewRFID.fullScroll(View.FOCUS_DOWN);
                                                    }

                                            );

                                        }
                                    });


                                } else {
                                    Toast.makeText(ctx, "No se pudo abrir Serial - Posiblemente USB", Toast.LENGTH_LONG).show();
                                    //probablemente sea usb
                                    txtNumero.requestFocus();
                                    isUsb = true;
                                }
                            }catch (Exception e){
                                Toast.makeText(ctx,"ERR: "+e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else {
                        Toast.makeText(ctx,"Permiso Denegado",Toast.LENGTH_LONG).show();
                        Log.d(TAG, "permission denied for device " + device);
                    }
                }
            }


        }
    };
    public static int cantidadPasajeros = 0;
    boolean isUsb = false;
    String rfidant = "";
    CaptureManager capture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Toast.makeText(ctx,"onCreate",Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_viaje);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getIntent()
                .putExtra("SCAN_CAMERA_ID",1);


        permissionIntent = PendingIntent.getBroadcast(ctx, 0, new Intent(INTENT_ACTION_GRANT_USB), 0);

        IntentFilter filter = new IntentFilter(INTENT_ACTION_GRANT_USB);

        registerReceiver(usbReceiverPermissionUSB, filter);

        IntentFilter filterDetached = new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(usbReceiverDetached, filterDetached);

        IntentFilter filterAttached = new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(usbReceiverAttached, filterAttached);
        txtNumero = findViewById(R.id.txtNumeroRFID);
        txtNumero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i<10){
                    String realRFID = txtNumero.getText().toString();
                    if (realRFID.length() == 10){
                        String strRFID = realRFID;
                        if(strRFID.length()==10){
                            if (!rfidant.equals(strRFID)){
                                rfidant = strRFID;
                                handler.post(()->{
                                    insertarRFID(strRFID);
                                });
                            }
                            else {
                                handler.post(()->{
                                    insertarRFID("");
                                });
                            }
                        }
                    }
                }
                else {
                    handler.post(()->{
                        insertarRFID("");
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        root = findViewById(R.id.root);

        PageViewModelViaje.init();

        //obteniendo bundle

        Bundle b = getIntent().getExtras();


        assert b != null;

        ViajeVO viajeVO = (ViajeVO) b.getSerializable(EXTRA_VIAJE);
        VIAJEVO  = viajeVO;

        PageViewModelViaje.set(viajeVO);

        statusLight = false;
        fAButtonLinterna = findViewById(R.id.fAButtonLinterna);
        fAButtonSwitch = findViewById(R.id.fAButtonSwitch);


        tViewRFID = findViewById(R.id.tViewRFID);
        scrollViewRFID = findViewById(R.id.scrollViewRFID);

        tViewDateEmbarque= findViewById(R.id.tViewDateEmbarque);
        tViewHEmbarque = findViewById(R.id.tViewHEmbarque);

        tViewHEmbarque.setText(""+getHour(viajeVO.gethInicio()));
        tViewDateEmbarque.setText(""+getDate(viajeVO.gethInicio()));


        //      Bundle b = getIntent().getExtras();

        setTitle(viajeVO.getRuta());


        fAButtonLinterna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusLight){
                    statusLight = false;
                    //   Toast.makeText(getBaseContext(),"apagando",Toast.LENGTH_SHORT).show();
                    fAButtonLinterna.setImageResource(R.drawable.flashlight_icon);
                    barcodeScannerView.setTorchOff();

                }else {
                    statusLight = true;
                    // Toast.makeText(getBaseContext(), "encendiendo", Toast.LENGTH_SHORT).show();
                    fAButtonLinterna.setImageResource(R.drawable.flashlight_icon);
                    barcodeScannerView.setTorchOn();
                }
            }
        });

        viewfinderView = (ViewfinderView) findViewById(R.id.zxing_viewfinder_view);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...

        changeMaskColor(null);

        /*
        fAButtonShowDialogMap = findViewById(R.id.fAButtonShowDialogMap);
        fAButtonShowDialogMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterDialogMapa_ListPasajeros adapterDialogMapa = new AdapterDialogMapa_ListPasajeros(v.getContext(),-8.1329634,-79.049854,-8.125603,-79.031894);
                adapterDialogMapa.popDialog();
            }
        });

         */

        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scannerReal);
        barcodeScannerView.setTorchListener(this);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39, BarcodeFormat.CODE_93,BarcodeFormat.CODE_128,BarcodeFormat.EAN_8,BarcodeFormat.EAN_13,BarcodeFormat.UPC_EAN_EXTENSION,BarcodeFormat.CODABAR);
        barcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeScannerView.initializeFromIntent(getIntent());
        barcodeScannerView.decodeContinuous(callback);


        beepManager = new BeepManager(this);

        final RecyclerView av_rViewPasajeros = findViewById(R.id.av_rViewPasajeros);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModelViaje.class);
        pageViewModel.get().observe(this, new Observer<ViajeVO>() {
            @Override
            public void onChanged(ViajeVO viajeVO) {
                Log.d(TAG,"tamaaño "+viajeVO.getPasajeroVOList().size());
                TextView cs_tView_Num_Pasajeros = findViewById(R.id.cs_tView_Num_Pasajeros);
                cantidadPasajeros = viajeVO.getPasajeroVOList().size();
                cs_tView_Num_Pasajeros.setText(""+cantidadPasajeros);
                adapter = new RViewAdapterListPasajerosOnViaje(getBaseContext(),viajeVO.getPasajeroVOList());
                //new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(av_rViewPasajeros);
                av_rViewPasajeros.setAdapter(adapter);
            }
        });

        validarPermisos();

        //extra devices directly conetion
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Toast.makeText(ctx,device.getDeviceName(),Toast.LENGTH_SHORT).show();
            manager.requestPermission(device, permissionIntent);

        }

        fAButtonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ctx,"Cambiando Camara",Toast.LENGTH_SHORT).show();
                barcodeScannerView.pause();
                Intent  i = getIntent();
                int cameraId = i.getExtras().getInt("SCAN_CAMERA_ID");

                if(cameraId==0) {
                    cameraId = 1;
                    //Toast.makeText(ctx,"cambiando de Trasera a Delantera",Toast.LENGTH_SHORT).show();
                }else {
                    cameraId=0;
                    //Toast.makeText(ctx,"cambiando de Delantera a Trasera",Toast.LENGTH_SHORT).show();
                }

                i.putExtra("SCAN_CAMERA_ID",cameraId);
                barcodeScannerView = null;
                barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scannerReal);
                barcodeScannerView.setTorchListener(ActivityViaje.this);
                Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39, BarcodeFormat.CODE_93,BarcodeFormat.CODE_128,BarcodeFormat.EAN_8,BarcodeFormat.EAN_13,BarcodeFormat.UPC_EAN_EXTENSION,BarcodeFormat.CODABAR);
                barcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
                barcodeScannerView.initializeFromIntent(getIntent());
                barcodeScannerView.decodeContinuous(callback);
                onResume();
            }
        });

    }



    String getDate(String dateTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// replace with your start date string
        Date d = null;
        try {
            d = df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar gc = new GregorianCalendar();
        gc.setTime(d);

        java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate=dateFormat.format(d);
        return formattedDate;

    }

    String getHour(String dateTime){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// replace with your start date string
        Date d = null;
        try {
            d = df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar gc = new GregorianCalendar();
        gc.setTime(d);

        java.text.DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedDate=dateFormat.format(d);
        return formattedDate;


    }

    RViewAdapterListPasajerosOnViaje adapter = null;

    public void verifyUSBInput(){

    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeScannerView.pause();
        handler.removeCallbacks(runSearchRFID);
        onBackPressed();
/*
        unregisterReceiver(usbReceiverPermissionUSB);
        unregisterReceiver(usbReceiverDetached);
        unregisterReceiver(usbReceiverAttached);

 */
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                break;
        }
        if (isUsb){
            if (!txtNumero.isFocused()) {
                txtNumero.getText().clear();
                txtNumero.requestFocus();
                char unicodeChar = (char)event.getUnicodeChar();
                txtNumero.append(""+unicodeChar);
            }
        }
        return true;
    }



    public void changeMaskColor(View view) {
        Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //viewfinderView.setMaskColor(color);
    }

    @Override
    public void onTorchOn() {

        statusLight = true;
        // Toast.makeText(getBaseContext(), "encendiendo", Toast.LENGTH_SHORT).show();
        fAButtonLinterna.setImageResource(R.drawable.flashlight_icon);
        //  barcodeScannerView.setTorchOn();

    }

    @Override
    public void onTorchOff() {

        statusLight = false;
        //   Toast.makeText(getBaseContext(),"apagando",Toast.LENGTH_SHORT).show();
        fAButtonLinterna.setImageResource(R.drawable.flashlight_icon);
        //            barcodeScannerView.setTorchOff();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }


    @Override
    public void onBackPressed() {

        try{
            unregisterReceiver(usbReceiverPermissionUSB);
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
        try{
            unregisterReceiver(usbReceiverDetached);
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }

        try{
            unregisterReceiver(usbReceiverAttached);
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }

        if (new LoginDataDAO(ctx).verficarLogueo().getIsTranquera() == 1){
            new ViajeDAO(ctx).toStatus1(VIAJEVO.getId());
        }

        this.finish();

    }


    private void closeActivity(){
        if(mPhysicaloid.close()) {
            mPhysicaloid.clearReadListener();
            scrollViewRFID.setVisibility(View.GONE);
            handler.removeCallbacks(runSearchRFID);
            // Toast.makeText(ctx,"Dispositivo Desconectado",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        //Toast.makeText(ctx,"onDestroy",Toast.LENGTH_SHORT).show();

        try{
            closeActivity();
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.viaje, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.terminar) {


            if(pageViewModel.get().getValue().getPasajeroVOList().size()>0){


                //Toast.makeText(ctx,""+pageViewModel.get().getValue().getPasajeroVOList().size(),Toast.LENGTH_LONG).show();

                new ViajeDAO(ctx).toStatus2(VIAJEVO.getId());
                new LoginDataDAO(ctx).uploadIdViaje(0); // esto desac el gps

                PageViewModelViajesActuales.viajeToStatus2(VIAJEVO.getId());

                int i =0;
                for(PasajeroVO pa : pageViewModel.get().getValue().getPasajeroVOList()){

                    if(pa.gethBajada()==null || pa.gethBajada().equals("")){//si solo tiene hora de subida
                        Log.d(TAG,"cambiado" +(++i));

                        pa.sethBajada(getFecha());
                        Log.d(TAG,"fecha"+getFecha());
                        if(new PasajeroDAO(ctx).updatehBajada(pa)){
                            Log.d(TAG,pa.getDni()+"actualizacion correcta");
                        }else {
                            Log.d(TAG,pa.getDni()+"actualizacion correcta");
                        }
                    }else {
                        Log.d(TAG,"con fin"+pa.toString());
                    }
                }
                VIAJEVO = new ViajeDAO(ctx).buscarById(VIAJEVO.getId());
                Log.d(TAG,VIAJEVO.toString());
                new ViajeDAO(ctx).updateHoraFin(VIAJEVO.getId(), getFecha());
                VIAJEVO.sethFin(getFecha());
                PageViewModelViajesActuales.updateViaje(VIAJEVO);

                onBackPressed();
            }else {//si no hay ningun pasajero

                Toast.makeText(ctx,"No se puede finalizar el viaje sin pasajeros",Toast.LENGTH_LONG).show();

            }

        }

        return super.onOptionsItemSelected(item);
    }
    boolean vibrate= true;
    private BeepManager beepManager;
    Handler handler = new Handler();
    static int count = 0;
    String prevousQR = "";
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            ProgressBar progress_circular = findViewById(R.id.progress_circular);

            String resultado = result.getText().toString();
            if  (prevousQR.equals(resultado))
                return;
            prevousQR = resultado;
            Log.d(TAG, "tamaño " + resultado.length());

            if (resultado == null  || resultado.length() != 8) {//si s e rechazo
                // Prevent duplicate scans
                Log.d(TAG, resultado + "DUPLICADO");

                return;
            } else {

                progress_circular.setVisibility(View.VISIBLE);

                barcodeScannerView.pause();

                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        beepManager.setVibrateEnabled(true);
                        beepManager.setBeepEnabled(true);


                        Log.d(TAG,""+count++);

                        if (new LoginDataDAO(ctx).verficarLogueo().getIsTranquera() != 1){
                            if(verifiarDuplicado(resultado)){//si ya se ingreso

                                PasajeroVO pasajeroVO  = getPasajeroDuplicado(resultado);

                                if(pasajeroVO.gethBajada()==null || pasajeroVO.gethBajada().equals("")){//si no tiene hora de bajada


                                    pageViewModel.get().getValue().getPasajeroVOList().remove(pasajeroVO);

                                    pasajeroVO.sethBajada(getFecha());

                                    PageViewModelViaje.addPasajero(0,pasajeroVO);
                                    Log.d(TAG, resultado + "registrando salida");

                                    new PasajeroDAO(ctx).updatehBajada(pasajeroVO);

                                }
                                else {
                                    // si tiene hora de bajada

                                    //     Toast.makeText(ctx,"registrado hora de salida hora de bajada",Toast.LENGTH_SHORT).show();


                                    pageViewModel.get().getValue().getPasajeroVOList().remove(pasajeroVO);//quitando de
                                    pasajeroVO.sethBajada("");
                                    new PasajeroDAO(ctx).updatehBajada(pasajeroVO);

                                    PageViewModelViaje.addPasajero(0,pasajeroVO);// volviendolo a agregar

                                    Snackbar snackbar = Snackbar.make(root,ctx.getString(R.string.pregunta_actualizar_hora_salida),Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            }
                            else {
                                // si es un nuevo ingreso

                                Log.d(TAG,"añadiendo"+resultado);
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date();

                                PasajeroVO pasajeroVO = new PasajeroVO();
                                pasajeroVO.setIdViaje(VIAJEVO.getId());
                                pasajeroVO.sethSubida(formatter.format(date));
                                pasajeroVO.setDni(resultado);

                                //Toast.makeText(getApplicationContext(), pasajeroVO.getDni(), Toast.LENGTH_LONG).show();

                                if  (!pasajeroVO.hasRestriccion(ctx)){
                                    new PasajeroDAO(ctx).insertar(
                                            pasajeroVO.getDni(),
                                            pasajeroVO.getName(),
                                            VIAJEVO.getId(),
                                            pasajeroVO.gethSubida(),
                                            "",
                                            ""
                                    );
                                    PageViewModelViaje.addPasajero(pasajeroVO);

                                    Log.d(TAG, resultado + "registrando entrada");
                                }

                                else {
                                    Snackbar.make(root, "Restricción: " + pasajeroVO.showRestriccion(ctx), Snackbar.LENGTH_LONG).show();
                                }

                            }
                        }
                        else {
                            Log.d(TAG,"añadiendo"+resultado);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();

                            PasajeroVO pasajeroVO = new PasajeroVO();
                            pasajeroVO.setIdViaje(VIAJEVO.getId());
                            pasajeroVO.sethSubida(formatter.format(date));
                            pasajeroVO.setDni(resultado);

                            //Toast.makeText(getApplicationContext(), pasajeroVO.getDni(), Toast.LENGTH_LONG).show();

                            if  (!pasajeroVO.hasRestriccion(ctx)){
                                new PasajeroDAO(ctx).insertar(
                                        pasajeroVO.getDni(),
                                        pasajeroVO.getName(),
                                        VIAJEVO.getId(),
                                        pasajeroVO.gethSubida(),
                                        "",
                                        ""
                                );
                                PageViewModelViaje.addPasajero(pasajeroVO);

                                Log.d(TAG, resultado + "registrando entrada");
                            }

                            else {
                                Snackbar.make(root, "Restricción: " + pasajeroVO.showRestriccion(ctx), Snackbar.LENGTH_LONG).show();
                            }
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                barcodeScannerView.setStatusText(result.getText());
                                if (vibrate) beepManager.playBeepSoundAndVibrate();
                                vibrate = true;
                                try {
                                    Thread.sleep(1000);
                                    //Thread.sleep(2000);
                                    //cambio dorita
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                progress_circular.setVisibility(View.GONE);
                                barcodeScannerView.resume();
                            }
                        });
                    }
                });


            }
        }


        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    boolean verifiarDuplicado(String DNI){
        boolean flag = false;


        List<PasajeroVO> pasajeroVOList = pageViewModel.get().getValue().getPasajeroVOList();

        for (PasajeroVO pasajeroVO : pasajeroVOList){
            if(pasajeroVO.getDni().equals(DNI)){
                flag=true;
                break;
            }
        }


        return flag;
    }


    PasajeroVO getPasajeroDuplicado(String DNI){
        PasajeroVO pasajeroVO = null;

        List<PasajeroVO> pasajeroVOList = pageViewModel.get().getValue().getPasajeroVOList();

        for (PasajeroVO pas : pasajeroVOList){
            if(pas.getDni().equals(DNI)){
                pasajeroVO = pas;
                break;
            }
        }
        return pasajeroVO;

    }

    private boolean validarPermisos(){
        if  (new LoginDataDAO(this).verficarLogueo().getIsTranquera() != 1){
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;
            Context ctx = this;
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(!gps_enabled && !network_enabled) {
                // notify user
                new AlertDialog.Builder(this)
                        .setMessage("GPS no activado")
                        .setPositiveButton("activar GPS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .show();
            }
        }

        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{CAMERA},100);
        }
        return false;

    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityViaje.this);
        dialog.setTitle("Permisos Desactivados");
        dialog.setMessage("Debe aceptar todos los permisos para poder tomar fotos");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                solicitarPermisosManual();
            }
        });
        dialog.show();
    }
    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {
                "si",
                "no"
        };
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ActivityViaje.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de Forma Manual?");
        alertOpciones.setItems(
                opciones,
                (dialog, i) -> {
                    if(opciones[i].equals("si")){
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }

                });
        alertOpciones.show();
    }
    public String getFecha(){
        DateFormat df = new DateFormat();
        return df.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
    }



    private void insertarRFID(String result){
        String resultado = result.trim();
        Log.d(TAG, "tamaño " + resultado.length());

        Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();
        /*
        if (resultado == null  || resultado.length() != 10) {//si s e rechazo
            // Prevent duplicate scans
            Log.d(TAG, resultado + "DUPLICADO");
            handler.post(()->{
                if (isUsb){
                    txtNumero.getText().clear();
                    txtNumero.requestFocus();
                }
                if (!txtNumero.isFocused()) txtNumero.requestFocus();
            });
            // Toast.makeText(ctx,resultado+resultado.length()+"no",Toast.LENGTH_SHORT).show();
            return;
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    // Toast.makeText(ctx,"ok",Toast.LENGTH_SHORT).show();

                    beepManager.setVibrateEnabled(true);
                    beepManager.setBeepEnabled(true);


                    Log.d(TAG,""+count++);
                    if (new LoginDataDAO(ctx).verficarLogueo().getIsTranquera() != 1){
                        if(verifiarDuplicado(resultado)){//si ya se ingreso

                            PasajeroVO pasajeroVO  = getPasajeroDuplicado(resultado);

                            if(pasajeroVO.gethBajada()==null || pasajeroVO.gethBajada().equals("")){//si no tiene hora de bajada


                                pageViewModel.get().getValue().getPasajeroVOList().remove(pasajeroVO);

                                pasajeroVO.sethBajada(getFecha());

                                PageViewModelViaje.addPasajero(0,pasajeroVO);
                                Log.d(TAG, resultado + "registrando salida");

                                new PasajeroDAO(ctx).updatehBajada(pasajeroVO);

                            }
                            else {
                                // si tiene hora de bajada

                                //     Toast.makeText(ctx,"registrado hora de salida hora de bajada",Toast.LENGTH_SHORT).show();


                                pageViewModel.get().getValue().getPasajeroVOList().remove(pasajeroVO);//quitando de
                                pasajeroVO.sethBajada("");
                                new PasajeroDAO(ctx).updatehBajada(pasajeroVO);

                                PageViewModelViaje.addPasajero(0,pasajeroVO);// volviendolo a agregar

                                Snackbar snackbar = Snackbar.make(root,ctx.getString(R.string.pregunta_actualizar_hora_salida),Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                        }
                        else {
                            // si es un nuevo ingreso

                            Log.d(TAG,"añadiendo"+resultado);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();

                            PasajeroVO pasajeroVO = new PasajeroVO();
                            pasajeroVO.setIdViaje(VIAJEVO.getId());
                            pasajeroVO.sethSubida(formatter.format(date));
                            pasajeroVO.setDni(resultado);

                            //Toast.makeText(getApplicationContext(), pasajeroVO.getDni(), Toast.LENGTH_LONG).show();

                            if  (!pasajeroVO.hasRestriccion(ctx)){
                                new PasajeroDAO(ctx).insertar(
                                        pasajeroVO.getDni(),
                                        pasajeroVO.getName(),
                                        VIAJEVO.getId(),
                                        pasajeroVO.gethSubida(),
                                        "",
                                        ""
                                );
                                PageViewModelViaje.addPasajero(pasajeroVO);

                                Log.d(TAG, resultado + "registrando entrada");
                            }

                            else {
                                Snackbar.make(root, "Restricción: " + pasajeroVO.showRestriccion(ctx), Snackbar.LENGTH_LONG).show();
                            }

                        }
                    }
                    else {
                        Log.d(TAG,"añadiendo"+resultado);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();

                        PasajeroVO pasajeroVO = new PasajeroVO();
                        pasajeroVO.setIdViaje(VIAJEVO.getId());
                        pasajeroVO.sethSubida(formatter.format(date));
                        pasajeroVO.setDni(resultado);

                        //Toast.makeText(getApplicationContext(), pasajeroVO.getDni(), Toast.LENGTH_LONG).show();

                        if  (!pasajeroVO.hasRestriccion(ctx)){
                            new PasajeroDAO(ctx).insertar(
                                    pasajeroVO.getDni(),
                                    pasajeroVO.getName(),
                                    VIAJEVO.getId(),
                                    pasajeroVO.gethSubida(),
                                    "",
                                    ""
                            );
                            PageViewModelViaje.addPasajero(pasajeroVO);

                            Log.d(TAG, resultado + "registrando entrada");
                        }

                        else {
                            Snackbar.make(root, "Restricción: " + pasajeroVO.showRestriccion(ctx), Snackbar.LENGTH_LONG).show();
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (vibrate) beepManager.playBeepSoundAndVibrate();
                            vibrate = true;
                            try {
                                Thread.sleep(1000);
                                handler.post(()->{
                                    if (isUsb){
                                        txtNumero.getText().clear();
                                        txtNumero.requestFocus();
                                    }
                                    if (!txtNumero.isFocused()) txtNumero.requestFocus();
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


        }*/
    }

}
