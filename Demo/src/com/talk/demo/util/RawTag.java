package com.talk.demo.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

final public class RawTag {

	private static final String TAG = "RawFriend";
	
    private final String mTagName;
    private final String mHandle;
    private final long mServerTagId;
    private final long mRawTagId;
    private final long mSyncState;
    
    public String getTagName() {
        return mTagName;
    }
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
    
    public RawTag(String name, String handle, long serverTagId,
    		long rawTagId, long syncState) {
    	mTagName = name;
    	mHandle = handle;
    	mServerTagId = serverTagId;
    	mRawTagId = rawTagId;
    	mSyncState = syncState;
    }
    
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
        	if (!TextUtils.isEmpty(mTagName)) {
        		json.put("t", mTagName);
        	}
        	if (!TextUtils.isEmpty(mHandle)) {
        		json.put("h", mHandle);
        	}
        	if (mServerTagId > 0) {
        		json.put("s", mServerTagId);
        	}
        	if (mRawTagId > 0) {
        		json.put("f", mRawTagId);
        	}

        } catch (final Exception ex) {
        	Log.d(TAG, "Error conveting to JSONObject"+ex.toString());
        }
        
        return json;
    }
    
    public static RawTag valueOf(JSONObject tag) {
    	try {
    		final String tagName = !tag.isNull("t")?tag.getString("t"): null;
    		final int serverTagId = !tag.isNull("s")?tag.getInt("s"): -1;
    		
    		if ((tagName == null) && (serverTagId <=0)) {
    			throw new JSONException("JSON friend missing required 'u' or 's' fields");
    		}
    		
    		final int rawTagId = !tag.isNull("f")?tag.getInt("f"): -1;
    		final String handle = !tag.isNull("h")?tag.getString("h"): null;
    		final long syncState = !tag.isNull("x")?tag.getLong("x"): 0;
    		
    		return new RawTag(tagName, handle, serverTagId,
    		        rawTagId, syncState);
    	} catch (final Exception ex) {
    		Log.d(TAG, "Error parsing JSON friend object" +ex.toString());
    	}
    	return null;
    }
    
    public static RawTag create(String name, String handle,
    		long serverTagId,long rawTagId) {
    	return new RawTag(name, handle, serverTagId, rawTagId, -1);
    }
    
    public static RawTag createDeletedTag(long serverTagId, long rawTagId) {
    	return new RawTag(null, null, serverTagId, rawTagId, -1);
    }
}
