package com.talk.demo.persistence;

public class FriendRecord {
    public int _id;
    
    public String userName;
    public String phoneMobile;
    public int deleted;
    
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
