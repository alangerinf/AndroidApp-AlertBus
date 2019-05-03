package com.ibao.alanger.agroq.models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.ibao.alanger.agroq.ConexionSQLiteHelper;
import com.ibao.alanger.agroq.models.vo.entitiesInternal.LoginDataVO;

import static com.ibao.alanger.agroq.ConexionSQLiteHelper.VERSION_DB;
import static com.ibao.alanger.agroq.utilities.Utilities.DATABASE_NAME;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_CODIGO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_IDCULTIVO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_IDPLANTA;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_IDTIPOPROCESO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_IDUSER;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_LASTNAME;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_LISTIDTIPOPROCESO;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_NAME;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_PASSWORD;
import static com.ibao.alanger.agroq.utilities.Utilities.TABLE_LOGINDATA_COL_USER;

public class LoginDataDAO {

    Context ctx;

  //  static String TAG = "LoginData DAO";
    
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
            values.put(TABLE_LOGINDATA_COL_CODIGO,loginDataVO.getCodigo());
            values.put(TABLE_LOGINDATA_COL_IDTIPOPROCESO,loginDataVO.getIdTipoProceso());
            values.put(TABLE_LOGINDATA_COL_LISTIDTIPOPROCESO,loginDataVO.getListIdTipoProcesos());
            values.put(TABLE_LOGINDATA_COL_IDCULTIVO,loginDataVO.getIdCultivo());
            values.put(TABLE_LOGINDATA_COL_IDPLANTA,loginDataVO.getIdPlanta());
        int id = (int)db.insert(TABLE_LOGINDATA, TABLE_LOGINDATA_COL_IDUSER, values);
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
                            "LD."+TABLE_LOGINDATA_COL_IDUSER        +", " +//0
                            "LD."+TABLE_LOGINDATA_COL_USER +", " +//1
                            "LD."+TABLE_LOGINDATA_COL_PASSWORD   +", " +//2
                            "LD."+TABLE_LOGINDATA_COL_NAME   +", "+//3
                            "LD."+TABLE_LOGINDATA_COL_LASTNAME+", "+//4
                            "LD."+TABLE_LOGINDATA_COL_CODIGO+", "+ //5
                            "LD."+TABLE_LOGINDATA_COL_IDCULTIVO+", "+ //6
                            "LD."+TABLE_LOGINDATA_COL_IDPLANTA+", "+ //7
                            "LD."+TABLE_LOGINDATA_COL_IDTIPOPROCESO+", "+ //8
                            "LD."+TABLE_LOGINDATA_COL_LISTIDTIPOPROCESO+ //9

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
                temp.setCodigo(cursor.getString(5));
               // Log.d(TAG,"6");
                temp.setIdCultivo(cursor.getInt(6));
               // Log.d(TAG,"7");
                temp.setIdPlanta((cursor.getInt(7)));
               // Log.d(TAG,"8");
                temp.setIdTipoProceso((cursor.getInt(8)));
               // Log.d(TAG,"9");
                temp.setListIdTipoProcesos(cursor.getString(9));

                if(temp.getIdTipoProceso()>0){
                    String nameTypeProcess;
                    nameTypeProcess = new TipoProcesoDAO(ctx).consultarByid(temp.getIdTipoProceso()).getName();
                    temp.setNameTypeProcess(String.valueOf(nameTypeProcess.charAt(0)).toUpperCase()+nameTypeProcess.toLowerCase().substring(1));

                }

            }
            cursor.close();

        }catch (Exception e){
          //  Log.d(TAG,"getEditing "+e.toString());
            Toast.makeText(ctx,"getEditing "+e.toString(),Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            c.close();
        }
        return temp;
    }

    public boolean editIdTipoProceso(int idTipoProceso) {

            boolean flag = false;
            ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx,DATABASE_NAME, null, VERSION_DB);
            SQLiteDatabase db = c.getWritableDatabase();
            /*String[] parametros =
                    {
                            String.valueOf(idTipoProceso),
                    };
            */
            ContentValues values = new ContentValues();
            values.put(TABLE_LOGINDATA_COL_IDTIPOPROCESO,idTipoProceso);
            int res = db.update(TABLE_LOGINDATA,values,null,null);
            if(res>0){
                flag=true;
            }
            db.close();
            c.close();
            return  flag;


    }
}
