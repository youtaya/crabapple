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
    	Cursor c = RecordOperations.queryCursorWithServerId(db, tr.server_id);
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
        Cursor c = FriendOperations.queryCursorWithServerId(db, fr.server_id);
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
    
    public void updateContent(TimeRecord tRecord) {
    	RecordOperations.updateContent(db, tRecord);
        rp.addRich(1);
    }
  
    public void updateTag(int id, String tag) {
        RecordOperations.updateTag(db, id, tag);
    }
    
    public void updateServerInfo(TimeRecord tRecord) {
    	RecordOperations.updateServerInfo(db, tRecord);
    }
    
    public void  updateFriendServerInfo(FriendRecord fRecord) {
    	FriendOperations.updateFriendServerInfo(db, fRecord);
    }
    
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryWithMultipleParams(String[] params) {  
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        Cursor c = RecordOperations.queryCursorWithMultipleParams(db, params);  
        
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
        Cursor c = RecordOperations.queryCursorWithParam(db, param);  
        
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
        Cursor c = RecordOperations.queryCursorFromOthers(db, param);  
        
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            RecordOperations.dumpRecord(tr, c);
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }   
    
    public TimeRecord queryTheParam(int param) {  
        Cursor c = RecordOperations.queryCursorWithId(db, param); 
        
        TimeRecord tr = new TimeRecord();
        if((c != null) && c.moveToFirst()) {
            RecordOperations.dumpRecord(tr, c);
        }
        c.close();  
        return tr;  
    }
    
    public List<TimeRecord> queryTag(String param) {
    	ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        Cursor c = RecordOperations.queryCursorWithTag(db, param); 
        
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            RecordOperations.dumpRecord(tr, c);
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }
    
    public FriendRecord queryFriendTheParam(int param) {  
        Cursor c = FriendOperations.queryFriendCursorWithId(db, param); 
        
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
        Cursor c = RecordOperations.queryTheCursor(db);  
        
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
        Cursor c = FriendOperations.queryFriendCursor(db);  
        
        while (c.moveToNext()) {  
            FriendRecord fr = new FriendRecord();  
            FriendOperations.dumpFriendRecord(fr, c);
            frList.add(fr);  
        }  
        c.close();  
        return frList;  
    } 

    public List<TagRecord> queryTag() {  
        ArrayList<TagRecord> tagrList = new ArrayList<TagRecord>();  
        Cursor c = TagOperations.queryTagCursor(db);  
        
        while (c.moveToNext()) {  
        	TagRecord tagr = new TagRecord();  
            TagOperations.dumpTagRecord(tagr, c);
            tagrList.add(tagr);  
        }  
        c.close();  
        return tagrList;  
    } 
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  
