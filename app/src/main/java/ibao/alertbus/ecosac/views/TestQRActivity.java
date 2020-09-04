package ibao.alertbus.ecosac.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import java.io.IOException;

import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.utilities.Utils;

public class TestQRActivity extends AppCompatActivity {

    
    private static String TAG = TestQRActivity.class.getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_qr);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button btnQR = findViewById(R.id.btnQR);
        ImageView iViewQR = findViewById(R.id.iViewQR);
        EditText eTextQR = findViewById(R.id.eTextQR);

        
        
        
        
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String comprimido = null;
                    comprimido = Utils.compress(eTextQR.getText().toString());

                    Log.d(TAG,comprimido);
                    Log.d(TAG,"tamaño original:"+eTextQR.getText().toString().length());
                    Log.d(TAG,"tamaño qr:"+(comprimido).length());

                    iViewQR.setImageBitmap(Utils.createQRCode(comprimido,2048));
                } catch (WriterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        
        
        
    }
}
