package com.talk.demo.util;

import android.accounts.Account;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackedFormData {
    public static Map<String, String> packedData(String username, String email, String password) {
        String name = username;
        String mail = email;
        String passwd = password;
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", name);
        data.put("password", passwd);
        data.put("password_confirm",passwd);
        
        return data;
    }
    
    public static Map<String, String> packedData(String username,String password) {
        String name = username;
        String passwd = password;
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", name);
        data.put("password", passwd);
        
        return data;
    }
    
    public static Map<String, String> packedShareRecord(RawDialog raw, String oring, String target) {
        Map<String, String> params = new HashMap<String, String>();
        JSONObject jsonRecord = raw.toJSONObject();
        params.put(ServerInterface.PARAM_USERNAME, oring);
        params.put("records", jsonRecord.toString());
        params.put("target", target);
        
        return params;
        
    }
    
    public static Map<String, String> packedDialog(String username, int id) {
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
    
    public static Map<String, String> packedSyncRecords(Account account, String authtoken, long serverSyncState, List<RawRecord> dirtyRecords) {
        Map<String, String> params = new HashMap<String, String>();
        
        // Convert our list of User objects into a list of JSONObject
        List<JSONObject> jsonRecords = new ArrayList<JSONObject>();
        for (RawRecord rawRecord : dirtyRecords) {
            jsonRecords.add(rawRecord.toJSONObject());
        }

        // Create a special JSONArray of our JSON contacts
        JSONArray buffer = new JSONArray(jsonRecords);

        // Prepare our POST data
        params.put(ServerInterface.PARAM_USERNAME, account.name);
        //params.add(new BasicNameValuePair(PARAM_AUTH_TOKEN, authtoken));
        params.put(ServerInterface.PARAM_RECORDS_DATA, buffer.toString());

        if (serverSyncState > 0) {
            params.put(ServerInterface.PARAM_SYNC_STATE, Long.toString(serverSyncState));
        }
        return params;
    }
    
    public static Map<String, String> packedSyncFriends(Account account, String authtoken, long serverSyncState, List<RawFriend> dirtyFriends) {
        Map<String, String> params = new HashMap<String, String>();
        
        // Convert our list of User objects into a list of JSONObject
        List<JSONObject> jsonRecords = new ArrayList<JSONObject>();
        for (RawFriend rawFriend : dirtyFriends) {
            jsonRecords.add(rawFriend.toJSONObject());
        }
        // Create a special JSONArray of our JSON contacts
        JSONArray buffer = new JSONArray(jsonRecords);

        // Prepare our POST data
        params.put(ServerInterface.PARAM_USERNAME, account.name);
        //params.add(new BasicNameValuePair(PARAM_AUTH_TOKEN, authtoken));
        params.put(ServerInterface.PARAM_RECORDS_DATA, buffer.toString());

        if (serverSyncState > 0) {
            params.put(ServerInterface.PARAM_SYNC_STATE, Long.toString(serverSyncState));
        }
        return params;
    }
    
}