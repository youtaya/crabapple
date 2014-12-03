package com.talk.demo.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class DailyNews {
	private HashMap<String, String> news;
	private String create_time;
	private String expired_time;
	
	private JSONArray jArray;
	
	public DailyNews() {
		news = new HashMap<String, String>();
	}
	
	public DailyNews(JSONArray arr) {
		news = new HashMap<String, String>();
		jArray = arr;
	}
	
	public void parseJSONArray(JSONArray arr) {
		for(int i=0;i<arr.length();i++) {
			JSONObject item;
			try {
				item = arr.getJSONObject(i);
				
				for (Iterator iter = item.keys(); iter.hasNext();) {
					String value = (String)iter.next();
					news.put(value, item.getString(value));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public void setCreateTime(String var) {
	    create_time = var;
	}
	public String getCreateTime() {
		return create_time;
	}

	public void setExpiredTime(String var) {
	    expired_time = var;
	}
	public String getExpiredTime() {
		return expired_time;
	}
	
	public HashMap<String, String> getNews() {
		return news;
	}

}
