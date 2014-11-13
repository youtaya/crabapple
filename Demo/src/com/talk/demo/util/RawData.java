package com.talk.demo.util;


public abstract class RawData {

	private static final String TAG = "RawData";
	
    private String mHandle;
    private long mServerId;
    private long mDataId;
    private long mSyncState;
    

    public String getHandle() {
        return mHandle;
    }
    public long getServerId() {
        return mServerId;
    }
    public long getDataId() {
        return mDataId;
    }
    public long getSyncState() {
        return mSyncState;
    } 
    
    public RawData(String handle, long serverTagId,
    		long rawTagId, long syncState) {
    	mHandle = handle;
    	mServerId = serverTagId;
    	mDataId = rawTagId;
    	mSyncState = syncState;
    }
    
}
