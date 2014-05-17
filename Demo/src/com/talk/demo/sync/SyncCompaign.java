package com.talk.demo.sync;

import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.RawRecord;

import java.util.ArrayList;
import java.util.List;

public class SyncCompaign {

    private static String TAG = "SyncCompaign";
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
                Log.i(TAG, "Contact Name: " + rawContact.getUserName());
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
            Log.d(TAG, "server id: " + rr.getServerContactId());
            db.updateServerInfo(tr);
        }
        
    }
    
    private static RawRecord getRawRecord(DBManager db, int clientId) {
        String name = null;
        String title = null;
        String content = null;
        String createDate = null;
        String createTime = null;
        int contentType = 0;
        String photo = null;
        String audio = null;
        String status = null;
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
        status = tr.status;
        serverRecordId = tr.server_id;
        rawRecordId = tr._id;
        
        RawRecord rr = RawRecord.create(name, title, content, 
                createDate, createTime, contentType, photo, 
                audio, status, deleted, serverRecordId, 
                rawRecordId, syncState, dirty);
        return rr;
    }
}
