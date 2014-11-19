package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.util.RawFriend;

public class FriendRecord extends CommonRecord {
    public String userName;
    public String phoneMobile;
    public String avatar;
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
    
    public FriendRecord() {
    }
    
    public FriendRecord(RawFriend rf) {
    	super(rf);
        userName = rf.getUserName();
        phoneMobile = rf.getPhoneMobile();
        avatar = rf.getAvatar();
    }
    public FriendRecord(String v1) {
        userName = v1;
       
    }

    public void setUserName(String v) {
        userName = v;
    }
	    
    public String getUserName() {
        return userName;
    }

	@Override
	public int getNumItems() {
		return 8;
	}

	@Override
	public void getObjectItems(Object[] obj) {
		obj[0] = super._id;
		obj[1] = super.server_id;
		obj[2] = super.handle;
		obj[3] = userName;
		obj[4] = phoneMobile;
		obj[5] = avatar;
		obj[6] = super.sync_time;
		obj[7] = super.dirty;
		obj[8] = super.deleted;
		
	}

	@Override
	public void dumpRecord(Cursor c) {
        super._id = c.getInt(c.getColumnIndex("id"));
        super.server_id = c.getInt(c.getColumnIndex("server_id"));
        userName = c.getString(c.getColumnIndex("username"));
        super.handle = c.getString(c.getColumnIndex("handle"));
        phoneMobile = c.getString(c.getColumnIndex("phone_mobile"));
        avatar = c.getString(c.getColumnIndex("avatar"));
        super.sync_time = c.getLong(c.getColumnIndex("sync_time"));
		
	}
}
