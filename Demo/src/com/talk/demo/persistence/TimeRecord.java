package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.time.TimeCache;
import com.talk.demo.util.RawRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRecord extends CommonRecord {
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
    
    public TimeRecord() {
    }
    
    public TimeRecord(RawRecord rr) {
    	super(rr);
        if(null != rr.getLink())
        	link = rr.getLink();
        content = rr.getContent();
        calc_date = rr.getCreateDate();
        create_time = rr.getCreateTime();
        content_type = rr.getContentType();
        title = rr.getTitle();
    }
    
    public TimeRecord(TimeCache rc) {
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
    
    public void setTitle(String v) {
    	title = v;
    }
    
    public void setSendInterval(int v) {
        send_interval_time = v;
    }
    
    public void setSendDoneTime(String v) {
        send_done_time = v;
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


	@Override
	public int getNumItems() {
		return 16;
	}

	@Override
	public void getObjectItems(Object[] obj) {
		obj[0] = super.server_id;
		obj[1] = super.handle;
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
		obj[13] = super.sync_time;
		obj[14] = super.dirty;
		obj[15] = super.deleted;
		
	}

	@Override
	public void dumpRecord(Cursor c) {
		super._id = c.getInt(c.getColumnIndex("id"));
		super.server_id = c.getInt(c.getColumnIndex("server_id"));
		super.handle = c.getString(c.getColumnIndex("handle"));
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
        super.sync_time = c.getLong(c.getColumnIndex("sync_time"));
		
	}

}
