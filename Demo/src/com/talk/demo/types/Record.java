
package com.talk.demo.types;

import android.database.Cursor;

public class Record implements TalkType {

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
    
    public String content;
    public String calc_date;
    public String create_time;
    public int send_interval_time;
    public String send_done_time;
    public int content_type;
    
    public String link;
    public String title = "time";
    public String photo;
    public String audio;
    public String tag;
    

    public String getHandle() {
        return handle;
    }
    public void setHandle(String v) {
        handle = v;
    }
    public int getServerId() {
        return server_id;
    }
    public void setServerId(int v) {
        server_id = v;
    }
    public int getDataId() {
        return _id;
    }
    public void setDataId(int v) {
        _id = v;
    }
    public long getSyncState() {
        return sync_time;
    } 
    public void setSyncState(int v) {
        sync_time = v;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String v) {
        title = v;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String v) {
        content = v;
    }

    public String getCreateDate() {
        return calc_date;
    }

    public void setCreateDate(String v) {
        calc_date = v;
    }

    public String getCreateTime() {
        return create_time;
    }

    public void setCreateTime(String v) {
        create_time = v;
    }
    
    public void setSendInterval(int v) {
        send_interval_time = v;
    }
    
    public void setSendDoneTime(String v) {
        send_done_time = v;
    }
    
    public int getContentType() {
        return content_type;
    }

    public void setContentType(int v) {
        content_type = v;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String v) {
        photo = v;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String v) {
        audio = v;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String v) {
        tag = v;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String v) {
        link = v;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int v) {
        deleted = v;
    }

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int v) {
        dirty = v;
    }
    
    public void getObjectItems(Object[] obj) {
        obj[0] = server_id;
        obj[1] = handle;
        obj[2] = link;
        obj[3] = title;
        obj[4] = content;
        obj[5] = calc_date;
        obj[6] = create_time;
        obj[7] = send_interval_time;
        obj[8] = send_done_time;
        obj[9] = content_type;
        obj[10] = photo;
        obj[11] = audio;
        obj[12] = tag;
        obj[13] = sync_time;
        obj[14] = dirty;
        obj[15] = deleted;
        
    }

    public void dumpRecord(Cursor c) {
        _id = c.getInt(c.getColumnIndex("id"));
        server_id = c.getInt(c.getColumnIndex("server_id"));
        handle = c.getString(c.getColumnIndex("handle"));
        link = c.getString(c.getColumnIndex("link"));
        title = c.getString(c.getColumnIndex("title"));
        content = c.getString(c.getColumnIndex("content")); 
        calc_date = c.getString(c.getColumnIndex("calc_date"));
        create_time = c.getString(c.getColumnIndex("create_time")); 
        send_interval_time = c.getInt(c.getColumnIndex("send_interval_time")); 
        send_done_time = c.getString(c.getColumnIndex("send_done_time")); 
        content_type = c.getInt(c.getColumnIndex("content_type"));
        photo = c.getString(c.getColumnIndex("photo")); 
        audio = c.getString(c.getColumnIndex("audio"));
        tag = c.getString(c.getColumnIndex("tag")); 
        sync_time = c.getLong(c.getColumnIndex("sync_time"));
        
    }
}
