
package com.talk.demo.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    /**
     * Version constant to increment when the database should be rebuilt
     */
    private static final int VERSION = 6;

    /**
     * Name of database file
     */
    private static final String NAME = "test.db";
    private final String TABLE_RECORD = "records";
    private final String TABLE_FRIEND = "friends";
    private final String TABLE_ROOM = "rooms";
    private final String TABLE_DIALOG = "dialogs";
    private final String TABLE_TAG = "tags";

    /**
     * @param context
     */
    public DBHelper(final Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORD
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " server_id INTEGER,"
                + " handle TEXT,"
                + " link TEXT,"
                + " title TEXT default '',"
                + " content TEXT default '',"
                + " calc_date TEXT,"
                + " create_time TEXT,"
                + " send_interval_time INTEGER,"
                + " send_done_time TEXT,"
                + " content_type INTEGER default 0,"
                + " photo TEXT default '',"
                + " audio TEXT default '',"
                + " tag TEXT default '',"
                + " sync_time INTEGER,"
                + " dirty INTEGER,"
                + " deleted INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_FRIEND
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " server_id INTEGER,"
                + " handle TEXT,"
                + " username TEXT,"
                + " phone_mobile TEXT,"
                + " avatar TEXT,"
                + " description TEXT,"
                + " sync_time TEXT,"
                + " dirty INTEGER,"
                + " deleted INTEGER);");
        
        db.execSQL("CREATE TABLE " + TABLE_ROOM
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " server_id INTEGER,"
                + " handle TEXT,"
                + " username TEXT,"
                + " last_msg_time TEXT,"
                + " sync_time TEXT,"
                + " dirty INTEGER,"
                + " deleted INTEGER);");
        
        db.execSQL("CREATE TABLE " + TABLE_DIALOG
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " server_id INTEGER,"
                + " handle TEXT,"
                + " roomname TEXT,"
                + " sender TEXT,"
                + " link TEXT,"
                + " direct INTEGER default 0,"
                + " content TEXT default '',"
                + " calc_date TEXT,"
                + " create_time TEXT,"
                + " send_interval_time INTEGER,"
                + " send_done_time TEXT,"
                + " content_type INTEGER default 0,"
                + " photo TEXT default '',"
                + " audio TEXT default '',"
                + " sync_time INTEGER,"
                + " dirty INTEGER,"
                + " deleted INTEGER);");      
        
        db.execSQL("CREATE TABLE " + TABLE_TAG
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " server_id INTEGER,"
                + " handle TEXT,"
                + " tagname TEXT,"
                + " sync_time TEXT,"
                + " dirty INTEGER,"
                + " deleted INTEGER);");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RECORD);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ROOM);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DIALOG);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TAG);
        onCreate(db);
    }
}
