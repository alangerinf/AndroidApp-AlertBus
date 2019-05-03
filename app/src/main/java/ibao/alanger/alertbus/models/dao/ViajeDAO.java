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
import ibao.alanger.alertbus.models.vo.ViajeVO;


import static ibao.alanger.alertbus.ConexionSQLiteHelper.VERSION_DB;
import static ibao.alanger.alertbus.utilities.Utilities.DATABASE_NAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_COMENTARIO;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_HORAFIN;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_HORAINICIO;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_ID;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_NUMPASAJEROS;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_PLACA;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_VIAJE_COL_PORCAPACIDAD;
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

    
    public boolean insertar(int id,
                            String hInicio,
                            String hFin,
                            String placa,
                            String ruta,
                            int numpasajeros,
                            float porCapacidad,
                            String proveedor){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
                values.put(TABLE_VIAJE_COL_ID,id);
                values.put(TABLE_VIAJE_COL_HORAINICIO,hInicio);
                values.put(TABLE_VIAJE_COL_HORAFIN,hFin);
                values.put(TABLE_VIAJE_COL_PLACA,placa);
                values.put(TABLE_VIAJE_COL_RUTA,ruta);
                values.put(TABLE_VIAJE_COL_NUMPASAJEROS,numpasajeros);
                values.put(TABLE_VIAJE_COL_PORCAPACIDAD,porCapacidad);
                values.put(TABLE_VIAJE_COL_PROVEEDOR,proveedor);
                values.put(TABLE_VIAJE_COL_COMENTARIO,"");
                values.put(TABLE_VIAJE_COL_STATUS,0);
            long temp = db.insert(TABLE_VIAJE,TABLE_VIAJE_COL_ID,values);
            db.close();
            conn.close();
            return (temp>0);
        }catch (Exception e){
            Log.d("ViajeDAO ins : ",e.toString());
        }
        return false;
    }

    

    public ViajeVO buscarById(int id){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        ViajeVO temp = null;
        try{
            temp = new ViajeVO();
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "V."+TABLE_VIAJE_COL_ID+", " +          //0
                            "V."+TABLE_VIAJE_COL_HORAINICIO+", "+   //1
                            "V."+TABLE_VIAJE_COL_HORAFIN+", "+      //2
                            "V."+TABLE_VIAJE_COL_PLACA+", "+        //3
                            "V."+TABLE_VIAJE_COL_RUTA+", "+         //4
                            "V."+TABLE_VIAJE_COL_NUMPASAJEROS+", "+ //5
                            "V."+TABLE_VIAJE_COL_PORCAPACIDAD+", "+ //6
                            "V."+TABLE_VIAJE_COL_PROVEEDOR+", "+    //7
                            "V."+TABLE_VIAJE_COL_COMENTARIO+", "+   //8
                            "V."+TABLE_VIAJE_COL_STATUS+" "+        //9
                        " FROM "+
                            TABLE_VIAJE+" as V"+
                        " WHERE "+
                            "V."+TABLE_VIAJE_COL_ID+" = "+String.valueOf(id)
                    ,null);
            cursor.moveToFirst();
                temp.setId(cursor.getInt(0));
                temp.sethInicio(cursor.getString(1));
                temp.sethFin(cursor.getString(2));
                temp.setPlaca(cursor.getString(3));
                temp.setRuta(cursor.getString(4));
                temp.setNumpasajeros(cursor.getInt(5));
                temp.setPorCapacidad(cursor.getFloat(6));
                temp.setProveedor(cursor.getString(7));
                temp.setComentario(cursor.getString(8));
                temp.setStatus(cursor.getInt(9));
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
                            "V."+TABLE_VIAJE_COL_ID+", " +
                            "V."+TABLE_VIAJE_COL_HORAINICIO+", "+
                            "V."+TABLE_VIAJE_COL_HORAFIN+", "+
                            "V."+TABLE_VIAJE_COL_PLACA+", "+
                            "V."+TABLE_VIAJE_COL_RUTA+", "+
                            "V."+TABLE_VIAJE_COL_NUMPASAJEROS+", "+
                            "V."+TABLE_VIAJE_COL_PORCAPACIDAD+", "+
                            "V."+TABLE_VIAJE_COL_PROVEEDOR+", "+
                            "V."+TABLE_VIAJE_COL_COMENTARIO+", "+
                            "V."+TABLE_VIAJE_COL_STATUS+" "+
                            " FROM "+
                                TABLE_VIAJE+" as V"
                    ,null);
            while(cursor.moveToNext()){
                ViajeVO temp = new ViajeVO();
                    temp.setId(cursor.getInt(0));
                    temp.sethInicio(cursor.getString(1));
                    temp.sethFin(cursor.getString(2));
                    temp.setPlaca(cursor.getString(3));
                    temp.setRuta(cursor.getString(4));
                    temp.setNumpasajeros(cursor.getInt(5));
                    temp.setPorCapacidad(cursor.getFloat(6));
                    temp.setProveedor(cursor.getString(7));
                    temp.setComentario(cursor.getString(8));
                    temp.setStatus(cursor.getInt(9));
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
}
