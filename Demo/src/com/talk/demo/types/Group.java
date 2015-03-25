package com.talk.demo.types;

import java.util.ArrayList;
import java.util.Collection;

public class Group<T extends TalkType> extends ArrayList<T> implements TalkType {

    private static final long serialVersionUID = 1L;

    private String mType;
    
    public Group() {
        super();
    }
    
    public Group(Collection<T> collection) {
        super(collection);
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }
}