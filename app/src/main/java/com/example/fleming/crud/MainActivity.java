package com.example.fleming.crud;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteListAdapter noteAdapter;
    private NotesDataSource dataSource;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private LinearLayout placeholder;
    private ListView noteListView;
    private Toolbar myToolbar;
    private int selectedNoteItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        placeholder = (LinearLayout) findViewById(R.id.empty_placeholder);

        setSupportActionBar(myToolbar);

        dataSource = NotesDataSource.getInstance(this);

        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createList();
        checkListCount();

        setEventListeners();

        Intent intent = getIntent();

        boolean noteDeleted = intent.getBooleanExtra("DELETED", false);

        if(noteDeleted) {
            showSnackBar();
        }

    }

    private void createList() {
        final List<Note> noteList = dataSource.getAll();

        noteAdapter = new NoteListAdapter(this, R.layout.activity_main, noteList);
        noteListView = (ListView) findViewById(R.id.noteListView);

        noteListView.setAdapter(noteAdapter);
        noteListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    private void setEventListeners() {
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showNoteDetailView(noteAdapter.getNote(position));
            }
        });

        placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });

        noteListView.setMultiChoiceModeListener(multiChoiceModeListener);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });
    }

    private AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {

        MenuItem shareBtn;
        MenuItem editBtn;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);

            shareBtn = menu.findItem(R.id.action_share);
            editBtn = menu.findItem(R.id.action_edit);
            return true;
        }

        @Override
        public void onItemCheckedStateChanged(
                ActionMode mode, int position, long id, boolean checked) {

            noteAdapter.toggleSelection(position);
            final int checkedCount = noteAdapter.getSelectedItemCount();
            //mode.setTitle(checkedCount + " Selected");

            if (checkedCount > 1 || checkedCount < 1) {
                showSingleActionMenuItems(false);
            } else {
                selectedNoteItem = noteAdapter.getSelectedItemsIds().keyAt(0);
                showSingleActionMenuItems(true);
            }
        }

        private void showSingleActionMenuItems(boolean value) {
            shareBtn.setVisible(value);
            editBtn.setVisible(value);
        }


        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_delete:

                    deleteNotes();
                    mode.finish();
                    return true;
                case R.id.action_edit:
                    showNoteDetailView(noteAdapter.getNote(selectedNoteItem));
                    mode.finish();
                    break;
                case R.id.action_share:
                    shareNote();
                    mode.finish();
                    break;
                default:
                    return false;
            }

            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            noteAdapter.removeSelection();
        }
    };

    private void showNoteDetailView(Note note) {
        Intent intent = new Intent(MainActivity.this, UpdateNoteActivity.class);
        intent.putExtra("id", Integer.toString(note.getId()));
        intent.putExtra("title", note.getTitle());
        intent.putExtra("content", note.getContent());

        startActivity(intent);
    }

    private void createNewNote() {
        Intent intent = new Intent(this, CreateNoteActivity.class);

        startActivity(intent);
    }

    private void deleteNote() {
        Note note = noteAdapter.getNoteList().get(selectedNoteItem);

        dataSource.delete(note);
        noteAdapter.remove(note);
        noteAdapter.notifyDataSetChanged();

        showSnackBar();
    }

    private void deleteNotes() {
        SparseBooleanArray selectedIds = noteAdapter.getSelectedItemsIds();

        int arraySize = selectedIds.size();
        String[] removeableIds = new String[arraySize];

        //must be reversed...
        for(int i = (arraySize - 1); i >= 0; i--) {
            if(selectedIds.valueAt(i)) {
                int key = selectedIds.keyAt(i);

                removeableIds[i] = String.valueOf(noteAdapter.getNote(key).getId());
                noteAdapter.remove(noteAdapter.getNote(key));
            }
        }

        dataSource.delete(removeableIds);
        noteAdapter.notifyDataSetChanged();
        showSnackBar(arraySize);

    }

    private void checkListCount() {
        if(noteAdapter.getNoteList().size() == 0) {
            placeholder.setVisibility(View.VISIBLE);
        } else {
            placeholder.setVisibility(View.GONE);
        }
    }

    private void showSnackBar() {
        showSnackBarMessage(0);
    }

    private void showSnackBar(int amount) {
        showSnackBarMessage(amount);
    }

    private void showSnackBarMessage(int amount) {
        String message;

        if(amount > 1) {
            message = String.valueOf(amount) + " Notes deleted.";
        } else {
            message = "Note deleted.";
        }

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();

        checkListCount();
    }

    private void shareNote() {
        Note note = noteAdapter.getNote(selectedNoteItem);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, note.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, note.getContent());

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    @Override
    protected void onResume() {
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //createList();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
