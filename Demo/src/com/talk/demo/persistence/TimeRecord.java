package com.talk.demo.persistence;

import com.talk.demo.util.RawRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class TimeRecord {
    public int _id;
    public int server_id;
    public String content;
    public String create_date;
    public String create_time;
    public int content_type;
    
    public String userName;
    public String title;
    public String photo;
    public String audio;
    public String status;
    public String sync_time;
    public int deleted;
    public int dirty;
    
    public TimeRecord() {
    }
    
    public TimeRecord(RawRecord rr) {
        _id = (int)rr.getRawContactId();
        server_id = (int)rr.getServerContactId();
        content = rr.getContent();
        create_date = rr.getCreateDate();
        create_time = rr.getCreateTime();
        content_type = rr.getContentType();
    }
    
    public TimeRecord(RecordCache rc) {
    	_id = rc.getId();
    	content = rc.getContent();
    	create_date = rc.getCreateDate();
    	create_time = rc.getCreateTime();
    	content_type = rc.getMediaType();
    }
    
    public TimeRecord(String v1) {
        content = v1;
        create_date = handledDate();
        create_time = handledTime();
       
    }
    
    public TimeRecord(String v1, String date) {
        content = v1;
        create_date = date;
        create_time = handledTime();
    }
    public void setContent(String v) {
    	content = v;
    }
    public void setContentType(int type) {
        content_type = type;
    }
    
    public String handledDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public String handledTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS"); 
        Date date = new Date();
        return dateFormat.format(date);
    }

}
