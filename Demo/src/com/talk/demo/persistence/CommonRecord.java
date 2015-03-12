package com.talk.demo.persistence;

import android.database.Cursor;

public abstract class CommonRecord {

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
    

    
}
