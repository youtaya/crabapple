package com.talk.demo.persistence;

import com.talk.demo.util.RawRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRecord {
    public int _id;
    public int server_id;
    public String content;
    public String calc_date;
    public String create_time;
    public int content_type;
    
    public String userName;
    public String link;
    public String title = "time";
    public String photo;
    public String audio;
    public String tag;
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
    
    public TimeRecord() {
    }
    
    public TimeRecord(RawRecord rr) {
        _id = (int)rr.getRawContactId();
        server_id = (int)rr.getServerContactId();
        if(null != rr.getLink())
        	link = rr.getLink();
        content = rr.getContent();
        calc_date = rr.getCreateDate();
        create_time = rr.getCreateTime();
        content_type = rr.getContentType();
        title = rr.getTitle();
        sync_time = rr.getSyncState();
    }
    
    public TimeRecord(RecordCache rc) {
    	_id = rc.getId();
    	content = rc.getContent();
    	calc_date = rc.getCreateDate();
    	create_time = rc.getCreateTime();
    	content_type = rc.getMediaType();
    }
    
    public TimeRecord(String v1) {
        content = v1;
        calc_date = handledDate();
        create_time = handledTime();
       
    }
    
    public TimeRecord(String v1, String date) {
        content = v1;
        calc_date = date;
        create_time = handledTime();
    }
    
    public TimeRecord(String v1, Date date) {
        content = v1;
        calc_date = handledDate(date);
        create_time = handledTime(date);
    }
    
    public void setLink(String v) {
    	link = v;
    }
    public void setContent(String v) {
    	content = v;
    }
    public void setContentType(int type) {
        content_type = type;
    }
    
    public void setPhoto(String photoPath) {
    	photo = photoPath;
    }
    
    public void setTag(String pTag) {
        tag = pTag;
    }
    
    public String handledDate() {
        Date date = new Date();
        return handledDate(date);
    }
    
    public String handledDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        return dateFormat.format(date);
    }
    
    public String handledTime() {
        Date date = new Date();
        return handledTime(date);
    }
    
    public String handledTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS"); 
        return dateFormat.format(date);
    }

}
