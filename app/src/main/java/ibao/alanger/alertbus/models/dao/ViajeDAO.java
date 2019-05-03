package ibao.alanger.alertbus.models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.ibao.alanger.agroq.ConexionSQLiteHelper;
import com.ibao.alanger.agroq.models.vo.entitiesDB.CalibreVO;

import java.util.ArrayList;
import java.util.List;

import static com.ibao.alanger.agroq.ConexionSQLiteHelper.VERSION_DB;
import static com.ibao.alanger.agroq.utilities.Utilities.DATABASE_NAME;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_ID;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_IDTIPOCALIBRE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_NAME;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_PESOMAX;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CALIBRE_COL_PESOMIN;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CRITERIODETALLE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CRITERIODETALLE_COL_ID;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CRITERIODETALLE_COL_IDEVALAUCIONDETALLE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_EVALUACIONDETALLE;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_EVALUACIONDETALLE_COL_ID;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_EVALUACIONDETALLE_COL_IDEVALUACION;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_EVALUACIONDETALLE_COL_IDMUESTRA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_MUESTRA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_MUESTRA_COL_ID;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_MUESTRA_COL_IDPROCESO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_PRODUCCION;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_PRODUCCION_COL_ID;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_PRODUCCION_COL_IDCALIBRE;


public class CalibreDAO {


    String TAG = CalibreDAO.class.getSimpleName();

    Context ctx;
    public CalibreDAO(Context ctx) {
        this.ctx=ctx;
    }


    public boolean borrarTable(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = db.delete(TABLE_CALIBRE,null,null);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }

    public boolean insertar(int id, String name,float pesoMin,float pesoMax,int idTipoCalibre){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_CALIBRE_COL_ID,id);
            values.put(TABLE_CALIBRE_COL_NAME,name);
            values.put(TABLE_CALIBRE_COL_PESOMIN,pesoMin);
            values.put(TABLE_CALIBRE_COL_PESOMAX,pesoMax);
            values.put(TABLE_CALIBRE_COL_IDTIPOCALIBRE,idTipoCalibre);
            long temp = db.insert(TABLE_CALIBRE,TABLE_CALIBRE_COL_ID,values);
            db.close();
            conn.close();
            return (temp>0);
        }catch (Exception e){
            Log.d("CalibreDAO ins : ",e.toString());
        }

        return false;
    }

    public CalibreVO consultarTolCalibreByidCriterioDetalle(int idCriterioDetalle){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        CalibreVO temp = null;
        try{

            temp = new CalibreVO();
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "CA."+TABLE_CALIBRE_COL_ID+", " +
                            "CA."+TABLE_CALIBRE_COL_NAME+", "+
                            "CA."+TABLE_CALIBRE_COL_PESOMIN+", "+
                            "CA."+TABLE_CALIBRE_COL_PESOMAX+", "+
                            "CA."+TABLE_CALIBRE_COL_IDTIPOCALIBRE+" "+
                        " FROM "+
                            TABLE_CALIBRE+" as CA, "+
                            TABLE_PRODUCCION+" as PRO, "+
                            TABLE_MUESTRA+" as M,"+
                            TABLE_EVALUACIONDETALLE+" as ED, "+
                            TABLE_CRITERIODETALLE+" as CD "+
                        " WHERE "+
                            "CD."+TABLE_CRITERIODETALLE_COL_ID+" = "+String.valueOf(idCriterioDetalle)+
                            " AND "+
                            "CD."+TABLE_CRITERIODETALLE_COL_IDEVALAUCIONDETALLE+" = "+"ED."+TABLE_EVALUACIONDETALLE_COL_ID+
                            " AND "+
                            "ED."+TABLE_EVALUACIONDETALLE_COL_IDMUESTRA+" = "+"M."+TABLE_MUESTRA_COL_ID+
                            " AND "+
                            "ED."+TABLE_EVALUACIONDETALLE_COL_IDEVALUACION+" = "+"3"+
                            " AND "+
                            "M."+TABLE_MUESTRA_COL_IDPROCESO+" = "+"PRO."+TABLE_PRODUCCION_COL_ID+
                            " AND "+
                            "PRO."+TABLE_PRODUCCION_COL_IDCALIBRE+" = "+"CA."+TABLE_CALIBRE_COL_ID
                    ,null);
           // cursor.moveToFirst();

            if(cursor.getCount()>0){
                cursor.moveToFirst();
                temp.setId(cursor.getInt(0));
                temp.setName(cursor.getString(1));
                temp.setPesoMin(cursor.getString(2));
                temp.setPesoMax(cursor.getString(3));
                temp.setIdTipoCalibre(cursor.getInt(4));
                Log.d(TAG,"consult cal by cd ="+temp.getId()+" "+temp.getPesoMin()+" "+temp.getPesoMax());
            }else {
                temp = null;
            }
            cursor.close();
        }catch (Exception e){
            temp = null;
            Log.d(TAG,"ExConsult cal by cd ="+temp.getId()+" "+temp.getPesoMin());
            Toast.makeText(ctx,"calibrexE :"+e.toString(), Toast.LENGTH_SHORT);
        }finally {
            db.close();
            c.close();
        }
        return temp;
    }

    public CalibreVO consultarByid(int id){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        CalibreVO temp = null;
        try{
            temp = new CalibreVO();
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "CA."+TABLE_CALIBRE_COL_ID+", " +
                            "CA."+TABLE_CALIBRE_COL_NAME+", "+
                            "CA."+TABLE_CALIBRE_COL_PESOMIN+", "+
                            "CA."+TABLE_CALIBRE_COL_PESOMAX+", "+
                            "CA."+TABLE_CALIBRE_COL_IDTIPOCALIBRE+" "+
                        " FROM "+
                            TABLE_CALIBRE+" as CA"+
                        " WHERE "+
                            "CA."+TABLE_CALIBRE_COL_ID+" = "+String.valueOf(id)
                    ,null);
            cursor.moveToFirst();
            temp.setId(cursor.getInt(0));
            temp.setName(cursor.getString(1));
            temp.setPesoMin(cursor.getString(2));
            temp.setPesoMax(cursor.getString(3));
            temp.setIdTipoCalibre(cursor.getInt(4));
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

    public List<CalibreVO> listByIdTipoCalibre(int idTipoCalibre){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
        List<CalibreVO> calibreVOList = new  ArrayList<>();
        try{

            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "C."+TABLE_CALIBRE_COL_ID+", " +
                            "C."+TABLE_CALIBRE_COL_NAME+", "+
                            "C."+TABLE_CALIBRE_COL_PESOMIN+", "+
                            "C."+TABLE_CALIBRE_COL_PESOMAX+", "+
                            "C."+TABLE_CALIBRE_COL_IDTIPOCALIBRE+
                            " FROM "+TABLE_CALIBRE+" as C"+
                            " WHERE "+"C."+TABLE_CALIBRE_COL_IDTIPOCALIBRE+" = "+String.valueOf(idTipoCalibre)
                    ,null);
            while(cursor.moveToNext()){
                CalibreVO temp = new CalibreVO();
                temp.setId(cursor.getInt(0));
                temp.setName(cursor.getString(1));
                temp.setPesoMin(cursor.getString(2));
                temp.setPesoMax(cursor.getString(3));
                temp.setIdTipoCalibre(cursor.getInt(4));
                Log.d("lsitar CalibreDAO : ",""+temp.getName());
                calibreVOList.add(temp);
                // Toast.makeText(ctx,temp.getName(),Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }catch (Exception e){
            Toast.makeText(ctx,e.toString(),Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            c.close();
        }
        return calibreVOList;
    }
    public boolean clearTableUpload(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = db.delete(TABLE_CALIBRE,null,null);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }
}
