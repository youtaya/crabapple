package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.time.TimeCache;
import com.talk.demo.types.PrvDialog;
import com.talk.demo.util.RawDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogRecord extends CommonRecord {
    public String content;
    public String calc_date;
    public String create_time;
    public int send_interval_time;
    public String send_done_time;
    public int content_type;
    
    public String room_name;
    public String sender;
    public String link;
    public String photo;
    public String audio;
    
    
    public DialogRecord() {
    }
    
    public DialogRecord(RawDialog rr) {
    	super(rr);
    	if(null != rr.getSender())
    		sender = rr.getSender();
        if(null != rr.getLink())
        	link = rr.getLink();
        content = rr.getContent();
        calc_date = rr.getCreateDate();
        create_time = rr.getCreateTime();
        content_type = rr.getContentType();
        room_name = rr.getRoomName();
    }
    
    public DialogRecord(PrvDialog rr) {
    	if(null != rr.getSender())
    		sender = rr.getSender();
        if(null != rr.getLink())
        	link = rr.getLink();
        content = rr.getContent();
        calc_date = rr.getCreateDate();
        create_time = rr.getCreateTime();
        content_type = rr.getContentType();
        room_name = rr.getRoomName();
    }
    
    public DialogRecord(TimeCache rc) {
    	_id = rc.getId();
    	content = rc.getContent();
    	calc_date = rc.getCreateDate();
    	create_time = rc.getCreateTime();
    	content_type = rc.getMediaType();
    }
    
    public DialogRecord(String v1) {
        content = v1;
        calc_date = handledDate();
        create_time = handledTime();
       
    }
    
    public DialogRecord(String v1, String date) {
        content = v1;
        calc_date = date;
        create_time = handledTime();
    }
    
    public DialogRecord(String v1, Date date) {
        content = v1;
        calc_date = handledDate(date);
        create_time = handledTime(date);
    }
    
    public void setSender(String v) {
    	sender = v;
    }  
    
    public void setLink(String v) {
    	link = v;
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
		obj[2] = room_name;
		obj[3] = sender;
		obj[4] = link;
		obj[5] = content;
		obj[6] = calc_date;
		obj[7] = create_time;
		obj[8] = send_interval_time;
		obj[9] = send_done_time;
		obj[10] = content_type;
		obj[11] = photo;
		obj[12] = audio;
		obj[13] = super.sync_time;
		obj[14] = super.dirty;
		obj[15] = super.deleted;
		
	}

	@Override
	public void dumpRecord(Cursor c) {
		super._id = c.getInt(c.getColumnIndex("id"));
		super.server_id = c.getInt(c.getColumnIndex("server_id"));
		super.handle = c.getString(c.getColumnIndex("handle"));
		sender = c.getString(c.getColumnIndex("sender"));
        link = c.getString(c.getColumnIndex("link"));
        room_name = c.getString(c.getColumnIndex("roomname"));
        content = c.getString(c.getColumnIndex("content")); 
        calc_date = c.getString(c.getColumnIndex("calc_date"));
        create_time = c.getString(c.getColumnIndex("create_time")); 
        send_interval_time = c.getInt(c.getColumnIndex("send_interval_time")); 
        send_done_time = c.getString(c.getColumnIndex("send_done_time")); 
        content_type = c.getInt(c.getColumnIndex("content_type"));
        photo = c.getString(c.getColumnIndex("photo")); 
        audio = c.getString(c.getColumnIndex("audio"));
        super.sync_time = c.getLong(c.getColumnIndex("sync_time"));
		
	}

}
