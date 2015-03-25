package com.talk.demo.types;

import android.database.Cursor;


public class BookTag implements TalkType {
    public int _id;
    public int server_id;
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
    
    public String tagName;
 
    public BookTag() {
    }
    
    public void setHandleName(String v) {
        handle = v;
    }
        
    public String getHandleName() {
        return handle;
    }
    
    public String getTagName() {
        return tagName;
    }
    
    public void setTagName(String v) {
        tagName = v;
    }
    
    public void setDirty(int v) {
        dirty = v;
    }
    
    public int getDirty() {
        return dirty;
    }
    public void getObjectItems(Object[] obj) {
        obj[0] = server_id;
        obj[1] = handle;
        obj[2] = tagName;
        obj[3] = sync_time;
        obj[4] = dirty;
        obj[5] = deleted;
    }

    public void dumpRecord(Cursor c) {
        _id = c.getInt(c.getColumnIndex("id"));
        server_id = c.getInt(c.getColumnIndex("server_id"));
        tagName = c.getString(c.getColumnIndex("tagname"));
        handle = c.getString(c.getColumnIndex("handle"));
        sync_time = c.getLong(c.getColumnIndex("sync_time"));
        
    }
}