package com.talk.demo.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.talk.demo.types.Group;
import com.talk.demo.types.TalkType;


public interface ResParser<T extends TalkType> {
    public abstract T parse(JSONObject json) throws JSONException;
    public Group parse(JSONArray array) throws JSONException;

}
