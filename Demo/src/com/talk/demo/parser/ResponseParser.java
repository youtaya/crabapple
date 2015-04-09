package com.talk.demo.parser;


import com.talk.demo.types.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseParser extends AbstractResParser<Response> {

    @Override
    public Response parse(JSONObject json) throws JSONException {
        Response obj = new Response();
        if (json.has("status")) {
            obj.setValue(json.getString("status"));
        } 
 
        return obj;
    }
}
