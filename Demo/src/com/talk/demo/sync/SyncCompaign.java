package com.talk.demo.sync;

import android.text.TextUtils;
import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.RawRecord;

import java.util.ArrayList;
import java.util.List;

public class SyncCompaign {

    private static String TAG = "SyncCompaign";
    
    private static String myLog(TimeRecord tr) {
    	
    	String isDirty = (tr.dirty ==1)?"yes":"no";
    	String info = " id: "+String.valueOf(tr._id)+
    			" server id: "+String.valueOf(tr.server_id)+
    			" user name: "+tr.userName+
    			" dirty: " +isDirty;
    	return info;
    }
	/*
	 *  add for get dirty records
	 */
    public static List<RawRecord> getDirtyRecords(DBManager db) {

    	List<RawRecord> dirtyTimes = new ArrayList<RawRecord>() ;
        /*
         *  get dirty records from db
         */
    	List<TimeRecord> trlist = db.query();
    	for(TimeRecord tr: trlist) {
    		Log.d(TAG, "time record: "+myLog(tr));
    	    //check the dirty and deleted flag
    	    final boolean isDeleted = (1 == tr.deleted);
    	    final boolean isDirty = (1 == tr.dirty);
            if (isDeleted) {
                Log.i(TAG, "Contact is marked for deletion");
                RawRecord rawContact = RawRecord.createDeletedRecord(tr._id,
                        tr.server_id);
                dirtyTimes.add(rawContact);
            } else if (isDirty) {
                RawRecord rawContact = getRawRecord(db, tr._id);
                Log.i(TAG, "Contact Name: " + rawContact.getHandle());
                dirtyTimes.add(rawContact);
            }
    	}
    	 	
    	return dirtyTimes;
    }
    
    /*
     * update records from server
     */
    public static void updateRecords(DBManager db, List<RawRecord> updateRecords) {
    	/*
    	 * 1: Update server id
    	 * 2: Clear dirty flag
    	 * 3: Delete deleted record
    	 * 4: Get sync state
    	 */
        for(RawRecord rr: updateRecords) {
            TimeRecord tr = new TimeRecord(rr);
            Log.d(TAG, "server id: " + rr.getServerId());
            Log.d(TAG, "client id: " + rr.getDataId());
            Log.d(TAG, "content: " + rr.getContent());
            
            if(rr.getDataId() == -1) {
            	Log.d(TAG, "[need add] server id: " + tr.server_id);
            	db.addFromServer(tr);
            } else {
            	Log.d(TAG, "[update] server id: " + tr.server_id);
            	db.updateServerInfo(tr);
            }
            Log.d(TAG, "content type is : "+rr.getContentType());
            Log.d(TAG, "photo is : "+rr.getPhoto());
            //TODO: update photo from server
            if(rr.getContentType() == 4) {
            	if(TextUtils.isEmpty(rr.getPhoto()))
            		NetworkUtilities.downloadPhoto("20140810231230");
            	else
            		NetworkUtilities.downloadPhoto(rr.getPhoto());
            }
        }
        
    }
    
    private static RawRecord getRawRecord(DBManager db, int clientId) {
        String name = null;
        String link = null;
        String title = null;
        String content = null;
        String createDate = null;
        String createTime = null;
        int contentType = 0;
        String photo = null;
        String audio = null;
        String tag = null;
        long serverRecordId = -1;;
        long rawRecordId = -1;
        long syncState = -1;
        boolean dirty = false;
        boolean deleted = false;
        
        TimeRecord tr = db.queryTheParam(clientId);
        
        name = tr.userName;
        title = tr.title;
        content = tr.content;
        createDate = tr.calc_date;
        createTime = tr.create_time;
        contentType = tr.content_type;
        photo = tr.photo;
        audio = tr.audio;
        tag = tr.tag;
        link = tr.link;
        serverRecordId = tr.server_id;
        rawRecordId = tr._id;
        
        RawRecord rr = RawRecord.create(name, link, title, content, 
                createDate, createTime, contentType, photo, 
                audio, tag, deleted, serverRecordId, 
                rawRecordId, syncState, dirty);
        return rr;
    }
}
