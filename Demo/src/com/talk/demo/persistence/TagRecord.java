package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.util.RawTag;

public class TagRecord extends CommonRecord {

    public String tagName;

    public TagRecord() {
    }
    
    public TagRecord(RawTag tag) {
    	super(tag);
        tagName = tag.getTagName();
    }
    
    public TagRecord(String v1) {
    	tagName = v1;
       
    }

    public void setTagName(String v) {
    	tagName = v;
    }
	    
    public String getTagName() {
        return tagName;
    }

	@Override
	public int getNumItems() {
		return 6;
	}

	@Override
	public void getObjectItems(Object[] obj) {
		obj[0] = super.server_id;
		obj[1] = super.handle;
		obj[2] = tagName;
		obj[3] = super.sync_time;
		obj[4] = super.dirty;
		obj[5] = super.deleted;
	}

	@Override
	public void dumpRecord(Cursor c) {
    	super._id = c.getInt(c.getColumnIndex("id"));
    	super.server_id = c.getInt(c.getColumnIndex("server_id"));
    	tagName = c.getString(c.getColumnIndex("tagname"));
    	super.handle = c.getString(c.getColumnIndex("handle"));
    	super.sync_time = c.getLong(c.getColumnIndex("sync_time"));
		
	}
}
