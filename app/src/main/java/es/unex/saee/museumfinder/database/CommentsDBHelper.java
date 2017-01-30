package es.unex.saee.museumfinder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Usuario on 30/01/2017.
 */

public class CommentsDBHelper extends SQLiteOpenHelper{

    private static int version = 1;
    private static String name = "CommentsDB";
    private static SQLiteDatabase.CursorFactory factory = null;

    public CommentsDBHelper(Context context){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i("SQLITE", "Creating CommentsDB");

        db.execSQL("CREATE TABLE COMMENTS(" +
            "_id INTEGER PRIMARY KEY," +
            "content TEXT NOT NULL," +
            "place_id TEXT," +
            "place_name TEXT NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
