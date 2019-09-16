package ibao.alanger.alertbus.models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ibao.alanger.alertbus.ConexionSQLiteHelper;
import ibao.alanger.alertbus.models.vo.TrackingVO;

import static ibao.alanger.alertbus.ConexionSQLiteHelper.VERSION_DB;
import static ibao.alanger.alertbus.utilities.Utilities.DATABASE_NAME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_TRACKING;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_TRACKING_COL_BEARING;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_TRACKING_COL_DATETIME;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_TRACKING_COL_ISUPDATE;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_TRACKING_COL_LAT;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_TRACKING_COL_LON;
import static ibao.alanger.alertbus.utilities.Utilities.TABLE_TRACKING_COL_SPEED;

public class TrackingDAO {

    Context ctx;

    private static String TAG = TrackingDAO.class.getSimpleName();
    
    public TrackingDAO(Context ctx) {
        this.ctx=ctx;
    }

    public int borrarTable(){
        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();
        int res =db.delete(TABLE_TRACKING,null,null);
        db.close();
        c.close();
        return res;
    }

    public void saveNewLocation(String lat, String lon,String bearing,String speed){

        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();

        ContentValues values = new ContentValues();
            values.put(TABLE_TRACKING_COL_LAT,lat);
            values.put(TABLE_TRACKING_COL_LON,lon);
            values.put(TABLE_TRACKING_COL_BEARING,bearing);
            values.put(TABLE_TRACKING_COL_SPEED,speed);
            db.insert(TABLE_TRACKING, null, values);
        db.close();
        c.close();

    }

    public void setUploadStatus(String dateTime){

        ConexionSQLiteHelper c = new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB);
        SQLiteDatabase db = c.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_TRACKING_COL_ISUPDATE,"1");
        db.update(
                TABLE_TRACKING,
                values,
                TABLE_TRACKING_COL_ISUPDATE+"=?",
                new String[]{
                            dateTime
                }
        );
        db.insert(TABLE_TRACKING, null, values);
        db.close();
        c.close();

    }


    public List<TrackingVO> getNoUpdates(){
        ConexionSQLiteHelper c= new ConexionSQLiteHelper(ctx, DATABASE_NAME,null,VERSION_DB );
        SQLiteDatabase db = c.getReadableDatabase();
      //  Log.d(TAG,"INTENTANDO LOGUEO");
        List<TrackingVO> trackingVOList = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery(
                    "SELECT " +
                            "LD."+TABLE_TRACKING_COL_ISUPDATE+", " +//0
                            "LD."+TABLE_TRACKING_COL_LAT+", " +//1
                            "LD."+TABLE_TRACKING_COL_LON+", " +//2
                            "LD."+TABLE_TRACKING_COL_DATETIME+", "+//3
                            "LD."+TABLE_TRACKING_COL_BEARING+", "+//4
                            "LD."+TABLE_TRACKING_COL_SPEED+" "+//5
                            " FROM "+
                            TABLE_TRACKING+" as LD "
                    ,null);
            while (cursor.moveToNext()){
                TrackingVO temp = new TrackingVO();
                temp.setUpdate(cursor.getInt(0)>0);
                temp.setLatitud(cursor.getString(1));
                temp.setLongitud(cursor.getString(2));
                temp.setDateTime(cursor.getString(3));
                temp.setBearing(cursor.getString(4));
                temp.setSpeed(cursor.getString(5));

                trackingVOList.add(temp);
            }
            cursor.close();

        }catch (Exception e){
          //  Log.d(TAG,"getEditing "+e.toString());
            Toast.makeText(ctx,"getNoUpdate-> "+e.toString(),Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            c.close();
        }

        Gson gson = new Gson();
        String trakingJson = gson.toJson(trackingVOList);
        Log.d(TAG,"getNoUpdate-> tracking:"+trakingJson);
        Toast.makeText(ctx,trakingJson,Toast.LENGTH_LONG).show();

        return trackingVOList;
    }


}
