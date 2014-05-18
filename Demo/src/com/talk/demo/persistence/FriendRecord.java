package com.talk.demo.persistence;

public class FriendRecord {
    public int _id;
    public int server_id;
    public String userName;
    public String handle;
    public String phoneMobile;
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
    
    public FriendRecord(String v1) {
        userName = v1;
       
    }

    public void setUserName(String v) {
        userName = v;
    }
	    
    public String getUserName() {
        return userName;
    }
}
