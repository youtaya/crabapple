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
    private String TABLE_TAG = "tags";
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
   
    public void recordExecSQL(TimeRecord tr, boolean isDirty) {
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
    public void add(TimeRecord tr) {  
        db.beginTransaction();  //开始事务  
        try {  
        	recordExecSQL(tr, true);
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }
        rp.addRich(2);
    }  
    public void addFromServer(TimeRecord tr) {
    	Cursor c = queryCursorWithServerId(tr.server_id);
    	if(c != null && c.moveToFirst()) {
    		Log.d(TAG, "No need to creat new record!");
    		updateServerInfo(tr);
    	} else {
	        db.beginTransaction();  //开始事务  
	        try {  
	        	recordExecSQL(tr, false);
	            db.setTransactionSuccessful();  //设置事务成功完成  
	        } finally {  
	            db.endTransaction();    //结束事务  
	        }
    	}
    }  
    
    public void addFriendFromServer(FriendRecord fr) {
        Cursor c = queryCursorWithServerId(fr.server_id);
        if(c != null && c.moveToFirst()) {
            Log.d(TAG, "No need to creat new record!");
            updateFriendServerInfo(fr);
        } else {
            db.beginTransaction();  //开始事务  
            try {  
                friendExecSQL(fr, false);
                db.setTransactionSuccessful();  //设置事务成功完成  
            } finally {  
                db.endTransaction();    //结束事务  
            }
        }
    }  
    
    public void friendExecSQL(FriendRecord fr, boolean isDirty) {
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
    
    public void tagExecSQL(TagRecord tagr, boolean isDirty) {
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
    
    public void addFriend(FriendRecord fr) {  
        db.beginTransaction();  //开始事务  
        try {  
        	friendExecSQL(fr, true);
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }
        
    }
    
    public void addTag(TagRecord tagr) {  
        db.beginTransaction();  //开始事务  
        try {  
        	tagExecSQL(tagr, true);
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
            	recordExecSQL(tr, true);
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
    
    public void updateFriendServerInfo(FriendRecord fRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("server_id", fRecord.server_id); 
        //set dirty flag : 0
        cv.put("dirty", 0);
        cv.put("sync_time", fRecord.sync_time);
        Log.d(TAG,"update id: "+fRecord._id);
        db.update(TABLE_FRIEND, cv, "id" + "='" +fRecord._id+"'", null);
    }  
    
    public void updateFriendInfo(FriendRecord fRecord) {  
        ContentValues cv = new ContentValues();  
        cv.put("phone_mobile", fRecord.phoneMobile);  
        db.update(TABLE_FRIEND, cv, "id" + "='" +fRecord._id+"'", null);
    }  
    
    public void dumpRecord(TimeRecord tr, Cursor c) {
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
    
    public void dumpFriendRecord(FriendRecord fr, Cursor c) {
        fr._id = c.getInt(c.getColumnIndex("id"));
        fr.server_id = c.getInt(c.getColumnIndex("server_id"));
        fr.userName = c.getString(c.getColumnIndex("username"));
        fr.handle = c.getString(c.getColumnIndex("handle"));
        fr.phoneMobile = c.getString(c.getColumnIndex("phone_mobile"));
        fr.avatar = c.getString(c.getColumnIndex("avatar"));
        fr.sync_time = c.getLong(c.getColumnIndex("sync_time"));
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
            dumpRecord(tr, c);
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
            dumpRecord(tr, c);
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }
    
    public List<TimeRecord> queryFromOthers(String param) {  
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        Cursor c = queryCursorFromOthers(param);  
        
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            dumpRecord(tr, c);
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }   
    
    public TimeRecord queryTheParam(int param) {  
        Cursor c = queryCursorWithId(param); 
        
        TimeRecord tr = new TimeRecord();
        if((c != null) && c.moveToFirst()) {
        	dumpRecord(tr, c);
        }
        c.close();  
        return tr;  
    }  
    
    public FriendRecord queryFriendTheParam(int param) {  
        Cursor c = queryFriendCursorWithId(param); 
        
        FriendRecord fr = new FriendRecord();
        if((c != null) && c.moveToFirst()) {
            dumpFriendRecord(fr, c);
        }
        c.close();  
        return fr;  
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
            dumpRecord(tr, c);
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
            fr.avatar = c.getString(c.getColumnIndex("avatar"));
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
    
    public Cursor queryCursorFromOthers(String param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE link!=?"
        		+" ORDER BY calc_date DESC, create_time DESC", new String[]{param,});  
        return c;  
    }
    
    public Cursor queryCursorWithId(int param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE "+"id" + "='" +param+"'", null);
        return c;  
    }
    
    public Cursor queryFriendCursorWithId(int param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_FRIEND
                +" WHERE "+"id" + "='" +param+"'", null);
        return c;  
    }  
    
    public Cursor queryCursorWithServerId(int param) {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE
                +" WHERE "+"server_id" + "='" +param+"'", null);
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
