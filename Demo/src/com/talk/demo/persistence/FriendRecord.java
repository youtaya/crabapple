package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.types.Friend;

import java.lang.reflect.Field;

public class FriendRecord extends CommonRecord {
    private Friend friend;
    private Field[] declaredFields;
    
    public FriendRecord() {
        friend = new Friend();     
        
        Class c = friend.getClass();     
         
        declaredFields = c.getDeclaredFields();
    }
    
    public void setHandleName(String v) {
        friend.setHandle(v);;
    }
        
    public String getHandleName() {
        return friend.getHandle();
    }
    
    public void setDirty(int v) {
        friend.setDirty(v);
    }
    
    public int getDirty() {
        return friend.getDirty();
    }

    public FriendRecord(String v1) {
        setUserName(v1);
       
    }

    public void setUserName(String v) {
        friend.setUsername(v);
    }
	    
    public String getUserName() {
        return friend.getUserName();
    }

	@Override
	public int getNumItems() {
	    return declaredFields.length-1;
	}

	@Override
	public void getObjectItems(Object[] obj) {
	    friend.getObjectItems(obj);
	}

	@Override
	public void dumpRecord(Cursor c) {
	    friend.dumpRecord(c);
		
	}
}
