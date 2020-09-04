package ibao.alertbus.ecosac.views;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import ibao.alertbus.ecosac.R;
import ibao.alertbus.ecosac.helpers.LoginHelper;

public class LoginActivity extends Activity {



    private Context ctx = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Animation animBtn =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.press_btn);
        final Button btnEnter = (Button) findViewById(R.id.btnEnter);

        final EditText eTextUser = findViewById(R.id.eTextUsuario);
        final EditText eTextPassword = findViewById(R.id.eTextPassword);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animBtn);
                Handler handler = new Handler();
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                //btnEnter.setClickable(false);
                                //Intent intent = new Intent(getBaseContext(),ActivityPostloader.class);
                                //startActivity(intent);

                                LoginHelper loginHelper = new LoginHelper(ctx);

                                if(eTextUser.getText().toString().equals("")){
                                    Toast.makeText(ctx,"Ingrese  USUARIO",Toast.LENGTH_SHORT).show();
                                }else {
                                    if(eTextPassword.getText().toString().equals("")){
                                        Toast.makeText(ctx,"Ingrese  USUARIO",Toast.LENGTH_SHORT).show();

                                    }else {
                                        loginHelper.intentoLogueo(eTextUser.getText().toString(),eTextPassword.getText().toString());
                                    }
                                }
                            }
                        },200
                );
            }
        });



        ImageView iViewPasswordSetVisible = findViewById(R.id.iViewPassword);
        iViewPasswordSetVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animBtn);
                if(eTextPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    eTextPassword.setInputType( InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else {
                    eTextPassword.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                }
                eTextPassword.setSelection(eTextPassword.getText().length());
            }
        });


        final ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.constCombo);
        cl.setVisibility(View.INVISIBLE);

        final Animation animLayout =
                android.view.animation.AnimationUtils.loadAnimation(getBaseContext(),R.anim.left_in);

        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        cl.startAnimation(animLayout);
                        cl.setVisibility(View.VISIBLE);
                    }
                },200
        );

    }
    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        moveTaskToBack(true);

    }
}