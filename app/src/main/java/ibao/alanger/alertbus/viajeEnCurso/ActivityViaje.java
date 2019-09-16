package ibao.alanger.alertbus.viajeEnCurso;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.adapters.AdapterDialogMapa_ListPasajeros;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.dao.PasajeroDAO;
import ibao.alanger.alertbus.models.dao.TrackingDAO;
import ibao.alanger.alertbus.models.dao.ViajeDAO;
import ibao.alanger.alertbus.models.vo.PasajeroVO;
import ibao.alanger.alertbus.models.vo.ViajeVO;

import static java.security.AccessController.getContext;


import static android.Manifest.permission.CAMERA;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class ActivityViaje extends AppCompatActivity implements
        DecoratedBarcodeView.TorchListener {

    private String TAG = ActivityViaje.class.getSimpleName();

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    private static FloatingActionButton fAButtonShowDialogMap;

    private ViewfinderView viewfinderView;

    private static FloatingActionButton fAButtonLinterna;

    private static boolean statusLight;


    private PageViewModelViaje pageViewModel;

    private static View root ;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viaje);

        ctx = this;

        root = findViewById(R.id.root);

        cargarData();


        statusLight = false;
        fAButtonLinterna = findViewById(R.id.fAButtonLinterna);

        //      Bundle b = getIntent().getExtras();

          setTitle("Lima - Trujillo");




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



        changeMaskColor(null);

        fAButtonShowDialogMap = findViewById(R.id.fAButtonShowDialogMap);
        fAButtonShowDialogMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdapterDialogMapa_ListPasajeros adapterDialogMapa = new AdapterDialogMapa_ListPasajeros(v.getContext(),-8.1329634,-79.049854,-8.125603,-79.031894);
                adapterDialogMapa.popDialog();
            }
        });


        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39,BarcodeFormat.CODABAR);
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

                cs_tView_Num_Pasajeros.setText(""+viajeVO.getPasajeroVOList().size());


                adapter = new RViewAdapterListPasajerosOnViaje(getBaseContext(),viajeVO.getPasajeroVOList());
                new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(av_rViewPasajeros);
                av_rViewPasajeros.setAdapter(adapter);
            }
        });

        validarPermisos();
    }

    RViewAdapterListPasajerosOnViaje adapter = null;




    private void cargarData() {

        PageViewModelViaje.init();

        ViajeVO viajeVO = new ViajeVO();
        viajeVO.setId(1);
        viajeVO.sethInicio("12:29");
        viajeVO.sethFin("12:30");
        viajeVO.setPlaca("123-ACV");
        viajeVO.setRuta("Trujillo - Lima");
        viajeVO.setNumPasajeros(23);
        viajeVO.setCapacidad(55);
        viajeVO.setProveedor("Proveedor 1");
        viajeVO.setStatus(1);
        viajeVO.setConductor("Conductor1");

        PageViewModelViaje.set(viajeVO);

    }




    @Override
    protected void onResume() {
        super.onResume();

        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeScannerView.pause();
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

        statusLight = true;
            // Toast.makeText(getBaseContext(), "encendiendo", Toast.LENGTH_SHORT).show();
            fAButtonLinterna.setImageResource(R.drawable.ic_highlight_white_on);
          //  barcodeScannerView.setTorchOn();

    }

    @Override
    public void onTorchOff() {

            statusLight = false;
            //   Toast.makeText(getBaseContext(),"apagando",Toast.LENGTH_SHORT).show();
            fAButtonLinterna.setImageResource(R.drawable.ic_light_white_off);
        //            barcodeScannerView.setTorchOff();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
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
            //Toast.makeText(ctx,""+pageViewModel.get().getValue().getPasajeroVOList().size(),Toast.LENGTH_LONG).show();
            new LoginDataDAO(ctx).uploadIdViaje(0);
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private BeepManager beepManager;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            String resultado = result.getText().toString();
            Log.d(TAG, "tamaño " + resultado.length());

            if (resultado == null || verifiarDuplicado(resultado) || resultado.length() != 8) {//si s e rechazo
                // Prevent duplicate scans
                Log.d(TAG, resultado + "DUPLICADO");

                return;
            } else {
                Log.d(TAG,"añadiendo"+resultado);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                PasajeroVO pasajeroVO = new PasajeroVO();

                pasajeroVO.sethSubida(formatter.format(date));
                pasajeroVO.setDni(resultado);

                PageViewModelViaje.addPasajero(pasajeroVO);

                Log.d(TAG, resultado + "ingresado");

                barcodeScannerView.setStatusText(result.getText());
                beepManager.setVibrateEnabled(true);
                beepManager.playBeepSoundAndVibrate();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int index = viewHolder.getAdapterPosition();//esto tiene q ir arriba porq la programacion reaccitva elimina de inmediato en el recycler view sin actualizar
            final PasajeroVO item = PageViewModelViaje.removePasajero(viewHolder.getAdapterPosition());
            Snackbar snackbar = Snackbar.make(root,"Se Borró un Pasajero"+index,Snackbar.LENGTH_LONG);
            snackbar.setAction("Deshacer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(verifiarDuplicado(item.getDni())){//si esta duplicador
                        Toast.makeText(ctx,"DNI ya agregado",Toast.LENGTH_LONG).show();
                    }else {
                        PageViewModelViaje.addPasajero(index,item);
                    }

                }
            });
            snackbar.show();
        }
    };

    private boolean validarPermisos(){
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


}
