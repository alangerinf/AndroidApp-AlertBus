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
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_HORABAJADA;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_HORASUBIDA;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_ID;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_IDVIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_NAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_PASAJERO_COL_OBSERVACION;


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

    public boolean insertar(String dni, String name,int idViaje, String hSubida, String observacion){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_PASAJERO_COL_DNI,dni);
            values.put(TABLE_PASAJERO_COL_NAME,name);
            values.put(TABLE_PASAJERO_COL_IDVIAJE,idViaje);
            values.put(TABLE_PASAJERO_COL_HORASUBIDA,hSubida);
            values.put(TABLE_PASAJERO_COL_OBSERVACION,observacion);

            Long temp = db.insert(TABLE_PASAJERO,TABLE_PASAJERO_COL_ID,values);
            db.close();
            conn.close();
            return (temp>0);
        }catch (Exception e){
            Log.d("ZonaDAO",e.toString());
        }

        return false;
    }


    public boolean editar(PasajeroVO pasajeroVO){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            String[] args = {
                    String.valueOf(pasajeroVO.getId())
            };


            ContentValues values = new ContentValues();
            values.put(TABLE_PASAJERO_COL_NAME,pasajeroVO.getName());
            values.put(TABLE_PASAJERO_COL_OBSERVACION,pasajeroVO.getObservacion());
            long temp = db.update(TABLE_PASAJERO,values,TABLE_PASAJERO_COL_ID+"=?",args);
            db.close();
            conn.close();
            return (temp>0);
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }

        return false;
    }

    public boolean insertar(PasajeroVO pasajeroVO){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_PASAJERO_COL_ID,pasajeroVO.getId());
            values.put(TABLE_PASAJERO_COL_DNI,pasajeroVO.getDni());
            values.put(TABLE_PASAJERO_COL_NAME,pasajeroVO.getName());
            values.put(TABLE_PASAJERO_COL_IDVIAJE,pasajeroVO.getIdViaje());
            values.put(TABLE_PASAJERO_COL_HORASUBIDA,pasajeroVO.gethSubida());
            values.put(TABLE_PASAJERO_COL_HORABAJADA,pasajeroVO.gethBajada());
            values.put(TABLE_PASAJERO_COL_OBSERVACION,pasajeroVO.getObservacion());

            Long temp = db.insert(TABLE_PASAJERO,TABLE_PASAJERO_COL_ID,values);
            db.close();
            conn.close();
            return (temp>0);
        }catch (Exception e){
            Toast.makeText(ctx,"insertar->"+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d(TAG,e.toString());
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
                    TABLE_PASAJERO_COL_IDVIAJE,
                    TABLE_PASAJERO_COL_HORASUBIDA,
                    TABLE_PASAJERO_COL_HORABAJADA,
                    TABLE_PASAJERO_COL_OBSERVACION
            };
            String[] arg = {String.valueOf(idViaje)};
            Cursor cursor= db.query(TABLE_PASAJERO,campos,TABLE_PASAJERO_COL_IDVIAJE+"=?",arg,null,null,TABLE_PASAJERO_COL_ID+" DESC"/*TABLE_PASAJERO_COL_NAME+" COLLATE UNICODE ASC"*/);
            while(cursor.moveToNext()){

                PasajeroVO temp = getAtributtes(cursor);
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




    public int deleteByIdViajeDNI(int idViaje,String DNI){


        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB);
        SQLiteDatabase db = conn.getWritableDatabase();

        String[] parametros = {
                String.valueOf(idViaje),
                DNI
        };

        int res = db.delete(
                TABLE_PASAJERO,
                TABLE_PASAJERO_COL_IDVIAJE+"=? AND "+TABLE_PASAJERO_COL_DNI+"=?",
                parametros);

        Log.d("borrando",String.valueOf(res));

        db.close();
        conn.close();
        return res;
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

    private PasajeroVO getAtributtes(Cursor cursor){
        PasajeroVO pasajeroVO = new PasajeroVO();
        String[] columnNames = cursor.getColumnNames();
        for(String name : columnNames){
            switch (name){
                case TABLE_PASAJERO_COL_ID:
                    pasajeroVO.setId(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_PASAJERO_COL_DNI:
                    pasajeroVO.setDni(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_PASAJERO_COL_NAME:
                    pasajeroVO.setName(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_PASAJERO_COL_IDVIAJE:
                    pasajeroVO.setIdViaje(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_PASAJERO_COL_HORASUBIDA:
                    pasajeroVO.sethSubida(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_PASAJERO_COL_HORABAJADA:
                    pasajeroVO.sethBajada(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_PASAJERO_COL_OBSERVACION:
                    pasajeroVO.setObservacion(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                default:
                    Toast.makeText(ctx,TAG+"getAtributes error no se encuentra campo "+name,Toast.LENGTH_LONG).show();
                    Log.d(TAG," getAtributes error no se encuentra campo "+name);
                    break;
            }
        }
        return pasajeroVO;

    }

}
