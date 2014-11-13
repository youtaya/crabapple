package com.talk.demo.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

final public class RawTag extends RawData {

	private static final String TAG = "RawTag";
	
    private final String mTagName;
    
    public String getTagName() {
        return mTagName;
    }
    
    public RawTag(String name, String handle, long serverTagId,
    		long rawTagId, long syncState) {
    	super(handle,serverTagId, rawTagId, syncState);
    	mTagName = name;
    }
    
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
        	if (!TextUtils.isEmpty(mTagName)) {
        		json.put("t", mTagName);
        	}
        	if (!TextUtils.isEmpty(super.getHandle())) {
        		json.put("h", super.getHandle());
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
