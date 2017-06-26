package com.example.fleming.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fleming on 3/12/16.
 */
public class NotesDataSource {

    private static NotesDataSource instance;
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE,
            MySQLiteHelper.COLUMN_CONTENT
    };

    public static NotesDataSource getInstance(Context context) {

        if(instance == null) {
            instance = new NotesDataSource(context.getApplicationContext());
        }

        return instance;
    }
    /**
     * Constructor.
     *
     * @param context
     */
    private NotesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    /**
     * Open databaseHelper.
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    /**
     * Close databaseHelper.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Save note.
     *
     * @param note
     * @return (Note)
     */
    public Note save(Note note) {
        int id = note.getId();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TITLE, note.getTitle() );
        values.put(MySQLiteHelper.COLUMN_CONTENT, note.getContent());

        if(id == 0) {
            return this.getNote( this.insertNewNote(values) );
        } else {
            return this.getNote( this.updateNote(values, id) );
        }
    }

    /**
     * Insert new note entry. Returns id of new entry.
     *
     * @param values
     * @return (int)
     */
    private int insertNewNote(ContentValues values) {
        return (int) db.insert(MySQLiteHelper.TABLE_NAME, null, values);
    }

    /**
     * Update note entry. Return id of updated entry.
     *
     * @param values
     * @param id
     * @return
     */
    private int updateNote(ContentValues values, int id) {
        db.update(MySQLiteHelper.TABLE_NAME, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);
        return id;
    }


    /**
     * Get single entry.
     *
     * @param id
     * @return (Note)
     */
    public Note find(int id) {
        return this.getNote(id);
    }

    /**
     * Delete note from db.
     *
     * @param note
     */
    public void delete(Note note) {
        int id = note.getId();

        //Delete query
        db.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Delete note from db.
     *
     * @param id
     */
    public void delete(int id) {

        //Delete query
        db.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public void delete(String[] ids) {
        String args = TextUtils.join(", ", ids);

        db.execSQL(String.format("DELETE FROM " + MySQLiteHelper.TABLE_NAME + " WHERE " + MySQLiteHelper.COLUMN_ID + " IN (%s);", args));
    }
    /**
     * Get all entries.
     *
     * @return (List)
     */
    public List<Note> getAll() {
        List<Note> notes = new ArrayList<Note>();

        //get All entries.
        Cursor cursor = db.query(MySQLiteHelper.TABLE_NAME, allColumns,
                null, null, null, null, null
        );

        cursor.moveToFirst();

        //store per row data in Note. Place note in array
        while ( !cursor.isAfterLast() ) {
            Note note = cursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }

        cursor.close();

        return notes;
    }

    /**
     * Get note by id.
     *
     * @param id
     * @return (Note)
     */
    private Note getNote(int id) {

        //retrieve data from database
        Cursor cursor = db.query(MySQLiteHelper.TABLE_NAME,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id,
                null, null, null, null
        );

        cursor.moveToFirst();

        //store data into new Note.
        Note note = cursorToNote(cursor);

        cursor.close();

        return note;
    }

    /**
     * Retrieve values from cursor and place it in Note object
     *
     * @param cursor
     * @return (Note)
     */
    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getInt(0));
        note.setTitle(cursor.getString(1));
        note.setContent(cursor.getString(2));

        return note;
    }
}
