package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.util.RawRoom;

public class RoomRecord extends CommonRecord {

    public String username;
    public String last_msg_time;

    public RoomRecord() {
    }
    
    public RoomRecord(RawRoom room) {
    	super(room);
    	username = room.getRoomName();
    	last_msg_time = room.getLastMsgTime();
    }
    
    public RoomRecord(String v1) {
    	username = v1;
       
    }

    public void setUserName(String v) {
    	username = v;
    }
	    
    public String getUserName() {
        return username;
    }

	@Override
	public int getNumItems() {
		return 7;
	}

	@Override
	public void getObjectItems(Object[] obj) {
		obj[0] = super.server_id;
		obj[1] = super.handle;
		obj[2] = username;
		obj[3] = last_msg_time;
		obj[4] = super.sync_time;
		obj[5] = super.dirty;
		obj[6] = super.deleted;
	}

	@Override
	public void dumpRecord(Cursor c) {
    	super._id = c.getInt(c.getColumnIndex("id"));
    	super.server_id = c.getInt(c.getColumnIndex("server_id"));
    	username = c.getString(c.getColumnIndex("username"));
    	last_msg_time = c.getString(c.getColumnIndex("last_msg_time"));
    	super.handle = c.getString(c.getColumnIndex("handle"));
    	super.sync_time = c.getLong(c.getColumnIndex("sync_time"));
		
	}
}
