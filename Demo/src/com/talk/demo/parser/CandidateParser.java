package com.talk.demo.parser;

import com.talk.demo.types.Candidate;

import org.json.JSONException;
import org.json.JSONObject;

public class CandidateParser extends AbstractResParser<Candidate> {

    @Override
    public Candidate parse(JSONObject json) throws JSONException {
        Candidate obj = new Candidate();
        
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
