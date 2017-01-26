package es.unex.saee.museumfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Usuario on 25/01/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favPlaces.db";
    public static final String PLACES_TABLE_NAME = "myPlaces";
    public static final String PLACES_COLUMN_ID = "id";
    public static final String PLACES_COLUMN_NAME = "name";
    public static final String PLACES_COLUMN_ADDRESS = "address";
    public static final String PLACES_COLUMN_LONG = "longitude";
    public static final String PLACES_COLUMN_LAT = "latitude";
    public static final String PLACES_COLUMN_PLACEID = "placeID";
    private HashMap hm;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table myPlaces " +
                "(id integer primary key, name text, address text, longitude test, latitude text, placeID text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS myPlaces");
        onCreate(db);
    }

    public boolean insertPlace (String name, String address, String latitude, String longitude, String placeid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("placeid", placeid);
        db.insert("myPlaces", null, values);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from myPlaces where id=" + id + "", null);
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = (int) DatabaseUtils.queryNumEntries(db, PLACES_TABLE_NAME);
        return rows;
    }

    public boolean updatePlace(Integer id, String name, String address, String latitude, String longitude, String placeid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("placeid", placeid);
        db.update("myPlaces", values, "id = ?", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deletePlace(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("myPlaces", "id = ?", new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllPlaces(){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from myPlaces", null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(PLACES_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

}
