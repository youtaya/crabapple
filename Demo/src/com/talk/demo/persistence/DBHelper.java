
package com.talk.demo.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    /**
     * Version constant to increment when the database should be rebuilt
     */
    private static final int VERSION = 3;

    /**
     * Name of database file
     */
    private static final String NAME = "test.db";
    private final String TABLE_RECORD = "records";
    private final String TABLE_FRIEND = "friends";

    /**
     * @param context
     */
    public DBHelper(final Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORD
                + " (id INTEGER PRIMARY KEY,"
                + " username TEXT,"
                + " title TEXT default '',"
                + " content TEXT default '',"
                + " create_date TEXT,"
                + " create_time TEXT,"
                + " content_type INTEGER default 0,"
                + " photo TEXT default '',"
                + " audio TEXT default '',"
                + " status TEXT default '',"
                + " deleted INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_FRIEND
                + " (id INTEGER PRIMARY KEY,"
                + " username TEXT,"
                + " phone_mobile TEXT,"
                + " deleted INTEGER);");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RECORD);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_FRIEND);
        onCreate(db);
    }
}
