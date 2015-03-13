package com.talk.demo.persistence;

import android.database.Cursor;

import com.talk.demo.time.TimeCache;
import com.talk.demo.types.PrvDialog;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DialogRecord extends CommonRecord {
    
    private PrvDialog dialogData;
    private Field[] declaredFields;
    
    public DialogRecord() {
        init();
    }

    private void init() {
        dialogData = new PrvDialog();     
        
        Class c = dialogData.getClass();     
         
        declaredFields = c.getDeclaredFields();
    }
    public PrvDialog getPrvDialog() {
    	return dialogData;
    }
    
    public DialogRecord(PrvDialog pd) {
        init();
        dialogData = pd;
    }
    
    public DialogRecord(TimeCache rc) {
    	dialogData.setDataId(rc.getId());
    	dialogData.setContent(rc.getContent());
    	dialogData.setCreateDate(rc.getCreateDate());
    	dialogData.setCreateTime(rc.getCreateTime());
    	dialogData.setContentType(rc.getMediaType());
    }
    
    public DialogRecord(String v1) {
        init();
    	dialogData.setContent(v1);
        dialogData.setCreateDate(handledDate());
        dialogData.setCreateTime(handledTime());
    }
    
    public DialogRecord(String v1, String date) {
    	dialogData.setContent(v1);
        dialogData.setCreateDate(date);
        dialogData.setCreateTime(handledTime());
    }
    
    public DialogRecord(String v1, Date date) {
    	dialogData.setContent(v1);
        dialogData.setCreateDate(handledDate(date));
        dialogData.setCreateTime(handledTime(date));
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

	    dialogData.getObjectItems(obj);
	}

	@Override
	public void dumpRecord(Cursor c) {
	    
	    dialogData.dumpRecord(c);
		
	}

}
