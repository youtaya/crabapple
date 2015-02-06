package com.talk.demo.parser;

import com.talk.demo.types.Friend;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendParser extends AbstractResParser<Friend> {

    @Override
    public Friend parse(JSONObject json) throws JSONException {
        Friend obj = new Friend();
        if (json.has("u")) {
            obj.setUsername(json.getString("u"));
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
