package com.talk.demo.parser;

import com.talk.demo.types.Friend;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendParser extends AbstractResParser<Friend> {

    @Override
    public Friend parse(JSONObject json) throws JSONException {
        Friend obj = new Friend();
        if (json.has("cityid")) {
            obj.setCityid(json.getString("cityid"));
        } 
        if (json.has("message")) {
            obj.setMessage(json.getString("message"));
        } 
        if (json.has("status")) {
            obj.setStatus("1".equals(json.getString("status")));
        }
        
        return obj;
    }
}
