package ibao.alanger.alertbus.models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.ibao.alanger.agroq.ConexionSQLiteHelper;
import com.ibao.alanger.agroq.models.vo.entitiesDB.CategoriaVO;

import java.util.ArrayList;
import java.util.List;

import static com.ibao.alanger.agroq.ConexionSQLiteHelper.VERSION_DB;
import static com.ibao.alanger.agroq.utilities.Utilities.DATABASE_NAME;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CATEGORIA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CATEGORIA_COL_ID;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_CATEGORIA_COL_NAME;


public class CategoriaDAO {

    Context ctx;
   public CategoriaDAO(Context ctx) {
         this.ctx=ctx;
    }


    public boolean borrarTable(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = db.delete(TABLE_CATEGORIA,null,null);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }

    public boolean insertar(int id, String name){

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TABLE_CATEGORIA_COL_ID,id);
            values.put(TABLE_CATEGORIA_COL_NAME,name);
            Long temp = db.insert(TABLE_CATEGORIA,TABLE_CATEGORIA_COL_ID,values);
            db.close();
            conn.close();
            return (temp>0);
        }catch (Exception e){
            Log.d("ZonaDAO",e.toString());
        }

        return false;
    }

    public CategoriaVO consultarByid(int id){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );

        SQLiteDatabase db = c.getReadableDatabase();
        CategoriaVO temp = null;
        try{
            temp = new CategoriaVO();
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "Z."+TABLE_CATEGORIA_COL_ID+", " +
                            "Z."+TABLE_CATEGORIA_COL_NAME+
                        " FROM "+
                            TABLE_CATEGORIA+" as Z"+
                        " WHERE "+
                            "Z."+TABLE_CATEGORIA_COL_ID+" = "+String.valueOf(id)
                    ,null);
            cursor.moveToFirst();
            temp.setId(cursor.getInt(0));
            temp.setName(cursor.getString(1));
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

    public List<CategoriaVO> listCategorias(){
        ConexionSQLiteHelper c=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );

        SQLiteDatabase db = c.getReadableDatabase();
        List<CategoriaVO> categoriaVOList = new ArrayList<>();
        try{
            String[] campos = {TABLE_CATEGORIA_COL_ID,TABLE_CATEGORIA_COL_NAME};
            Cursor cursor= db.query(TABLE_CATEGORIA,campos,null,null,null,null,TABLE_CATEGORIA_COL_NAME+" COLLATE UNICODE ASC");
            while(cursor.moveToNext()){
                CategoriaVO temp = new CategoriaVO();
                temp.setId(cursor.getInt(0));
                temp.setName(cursor.getString(1));
                categoriaVOList.add(temp);
                //Log.d("zonaDAOtag",temp.getName());
               // Toast.makeText(ctx,temp.getName(),Toast.LENGTH_SHORT).show();
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

    public boolean clearTableUpload(){
        boolean flag = false;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB );
        SQLiteDatabase db = conn.getWritableDatabase();
        int res = db.delete(TABLE_CATEGORIA,null,null);
        if(res>0){
            flag=true;
        }
        db.close();
        conn.close();
        return flag;
    }
}
