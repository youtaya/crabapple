package com.talk.demo.types;

import android.database.Cursor;


public class Friend implements TalkType {

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
    
    public String userName;
    public String phoneMobile;
    public String avatar;
    public String description;
    

    public String getHandle() {
        return handle;
    }
    public void setHandle(String v) {
        handle = v;
    }
    public long getServerId() {
        return server_id;
    }
    public void setServerId(int v) {
        server_id = v;
    }
    public long getDataId() {
        return _id;
    }
    public void setDataId(int v) {
        _id = v;
    }
    public long getSyncState() {
        return sync_time;
    } 
    public void setSyncState(long v) {
        sync_time = v;
    }
    
    public void setUsername(String v) {
        userName = v;
    }
    public String getUserName() {
        return userName;
    }
    
    public void setPhoneMobile(String v) {
        phoneMobile = v;
    }
    public String getPhoneMobile() {
        return phoneMobile;
    }
    
    public void setAvatar(String v) {
        avatar = v;
    }
    public String getAvatar() {
        return avatar;
    }
    
    public void setDescription(String v) {
        description = v;
    }
    public String getDescription() {
        return description;
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
        obj[2] = userName;
        obj[3] = phoneMobile;
        obj[4] = avatar;
        obj[5] = description;
        obj[6] = sync_time;
        obj[7] = dirty;
        obj[8] = deleted;
        
    }

    public void dumpRecord(Cursor c) {
        _id = c.getInt(c.getColumnIndex("id"));
        server_id = c.getInt(c.getColumnIndex("server_id"));
        userName = c.getString(c.getColumnIndex("username"));
        handle = c.getString(c.getColumnIndex("handle"));
        phoneMobile = c.getString(c.getColumnIndex("phone_mobile"));
        avatar = c.getString(c.getColumnIndex("avatar"));
        description = c.getString(c.getColumnIndex("description"));
        sync_time = c.getLong(c.getColumnIndex("sync_time"));
        
    }

}