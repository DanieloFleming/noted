package com.example.fleming.crud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;

/**
 * Created by fleming on 3/23/16.
 */
public abstract class NoteEditor extends AppCompatActivity {

    protected static int CONTENT_VIEW = R.layout.activity_edit_note;
    protected NotesDataSource dataSource;
    protected TextView textFieldId;
    protected EditText textFieldTitle;
    protected EditText textFieldContent;
    protected Note note;
    protected boolean itemDeleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(CONTENT_VIEW);

        note = new Note();
        dataSource = NotesDataSource.getInstance(this);
        textFieldId = (TextView) findViewById(R.id.editId);
        textFieldTitle = (EditText) findViewById(R.id.editTitle);
        textFieldContent = (EditText) findViewById(R.id.editContent);

        setupToolbar();
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                saveNote();
                navigateHome();
                return true;
            case R.id.action_share:
                saveNote();
                shareNote();
                return true;
            case R.id.action_delete:
                deleteNote();
                navigateHome();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void navigateHome() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);

        if(itemDeleted) {
            upIntent.putExtra("DELETED", true);
        }
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder
                    .create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    protected boolean setNoteContent() {
        int noteId = Integer.valueOf(textFieldId.getText().toString().trim());
        String title =  textFieldTitle.getText().toString().trim();
        String content = textFieldContent.getText().toString().trim();

        note.setId(noteId);
        note.setTitle(title);
        note.setContent(content);

        return !(title.isEmpty() && content.isEmpty());

    }

    protected void saveNote() {
        if(setNoteContent()) {
            dataSource.save(note);
        }
    }

    protected void shareNote() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, note.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, note.getContent());

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    protected void deleteNote() {
        setNoteContent();

        if(note.getId() != 0) {
            itemDeleted = true;
            dataSource.delete(note);
        }
    }

    @Override
    protected void onResume() {
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        saveNote();

        super.onBackPressed();
    }
}
