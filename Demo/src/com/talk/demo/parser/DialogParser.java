package com.talk.demo.parser;

import com.talk.demo.types.Dialog;

import org.json.JSONException;
import org.json.JSONObject;

public class DialogParser extends AbstractResParser<Dialog> {

    @Override
    public Dialog parse(JSONObject json) throws JSONException {
        Dialog obj = new Dialog();
        
        if (json.has("room")) {
            obj.setRoomName(json.getString("room"));
        } 
        if (json.has("content")) {
            obj.setContent(json.getString("content"));
        } 
        if (json.has("date")) {
            obj.setCreateDate(json.getString("date"));
        }
        if (json.has("time")) {
            /*
            if(createTime.length() > 24) {
                String temp = createTime.substring(0, 24);
                createTime = temp;
            }
            */
        	obj.setCreateTime(json.getString("time"));
        }
        if (json.has("ctx")) {
            obj.setContentType(json.getInt("ctx"));
        }
        if (json.has("ao")) {
            obj.setAudio(json.getString("ao"));
        }
        if (json.has("po")) {
            obj.setPhoto(json.getString("po"));
        }
        if (json.has("del")) {
            obj.setDeleted(json.getBoolean("del"));
        }
        
        
        return obj;
    }
}
