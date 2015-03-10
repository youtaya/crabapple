package com.talk.demo.parser;

import com.talk.demo.types.BookTag;

import org.json.JSONException;
import org.json.JSONObject;

public class TagParser extends AbstractResParser<BookTag> {

    @Override
    public BookTag parse(JSONObject json) throws JSONException {
        BookTag obj = new BookTag();
        if (json.has("t")) {
            obj.setTagName(json.getString("t"));
        } 
        
        return obj;
    }
}
