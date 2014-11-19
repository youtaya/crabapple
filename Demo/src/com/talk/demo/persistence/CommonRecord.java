package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.util.RawData;

public abstract class CommonRecord {
    protected int _id;
    protected int server_id;
    protected String handle;
    protected long sync_time;
 	/*
 	 * deleted flag :
 	 * default : 0 mean don't delete, other: 1 mean need to delete
 	 */
    protected int deleted = 0; 
 	/*
 	 * dirty flag :
 	 * default : 1 mean dirty and need to sync, other: 0 mean not need sync
 	 */
    protected int dirty = 1;
    
    public CommonRecord() {
    }
    
    public static CommonRecord makeRecord(int id) {
    	switch(id) {
    	case 0:
    		return new TimeRecord();
    	case 1:
    		return new FriendRecord();
    	case 2:
    		return new TagRecord();
    	case 3:
    	case 4:
    		break;
    	}
    	
    	return new TimeRecord();
    }
    
    public String getTableName(int id) {
    	switch(id) {
    	case 0:
    		return "records";
    	case 1:
    		return "friends";
    	case 2:
    		return "tags";
    	case 3:
    	case 4:
    		break;
    	}
    	
    	return "";
    }
    public abstract int getNumItems();
    public abstract void getObjectItems(Object[] obj);
    public abstract void dumpRecord(Cursor c);
    
    public CommonRecord(RawData data) {
        _id = (int)data.getDataId();
        server_id = (int)data.getServerId();
        handle = data.getHandle();
        sync_time = data.getSyncState();
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
    
    public void setDirty(int v) {
    	dirty = v;
    }
    
    public int getDirty() {
    	return dirty;
    }
    
}
