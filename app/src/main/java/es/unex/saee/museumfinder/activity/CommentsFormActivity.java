package es.unex.saee.museumfinder.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Comment;

import es.unex.saee.museumfinder.R;
import es.unex.saee.museumfinder.database.CommentsDBAdapter;

public class CommentsFormActivity extends AppCompatActivity {

    private CommentsDBAdapter dbAdapter;
    private Cursor cursor;

    private int mode;

    private long id;
    private EditText content;
    private EditText place_name;

    private Button btn_save;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_form);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras == null) return;

        content = (EditText) findViewById(R.id.content);
        place_name = (EditText) findViewById(R.id.place_name);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        dbAdapter = new CommentsDBAdapter(this);
        dbAdapter.open();

        if(extras.containsKey(CommentsDBAdapter.C_COLUMNA_ID)){
            id = extras.getLong(CommentsDBAdapter.C_COLUMNA_ID);
            pickComment(id);
        }

        setMode(extras.getInt(CommentsActivity.MODE));

        // Button actions
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.clear();
        if(mode == CommentsActivity.PICK)
            getMenuInflater().inflate(R.menu.comment_pick, menu);
        else
            getMenuInflater().inflate(R.menu.comment_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_delete_comment:
                delete(id);
                return true;
            case R.id.menu_cancel_comment:
                cancel();
                return true;
            case R.id.menu_save_comment:
                save();
                return true;
            case R.id.menu_edit_comment:
                setMode(CommentsActivity.EDIT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pickComment(long id){
        cursor = dbAdapter.getComment(id);

        content.setText(cursor.getString(cursor.getColumnIndex(CommentsDBAdapter.C_COLUMNA_CONTENT)));
        place_name.setText(cursor.getString(cursor.getColumnIndex(CommentsDBAdapter.C_COLUMNA_PLACENAME)));
    }

    private void setMode(int m){
        this.mode = m;

        if (mode == CommentsActivity.PICK){
            this.setTitle("Comment");
            this.setEditable(false);
        } else if (mode == CommentsActivity.NEW){
            this.setTitle("New comment");
            this.setEditable(true);
        } else if (mode == CommentsActivity.EDIT){
            this.setTitle("Edit comment");
            this.setEditable(true);
        }
    }

    private void setEditable(boolean option){
        content.setEnabled(option);
        place_name.setEnabled(option);
    }

    private void save(){
        ContentValues cv = new ContentValues();

        if(mode == CommentsActivity.EDIT)
            cv.put(CommentsDBAdapter.C_COLUMNA_ID, id);

        cv.put(CommentsDBAdapter.C_COLUMNA_CONTENT, content.getText().toString());
        cv.put(CommentsDBAdapter.C_COLUMNA_PLACENAME, place_name.getText().toString());

        if(mode == CommentsActivity.NEW){
            dbAdapter.insertValues(cv);
            Toast.makeText(CommentsFormActivity.this, "Comment added!", Toast.LENGTH_SHORT).show();
        } else if (mode == CommentsActivity.EDIT){
            Toast.makeText(CommentsFormActivity.this, "Comment modified!", Toast.LENGTH_SHORT).show();
            dbAdapter.update(cv);
        }

        setResult(RESULT_OK);
        finish();
    }

    private void cancel(){
        setResult(RESULT_CANCELED, null);
        finish();
    }

    private void delete(final long id){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle("Delete comment");
        dialogDelete.setMessage("Push to continue");
        dialogDelete.setCancelable(false);

        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbAdapter.delete(id);
                Toast.makeText(CommentsFormActivity.this, "Comment deleted!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        dialogDelete.setNegativeButton("NO", null);
        dialogDelete.show();
    }


}
