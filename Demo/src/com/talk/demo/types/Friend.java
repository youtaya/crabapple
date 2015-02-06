package com.talk.demo.types;


public class Friend implements TalkType {

    private String mUserName;
    private String mPhoneMobile;
    private String mAvatar;
    private String mDescription;
 
    public Friend() {
    }
    
    public void setUsername(String v) {
    	mUserName = v;
    }
    public String getUserName() {
        return mUserName;
    }
    
    public void setPhoneMobile(String v) {
    	mPhoneMobile = v;
    }
    public String getPhoneMobile() {
        return mPhoneMobile;
    }
    
    public void setAvatar(String v) {
    	mAvatar = v;
    }
    public String getAvatar() {
        return mAvatar;
    }
    
    public void setDescription(String v) {
    	mDescription = v;
    }
    public String getDescription() {
        return mDescription;
    }    


}