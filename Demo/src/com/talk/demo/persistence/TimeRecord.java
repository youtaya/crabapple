package com.talk.demo.persistence;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRecord {
    public int _id;
    public String content;
    public String create_time;
    
    
    public TimeRecord() {
    }
    
    public TimeRecord(String v1) {
        content = v1;
        create_time = handledTime();
    }
    public String handledTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = new Date();
        return dateFormat.format(date);
    }
    

}
