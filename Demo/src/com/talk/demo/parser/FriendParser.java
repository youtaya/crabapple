package com.talk.demo.parser;

import com.talk.demo.types.Friend;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendParser extends AbstractResParser<Friend> {

    @Override
    public Friend parse(JSONObject json) throws JSONException {
        Friend obj = new Friend();
        
        if (json.has("user")) {
            obj.setHandle(json.getString("user"));
        } 
        if (json.has("sid")) {
            obj.setServerId(json.getInt("sid"));
        } 
        if (json.has("cid")) {
            obj.setDataId(json.getInt("cid"));
        } 
        if (json.has("x")) {
            obj.setSyncState(json.getInt("x"));
        } 
        
        if (json.has("f")) {
            obj.setUsername(json.getString("f"));
        } 
        if (json.has("p")) {
            obj.setPhoneMobile(json.getString("p"));
        } 
        if (json.has("a")) {
            obj.setAvatar(json.getString("a"));
        }
        if (json.has("d")) {
        	obj.setDescription(json.getString("d"));
        }
        
        return obj;
    }
}
