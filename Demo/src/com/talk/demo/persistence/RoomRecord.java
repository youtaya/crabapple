package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.types.WalkRoom;

import java.lang.reflect.Field;

public class RoomRecord extends CommonRecord {
    private WalkRoom room;
    private Field[] declaredFields;
    public RoomRecord() {
    	room = new WalkRoom(); 
    }
    
    public RoomRecord(String v1) {
    	room = new WalkRoom(); 
    	room.setHandle(v1);
    }

	@Override
	public int getNumItems() {
		declaredFields = room.getClass().getDeclaredFields();
		return declaredFields.length-1;
	}

	@Override
	public void getObjectItems(Object[] obj) {
	    room.getObjectItems(obj);
	}

	@Override
	public void dumpRecord(Cursor c) {
	    room.dumpRecord(c);
		
	}
}
