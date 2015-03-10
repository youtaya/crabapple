package com.talk.demo.types;


public class BookTag implements TalkType {

    private String mTagName;
 
    public BookTag() {
    }
    
    public String getTagName() {
        return mTagName;
    }
    
    public void setTagName(String v) {
        mTagName = v;
    }

}