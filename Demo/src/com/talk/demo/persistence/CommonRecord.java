package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.util.RawTag;

public abstract class CommonRecord {
    public int _id;
    public int server_id;
    public String handle;
    public long sync_time;
 	/*
 	 * deleted flag :
 	 * default : 0 mean don't delete, other: 1 mean need to delete
 	 */
    public int deleted = 0; 
 	/*
 	 * dirty flag :
 	 * default : 1 mean dirty and need to sync, other: 0 mean not need sync
 	 */
    public int dirty = 1;
    
    public CommonRecord() {
    }
    
    public abstract String getTableName();
    public abstract int getNumItems();
    public abstract void getObjectItems(Object[] obj);
    public abstract void dumpRecord(Cursor c);
    
    public CommonRecord(RawTag rt) {
        _id = (int)rt.getRawTagId();
        server_id = (int)rt.getServerTagId();
        handle = rt.getHandle();
        sync_time = rt.getSyncState();
    }
    public CommonRecord(String v1) {
    	handle = v1;
    }

    public void setHandleName(String v) {
        handle = v;
    }
	    
    public String getHandleName() {
        return handle;
    }
}
