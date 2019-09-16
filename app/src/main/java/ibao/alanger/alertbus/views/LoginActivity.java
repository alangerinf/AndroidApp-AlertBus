package ibao.alanger.alertbus.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.helpers.LoginHelper;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;
import ibao.alanger.alertbus.models.vo.LoginDataVO;
import ibao.alanger.alertbus.services.SearchViajesService;
import ibao.alanger.alertbus.services.UploadService;

public class LoginActivity extends Activity {



    private Context ctx = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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

                                LoginDataVO loginDataVO = new LoginDataVO();
                                loginDataVO.setId(1);
                                loginDataVO.setName("Alan");
                                loginDataVO.setUser("AlanGer");
                                loginDataVO.setPassword("123456");

                                new LoginDataDAO(ctx).guardarUsuarioNuevo(loginDataVO);
                                LoginDataVO logueo = new LoginDataDAO(ctx).verficarLogueo();
                                if( logueo != null){
                                    Intent intent = new Intent(ctx, SearchViajesService.class);
                                    ctx.startService(intent);

                                    intent = new Intent(ctx, UploadService.class);
                                    ctx.startService(intent);

                                    intent = new Intent(ctx, MainConductorActivity.class);//para hacer testing cambiar segun requiera
                                    ctx.startActivity(intent);
                                }else {
                                    Toast.makeText(ctx,"Error de Base de Datos Interna",Toast.LENGTH_LONG).show();
                                }



                                /* todo : descomentar para habilitar la  autentificacion con el servidor
                                Log.d("autentification","intentando");
                                LoginHelper loginHelper = new LoginHelper(ctx);
                                loginHelper.intentoLogueo(
                                        eTextUser.getText().toString(),
                                        eTextPassword.getText().toString()
                                );
                                */

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
