package com.talk.demo.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RawData {

	private static final String TAG = "RawData";
	
    private final String mHandle;
    private final long mServerTagId;
    private final long mRawTagId;
    private final long mSyncState;
    

    public String getHandle() {
        return mHandle;
    }
    public long getServerTagId() {
        return mServerTagId;
    }
    public long getRawTagId() {
        return mRawTagId;
    }
    public long getSyncState() {
        return mSyncState;
    } 
    
    public RawData(String name, String handle, long serverTagId,
    		long rawTagId, long syncState) {
    	mHandle = handle;
    	mServerTagId = serverTagId;
    	mRawTagId = rawTagId;
    	mSyncState = syncState;
    }
    
    public abstract JSONObject toJSONObject();
    
    public abstract RawData valueOf(JSONObject tag);
    
    public RawData create(String name, String handle,
    		long serverTagId,long rawTagId) {
    	return new RawData(name, handle, serverTagId, rawTagId, -1);
    }
    
    public RawData createDeletedTag(long serverTagId, long rawTagId) {
    	return new RawData(null, null, serverTagId, rawTagId, -1);
    }
}
