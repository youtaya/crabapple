package com.talk.demo.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

final public class RawFriend {

	private static final String TAG = "RawFriend";
	
    private final String mUserName;
    private final  String mHandle;
    private final  String mPhoneMobile;
    private final long mServerFriendId;
    private final long mRawFriendId;
    private final long mSyncState;
    
    public String getUserName() {
        return mUserName;
    }
    public String getHandle() {
        return mHandle;
    }
    public String getPhoneMobile() {
        return mPhoneMobile;
    }
    public long getServerFriendId() {
        return mServerFriendId;
    }
    public long getRawFriendId() {
        return mRawFriendId;
    }
    public long getSyncState() {
        return mSyncState;
    } 
    
    public RawFriend(String name, String handle, String phone,
    		long serverFriendId,long rawFriendId, long syncState) {
    	mUserName = name;
    	mHandle = handle;
    	mPhoneMobile = phone;
    	mServerFriendId = serverFriendId;
    	mRawFriendId = rawFriendId;
    	mSyncState = syncState;
    }
    
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
        	if (!TextUtils.isEmpty(mUserName)) {
        		json.put("u", mUserName);
        	}
        	if (!TextUtils.isEmpty(mHandle)) {
        		json.put("h", mHandle);
        	}
        	if (!TextUtils.isEmpty(mPhoneMobile)) {
        		json.put("p", mPhoneMobile);
        	}
        	if (mServerFriendId > 0) {
        		json.put("s", mServerFriendId);
        	}
        	if (mRawFriendId > 0) {
        		json.put("f", mRawFriendId);
        	}

        } catch (final Exception ex) {
        	Log.d(TAG, "Error conveting to JSONObject"+ex.toString());
        }
        
        return json;
    }
    
    public static RawFriend valueOf(JSONObject friend) {
    	try {
    		final String userName = !friend.isNull("u")?friend.getString("u"): null;
    		final int serverFriendId = !friend.isNull("s")?friend.getInt("s"): -1;
    		
    		if ((userName == null) && (serverFriendId <=0)) {
    			throw new JSONException("JSON friend missing required 'u' or 's' fields");
    		}
    		
    		final int rawFriendId = !friend.isNull("f")?friend.getInt("f"): -1;
    		final String handle = !friend.isNull("h")?friend.getString("h"): null;
    		final String phone = !friend.isNull("p")?friend.getString("p"): null;
    		final long syncState = !friend.isNull("x")?friend.getLong("x"): 0;
    		
    		return new RawFriend(userName, handle, phone, serverFriendId, rawFriendId, syncState);
    	} catch (final Exception ex) {
    		Log.d(TAG, "Error parsing JSON friend object" +ex.toString());
    	}
    	return null;
    }
    
    public static RawFriend create(String name, String handle, String phone,
    		long serverFriendId,long rawFriendId) {
    	return new RawFriend(name, handle, phone, serverFriendId, rawFriendId, -1);
    }
    
    public static RawFriend createDeletedFriend(long serverFriendId, long rawFriendId) {
    	return new RawFriend(null, null, null, serverFriendId, rawFriendId, -1);
    }
}
