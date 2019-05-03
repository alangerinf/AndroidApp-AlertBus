package com.ibao.alanger.agroq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_CALIBRE;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_CATEGORIA;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_CONFEVALUACION;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_CRITERIO;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_CRITERIODETALLE;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_CULTIVO;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_DESCARTE;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_DESPACHO;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_DESTINO;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_EMPRESA;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_ENVASE;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_EVALUACION;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_EVALUACIONDETALLE;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_FOTO;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_FUNDO;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_LOGINDATA;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_MUESTRA;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_PLANTA;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_PLANTACULTIVO;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_PRODUCCION;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_RECEPCION;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_TIPOCALIBRE;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_TIPOPROCESO;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_TOLEVALUACION;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_VARIEDAD;
import static com.ibao.alanger.agroq.utilities.Utilities.CREATE_TABLE_ZONA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CATEGORIA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CONFEVALUACION;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CRITERIO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CRITERIODETALLE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CULTIVO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_DESCARTE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_DESPACHO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_DESPACHO_COL_CLIENTE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_DESPACHO_TYPECOL_CLIENTE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_DESTINO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_EMPRESA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_ENVASE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_EVALUACION;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_EVALUACIONDETALLE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_FOTO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_FUNDO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_MUESTRA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_PLANTA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_PLANTACULTIVO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_PRODUCCION;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_RECEPCION;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_TIPOCALIBRE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_TIPOPROCESO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_TOLEVALUACION;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_VARIEDAD;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_ZONA;

public class ConexionSQLiteHelper extends SQLiteOpenHelper{

    public static int VERSION_DB = 6;
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
        db.execSQL(CREATE_TABLE_LOGINDATA);//0

        /**
         * DATA MAESTRA
         */
        db.execSQL(CREATE_TABLE_ZONA);//1
        db.execSQL(CREATE_TABLE_EMPRESA);//2
        db.execSQL(CREATE_TABLE_FUNDO);//3
        db.execSQL(CREATE_TABLE_CULTIVO);//4
        db.execSQL(CREATE_TABLE_VARIEDAD);//5
        db.execSQL(CREATE_TABLE_PLANTA);//6
        db.execSQL(CREATE_TABLE_PLANTACULTIVO);//7
        db.execSQL(CREATE_TABLE_TIPOPROCESO);//8
        db.execSQL(CREATE_TABLE_ENVASE);//9
        db.execSQL(CREATE_TABLE_CRITERIO);//10
        db.execSQL(CREATE_TABLE_CALIBRE);//11
        db.execSQL(CREATE_TABLE_TIPOCALIBRE);//12
        db.execSQL(CREATE_TABLE_CONFEVALUACION);//13
        db.execSQL(CREATE_TABLE_EVALUACION);//14
        db.execSQL(CREATE_TABLE_TOLEVALUACION);//15
        db.execSQL(CREATE_TABLE_DESTINO);//16
        db.execSQL(CREATE_TABLE_CATEGORIA);//17
        /**
         * DATA INTERNA
         */
        db.execSQL(CREATE_TABLE_MUESTRA);//18
        db.execSQL(CREATE_TABLE_EVALUACIONDETALLE);//19
        db.execSQL(CREATE_TABLE_CRITERIODETALLE);//20
        db.execSQL(CREATE_TABLE_FOTO);//21
        db.execSQL(CREATE_TABLE_RECEPCION);//22
        db.execSQL(CREATE_TABLE_PRODUCCION);//23
        db.execSQL(CREATE_TABLE_DESCARTE);//24
        db.execSQL(CREATE_TABLE_DESPACHO);//25

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /**
         * DATA DE LOGUEO
         */
//        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGINDATA);//0

        /**
         * DATA MAESTRA
         */
 /*       db.execSQL("DROP TABLE IF EXISTS "+TABLE_ZONA);//1
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EMPRESA);//2
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_FUNDO);//3
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CULTIVO);//4
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_VARIEDAD);//5
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PLANTA);//6
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PLANTACULTIVO);//7
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TIPOPROCESO);//8
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ENVASE);//9
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CRITERIO);//10
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CALIBRE);//11
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TIPOCALIBRE);//12
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONFEVALUACION);//13
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EVALUACION);//14
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TOLEVALUACION);//15
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DESTINO);//16
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORIA);//17
   */
        /**
         * DATA INTERNA
         */
     /*
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MUESTRA);//18
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EVALUACIONDETALLE);//19
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CRITERIODETALLE);//20
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_FOTO);//21
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RECEPCION);//22
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCCION);//23
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DESCARTE);//24
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DESPACHO);//25
       */

     /*
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        */


        //onCreate(db);
        /***
         * update to v6
         */
        try {
            db.execSQL(
                    "ALTER TABLE "+TABLE_DESPACHO+ " ADD "+
                            TABLE_DESPACHO_COL_CLIENTE+" "+
                            TABLE_DESPACHO_TYPECOL_CLIENTE
            );
        }catch (Exception e){
            Toast.makeText(ctx, e.toString(),Toast.LENGTH_LONG).show();
            Log.d(TAG,e.toString());
        }

    }
}
