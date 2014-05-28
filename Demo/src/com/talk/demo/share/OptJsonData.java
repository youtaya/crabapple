package com.talk.demo.share;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class OptJsonData {
	private static String TAG = "OptJsonData";
	private Context mContext;
	public OptJsonData(Context context) {
		mContext = context;
	}
    public void saveLocalFile(String str) {
    	String string = str;
    	FileOutputStream outputStream;
    	File file = new File(mContext.getFilesDir(), "test");
    	
    	if(file.exists()) {
    		Log.d(TAG, "already exists!");
    	}
    	try {
    	  outputStream = new FileOutputStream(file);
    	  outputStream.write(string.getBytes());
    	  Log.d(TAG, "new Gen: "+string);
    	  outputStream.close();
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    }
    
    public String appendJsonData(String dataJson, ShareEntity shareEntity) {
     	try {
    		JSONObject jsonObject = new JSONObject(dataJson);
   
        	JSONArray talkArray = jsonObject.getJSONArray("talk");
        	
    		JSONObject talkObject = new JSONObject();
    		talkObject.put("content", shareEntity.getContent());
    		talkObject.put("time", shareEntity.getShareTime());
    		talkObject.put("from", "alice");
    		talkObject.put("to", "bob");
        	talkArray.put(talkObject);
        	jsonObject.put("talk", talkArray);
        	
        	Log.d(TAG, "update json data: " + jsonObject.toString());
        	
        	return jsonObject.toString();
        	
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
        return "";
    }
    
    public String readLocalFile(String filename) {
    	String dataJson = null; 
    	FileInputStream inputStream;
    	byte[] buffer = new byte[1024];
    	try {
    		inputStream = mContext.openFileInput(filename);
    		int length = inputStream.read(buffer);
    		dataJson = new String(buffer, "UTF-8");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	dumpJsonData(dataJson);
    	
    	return dataJson;
    }
    
    public void appendJsonData(ShareEntity shareEntity) {
    	saveLocalFile(appendJsonData(readLocalFile("test"), shareEntity));
    }
    
    public void dumpJsonData(String data) {

    	try {
    		JSONObject jsonObject = new JSONObject(data);
        	JSONArray talkArray = jsonObject.getJSONArray("talk");
        	for(int i = 0; i < talkArray.length(); i++) {
        		JSONObject talkObject = talkArray.getJSONObject(i);
	        	Log.d(TAG, "content: "+talkObject.getString("content"));

        	}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

    }
}
