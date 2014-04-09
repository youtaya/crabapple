package com.talk.demo.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
  
public class DBManager {  
    
    private DBHelper helper;  
    private SQLiteDatabase db;  
    private String DATABASE_TABLE = "records";
      
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
        db = helper.getWritableDatabase();  
    }  
      
    /** 
     * add record 
     * @param TimeRecord 
     */  
    public void add(List<TimeRecord> tRecord) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (TimeRecord tr : tRecord) {  
                db.execSQL("INSERT INTO "+DATABASE_TABLE+" VALUES(null, ?, ?)", new Object[]{tr.content, tr.create_time});
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
        db.update(DATABASE_TABLE, cv, "content = ?", new String[]{tRecord.content});  
    }  
      
     
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> query() {  
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        Cursor c = queryTheCursor();  
        if(c!=null) {
            Log.d("db", "have date");
        }
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr._id = c.getInt(c.getColumnIndex("id"));  
            tr.content = c.getString(c.getColumnIndex("content"));  
            tr.create_time = c.getString(c.getColumnIndex("create_time"));  
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }  
      
    /** 
     * query all content, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM "+DATABASE_TABLE, null);  
        return c;  
    }  
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  