package com.talk.demo.types;


public class Response implements TalkType {

    private String mValue;

    public Response() {
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}