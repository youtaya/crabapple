package com.talk.demo.types;


public class Friend implements TalkType {

    private String mUserName;
    private String mPhoneMobile;
    private String mAvatar;
    private String mDescription;
    
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
    
    public void setUsername(String v) {
    	mUserName = v;
    }
    public String getUserName() {
        return mUserName;
    }
    
    public void setPhoneMobile(String v) {
    	mPhoneMobile = v;
    }
    public String getPhoneMobile() {
        return mPhoneMobile;
    }
    
    public void setAvatar(String v) {
    	mAvatar = v;
    }
    public String getAvatar() {
        return mAvatar;
    }
    
    public void setDescription(String v) {
    	mDescription = v;
    }
    public String getDescription() {
        return mDescription;
    }    


}