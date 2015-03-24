package com.talk.demo.parser;

import com.talk.demo.types.Record;

import org.json.JSONException;
import org.json.JSONObject;

public class RecordParser extends AbstractResParser<Record> {

    @Override
    public Record parse(JSONObject json) throws JSONException {
        Record obj = new Record();
        
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
        
        if (json.has("link")) {
            obj.setLink(json.getString("link"));
        } 
        if (json.has("title")) {
            obj.setTitle(json.getString("title"));
        } 
        if (json.has("content")) {
            obj.setContent(json.getString("content"));
        } 
        if (json.has("date")) {
            obj.setCreateDate(json.getString("date"));
        }
        if (json.has("time")) {
            String createTime = json.getString("time");
            
            if(createTime.length() > 24) {
                obj.setCreateTime(createTime.substring(0, 24));
            } else {
                obj.setCreateTime(createTime);
            }
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
        if (json.has("tag")) {
            obj.setTag(json.getString("tag"));
        }
        if (json.has("del")) {
            obj.setDeleted(json.getInt("del"));
        }
        
        
        return obj;
    }
}
