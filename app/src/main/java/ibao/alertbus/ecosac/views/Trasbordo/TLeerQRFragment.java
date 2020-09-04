package ibao.alertbus.ecosac.views.Trasbordo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import ibao.alertbus.ecosac.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TLeerQRFragment extends Fragment implements DecoratedBarcodeView.TorchListener {

    RecyclerView  rViewPasajeros;
    DecoratedBarcodeView barcodeScannerView;
    ViewfinderView viewfinderView;
    BeepManager beepManager;
    CaptureManager capture;
    Handler handler = new Handler();
    public TLeerQRFragment() {
        // Required empty public constructor
    }

    public void changeMaskColor(View view) {
        Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //viewfinderView.setMaskColor(color);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            String resultado = result.getText().toString();
            //Log.d(TAG, "tamaño " + resultado.length());

            if (resultado == null  || resultado.length() != 8) {//si s e rechazo
                // Prevent duplicate scans
                //Log.d(TAG, resultado + "DUPLICADO");

                return;
            } else {

                barcodeScannerView.pause();

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });


            }
        }


        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        barcodeScannerView = (DecoratedBarcodeView) getActivity().findViewById(R.id.zxing_barcode_scanner2);
        barcodeScannerView.setTorchListener(this);


        viewfinderView = (ViewfinderView) getActivity().findViewById(R.id.zxing_viewfinder_view);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        capture = new CaptureManager(getActivity(), barcodeScannerView);
        capture.initializeFromIntent(getActivity().getIntent(), savedInstanceState);
        capture.decode();
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

        barcodeScannerView = (DecoratedBarcodeView) getActivity().findViewById(R.id.zxing_barcode_scanner2);
        barcodeScannerView.setTorchListener(this);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39, BarcodeFormat.CODE_93,BarcodeFormat.CODE_128,BarcodeFormat.EAN_8,BarcodeFormat.EAN_13,BarcodeFormat.UPC_EAN_EXTENSION,BarcodeFormat.CODABAR);
        barcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeScannerView.initializeFromIntent(getActivity().getIntent());
        barcodeScannerView.decodeContinuous(callback);
        beepManager = new BeepManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_t_leer_qr, container, false);
    }

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }
}
