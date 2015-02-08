package com.talk.demo.parser;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.talk.demo.types.Group;
import com.talk.demo.types.TalkType;

public class GroupParser extends AbstractResParser<Group> {

	private ResParser<? extends TalkType> mSubParser;
	
	public GroupParser(ResParser<? extends TalkType> subParser) {
		mSubParser = subParser;
	}
	public Group<TalkType> parse(JSONObject json) throws JSONException {
		Group<TalkType> group = new Group<TalkType>();
		Iterator<String> it = (Iterator<String>)json.keys();
		while(it.hasNext()) {
			String key = it.next();
			if(key.equals("type")) {
				group.setType(json.getString(key));
			} else {
				Object obj = json.get(key);
				if(obj instanceof JSONArray) {
					parse(group, (JSONArray)obj);
				} else {
					throw new JSONException("Could not parse data.");
				}
			}
		}
				
		return group;
	}
	
	@Override
	public Group parse(JSONArray array) throws JSONException {
		Group<TalkType> group = new Group<TalkType>();
		parse(group, array);
		return group;
	}
	
	private void parse(Group group, JSONArray array) throws JSONException {
		for (int i=0, m = array.length(); i<m; i++) {
			Object element = array.get(i);
			TalkType item = null;
			
			if(element instanceof JSONArray) {
				item = mSubParser.parse((JSONArray)element);
			} else {
				item = mSubParser.parse((JSONObject)element);
			}
		}
	}

}
