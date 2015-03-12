package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.types.BookTag;
import java.lang.reflect.Field;

public class TagRecord extends CommonRecord {
    private BookTag tag;
    private Field[] declaredFields;
    public TagRecord() {
        tag = new BookTag();     
        
        Class c = tag.getClass();     
         
        declaredFields = c.getDeclaredFields();
    }
    public void setHandleName(String v) {
        tag.setHandleName(v);
    }
        
    public String getHandleName() {
        return tag.getHandleName();
    }
    
    public void setDirty(int v) {
        tag.setDirty(v);
    }
    
    public int getDirty() {
        return tag.getDirty();
    }
    
    public TagRecord(String v1) {
    	setTagName(v1);
       
    }

    public void setTagName(String v) {
    	tag.setTagName(v);
    }
	    
    public String getTagName() {
        return tag.getTagName();
    }

	@Override
	public int getNumItems() {
		return declaredFields.length-1;
	}

	@Override
	public void getObjectItems(Object[] obj) {
	    tag.getObjectItems(obj);
	}

	@Override
	public void dumpRecord(Cursor c) {
    	tag.dumpRecord(c);
	}
}
