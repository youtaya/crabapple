package com.talk.demo.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FriendOperations {
    private static final String TAG = "FriendOperations";
    private static final String TABLE_FRIEND = "friends";
    public static void friendExecSQL(SQLiteDatabase db, FriendRecord fr, boolean isDirty) {
        db.execSQL("INSERT INTO "+TABLE_FRIEND+""
                + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?)", 
                new Object[]{
                    fr.server_id,
                    fr.handle,
                    fr.userName,
                    fr.phoneMobile,
                    fr.avatar,
                    fr.sync_time,
                    isDirty?fr.dirty:0,
                    fr.deleted});
    }
    
    public static void dumpFriendRecord(FriendRecord fr, Cursor c) {
        fr._id = c.getInt(c.getColumnIndex("id"));
        fr.server_id = c.getInt(c.getColumnIndex("server_id"));
        fr.userName = c.getString(c.getColumnIndex("username"));
        fr.handle = c.getString(c.getColumnIndex("handle"));
        fr.phoneMobile = c.getString(c.getColumnIndex("phone_mobile"));
        fr.avatar = c.getString(c.getColumnIndex("avatar"));
        fr.sync_time = c.getLong(c.getColumnIndex("sync_time"));
    }
    
    public static void updateFriendServerInfo(SQLiteDatabase db, FriendRecord fRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("server_id", fRecord.server_id); 
        //set dirty flag : 0
        cv.put("dirty", 0);
        cv.put("sync_time", fRecord.sync_time);
        Log.d(TAG,"update id: "+fRecord._id);
        db.update(TABLE_FRIEND, cv, "id" + "='" +fRecord._id+"'", null);
    }  
    
    public static void updateFriendInfo(SQLiteDatabase db, FriendRecord fRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("phone_mobile", fRecord.phoneMobile);  
        db.update(TABLE_FRIEND, cv, "id" + "='" +fRecord._id+"'", null);
    } 
}
