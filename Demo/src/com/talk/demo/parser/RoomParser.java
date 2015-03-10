package com.talk.demo.parser;

import com.talk.demo.types.WalkRoom;

import org.json.JSONException;
import org.json.JSONObject;

public class RoomParser extends AbstractResParser<WalkRoom> {

    @Override
    public WalkRoom parse(JSONObject json) throws JSONException {
        WalkRoom obj = new WalkRoom();
        if (json.has("t")) {
            obj.setRoomName(json.getString("t"));
        }
        
        if (json.has("l")) {
            obj.setLastMsgTime(json.getString("l"));
        }    
        return obj;
    }
}
