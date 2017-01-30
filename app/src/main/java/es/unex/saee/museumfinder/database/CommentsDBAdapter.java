package es.unex.saee.museumfinder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Usuario on 30/01/2017.
 */

public class CommentsDBAdapter {

    public static final String C_TABLA = "COMMENTS";

    public static final String C_COLUMNA_ID = "_id";
    public static final String C_COLUMNA_CONTENT = "content";
    public static final String C_COLUMNA_PLACEID = "place_id";
    public static final String C_COLUMNA_PLACENAME = "place_name";

    private Context context;
    private CommentsDBHelper dbHelper;
    private SQLiteDatabase db;

    private String[] columns = new String[]{
            C_COLUMNA_ID, C_COLUMNA_CONTENT, C_COLUMNA_PLACEID, C_COLUMNA_PLACENAME
    };

    public CommentsDBAdapter(Context context){
        this.context = context;
    }

    public CommentsDBAdapter open() throws SQLException{
        dbHelper = new CommentsDBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public Cursor getCursor() throws SQLException{
        Cursor c = db.query(true, C_TABLA, columns, null, null, null, null, null, null);
        return c;
    }

    public Cursor getComment(long id) throws SQLException{
        Cursor c = db.query(true, C_TABLA, columns, C_COLUMNA_ID + "=" + id, null, null, null, null, null);
        if(c!=null)
            c.moveToFirst();
        return c;
    }

    public long insertValues(ContentValues cv){
        if(db == null)
            open();
        return db.insert(C_TABLA, null, cv);
    }

    public long delete(long id){
        if(db == null)
            open();
        return db.delete(C_TABLA, "_id=" + id, null);
    }

    public long update(ContentValues cv){
        long result = 0;

        if(db == null)
            open();

        if(cv.containsKey(C_COLUMNA_ID)){
            long id = cv.getAsLong(C_COLUMNA_ID);
            cv.remove(C_COLUMNA_ID);

            result = db.update(C_TABLA, cv, "_id=" + id, null);
        }
        return result;
    }
}
