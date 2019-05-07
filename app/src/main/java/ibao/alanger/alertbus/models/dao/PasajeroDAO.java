package ibao.alanger.alertbus.models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import ibao.alanger.alertbus.ConexionSQLiteHelper;
import ibao.alanger.alertbus.models.vo.PasajeroVO;

import static ibao.alanger.alertbus.ConexionSQLiteHelper.VERSION_DB;
import static ibao.alanger.alertbus.utilities.Utilities.DATABASE_NAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_DNI;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_ID;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_IDVIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_NAME;


public class PasajeroDAO {

    Context ctx;

    String TAG = PasajeroDAO.class.getSimpleName();
    public PasajeroDAO(Context ctx) {
         this.ctx=ctx;
    }

    public boolean borrarTable(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = db.delete(TABLE_PASAJERO,null,null);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }

    public boolean insertar(String dni, String name,int idViaje){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_PASAJERO_COL_DNI,dni);
            values.put(TABLE_PASAJERO_COL_NAME,name);
            values.put(TABLE_PASAJERO_COL_IDVIAJE,idViaje);
            Long temp = db.insert(TABLE_PASAJERO,TABLE_PASAJERO_COL_ID,values);
            db.close();
            conn.close();
            return (temp>0);
        }catch (Exception e){
            Log.d("ZonaDAO",e.toString());
        }

        return false;
    }
/*
    public PasajeroVO consultarByid(int id){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );

        SQLiteDatabase db = c.getReadableDatabase();
        PasajeroVO temp = null;
        try{
            temp = new PasajeroVO();
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "P."+TABLE_PASAJERO_COL_ID+", " +
                            "P."+TABLE_PASAJERO_COL_NAME+", "+
                            "P."+TABLE_PASAJERO_COL_DNI+", "+
                            "P."+TABLE_PASAJERO_COL_IDVIAJE+" "+
                            " FROM "+
                            TABLE_PASAJERO+" as P"+
                        " WHERE "+
                            "P."+TABLE_PASAJERO_COL_ID+" = "+ id
                    ,null);
            cursor.moveToFirst();
                temp.setId(cursor.getInt(0));
                temp.setName(cursor.getString(1));
                temp.setDni(cursor.getString(2));
                temp.setIdViaje(cursor.getInt(3));
            cursor.close();
        }catch (Exception e){
            Toast.makeText(ctx,e.toString(), Toast.LENGTH_SHORT);
        }finally {
            db.close();
            c.close();
        }
        return temp;
    }
*/
    //guardando f:1 v:2 env:1

    public List<PasajeroVO> listByIdViaje(int idViaje){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );

        SQLiteDatabase db = c.getReadableDatabase();
        List<PasajeroVO> categoriaVOList = new ArrayList<>();
        try{
            String[] campos = {
                    TABLE_PASAJERO_COL_ID,
                    TABLE_PASAJERO_COL_NAME,
                    TABLE_PASAJERO_COL_DNI,
                    TABLE_PASAJERO_COL_IDVIAJE
            };
            String[] arg = {String.valueOf(idViaje)};
            Cursor cursor= db.query(TABLE_PASAJERO,campos,TABLE_PASAJERO_COL_IDVIAJE+"=?",arg,null,null,TABLE_PASAJERO_COL_NAME+" COLLATE UNICODE ASC");
            while(cursor.moveToNext()){

                PasajeroVO temp = new PasajeroVO();
                    temp.setId(cursor.getInt(0));
                    temp.setName(cursor.getString(1));
                    temp.setDni(cursor.getString(2));
                    temp.setIdViaje(cursor.getInt(3));
                categoriaVOList.add(temp);

            }
            cursor.close();
        }catch (Exception e){
            Log.d("zonaDAOtag",e.toString());
            Toast.makeText(ctx,e.toString(),Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            c.close();
        }
        return categoriaVOList;
    }




    public int deleteByIdViaje(int idViaje){


        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB);
        SQLiteDatabase db = conn.getWritableDatabase();

        String[] parametros = {
                String.valueOf(idViaje)
        };

        int res = db.delete(
                TABLE_PASAJERO,
                TABLE_PASAJERO_COL_IDVIAJE+"=?",
                parametros);

        Log.d("borrando",String.valueOf(res));

        db.close();
        conn.close();
        return res;
    }


/*
    public int borrarById(int id){


        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {String.valueOf(id)};
        int res =db.delete(TABLE_PASAJERO,TABLE_PASAJERO_COL_ID+"=?",parametros);
        Log.d(TAG,"ELIMINADO MUESTRA "+String.valueOf(res));

        db.close();
        conn.close();
        return res;
    }
*/



}
