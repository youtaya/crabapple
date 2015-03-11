package com.talk.demo.types;


public class WalkRoom implements TalkType {

    private String mUserName;
    private String last_msg_time;
    
    private String mHandle;
    private long mServerId;
    private long mDataId;
    private long mSyncState;
    

    public String getHandle() {
        return mHandle;
    }
    public void setHandle(String v) {
        mHandle = v;
    }
    public long getServerId() {
        return mServerId;
    }
    public void setServerId(long v) {
        mServerId = v;
    }
    public long getDataId() {
        return mDataId;
    }
    public void setDataId(long v) {
        mDataId = v;
    }
    public long getSyncState() {
        return mSyncState;
    } 
    public void setSyncState(long v) {
        mSyncState = v;
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