package com.talk.demo.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

final public class RawRoom extends RawData {

	private static final String TAG = "RawRoom";
	
    private final String mUserName;
    private final String last_msg_time;
    
    public String getRoomName() {
        return mUserName;
    }
    
    public String getLastMsgTime() {
    	return last_msg_time;
    }
    
    public RawRoom(String name, String handle, String msg_time, long serverRoomId,
    		long rawRoomId, long syncState) {
    	super(handle,serverRoomId, rawRoomId, syncState);
    	mUserName = name;
    	last_msg_time = msg_time;
    }
    
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
        	if (!TextUtils.isEmpty(mUserName)) {
        		json.put("t", mUserName);
        	}
        	if (!TextUtils.isEmpty(super.getHandle())) {
        		json.put("h", super.getHandle());
        	}
        	if (!TextUtils.isEmpty(last_msg_time)) {
        		json.put("l", last_msg_time);
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
    
    public static RawRoom valueOf(JSONObject room) {
    	try {
    		final String userName = !room.isNull("t")?room.getString("t"): null;
    		final int serverRoomId = !room.isNull("s")?room.getInt("s"): -1;
    		
    		if ((userName == null) && (serverRoomId <=0)) {
    			throw new JSONException("JSON friend missing required 'u' or 's' fields");
    		}
    		
    		final int rawRoomId = !room.isNull("f")?room.getInt("f"): -1;
    		final String handle = !room.isNull("h")?room.getString("h"): null;
    		final String last_msg_time = !room.isNull("l")?room.getString("l"): null;
    		final long syncState = !room.isNull("x")?room.getLong("x"): 0;
    		
    		return new RawRoom(userName, handle, last_msg_time, serverRoomId,
    		        rawRoomId, syncState);
    	} catch (final Exception ex) {
    		Log.d(TAG, "Error parsing JSON friend object" +ex.toString());
    	}
    	return null;
    }
    
    public static RawRoom create(String name, String handle, String msg_time,
    		long serverRoomId,long rawRoomId) {
    	return new RawRoom(name, handle, msg_time, serverRoomId, rawRoomId, -1);
    }
    
    public static RawRoom createDeletedRoom(long serverRoomId, long rawRoomId) {
    	return new RawRoom(null, null, null, serverRoomId, rawRoomId, -1);
    }
}
