package com.talk.demo.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataOperation {
    private static final String TAG = "DataOperation";
    private CommonRecord commonRec;
    private SQLiteDatabase innerDB;
    private Cursor innerCursor;
    
    public DataOperation(SQLiteDatabase db, CommonRecord record) {
        innerDB = db;
        commonRec = record;
    }
    
    public DataOperation(CommonRecord record, Cursor cursor) {
        commonRec = record;
        innerCursor = cursor;
    }
    
    public void insertRecord() {
        String table_name = commonRec.getTableName();
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
    }
    

    public void dumpRecord() {
        commonRec.dumpRecord(innerCursor);
    }

    public Cursor queryCursorByName() {
        String table_name = commonRec.getTableName();
        StringBuilder sqlValue = new StringBuilder(" ORDER BY ");
        sqlValue.append(commonRec.getHandleName());
        sqlValue.append(" DESC");
        Cursor c = innerDB.rawQuery("SELECT * FROM "+table_name
                +sqlValue.toString(), null);
        return c;  
    }
}