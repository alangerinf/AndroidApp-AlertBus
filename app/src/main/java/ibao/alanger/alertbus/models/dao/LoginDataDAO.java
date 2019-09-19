package ibao.alanger.alertbus.models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import ibao.alanger.alertbus.ConexionSQLiteHelper;
import ibao.alanger.alertbus.models.vo.LoginDataVO;

import static ibao.alanger.alertbus.ConexionSQLiteHelper.VERSION_DB;
import static ibao.alanger.alertbus.utilities.Utilities.DATABASE_NAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_LOGINDATA;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_LOGINDATA_COL_IDUSER;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_LOGINDATA_COL_IDVIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_LOGINDATA_COL_LASTNAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_LOGINDATA_COL_NAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_LOGINDATA_COL_PASSWORD;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_LOGINDATA_COL_TYPEUSER;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_LOGINDATA_COL_USER;

public class LoginDataDAO {

    Context ctx;

    private static String TAG = LoginDataDAO.class.getSimpleName();
    
    public LoginDataDAO(Context ctx) {
        this.ctx=ctx;
    }

    public int borrarTable(){
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();

        int res =db.delete(TABLE_LOGINDATA,null,null);
        db.close();
        c.close();
        return res;
    }

    public int guardarUsuarioNuevo(LoginDataVO loginDataVO){
     //   Log.d(TAG,loginDataVO.toString());

     //   Log.d(TAG,loginDataVO.getName()+" >-< "+loginDataVO.getListIdTipoProcesos());
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();

        ContentValues values = new ContentValues();
            values.put(TABLE_LOGINDATA_COL_IDUSER,loginDataVO.getId());
            values.put(TABLE_LOGINDATA_COL_USER,loginDataVO.getUser());
            values.put(TABLE_LOGINDATA_COL_PASSWORD, loginDataVO.getPassword());
            values.put(TABLE_LOGINDATA_COL_NAME,loginDataVO.getName());
            values.put(TABLE_LOGINDATA_COL_LASTNAME,loginDataVO.getLastName());
            values.put(TABLE_LOGINDATA_COL_IDVIAJE,loginDataVO.getIdViaje());
            values.put(TABLE_LOGINDATA_COL_TYPEUSER,loginDataVO.getTypeUser());
            int id = (int)db.insert(TABLE_LOGINDATA, TABLE_LOGINDATA_COL_IDUSER, values);
        db.close();
        c.close();

        return id;
    }

    public int uploadIdViaje(int  idViaje){
        //   Log.d(TAG,loginDataVO.toString());

        //   Log.d(TAG,loginDataVO.getName()+" >-< "+loginDataVO.getListIdTipoProcesos());
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_LOGINDATA_COL_IDVIAJE,idViaje);
        int id = (int)db.update(TABLE_LOGINDATA,values,null,null);
        db.close();
        c.close();

        return id;
    }


    public LoginDataVO verficarLogueo(){
        ConexionSQLiteHelper c= new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
      //  Log.d(TAG,"INTENTANDO LOGUEO");
        LoginDataVO temp = null;
        try{
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "LD."+TABLE_LOGINDATA_COL_IDUSER+", " +//0
                            "LD."+TABLE_LOGINDATA_COL_USER+", " +//1
                            "LD."+TABLE_LOGINDATA_COL_PASSWORD+", " +//2
                            "LD."+TABLE_LOGINDATA_COL_NAME+", "+//3
                            "LD."+TABLE_LOGINDATA_COL_LASTNAME+", "+//4
                            "LD."+TABLE_LOGINDATA_COL_IDVIAJE+", "+//5
                            "LD."+TABLE_LOGINDATA_COL_TYPEUSER+" "+//6
                            " FROM "+
                            TABLE_LOGINDATA+" as LD "
                    ,null);
            if(cursor.getCount()>0){
                cursor.moveToFirst() ;
               // Log.d(TAG,"hay primero");
                temp = new LoginDataVO();
               // Log.d(TAG,"0");
                temp.setId(cursor.getInt(0));
               // Log.d(TAG,"1");
                temp.setUser(cursor.getString(1));
               // Log.d(TAG,"2");
                temp.setPassword(cursor.getString(2));
               // Log.d(TAG,"3");
                temp.setName(cursor.getString(3));
                // Log.d(TAG,"4");
                temp.setLastName(cursor.getString(4));
                // Log.d(TAG,"5");
                temp.setIdViaje(cursor.getInt(5));
                // Log.d(TAG,"6");
                temp.setTypeUser(cursor.getInt(6));
            }
            cursor.close();

        }catch (Exception e){
          //  Log.d(TAG,"getEditing "+e.toString());
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx,"verficarLogueo-> "+e.toString(),Toast.LENGTH_SHORT).show();
                }
            });

        }finally {
            db.close();
            c.close();
        }

        Gson gson = new Gson();
        String usuarioJson = gson.toJson(temp);
        Log.d(TAG,"verficarLogueo-> "+"usuario:"+usuarioJson);

        return temp;
    }


}
