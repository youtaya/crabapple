package com.talk.demo.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.talk.demo.setting.RichPresent;

import java.util.ArrayList;
import java.util.List;
  
public class DBManager {  
    private static String TAG = "DBManager";
    private DBHelper helper;  
    private SQLiteDatabase db;  
    private String DATABASE_TABLE = "records";
    private String TABLE_FRIEND = "friends";
    // Get rich values
    private RichPresent rp;
    
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        /*
         * 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
         * 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
         */
        db = helper.getWritableDatabase();  
        rp = RichPresent.getInstance(context);
    }  
      
    public void add(TimeRecord tr) {  
        db.beginTransaction();  //开始事务  
        try {  
            db.execSQL("INSERT INTO "+DATABASE_TABLE+""
                    + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                    new Object[]{
                        tr.userName, 
                        tr.server_id,
                        tr.title, 
                        tr.content, 
                        tr.calc_date, 
                        tr.create_time,
                        tr.content_type,
                        tr.photo,
                        tr.audio,
                        tr.status,
                        tr.sync_time,
                        tr.dirty,
                        tr.deleted});
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }
        
        rp.addRich(2);
    }  
    public void addFromServer(TimeRecord tr) {  
        db.beginTransaction();  //开始事务  
        try {  
            db.execSQL("INSERT INTO "+DATABASE_TABLE+""
                    + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                    new Object[]{
                        tr.userName, 
                        tr.server_id,
                        tr.title, 
                        tr.content, 
                        tr.calc_date, 
                        tr.create_time,
                        tr.content_type,
                        tr.photo,
                        tr.audio,
                        tr.status,
                        tr.sync_time,
                        0,//not dirty
                        tr.deleted});
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }
    }  
    
    public void addFriend(FriendRecord fr) {  
        db.beginTransaction();  //开始事务  
        try {  
            db.execSQL("INSERT INTO "+TABLE_FRIEND+""
                    + " VALUES(null, ?, ?, ?, ?, ?, ?, ?)", 
                    new Object[]{
                        fr.server_id,
                        fr.handle,
                        fr.userName,
                        fr.phoneMobile, 
                        fr.sync_time,
                        fr.dirty,
                        fr.deleted});
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }
        
    }  
    
    /** 
     * add record 
     * @param TimeRecord 
     */  
    public void add(List<TimeRecord> tRecord) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (TimeRecord tr : tRecord) {  
                db.execSQL("INSERT INTO "+DATABASE_TABLE+""
                        + " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                        new Object[]{
                            tr.userName, 
                            tr.server_id,
                            tr.title, 
                            tr.content, 
                            tr.calc_date, 
                            tr.create_time,
                            tr.content_type,
                            tr.photo,
                            tr.audio,
                            tr.status,
                            tr.sync_time,
                            tr.dirty,
                            tr.deleted});
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
    
    
    /** 
     * update time record content 
     * @param TimeRecord 
     */  
    public void updateContent(TimeRecord tRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("content", tRecord.content);  
        Log.d(TAG,"update id: "+tRecord._id);
        db.update(DATABASE_TABLE, cv, "id" + "='" +tRecord._id+"'", null);
        rp.addRich(1);
    } 
    
    public void updateServerInfo(TimeRecord tRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("server_id", tRecord.server_id); 
        //set dirty flag : 0
        cv.put("dirty", 0);
        cv.put("sync_time", tRecord.sync_time);
        Log.d(TAG,"update id: "+tRecord._id);
        db.update(DATABASE_TABLE, cv, "id" + "='" +tRecord._id+"'", null);
    }  
    
    public void updateFriendInfo(FriendRecord fRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("phone_mobile", fRecord.phoneMobile);  
        db.update(TABLE_FRIEND, cv, "id" + "='" +fRecord._id+"'", null);
    }  
    
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryWithMultipleParams(String[] params) {  
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        Cursor c = queryCursorWithMultipleParams(params);  
        
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr._id = c.getInt(c.getColumnIndex("id"));  
            tr.content = c.getString(c.getColumnIndex("content"));  
            tr.calc_date = c.getString(c.getColumnIndex("calc_date"));
            tr.create_time = c.getString(c.getColumnIndex("create_time"));  
            tr.content_type = c.getInt(c.getColumnIndex("content_type"));
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }  
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryWithParam(String param) {  
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        Cursor c = queryCursorWithParam(param);  
        
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr._id = c.getInt(c.getColumnIndex("id"));  
            tr.content = c.getString(c.getColumnIndex("content"));  
            tr.calc_date = c.getString(c.getColumnIndex("calc_date"));
            tr.create_time = c.getString(c.getColumnIndex("create_time"));  
            tr.content_type = c.getInt(c.getColumnIndex("content_type"));
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }   
    
    public TimeRecord queryTheParam(int param) {  
        Cursor c = queryCursorWithId(param); 
        
        TimeRecord tr = new TimeRecord();
        if((c != null) && c.moveToFirst()) {
            tr._id = c.getInt(c.getColumnIndex("id"));  
            tr.content = c.getString(c.getColumnIndex("content"));  
            tr.calc_date = c.getString(c.getColumnIndex("calc_date"));
            tr.create_time = c.getString(c.getColumnIndex("create_time"));  
            tr.content_type = c.getInt(c.getColumnIndex("content_type"));
        }
        c.close();  
        return tr;  
    }  
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> query() {  
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        Cursor c = queryTheCursor();  
        
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr._id = c.getInt(c.getColumnIndex("id"));
            tr.content = c.getString(c.getColumnIndex("content"));  
            tr.calc_date = c.getString(c.getColumnIndex("calc_date"));
            tr.create_time = c.getString(c.getColumnIndex("create_time"));  
            tr.content_type = c.getInt(c.getColumnIndex("content_type"));
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }  
    
    public List<FriendRecord> queryFriend() {  
        ArrayList<FriendRecord> frList = new ArrayList<FriendRecord>();  
        Cursor c = queryFriendCursor();  
        
        while (c.moveToNext()) {  
            FriendRecord fr = new FriendRecord();  
            fr._id = c.getInt(c.getColumnIndex("id"));  
            fr.userName = c.getString(c.getColumnIndex("username"));  
            fr.phoneMobile = c.getString(c.getColumnIndex("phone_mobile"));
            frList.add(fr);  
        }  
        c.close();  
        return frList;  
    } 
    /** 
     * query all content, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryCursorWithMultipleParams(String[] params) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
        		+" WHERE calc_date=? OR calc_date=? OR calc_date=? OR calc_date=?"
        		+" ORDER BY calc_date DESC, create_time DESC", params);  
        return c;  
    } 
    /** 
     * query all content, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryCursorWithParam(String param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE calc_date=?"
        		+" ORDER BY calc_date DESC, create_time DESC", new String[]{param,});  
        return c;  
    }  
    
    public Cursor queryCursorWithId(int param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE "+"id" + "='" +param+"'", null);
        return c;  
    }  
    /** 
     * query all content, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
        		+" ORDER BY calc_date DESC, create_time DESC", null);  
        return c;  
    }
    
    public Cursor queryFriendCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_FRIEND
                +" ORDER BY username DESC", null);  
        return c;  
    }  
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  
