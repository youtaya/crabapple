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
    
    public BookTag getTag() {
    	return tag;
    }
    public TagRecord(String v1) {
    	tag.setTagName(v1);
       
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
