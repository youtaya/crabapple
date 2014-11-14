package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.util.RawFriend;

public class FriendRecord extends CommonRecord {
    public int _id;
    public int server_id;
    public String userName;
    public String handle;
    public String phoneMobile;
    public String avatar;
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
    
    public FriendRecord() {
    }
    
    public FriendRecord(RawFriend rf) {
        _id = (int)rf.getDataId();
        server_id = (int)rf.getServerId();
        userName = rf.getUserName();
        handle = rf.getHandle();
        phoneMobile = rf.getPhoneMobile();
        avatar = rf.getAvatar();
        sync_time = rf.getSyncState();
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
	public String getTableName() {
		return "friends";
	}

	@Override
	public int getNumItems() {
		return 8;
	}

	@Override
	public void getObjectItems(Object[] obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dumpRecord(Cursor c) {
		// TODO Auto-generated method stub
		
	}
}
