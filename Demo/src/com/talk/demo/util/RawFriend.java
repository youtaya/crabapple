package com.talk.demo.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

final public class RawFriend extends RawData {

	private static final String TAG = "RawFriend";
	
    private final String mUserName;
    private final String mPhoneMobile;
    private final String mAvatar;
    
    public String getUserName() {
        return mUserName;
    }
    public String getPhoneMobile() {
        return mPhoneMobile;
    }
    public String getAvatar() {
        return mAvatar;
    }    

    public RawFriend(String name, String handle, String phone, String avatar,
    		long serverFriendId,long rawFriendId, long syncState) {
    	super(handle,serverFriendId, rawFriendId, syncState);
    	mUserName = name;
    	mPhoneMobile = phone;
    	mAvatar = avatar;
    }
    
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
        	if (!TextUtils.isEmpty(mUserName)) {
        		json.put("u", mUserName);
        	}
        	if (!TextUtils.isEmpty(super.getHandle())) {
        		json.put("h", super.getHandle());
        	}
        	if (!TextUtils.isEmpty(mPhoneMobile)) {
        		json.put("p", mPhoneMobile);
        	}
            if (!TextUtils.isEmpty(mAvatar)) {
                json.put("a", mAvatar);
            }        	
        	if (super.getServerId() > 0) {
        		json.put("s", super.getServerId());
        	}
        	if (super.getDataId() > 0) {
        		json.put("f", super.getDataId());
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
    		final String avatar = !friend.isNull("a")?friend.getString("a"): null;
    		final long syncState = !friend.isNull("x")?friend.getLong("x"): 0;
    		
    		return new RawFriend(userName, handle, phone, avatar, serverFriendId,
    		        rawFriendId, syncState);
    	} catch (final Exception ex) {
    		Log.d(TAG, "Error parsing JSON friend object" +ex.toString());
    	}
    	return null;
    }
    
    public static RawFriend create(String name, String handle, String phone, String avatar,
    		long serverFriendId,long rawFriendId) {
    	return new RawFriend(name, handle, phone, avatar, serverFriendId, rawFriendId, -1);
    }
    
    public static RawFriend createDeletedFriend(long serverFriendId, long rawFriendId) {
    	return new RawFriend(null, null, null, null, serverFriendId, rawFriendId, -1);
    }
}
