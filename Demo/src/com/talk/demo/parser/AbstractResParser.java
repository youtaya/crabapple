package com.talk.demo.parser;

import com.talk.demo.types.TalkType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;

public abstract class AbstractResParser<T extends TalkType> implements ResParser<T> {

    /** 
     * All derived parsers must implement parsing a JSONObject instance of themselves. 
     */
    public abstract T parse(JSONObject json) throws JSONException;
    
    /**
     * Only the GroupParser needs to implement this.
     */
    public Group parse(JSONArray array) throws JSONException {
        throw new JSONException("Unexpected JSONArray parse type encountered.");
    }
}
