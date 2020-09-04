package ibao.alertbus.ecosac.models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ibao.alertbus.ecosac.ConexionSQLiteHelper;
import ibao.alertbus.ecosac.models.vo.ViajeVO;


import static ibao.alertbus.ecosac.ConexionSQLiteHelper.VERSION_DB;
import static ibao.alertbus.ecosac.utilities.Utilities.DATABASE_NAME;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_RESTRICCION;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_RESTRICCION_COL_IDVIAJE;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_COMENTARIO;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_CONDUCTOR;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_EMPRESA;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_HORACONFIRMADO;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_HORAFIN;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_HORAINICIO;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_HORAPROGRAMADA;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_ID;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_IDWEB;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_NUMPASAJEROS;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_NUMRESTRICCIONES;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_PLACA;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_CAPACIDAD;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_PROVEEDOR;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_RUTA;
import static ibao.alertbus.ecosac.utilities.Utilities.TABLE_VIAJE_COL_STATUS;


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

    public boolean deleteByStatusSicronized(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        List<ViajeVO>  viajeVOList = listByStatusSyncronized();

        for (ViajeVO v : viajeVOList){
            deleteById(v.getId());
        }
        db.close();
        conn.close();
        return flag;
    }

    public boolean insertar(ViajeVO viajeVO
    ){

        long temp=0;

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_VIAJE_COL_ID,viajeVO.getId());
            values.put(TABLE_VIAJE_COL_EMPRESA,viajeVO.getEmpresa());
            values.put(TABLE_VIAJE_COL_PROVEEDOR,viajeVO.getProveedor());
            values.put(TABLE_VIAJE_COL_PLACA,viajeVO.getPlaca());
            values.put(TABLE_VIAJE_COL_CONDUCTOR,viajeVO.getConductor());
            values.put(TABLE_VIAJE_COL_RUTA,viajeVO.getRuta());
            values.put(TABLE_VIAJE_COL_HORAPROGRAMADA,viajeVO.gethProgramada());
            values.put(TABLE_VIAJE_COL_HORAINICIO,viajeVO.gethInicio());
            values.put(TABLE_VIAJE_COL_HORAFIN,viajeVO.gethFin());
            values.put(TABLE_VIAJE_COL_CAPACIDAD,viajeVO.getCapacidad());
            values.put(TABLE_VIAJE_COL_COMENTARIO,viajeVO.getComentario());
            values.put(TABLE_VIAJE_COL_STATUS,viajeVO.getStatus());
            temp = db.insert(TABLE_VIAJE,TABLE_VIAJE_COL_ID,values);
            db.close();
            conn.close();

        }catch (Exception e){
            Log.d("ViajeDAO ins : ",e.toString());
        }
        return (temp>0);
    }

    public boolean insertarBYQR(ViajeVO viajeVO
    ){

        long temp=0;

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_VIAJE_COL_ID,viajeVO.getId());
            values.put(TABLE_VIAJE_COL_IDWEB,viajeVO.getIdWeb());
            values.put(TABLE_VIAJE_COL_PROVEEDOR,viajeVO.getProveedor());
            values.put(TABLE_VIAJE_COL_PLACA,viajeVO.getPlaca());
            values.put(TABLE_VIAJE_COL_NUMPASAJEROS,viajeVO.getNumPasajerosRegistrados());
            values.put(TABLE_VIAJE_COL_NUMRESTRICCIONES,viajeVO.getNumRestriccionesRegistradas());
            values.put(TABLE_VIAJE_COL_CONDUCTOR,viajeVO.getConductor());
            values.put(TABLE_VIAJE_COL_RUTA,viajeVO.getRuta());
            values.put(TABLE_VIAJE_COL_HORAPROGRAMADA,viajeVO.gethProgramada());
            values.put(TABLE_VIAJE_COL_HORAINICIO,viajeVO.gethInicio());
            values.put(TABLE_VIAJE_COL_HORAFIN,viajeVO.gethFin());
            values.put(TABLE_VIAJE_COL_CAPACIDAD,viajeVO.getCapacidad());
            values.put(TABLE_VIAJE_COL_COMENTARIO,viajeVO.getComentario());
            values.put(TABLE_VIAJE_COL_STATUS,viajeVO.getStatus());
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
        int res = db.updateViaje(TABLE_VIAJE,values,TABLE_VIAJE_COL_ID+"=? AND "+TABLE_VIAJE_COL_STATUS+"<1",parametros);
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


    public boolean toStatus3(int id,int idWeb) {

        boolean flag = false;
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();
        String[] parametros =
                {
                        String.valueOf(id),
                };
        ContentValues values = new ContentValues();
        values.put(TABLE_VIAJE_COL_STATUS,3);
        values.put(TABLE_VIAJE_COL_IDWEB,idWeb);
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
    public boolean updateHoraInicio(int id, String hinicio) {
        boolean flag = false;
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();
        String[] parametros =
                {
                        String.valueOf(id),
                };
        ContentValues values = new ContentValues();
        values.put(TABLE_VIAJE_COL_HORAINICIO,hinicio);
        int res = db.update(TABLE_VIAJE,values,TABLE_VIAJE_COL_ID+"=?",parametros);
        if(res>0){
            flag=true;
        }
        db.close();
        c.close();
        return  flag;
    }

    public boolean updateHoraFin(int id, String hFin) {
        boolean flag = false;
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();
        String[] parametros =
                {
                        String.valueOf(id),
                };
        ContentValues values = new ContentValues();
        values.put(TABLE_VIAJE_COL_HORAFIN,hFin);
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
                            "*"+
                        " FROM "+
                            TABLE_VIAJE+" as V"+
                        " WHERE "+
                            "V."+TABLE_VIAJE_COL_ID+" = "+String.valueOf(id)
                    ,null);
            cursor.moveToFirst();
                    temp = getAtributtes(cursor,true);
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

    public List<ViajeVO> listAll(boolean detail){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        List<ViajeVO> ViajeVOList = new  ArrayList<>();
        try{
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "*"+
                            " FROM "+
                                TABLE_VIAJE+" as V"
                    ,null);
            while(cursor.moveToNext()){
                ViajeVO temp = getAtributtes(cursor,detail);

                Cursor cursor2 = db.rawQuery(
                        "SELECT " +
                                "count(*)"+" " +//0
                                " FROM "+
                                TABLE_RESTRICCION+" "+
                                " WHERE "+
                                TABLE_RESTRICCION_COL_IDVIAJE+" = "+temp.getId()
                        ,null);



                Log.d(TAG,""+temp.getId());
                ViajeVOList.add(temp);
                // Toast.makeText(ctx,temp.getName(),Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }catch (Exception e){
            Toast.makeText(ctx,e.toString(),Toast.LENGTH_SHORT).show();
            Log.d(TAG,"listAll()"+e.toString());
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
        List<ViajeVO> viajeVOList = new ViajeDAO(ctx).listAll(true);
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

            new PasajeroDAO(ctx).deleteByIdViaje(id);
            new RestriccionDAO(ctx).deleteByIdViaje(id);

        }
        db.close();
        conn.close();
        return flag;
    }



    public List<ViajeVO> listByStatusSyncronized() {
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        List<ViajeVO> ViajeVOList = new  ArrayList<>();
        try{
            //si es condctor
            String sql ="SELECT " +
                    "*"+
                    " FROM "+
                    TABLE_VIAJE+" as V"+" "+
                    " WHERE "+
                    "V."+TABLE_VIAJE_COL_STATUS+" = "+3;
            if(new LoginDataDAO(ctx).verficarLogueo().getTypeUser()==1){//so es suopervisor
                sql ="SELECT " +
                        "*"+
                        " FROM "+
                        TABLE_VIAJE+" as V"+" "+
                        " WHERE "+
                        "V."+TABLE_VIAJE_COL_STATUS+" = "+2;
            }

            Cursor cursor = db.rawQuery(
                    sql
                    ,null);
            while(cursor.moveToNext()){
                ViajeVO temp = getAtributtes(cursor,true);
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

    public List<ViajeVO> listByStatusWaitingAtUpload() {
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        List<ViajeVO> ViajeVOList = new  ArrayList<>();
        try{
            //si es condctor
            String sql = "";
            if (new LoginDataDAO(ctx).verficarLogueo().getIsTranquera() == 1){
                sql ="SELECT " +
                        "*"+
                        " FROM "+
                        TABLE_VIAJE+" as V"+" "+
                        " WHERE "+
                        "V."+TABLE_VIAJE_COL_STATUS+" = "+1 + " or " +  "V."+TABLE_VIAJE_COL_STATUS+" = "+2;
            }
            else {
                sql ="SELECT " +
                        "*"+
                        " FROM "+
                        TABLE_VIAJE+" as V"+" "+
                        " WHERE "+
                        "V."+TABLE_VIAJE_COL_STATUS+" = "+2;
                if(new LoginDataDAO(ctx).verficarLogueo().getTypeUser()==1){//si es suopervisor
                    sql ="SELECT " +
                            "V."+TABLE_VIAJE_COL_ID+", " +//0
                            "V."+TABLE_VIAJE_COL_PROVEEDOR+", "+//1
                            "V."+TABLE_VIAJE_COL_IDWEB+", "+
                            "V."+TABLE_VIAJE_COL_PLACA+", "+//2
                            "V."+TABLE_VIAJE_COL_CONDUCTOR+", "+//3
                            "V."+TABLE_VIAJE_COL_RUTA+", "+//4
                            "V."+TABLE_VIAJE_COL_HORAINICIO+", "+//5
                            "V."+TABLE_VIAJE_COL_HORAFIN+", "+//6
                            "V."+ TABLE_VIAJE_COL_CAPACIDAD +", "+//8
                            "V."+TABLE_VIAJE_COL_COMENTARIO+", "+//9
                            "V."+TABLE_VIAJE_COL_STATUS+", "+//10
                            "V."+TABLE_VIAJE_COL_HORACONFIRMADO+" "+
                            " FROM "+
                            TABLE_VIAJE+" as V"+" "+
                            " WHERE "+
                            "V."+TABLE_VIAJE_COL_STATUS+" = "+1;
                }
            }



            Cursor cursor = db.rawQuery(
                    sql
                    ,null);
            while(cursor.moveToNext()){
                ViajeVO temp = getAtributtes(cursor,true);
                if (temp.getProveedor().equals("")){
                    temp.setTrasbordo(1);
                }
                else {
                    temp.setTrasbordo(0);
                }
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

    public List<ViajeVO> listByStatusNoFinished() {
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        List<ViajeVO> ViajeVOList = new  ArrayList<>();
        try{
            //si es conductor
            String sql = "SELECT " +
                    "*"+
                    " FROM "+
                    TABLE_VIAJE+" as V"+" "+
                    " WHERE "+
                    "V."+TABLE_VIAJE_COL_STATUS+" != "+3;
            if(new LoginDataDAO(ctx).verficarLogueo().getTypeUser()==1){//si es suopervisor
                sql="SELECT " +
                        "*"+
                        " FROM "+
                        TABLE_VIAJE+" as V"+" "+
                        " WHERE "+
                        "V."+TABLE_VIAJE_COL_STATUS+" != "+2;
            }

            Cursor cursor = db.rawQuery(
                    sql
                    ,null);
            while(cursor.moveToNext()){
                ViajeVO temp = getAtributtes(cursor,false);
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

    private ViajeVO getAtributtes(Cursor cursor,boolean detail){
        ViajeVO viajeVO = new ViajeVO();
        String[] columnNames = cursor.getColumnNames();
        for(String name : columnNames){
            switch (name){
                case TABLE_VIAJE_COL_ID:
                    viajeVO.setId(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_IDWEB:
                    viajeVO.setIdWeb(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_EMPRESA:
                    viajeVO.setEmpresa(cursor.getString(cursor.getColumnIndex(name)));
                case TABLE_VIAJE_COL_PROVEEDOR:
                    viajeVO.setProveedor(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_HORAPROGRAMADA:
                    viajeVO.sethProgramada(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_PLACA:
                    viajeVO.setPlaca(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_CONDUCTOR:
                    viajeVO.setConductor(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_CAPACIDAD:
                    viajeVO.setCapacidad(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_HORAINICIO:
                    viajeVO.sethInicio(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_HORAFIN:
                    viajeVO.sethFin(cursor.getString(cursor.getColumnIndex(name)));
                    break;

                case TABLE_VIAJE_COL_RUTA:
                    viajeVO.setRuta(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_COMENTARIO:
                    viajeVO.setComentario(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_STATUS:
                    viajeVO.setStatus(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_NUMPASAJEROS:
                    viajeVO.setNumPasajerosRegistrados(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_NUMRESTRICCIONES:
                    viajeVO.setNumRestriccionesRegistradas(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_VIAJE_COL_HORACONFIRMADO:
                    viajeVO.sethConfirmado(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                default:
                    Toast.makeText(ctx,TAG+"getAtributes error no se encuentra campo "+name,Toast.LENGTH_LONG).show();
                    Log.d(TAG," getAtributes error no se encuentra campo "+name);
                    break;
            }

            if(detail){
                viajeVO.setPasajeroVOList(new PasajeroDAO(ctx).listByIdViaje(viajeVO.getId()));
                viajeVO.setRestriccionVOList(new RestriccionDAO(ctx).listByIdViaje(viajeVO.getId()));
            }
        }
        return viajeVO;
    }

    public boolean updateViaje(ViajeVO viajeVO) {
        boolean flag = false;
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();
        String[] parametros =
                {
                        String.valueOf(viajeVO.getId()),
                };
        ContentValues values = new ContentValues();
        values.put(TABLE_VIAJE_COL_ID,viajeVO.getId());
        values.put(TABLE_VIAJE_COL_PROVEEDOR,viajeVO.getProveedor());
        values.put(TABLE_VIAJE_COL_PLACA,viajeVO.getPlaca());
        values.put(TABLE_VIAJE_COL_CONDUCTOR,viajeVO.getConductor());
        values.put(TABLE_VIAJE_COL_RUTA,viajeVO.getRuta());
        values.put(TABLE_VIAJE_COL_HORAPROGRAMADA,viajeVO.gethProgramada());
        values.put(TABLE_VIAJE_COL_CAPACIDAD,viajeVO.getCapacidad());

        int res = db.update(TABLE_VIAJE,values,TABLE_VIAJE_COL_ID+"=?",parametros);
        if(res>0){
            flag=true;
        }
        db.close();
        c.close();
        return  flag;

    }
}
