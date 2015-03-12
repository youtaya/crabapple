package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.types.Friend;
import com.talk.demo.types.WalkRoom;
import com.talk.demo.util.RawRoom;

import java.lang.reflect.Field;

public class RoomRecord extends CommonRecord {
    private WalkRoom room;
    private Field[] declaredFields;
    public RoomRecord() {
        room = new WalkRoom();     
        
        Class c = room.getClass();     
         
        declaredFields = c.getDeclaredFields();
    }
    public void setHandleName(String v) {
        room.setHandle(v);
    }
        
    public String getHandleName() {
        return room.getHandle();
    }
    
    public void setDirty(int v) {
        room.setDirty(v);
    }
    
    public int getDirty() {
        return room.getDirty();
    }
    
    public RoomRecord(String v1) {
        setHandleName(v1);
       
    }

    public void setUserName(String v) {
    	room.setRoomName(v);
    }
	    
    public String getUserName() {
        return room.getRoomName();
    }

	@Override
	public int getNumItems() {
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
