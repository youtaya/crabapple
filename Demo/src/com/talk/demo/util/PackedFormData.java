package com.talk.demo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;

import com.talk.demo.types.Friend;
import com.talk.demo.types.Record;

public class PackedFormData {
	
    public static Map<String, String> signup(String username, String email, String password) {
        String name = username;
        String mail = email;
        String passwd = password;
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", name);
        data.put("password", passwd);
        data.put("password_confirm",passwd);
        
        return data;
    }
    
    public static Map<String, String> login(String username,String password) {
        String name = username;
        String passwd = password;
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", name);
        data.put("password", passwd);
        
        return data;
    }
    
    public static Map<String, String> shareRecord(RawDialog raw, String oring, String target) {
        Map<String, String> params = new HashMap<String, String>();
        JSONObject jsonRecord = raw.toJSONObject();
        params.put(NetworkUtilities.PARAM_USERNAME, oring);
        params.put("records", jsonRecord.toString());
        params.put("target", target);
        
        return params;
        
    }
    
    public static Map<String, String> getDialog(String username, int id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("id", String.valueOf(id));
        
        return params;
    }
    
    public static Map<String, String> packedUpdateChannel(String friend, String last_date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("friend", friend);
        params.put("last_date", last_date);
        return params;
    }
    
    public static Map<String, String> syncRecords(
            Account account, String authtoken, long serverSyncState, List<Record> dirtyRecords) {
        Map<String, String> params = new HashMap<String, String>();
        
        // Convert our list of User objects into a list of JSONObject
        List<JSONObject> jsonRecords = new ArrayList<JSONObject>();
        for (Record rawRecord : dirtyRecords) {
            jsonRecords.add(rawRecord.toJSONObject());
        }

        // Create a special JSONArray of our JSON contacts
        JSONArray buffer = new JSONArray(jsonRecords);

        // Prepare our POST data
        params.put(NetworkUtilities.PARAM_USERNAME, account.name);
        //params.add(new BasicNameValuePair(PARAM_AUTH_TOKEN, authtoken));
        params.put(NetworkUtilities.PARAM_RECORDS_DATA, buffer.toString());

        if (serverSyncState > 0) {
            params.put(NetworkUtilities.PARAM_SYNC_STATE, Long.toString(serverSyncState));
        }
        return params;
    }
    
    public static Map<String, String> syncFriends(
            Account account, String authtoken, long serverSyncState, List<Friend> dirtyFriends) {
        Map<String, String> params = new HashMap<String, String>();
        
        // Convert our list of User objects into a list of JSONObject
        List<JSONObject> jsonRecords = new ArrayList<JSONObject>();
        for (Friend rawFriend : dirtyFriends) {
            jsonRecords.add(rawFriend.toJSONObject());
        }
        // Create a special JSONArray of our JSON contacts
        JSONArray buffer = new JSONArray(jsonRecords);

        // Prepare our POST data
        params.put(NetworkUtilities.PARAM_USERNAME, account.name);
        //params.add(new BasicNameValuePair(PARAM_AUTH_TOKEN, authtoken));
        params.put(NetworkUtilities.PARAM_RECORDS_DATA, buffer.toString());

        if (serverSyncState > 0) {
            params.put(NetworkUtilities.PARAM_SYNC_STATE, Long.toString(serverSyncState));
        }
        return params;
    }
    
    public static Map<String, String> addFriend(String username,String friends) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", username);
        data.put("target_user", friends);  
        return data;
    }
    
    public static Map<String, String> acceptFriend(String username,boolean answer,String friends) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", username);
        data.put("nok", answer?"1":"0");
        data.put("target_user", friends);  
        return data;
    }
    
    public static Map<String, String> updateFriend(String username,String comment,String des,String friends) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", username);
        data.put("name_comment", comment);
        data.put("description", des);
        data.put("target_user", friends);  
        return data;
    }
    
}
