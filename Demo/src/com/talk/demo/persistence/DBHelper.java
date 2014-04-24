
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
    private final String DATABASE_TABLE = "records";

    /**
     * @param context
     */
    public DBHelper(final Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_TABLE
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
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(db);
    }
}
