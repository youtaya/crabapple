package com.talk.demo.types;


public class Friend implements TalkType {

    private String mUserName;
    private String mPhoneMobile;
    private String mAvatar;
    private String mDescription;
 
    public Friend() {
    }
    
    public String getUserName() {
        return mUserName;
    }
    public String getPhoneMobile() {
        return mPhoneMobile;
    }
    public String getAvatar() {
        return mAvatar;
    }
    public String getDescription() {
        return mDescription;
    }    


}