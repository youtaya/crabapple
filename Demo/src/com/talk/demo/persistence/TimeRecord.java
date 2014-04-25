package com.talk.demo.persistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class TimeRecord {
    public int _id;
    public String content;
    public String create_date;
    public String create_time;
    public int content_type;
    
    public String userName;
    public String title;
    public String photo;
    public String audio;
    public String status;
    public int deleted;
    
    public TimeRecord() {
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
