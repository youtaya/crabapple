package com.talk.demo.persistence;

import android.database.sqlite.SQLiteDatabase;

public class TagOperations {
    private static final String TAG = "TagOperations";
    private static final String TABLE_TAG = "tags";
    
    public static void tagExecSQL(SQLiteDatabase db, TagRecord tagr, boolean isDirty) {
        db.execSQL("INSERT INTO "+TABLE_TAG+""
                + " VALUES(null, ?, ?, ?, ?, ?, ?)", 
                new Object[]{
                    tagr.server_id,
                    tagr.handle,
                    tagr.tagName,
                    tagr.sync_time,
                    isDirty?tagr.dirty:0,
                    tagr.deleted});
    }
}
