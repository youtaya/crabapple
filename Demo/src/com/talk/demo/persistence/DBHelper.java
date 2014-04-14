
package com.talk.demo.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    /**
     * Version constant to increment when the database should be rebuilt
     */
    private static final int VERSION = 2;

    /**
     * Name of database file
     */
    private static final String NAME = "test.db";

    /**
     * @param context
     */
    public DBHelper(final Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE records (id INTEGER PRIMARY KEY, "
                + "content TEXT, create_date TEXT, create_time TEXT, "
                + "media_type INTEGER);");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS records");
        onCreate(db);
    }
}
