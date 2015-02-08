package com.talk.demo.types;

import java.util.HashMap;


public class News implements TalkType {

	private String mNewsContent;
	private String mCreateTime;
	private String mExpiredTime;
 
    public News() {
    }
    
    public void setNewsContent(String v) {
    	mNewsContent = v;
    }
    public String getNewsContent() {
        return mNewsContent;
    }
    
    public void setCreateTime(String v) {
    	mCreateTime = v;
    }
    public String getCreateTime() {
        return mCreateTime;
    }
    
    public void setExpiredTime(String v) {
    	mExpiredTime = v;
    }
    public String getExpiredTime() {
        return mExpiredTime;
    }
    

}