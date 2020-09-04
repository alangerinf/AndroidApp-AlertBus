package ibao.alertbus.ecosac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static ibao.alertbus.ecosac.utilities.Utilities.CREATE_TABLE_LOGINDATA;
import static ibao.alertbus.ecosac.utilities.Utilities.CREATE_TABLE_PASAJERO;
import static ibao.alertbus.ecosac.utilities.Utilities.CREATE_TABLE_RESTRICCION;
import static ibao.alertbus.ecosac.utilities.Utilities.CREATE_TABLE_TRACKING;
import static ibao.alertbus.ecosac.utilities.Utilities.CREATE_TABLE_VIAJE;

public class ConexionSQLiteHelper extends SQLiteOpenHelper{
    public static int VERSION_DB = 121;
    private Context ctx;
    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        ctx=context;
    }

    String TAG = "CREATE_TABLE";
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * DATA DE LOGUEO
         */
        Log.d(TAG,TAG+"-> cnCreate");

        db.execSQL(CREATE_TABLE_LOGINDATA);//0
        db.execSQL(CREATE_TABLE_TRACKING);//1
        /**
         * DATA
         */
        db.execSQL(CREATE_TABLE_VIAJE);//1
        db.execSQL(CREATE_TABLE_PASAJERO);//2
        db.execSQL(CREATE_TABLE_RESTRICCION);//2
        db.execSQL("CREATE TABLE IF NOT EXISTS TrabajadorRestriccion (rfid varchar(20), dni varchar(10), restriccion varchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TrabajadorRestriccion ");
        db.execSQL("DROP TABLE IF EXISTS loginData ");
        onCreate(db);
    }
}
