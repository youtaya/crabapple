package com.talk.demo.types;

import android.database.Cursor;


public class WalkRoom implements TalkType {

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
    
    public String username;
    public String last_msg_time;
    

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
    
    public String getRoomName() {
        return username;
    }
    public void setRoomName(String v) {
        username = v;
    }
    
    public String getLastMsgTime() {
        return last_msg_time;
    }
    
    public void setLastMsgTime(String v) {
        last_msg_time = v;
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
        obj[2] = username;
        obj[3] = last_msg_time;
        obj[4] = sync_time;
        obj[5] = dirty;
        obj[6] = deleted;
    }

    public void dumpRecord(Cursor c) {
        _id = c.getInt(c.getColumnIndex("id"));
        server_id = c.getInt(c.getColumnIndex("server_id"));
        username = c.getString(c.getColumnIndex("username"));
        last_msg_time = c.getString(c.getColumnIndex("last_msg_time"));
        handle = c.getString(c.getColumnIndex("handle"));
        sync_time = c.getLong(c.getColumnIndex("sync_time"));
        
    }
}