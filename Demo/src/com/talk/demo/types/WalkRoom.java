package com.talk.demo.types;


public class WalkRoom implements TalkType {

    private String mUserName;
    private String last_msg_time;
 
    public WalkRoom() {
    }
    
    public String getRoomName() {
        return mUserName;
    }
    public void setRoomName(String v) {
        mUserName = v;
    }
    
    public String getLastMsgTime() {
        return last_msg_time;
    }
    
    public void setLastMsgTime(String v) {
        last_msg_time = v;
    }
}