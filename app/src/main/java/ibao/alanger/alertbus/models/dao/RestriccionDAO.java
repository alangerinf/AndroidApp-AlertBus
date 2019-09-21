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
import ibao.alanger.alertbus.models.vo.RestriccionVO;

import static ibao.alanger.alertbus.ConexionSQLiteHelper.VERSION_DB;
import static ibao.alanger.alertbus.utilities.Utilities.DATABASE_NAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_RESTRICCION;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_RESTRICCION_COL_DESC;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_RESTRICCION_COL_ID;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_RESTRICCION_COL_IDVIAJE;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_RESTRICCION_COL_NAME;


public class RestriccionDAO {

    Context ctx;

    String TAG = RestriccionDAO.class.getSimpleName();
    public RestriccionDAO(Context ctx) {
         this.ctx=ctx;
    }

    public boolean borrarTable(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = db.delete(TABLE_RESTRICCION,null,null);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }

    public boolean insertar( String name,String desc, int idViaje){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_RESTRICCION_COL_NAME,name);
            values.put(TABLE_RESTRICCION_COL_DESC,desc);
            values.put(TABLE_RESTRICCION_COL_IDVIAJE,idViaje);

            long temp = db.insert(TABLE_RESTRICCION,TABLE_RESTRICCION_COL_ID,values);
            if(temp>0){
                Log.d(TAG,"insertar "+name+" en idViaje "+idViaje);
            }
            db.close();
            conn.close();
            return (temp>0);
        }catch (Exception e){
            Log.d(TAG,"insertar error: "+ e.toString());
        }

        return false;
    }

    public List<RestriccionVO> listByIdViaje(int idViaje){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );

        SQLiteDatabase db = c.getReadableDatabase();
        List<RestriccionVO> categoriaVOList = new ArrayList<>();
        try{
            String[] campos = {
                    TABLE_RESTRICCION_COL_ID,
                    TABLE_RESTRICCION_COL_NAME,
                    TABLE_RESTRICCION_COL_DESC,
                    TABLE_RESTRICCION_COL_IDVIAJE,
            };
            String[] arg = {String.valueOf(idViaje)};
            Cursor cursor= db.query(TABLE_RESTRICCION,campos,TABLE_RESTRICCION_COL_IDVIAJE+"=?",arg,null,null,TABLE_RESTRICCION_COL_NAME+" COLLATE UNICODE ASC");
            while(cursor.moveToNext()){

                RestriccionVO temp = getAtributtes(cursor);
                categoriaVOList.add(temp);

            }
            cursor.close();
        }catch (Exception e){
            Log.d(TAG,"listByIdViaje"+e.toString());
            Toast.makeText(ctx,TAG+e.toString(),Toast.LENGTH_SHORT).show();
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
                TABLE_RESTRICCION,
                TABLE_RESTRICCION_COL_IDVIAJE+"=?",
                parametros);

        Log.d(TAG," borrando->"+res);

        db.close();
        conn.close();
        return res;
    }


    private RestriccionVO getAtributtes(Cursor cursor){
        RestriccionVO restriccionVO = new RestriccionVO();
        String[] columnNames = cursor.getColumnNames();
        for(String name : columnNames){
            switch (name){
                case TABLE_RESTRICCION_COL_ID:
                    restriccionVO.setId(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                case TABLE_RESTRICCION_COL_NAME:
                    restriccionVO.setName(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_RESTRICCION_COL_DESC:
                    restriccionVO.setDesc(cursor.getString(cursor.getColumnIndex(name)));
                    break;
                case TABLE_RESTRICCION_COL_IDVIAJE:
                    restriccionVO.setIdViaje(cursor.getInt(cursor.getColumnIndex(name)));
                    break;
                default:
                    Toast.makeText(ctx,TAG+"getAtributes error no se encuentra campo "+name,Toast.LENGTH_LONG).show();
                    Log.d(TAG," getAtributes error no se encuentra campo "+name);
                    break;
            }
        }
        return restriccionVO;
    }


}
