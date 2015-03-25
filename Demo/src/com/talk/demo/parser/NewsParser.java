package com.talk.demo.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.talk.demo.types.News;

public class NewsParser extends AbstractResParser<News> {

    @Override
    public News parse(JSONObject json) throws JSONException {
    	News obj = new News();
        if (json.has("content")) {
            obj.setNewsContent(json.getString("content"));
        } 
        if (json.has("create_time")) {
            obj.setCreateTime(json.getString("create_time"));
        } 
        if (json.has("expired_time")) {
            obj.setExpiredTime(json.getString("expired_time"));
        }
        
        return obj;
    }
}
