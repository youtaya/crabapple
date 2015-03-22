package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.time.TimeCache;
import com.talk.demo.types.Record;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRecord extends CommonRecord {
    private Record record;
    private Field[] declaredFields;
    
    private void init() {
        record = new Record();     
        
        Class c = record.getClass();     
         
        declaredFields = c.getDeclaredFields();
    }
    
    public TimeRecord() {
    	init();
    }
    
    public Record getTimeRecord() {
    	return record;
    }
    
    public TimeRecord(Record r) {
    	init();
    	record = r;
    }
  
    public TimeRecord(TimeCache rc) {
    	init();
    	record.setDataId(rc.getId());
    	record.setContent(rc.getContent());
    	record.setCreateDate(rc.getCreateDate());
    	record.setCreateTime(rc.getCreateTime());
    	record.setContentType(rc.getMediaType());
    }
    
    public TimeRecord(String v1) {
    	init();
    	record.setContent(v1);
        record.setCreateDate(handledDate());
        record.setCreateTime(handledTime());
       
    }
    
    public TimeRecord(String v1, String date) {
    	init();
    	record.setContent(v1);
        record.setCreateDate(date);
        record.setCreateTime(handledTime());
    }
    
    public TimeRecord(String v1, Date date) {
    	init();
    	record.setContent(v1);
        record.setCreateDate(handledDate(date));
        record.setCreateTime(handledTime(date));
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


	@Override
	public int getNumItems() {
	    return declaredFields.length-1;
	}

	@Override
	public void getObjectItems(Object[] obj) {
		record.getObjectItems(obj);
	}

	@Override
	public void dumpRecord(Cursor c) {
		record.dumpRecord(c);
	}

}
