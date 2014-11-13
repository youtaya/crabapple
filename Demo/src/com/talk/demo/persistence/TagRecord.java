package com.talk.demo.persistence;

import com.talk.demo.util.RawTag;

public class TagRecord {
    public int _id;
    public int server_id;
    public String tagName;
    public String handle;
    public long sync_time;
 	/*
 	 * deleted flag :
 	 * default : 0 mean don't delete, other: 1 mean need to delete
 	 */
    public int deleted = 0; 
 	/*
 	 * dirty flag :
 	 * default : 1 mean dirty and need to sync, other: 0 mean not need sync
 	 */
    public int dirty = 1;
    
    public TagRecord() {
    }
    
    public TagRecord(RawTag rt) {
        _id = (int)rt.getDataId();
        server_id = (int)rt.getServerId();
        tagName = rt.getTagName();
        handle = rt.getHandle();
        sync_time = rt.getSyncState();
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
}
