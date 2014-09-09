package com.talk.demo.persistence;

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
   
    public void add(TimeRecord tr) {  
        db.beginTransaction();  //开始事务  
        try {  
            RecordOperations.recordExecSQL(db, tr, true);
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
    		RecordOperations.updateServerInfo(db, tr);
    	} else {
	        db.beginTransaction();  //开始事务  
	        try {  
	            RecordOperations.recordExecSQL(db, tr, false);
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
            FriendOperations.updateFriendServerInfo(db, fr);
        } else {
            db.beginTransaction();  //开始事务  
            try {  
                FriendOperations.friendExecSQL(db, fr, false);
                db.setTransactionSuccessful();  //设置事务成功完成  
            } finally {  
                db.endTransaction();    //结束事务  
            }
        }
    }  

    
    public void addFriend(FriendRecord fr) {  
        db.beginTransaction();  //开始事务  
        try {  
            FriendOperations.friendExecSQL(db, fr, true);
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }
        
    }
    
    
    public void addTag(TagRecord tagr) {  
        db.beginTransaction();  //开始事务  
        try {  
            TagOperations.tagExecSQL(db, tagr, true);
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
                RecordOperations.recordExecSQL(db, tr, true);
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
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
            RecordOperations.dumpRecord(tr, c);
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
            RecordOperations.dumpRecord(tr, c);
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
            RecordOperations.dumpRecord(tr, c);
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }   
    
    public TimeRecord queryTheParam(int param) {  
        Cursor c = queryCursorWithId(param); 
        
        TimeRecord tr = new TimeRecord();
        if((c != null) && c.moveToFirst()) {
            RecordOperations.dumpRecord(tr, c);
        }
        c.close();  
        return tr;  
    }  
    
    public FriendRecord queryFriendTheParam(int param) {  
        Cursor c = queryFriendCursorWithId(param); 
        
        FriendRecord fr = new FriendRecord();
        if((c != null) && c.moveToFirst()) {
            FriendOperations.dumpFriendRecord(fr, c);
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
            RecordOperations.dumpRecord(tr, c);
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
