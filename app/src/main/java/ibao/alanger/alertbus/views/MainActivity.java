package ibao.alanger.alertbus.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ibao.alanger.alertbus.BuildConfig;
import ibao.alanger.alertbus.R;
import ibao.alanger.alertbus.models.dao.LoginDataDAO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        moveTaskToBack(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.version) {
            try {
                Toast.makeText(getBaseContext(),"Versión "+ BuildConfig.VERSION_NAME+" code."+BuildConfig.VERSION_CODE,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.logout) {
            int i=0;

                Toast.makeText(getBaseContext(), "Cerrando Sesión...", Toast.LENGTH_LONG).show();
                new LoginDataDAO(getBaseContext()).borrarTable();
                Intent intent = new Intent(getBaseContext(), ActivityPreloader.class);
                startActivity(intent);
                //finish();
            }

        return super.onOptionsItemSelected(item);
    }
}