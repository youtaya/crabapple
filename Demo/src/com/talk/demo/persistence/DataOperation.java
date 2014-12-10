package com.talk.demo.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Map;

public class DataOperation {
    private static final String TAG = "DataOperation";
    private CommonRecord commonRec;
    private SQLiteDatabase innerDB;
    private Cursor innerCursor;
    private String table_name;
   
    public DataOperation(SQLiteDatabase db, String tab_name) {
        innerDB = db;
        table_name = tab_name;
    }
    
    public DataOperation(SQLiteDatabase db, String tab_name, CommonRecord record) {
        innerDB = db;
        table_name = tab_name;
        commonRec = record;
    }
    
    public DataOperation(CommonRecord record, Cursor cursor) {
        commonRec = record;
        innerCursor = cursor;
    }
    
    public void insertRecord() {
    	
    	innerDB.beginTransaction();  //开始事务  
        try {  
            int num_items = commonRec.getNumItems();
            
            StringBuilder sqlValue = new StringBuilder(" VALUES(null");
            for(int i=0; i<num_items;i++) {
                sqlValue.append(" ,?");
            }
            sqlValue.append(")");
            
            Object[] obj_items = new Object[num_items];
            
            commonRec.getObjectItems(obj_items);
          
            innerDB.execSQL("INSERT INTO "+table_name+""
                    + sqlValue.toString(), obj_items);
            
            innerDB.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
        	innerDB.endTransaction();    //结束事务  
        }

    }
    
    public void dumpRecord() {
        commonRec.dumpRecord(innerCursor);
    }

    public Cursor queryCursorByName(String[] sortVar) {
        StringBuilder sqlValue = new StringBuilder(" ORDER BY ");
        for(int i=0;i<sortVar.length;i++) {
        	sqlValue.append(sortVar[i]);
        	sqlValue.append(" DESC");
        	if(i != sortVar.length -1)
        		sqlValue.append(",");
        }
        
        Cursor c = innerDB.rawQuery("SELECT * FROM "+table_name
                +sqlValue.toString(), null);
        return c;  
    }
    
    public Cursor queryCursorWithCond(Map<String, Object> queryVar) {
        StringBuilder sqlValue = new StringBuilder(" WHERE ");
        for(String key: queryVar.keySet())
        	sqlValue.append(key + "='" +queryVar.get(key)+"'");
        
        Cursor c = innerDB.rawQuery("SELECT * FROM "+table_name
                +sqlValue.toString()
                +" ORDER BY calc_date DESC, create_time DESC", null);
        return c;  
    }
    
    public Cursor queryCursorWithComplexCond(String[] whereVar, String[] sortVar, String[] params) {
        StringBuilder sqlValue = new StringBuilder(" WHERE ");
        
        for(int i=0;i<whereVar.length;i++) {
        	sqlValue.append(whereVar[i]);
        	sqlValue.append("=?");
        	
        	if(i != whereVar.length -1)
        		sqlValue.append(" OR ");
        	
        }
        
        sqlValue.append(" ORDER BY ");
        for(int i=0;i<sortVar.length;i++) {
        	sqlValue.append(sortVar[i]);
        	sqlValue.append(" DESC");
        	if(i != sortVar.length -1)
        		sqlValue.append(",");
        }
        
        Cursor c = innerDB.rawQuery("SELECT * FROM "+table_name
        		+sqlValue.toString(), params);  
        return c;  
    }
    
    public void updateServerId(CommonRecord record) {  
        ContentValues cv = new ContentValues();  
        cv.put("server_id", record.server_id); 
        //set dirty flag : 0
        cv.put("dirty", 0);
        cv.put("sync_time", record.sync_time);
        Log.d(TAG,"update id: "+record._id);
        innerDB.update(table_name, cv, "id" + "='" +record._id+"'", null);
    }
    
    public void updateTag(int id, String tag) {  
        ContentValues cv = new ContentValues();  
        cv.put("tag", tag);  
        Log.d(TAG,"update id: "+id);
        innerDB.update(table_name, cv, "id" + "='" +id+"'", null);
    } 
    
    public void updateDescription(int id, String des) {  
        ContentValues cv = new ContentValues();  
        cv.put("description", des);  
        Log.d(TAG,"update description: "+des);
        innerDB.update(table_name, cv, "id" + "='" +id+"'", null);
    } 
    
    public void updateContent(CommonRecord record, String content) {
        ContentValues cv = new ContentValues();  
        cv.put("content", content);  
        Log.d(TAG,"update id: "+record._id);
        innerDB.update(table_name, cv, "id" + "='" +record._id+"'", null);
    }
}
