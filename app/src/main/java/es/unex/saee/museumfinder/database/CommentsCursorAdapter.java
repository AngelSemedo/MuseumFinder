package es.unex.saee.museumfinder.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Usuario on 30/01/2017.
 */

public class CommentsCursorAdapter extends CursorAdapter {

    private CommentsDBAdapter dbAdapter = null;

    public CommentsCursorAdapter(Context context, Cursor c){
        super(context, c);
        dbAdapter = new CommentsDBAdapter(context);
        dbAdapter.open();
    }

    public void bindView(View view, Context context, Cursor cursor){

        TextView tv = (TextView) view;
        tv.setText(cursor.getString(cursor.getColumnIndex(CommentsDBAdapter.C_COLUMNA_PLACENAME)));
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        return view;
    }
}
