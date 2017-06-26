package com.example.fleming.crud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fleming on 3/12/16.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    /**
     * Database
     */
    public static final String DATABASE_NAME = "notes.db";
    public static final int DATABASE_VERSION = 1;

    /**
     * Table + column names
     */
    public static final String TABLE_NAME = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";

    /**
     * create Query
     */
    public static final String CREATE_QUERY = "create table " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_TITLE + " text,"
            + COLUMN_CONTENT + " text not null);";

    /**
     * Constructor.
     *
     * @param context
     */
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create database.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    /**
     * upgrade database.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(
                MySQLiteHelper.class.getName(),
                "database upgrade from " + oldVersion + " to " + newVersion + "destroying al content"
        );

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
