package com.talk.demo.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecordOperations {
    private static final String TAG = "RecordOperations";
    private static final String DATABASE_TABLE = "records";
    
    public static void recordExecSQL(SQLiteDatabase db, TimeRecord tr, boolean isDirty) {
        db.execSQL("INSERT INTO "+DATABASE_TABLE+""
                + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                new Object[]{
                    tr.server_id,
                    tr.userName, 
                    tr.link,
                    tr.title, 
                    tr.content, 
                    tr.calc_date, 
                    tr.create_time,
                    tr.content_type,
                    tr.photo,
                    tr.audio,
                    tr.tag,
                    tr.sync_time,
                    isDirty?tr.dirty:0,
                    tr.deleted});
    }
    
    public static void dumpRecord(TimeRecord tr, Cursor c) {
        tr._id = c.getInt(c.getColumnIndex("id"));
        tr.server_id = c.getInt(c.getColumnIndex("server_id"));
        tr.userName = c.getString(c.getColumnIndex("username"));
        tr.link = c.getString(c.getColumnIndex("link"));
        tr.content = c.getString(c.getColumnIndex("content")); 
        tr.calc_date = c.getString(c.getColumnIndex("calc_date"));
        tr.create_time = c.getString(c.getColumnIndex("create_time"));  
        tr.content_type = c.getInt(c.getColumnIndex("content_type"));
        tr.photo = c.getString(c.getColumnIndex("photo")); 
        tr.audio = c.getString(c.getColumnIndex("audio"));
        tr.tag = c.getString(c.getColumnIndex("tag")); 
        tr.sync_time = c.getLong(c.getColumnIndex("sync_time"));
    }
     
    /** 
     * update time record content 
     * @param TimeRecord 
     */  
    public static void updateContent(SQLiteDatabase db, TimeRecord tRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("content", tRecord.content);  
        Log.d(TAG,"update id: "+tRecord._id);
        db.update(DATABASE_TABLE, cv, "id" + "='" +tRecord._id+"'", null);

    } 
    
    public static void updateServerInfo(SQLiteDatabase db, TimeRecord tRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("server_id", tRecord.server_id); 
        //set dirty flag : 0
        cv.put("dirty", 0);
        cv.put("sync_time", tRecord.sync_time);
        Log.d(TAG,"update id: "+tRecord._id);
        db.update(DATABASE_TABLE, cv, "id" + "='" +tRecord._id+"'", null);
    }
    
    /** 
     * query all content, return cursor 
     * @return  Cursor 
     */  
    public static Cursor queryCursorWithMultipleParams(SQLiteDatabase db,String[] params) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
        		+" WHERE calc_date=? OR calc_date=? OR calc_date=? OR calc_date=?"
        		+" ORDER BY calc_date DESC, create_time DESC", params);  
        return c;  
    } 

    public static Cursor queryCursorWithParam(SQLiteDatabase db,String param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE calc_date=?"
        		+" ORDER BY calc_date DESC, create_time DESC", new String[]{param,});  
        return c;  
    }
    
    public static Cursor queryCursorFromOthers(SQLiteDatabase db,String param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE link!=?"
        		+" ORDER BY calc_date DESC, create_time DESC", new String[]{param,});  
        return c;  
    }
    
    public static Cursor queryCursorWithId(SQLiteDatabase db,int param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE "+"id" + "='" +param+"'", null);
        return c;  
    }
    
    public static Cursor queryCursorWithTag(SQLiteDatabase db,String param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE "+"tag" + "='" +param+"'", null);
        return c;  
    }  
    
    public static Cursor queryTheCursor(SQLiteDatabase db) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
        		+" ORDER BY calc_date DESC, create_time DESC", null);  
        return c;  
    }
    
    
    public static Cursor queryCursorWithServerId(SQLiteDatabase db, int param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE "+"server_id" + "='" +param+"'", null);
        return c;  
    }
    
}
