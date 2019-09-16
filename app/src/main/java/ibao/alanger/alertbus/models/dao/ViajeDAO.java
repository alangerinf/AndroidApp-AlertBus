package ibao.alanger.alertbus.models.dao;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ibao.alanger.alertbus.ConexionSQLiteHelper;
import ibao.alanger.alertbus.models.vo.ViajeVO;


import static ibao.alanger.alertbus.ConexionSQLiteHelper.VERSION_DB;
import static ibao.alanger.alertbus.utilities.Utilities.DATABASE_NAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_RESTRICCION;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_RESTRICCION_COL_IDVIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_COMENTARIO;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_CONDUCTOR;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_HORACONFIRMADO;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_HORAFIN;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_HORAINICIO;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_ID;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_NUMPASAJEROS;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_PLACA;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_CAPACIDAD;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_PROVEEDOR;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_RUTA;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_STATUS;


public class ViajeDAO {


    String TAG = ViajeDAO.class.getSimpleName();

    Context ctx;
    public ViajeDAO(Context ctx) {
        this.ctx=ctx;
    }


    public boolean borrarTable(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = db.delete(TABLE_VIAJE,null,null);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }

    public boolean deleteByStatus2(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] args = {
                "2"
        };
        int res = db.delete(TABLE_VIAJE,TABLE_VIAJE_COL_STATUS+"=?",args);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }

    public boolean insertar(int id,
                            String proveedor,
                            String placa,
                            String conductor,
                            String ruta,
                            String hInicio,
                            String hFin,
                            int numpasajeros,
                            int capacidad
                            ){

        long temp=0;

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
                values.put(TABLE_VIAJE_COL_ID,id);
                values.put(TABLE_VIAJE_COL_PROVEEDOR,proveedor);
                values.put(TABLE_VIAJE_COL_PLACA,placa);
                values.put(TABLE_VIAJE_COL_CONDUCTOR,conductor);
                values.put(TABLE_VIAJE_COL_RUTA,ruta);
                values.put(TABLE_VIAJE_COL_HORAINICIO,hInicio);
                values.put(TABLE_VIAJE_COL_HORAFIN,hFin);
                values.put(TABLE_VIAJE_COL_NUMPASAJEROS,numpasajeros);
                values.put(TABLE_VIAJE_COL_CAPACIDAD,capacidad);
                values.put(TABLE_VIAJE_COL_COMENTARIO,"");
                values.put(TABLE_VIAJE_COL_STATUS,0);
            temp = db.insert(TABLE_VIAJE,TABLE_VIAJE_COL_ID,values);
            db.close();
            conn.close();

        }catch (Exception e){
            Log.d("ViajeDAO ins : ",e.toString());
        }
        return (temp>0);
    }

    public boolean toStatus1(int id) {

        boolean flag = false;
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();

        String sql = "UPDATE "+
                TABLE_VIAJE+
                " SET "+
                TABLE_VIAJE_COL_HORACONFIRMADO+" = datetime('now','localtime')" +
                " , " +
                TABLE_VIAJE_COL_STATUS+" = 1"+
                " WHERE " +
                TABLE_VIAJE_COL_ID+"="+String.valueOf(id)+" "+
                " AND "+
                TABLE_VIAJE_COL_STATUS+"<1"
                ;
        db.execSQL(sql);
        /*
            String[] parametros =
                    {
                            String.valueOf(id),
                    };

        ContentValues values = new ContentValues();
        values.put(TABLE_VIAJE_COL_STATUS,1);
        values.put(TABLE_VIAJE_COL_HORACONFIRMADO,"datetime('now','localtime')");
        int res = db.update(TABLE_VIAJE,values,TABLE_VIAJE_COL_ID+"=? AND "+TABLE_VIAJE_COL_STATUS+"<1",parametros);
        if(res>0){
            flag=true;
        }*/
        db.close();
        c.close();
        return  flag;
    }

    public boolean toStatus2(int id) {

        boolean flag = false;
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();
        String[] parametros =
                {
                        String.valueOf(id),
                };
        ContentValues values = new ContentValues();
        values.put(TABLE_VIAJE_COL_STATUS,2);
        int res = db.update(TABLE_VIAJE,values,TABLE_VIAJE_COL_ID+"=?",parametros);
        if(res>0){
            flag=true;
        }
        db.close();
        c.close();
        return  flag;
    }

    public boolean editComentarioById(int id, String comentario) {
        boolean flag = false;
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();
        String[] parametros =
                {
                        String.valueOf(id),
                };
        ContentValues values = new ContentValues();
        values.put(TABLE_VIAJE_COL_COMENTARIO,comentario);
        int res = db.update(TABLE_VIAJE,values,TABLE_VIAJE_COL_ID+"=?",parametros);
        if(res>0){
            flag=true;
        }
        db.close();
        c.close();
        return  flag;
    }


    public ViajeVO buscarById(int id){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        ViajeVO temp = null;
        try{

            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "V."+TABLE_VIAJE_COL_ID+", " +//0
                            "V."+TABLE_VIAJE_COL_PROVEEDOR+", "+//1
                            "V."+TABLE_VIAJE_COL_PLACA+", "+//2
                            "V."+TABLE_VIAJE_COL_CONDUCTOR+", "+//3
                            "V."+TABLE_VIAJE_COL_RUTA+", "+//4
                            "V."+TABLE_VIAJE_COL_HORAINICIO+", "+//5
                            "V."+TABLE_VIAJE_COL_HORAFIN+", "+//6
                            "V."+TABLE_VIAJE_COL_NUMPASAJEROS+", "+//7
                            "V."+ TABLE_VIAJE_COL_CAPACIDAD +", "+//8
                            "V."+TABLE_VIAJE_COL_COMENTARIO+", "+//9
                            "V."+TABLE_VIAJE_COL_STATUS+", "+//10
                            "V."+TABLE_VIAJE_COL_HORACONFIRMADO+" "+
                        " FROM "+
                            TABLE_VIAJE+" as V"+
                        " WHERE "+
                            "V."+TABLE_VIAJE_COL_ID+" = "+String.valueOf(id)
                    ,null);
            cursor.moveToFirst();
                    temp = getAtributtes(cursor);
            cursor.close();
        }catch (Exception e){
            Toast.makeText(ctx,e.toString(), Toast.LENGTH_SHORT);
        }finally {
            db.close();
            c.close();
        }
        return temp;
    }

    //guardando f:1 v:2 env:1

    public List<ViajeVO> listAll(){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        List<ViajeVO> ViajeVOList = new  ArrayList<>();
        try{
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "V."+TABLE_VIAJE_COL_ID+", " +//0
                            "V."+TABLE_VIAJE_COL_PROVEEDOR+", "+//1
                            "V."+TABLE_VIAJE_COL_PLACA+", "+//2
                            "V."+TABLE_VIAJE_COL_CONDUCTOR+", "+//3
                            "V."+TABLE_VIAJE_COL_RUTA+", "+//4
                            "V."+TABLE_VIAJE_COL_HORAINICIO+", "+//5
                            "V."+TABLE_VIAJE_COL_HORAFIN+", "+//6
                            "V."+TABLE_VIAJE_COL_NUMPASAJEROS+", "+//7
                            "V."+ TABLE_VIAJE_COL_CAPACIDAD +", "+//8
                            "V."+TABLE_VIAJE_COL_COMENTARIO+", "+//9
                            "V."+TABLE_VIAJE_COL_STATUS+", "+//10
                            "V."+TABLE_VIAJE_COL_HORACONFIRMADO+" "+
                            " FROM "+
                                TABLE_VIAJE+" as V"
                    ,null);
            while(cursor.moveToNext()){
                ViajeVO temp = getAtributtes(cursor);

                Cursor cursor2 = db.rawQuery(
                        "SELECT " +
                                "count(*)"+" " +//0
                                " FROM "+
                                TABLE_RESTRICCION+" "+
                                " WHERE "+
                                TABLE_RESTRICCION_COL_IDVIAJE+" = "+temp.getId()
                        ,null);
                    if(cursor2.getCount()>0){
                        cursor2.moveToFirst();
                        temp.setNumRestricciones(cursor2.getInt(0));
                    }


                Log.d("listar ViajeDAO : ",""+temp.getId());
                ViajeVOList.add(temp);
                // Toast.makeText(ctx,temp.getName(),Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }catch (Exception e){
            Toast.makeText(ctx,e.toString(),Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            c.close();
        }
        return ViajeVOList;
    }

    public boolean clearTableUpload(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        List<ViajeVO> viajeVOList = new ViajeDAO(ctx).listAll();
        for(int i=0;i<viajeVOList.size();i++){

            deleteById(viajeVOList.get(i).getId());
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }

    public boolean clearTableUpload(int id){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        ViajeVO viajeVO = new ViajeDAO(ctx).buscarById(id);


        deleteById(viajeVO.getId());

        flag=true;

        db.close();
        conn.close();
        return flag;
    }


    public boolean deleteById(int id){
        boolean flag = false;
        //borrar pasajeros
        new PasajeroDAO(ctx).deleteByIdViaje(id);
        //borrar restricciones
        new RestriccionDAO(ctx).deleteByIdViaje(id);

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();

        String[] parametros =
                {
                        String.valueOf(id),
                };

        int res = db.delete(TABLE_VIAJE,TABLE_VIAJE_COL_ID+"=?",parametros);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }

    public List<ViajeVO> listByStatus1() {
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        List<ViajeVO> ViajeVOList = new  ArrayList<>();
        try{

            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "V."+TABLE_VIAJE_COL_ID+", " +//0
                            "V."+TABLE_VIAJE_COL_PROVEEDOR+", "+//1
                            "V."+TABLE_VIAJE_COL_PLACA+", "+//2
                            "V."+TABLE_VIAJE_COL_CONDUCTOR+", "+//3
                            "V."+TABLE_VIAJE_COL_RUTA+", "+//4
                            "V."+TABLE_VIAJE_COL_HORAINICIO+", "+//5
                            "V."+TABLE_VIAJE_COL_HORAFIN+", "+//6
                            "V."+TABLE_VIAJE_COL_NUMPASAJEROS+", "+//7
                            "V."+ TABLE_VIAJE_COL_CAPACIDAD +", "+//8
                            "V."+TABLE_VIAJE_COL_COMENTARIO+", "+//9
                            "V."+TABLE_VIAJE_COL_STATUS+", "+//10
                            "V."+TABLE_VIAJE_COL_HORACONFIRMADO+" "+
                            " FROM "+
                            TABLE_VIAJE+" as V"+" "+
                            " WHERE "+
                            "V."+TABLE_VIAJE_COL_STATUS+" = "+1
                    ,null);
            while(cursor.moveToNext()){
                ViajeVO temp = getAtributtes(cursor);
                Log.d("listar ViajeDAO s1 : ",""+temp.getId());
                ViajeVOList.add(temp);
                // Toast.makeText(ctx,temp.getName(),Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }catch (Exception e){
            Toast.makeText(ctx,e.toString(),Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            c.close();
        }
        return ViajeVOList;

    }

    public List<ViajeVO> listByStatusNo2() {
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        List<ViajeVO> ViajeVOList = new  ArrayList<>();
        try{

            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "V."+TABLE_VIAJE_COL_ID+", " +//0
                            "V."+TABLE_VIAJE_COL_PROVEEDOR+", "+//1
                            "V."+TABLE_VIAJE_COL_PLACA+", "+//2
                            "V."+TABLE_VIAJE_COL_CONDUCTOR+", "+//3
                            "V."+TABLE_VIAJE_COL_RUTA+", "+//4
                            "V."+TABLE_VIAJE_COL_HORAINICIO+", "+//5
                            "V."+TABLE_VIAJE_COL_HORAFIN+", "+//6
                            "V."+TABLE_VIAJE_COL_NUMPASAJEROS+", "+//7
                            "V."+ TABLE_VIAJE_COL_CAPACIDAD +", "+//8
                            "V."+TABLE_VIAJE_COL_COMENTARIO+", "+//9
                            "V."+TABLE_VIAJE_COL_STATUS+", "+//10
                            "V."+TABLE_VIAJE_COL_HORACONFIRMADO+" "+
                            " FROM "+
                            TABLE_VIAJE+" as V"+" "+
                            " WHERE "+
                            "V."+TABLE_VIAJE_COL_STATUS+" != "+2
                    ,null);
            while(cursor.moveToNext()){
                ViajeVO temp = getAtributtes(cursor);
                Log.d("listar ViajeDAO s1 : ",""+temp.getId());
                ViajeVOList.add(temp);
                // Toast.makeText(ctx,temp.getName(),Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }catch (Exception e){
            Toast.makeText(ctx,e.toString(),Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            c.close();
        }
        return ViajeVOList;

    }

    /*
    ViajeVO getAtributes (Cursor cursor){

    }
    */

    private ViajeVO getAtributtes(Cursor cursor){
        ViajeVO salidaVO = new ViajeVO();
        String[] columnNames = cursor.getColumnNames();
        for(String name : columnNames){
            switch (name){
                case TABLE_VIAJE_COL_ID:
                    salidaVO.setId(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_PROVEEDOR:
                    salidaVO.setProveedor(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_PLACA:
                    salidaVO.setPlaca(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_CONDUCTOR:
                    salidaVO.setConductor(cursor.getString(cursor.getColumnIndex(name)));
                    break;

                case TABLE_VIAJE_COL_CAPACIDAD:
                    salidaVO.setCapacidad(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_NUMPASAJEROS:
                    salidaVO.setNumPasajeros(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_HORAINICIO:
                    salidaVO.sethInicio(cursor.getString(cursor.getColumnIndex(name)));
                    break;

                case TABLE_VIAJE_COL_HORAFIN:
                    salidaVO.sethFin(cursor.getString(cursor.getColumnIndex(name)));
                    break;

                case TABLE_VIAJE_COL_RUTA:
                    salidaVO.setRuta(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_COMENTARIO:
                    salidaVO.setComentario(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_STATUS:
                    salidaVO.setStatus(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_HORACONFIRMADO:
                    salidaVO.sethConfirmado(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                default:
                    Toast.makeText(ctx,TAG+"getAtributes error no se encuentra campo "+name,Toast.LENGTH_LONG).show();
                    Log.d(TAG," getAtributes error no se encuentra campo "+name);
                    break;
            }
        }
        return salidaVO;
    }
}
