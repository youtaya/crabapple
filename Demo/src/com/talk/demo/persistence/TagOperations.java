package com.talk.demo.persistence;

import android.database.Cursor;
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
    
    public static void dumpTagRecord(TagRecord tagr, Cursor c) {
    	tagr._id = c.getInt(c.getColumnIndex("id"));
    	tagr.server_id = c.getInt(c.getColumnIndex("server_id"));
    	tagr.tagName = c.getString(c.getColumnIndex("tagname"));
    	tagr.handle = c.getString(c.getColumnIndex("handle"));
    	tagr.sync_time = c.getLong(c.getColumnIndex("sync_time"));
    }
    
    public static Cursor queryTagCursor(SQLiteDatabase db) {  
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_TAG
                +" ORDER BY tagname DESC", null);  
        return c;  
    }
}
