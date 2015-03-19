package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.types.Friend;

import java.lang.reflect.Field;

public class FriendRecord extends CommonRecord {
    private Friend friend;
    private Field[] declaredFields;
    
    public FriendRecord() {
        init();
    }
    
    private void init() {
        friend = new Friend();     
        
        Class c = friend.getClass();     
         
        declaredFields = c.getDeclaredFields();
    }
    
    public Friend getFriend() {
    	return friend;
    }
    
    public FriendRecord(Friend f) {
        init();
    	friend = f;
    }
  
    public FriendRecord(String v1) {
    	init();
        friend.setUsername(v1);
       
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
