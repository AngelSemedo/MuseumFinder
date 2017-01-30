package es.unex.saee.museumfinder.activity;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import es.unex.saee.museumfinder.R;
import es.unex.saee.museumfinder.database.CommentsCursorAdapter;
import es.unex.saee.museumfinder.database.CommentsDBAdapter;

public class CommentsActivity extends ListActivity{

    public static final String MODE = "mode";
    public static final int PICK = 551; // Consultar comentarios
    public static final int NEW = 552; // Nuevo comentario
    public static final int EDIT = 553; // Editar comentario

    private CommentsDBAdapter dbAdapter;
    private Cursor cursor;
    private CommentsCursorAdapter commentsAdapter;
    private ListView lista;

    private Button newComment;

    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        this.setTitle("My Comments");

        lista = (ListView) findViewById(android.R.id.list);
        newComment = (Button) findViewById(R.id.btn_new_comment);
        newComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentsActivity.this, CommentsFormActivity.class);
                intent.putExtra(MODE, NEW);
                startActivityForResult(intent, NEW);
            }
        });

        dbAdapter = new CommentsDBAdapter(this);
        dbAdapter.open();

        getComments();
    }

    private void getComments(){
        cursor = dbAdapter.getCursor();
        startManagingCursor(cursor);
        commentsAdapter = new CommentsCursorAdapter(this, cursor);
        lista.setAdapter(commentsAdapter);
    }

    private void selectComment(long id){
        Intent intent = new Intent(CommentsActivity.this, CommentsFormActivity.class);
        intent.putExtra(MODE, PICK);
        intent.putExtra(CommentsDBAdapter.C_COLUMNA_ID, id);

        startActivityForResult(intent, PICK);
    }

    protected void onListItemClick(ListView list, View v, int pos, long id){
        super.onListItemClick(list, v, pos, id);
        selectComment(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_comments, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item){
        Intent intent;
        switch(item.getItemId()){
            case R.id.menu_create:
                intent = new Intent(CommentsActivity.this, CommentsFormActivity.class);
                intent.putExtra(MODE, NEW);
                startActivityForResult(intent, NEW);
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case NEW:
                if(resultCode == RESULT_OK)
                    getComments();
            case PICK:
                if(resultCode == RESULT_OK)
                    getComments();
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
