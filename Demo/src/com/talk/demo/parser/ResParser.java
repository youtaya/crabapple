package com.talk.demo.parser;

import com.talk.demo.types.TalkType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;

public interface ResParser<T extends TalkType> {
    public abstract T parse(JSONObject json) throws JSONException;
    public Group parse(JSONArray array) throws JSONException;

}
