package com.talk.demo.sync;

import android.text.TextUtils;
import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.types.Record;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.RawRecord;

import java.util.ArrayList;
import java.util.List;

public class SyncCompaign {

    private static String TAG = "SyncCompaign";
    
    private static String myLog(TimeRecord tr) {
    	
    	String isDirty = (tr.getTimeRecord().getDirty() ==1)?"yes":"no";
    	String info = " id: "+String.valueOf(tr.getTimeRecord().getDataId())+
    			" server id: "+String.valueOf(tr.getTimeRecord().getServerId())+
    			" user name: "+tr.getTimeRecord().getHandle()+
    			" dirty: " +isDirty;
    	return info;
    }
	/*
	 *  add for get dirty records
	 */
    public static List<Record> getDirtyRecords(DBManager db) {

    	List<Record> dirtyTimes = new ArrayList<Record>() ;
        /*
         *  get dirty records from db
         */
    	List<TimeRecord> trlist = db.queryTime();
    	for(TimeRecord tr: trlist) {
    		Log.d(TAG, "time record: "+myLog(tr));
    	    //check the dirty and deleted flag
    	    final boolean isDeleted = (1 == tr.getTimeRecord().getDeleted());
    	    final boolean isDirty = (1 == tr.getTimeRecord().getDirty());
            if (isDeleted) {
                Log.i(TAG, "Contact is marked for deletion");
                Record rawContact = Record.createDeletedRecord(tr.getTimeRecord().getDataId(),
                        tr.getTimeRecord().getServerId());
                dirtyTimes.add(rawContact);
            } else if (isDirty) {
                Record rawContact = getRawRecord(db, tr.getTimeRecord().getDataId());
                Log.i(TAG, "Contact Name: " + rawContact.getHandle());
                dirtyTimes.add(rawContact);
            }
    	}
    	 	
    	return dirtyTimes;
    }
    
    /*
     * update records from server
     */
    public static void updateRecords(DBManager db, List<Record> updateRecords) {
    	/*
    	 * 1: Update server id
    	 * 2: Clear dirty flag
    	 * 3: Delete deleted record
    	 * 4: Get sync state
    	 */
        for(Record rr: updateRecords) {
            TimeRecord tr = new TimeRecord(rr);
            Log.d(TAG, "server id: " + rr.getServerId());
            Log.d(TAG, "client id: " + rr.getDataId());
            Log.d(TAG, "content: " + rr.getContent());
            
            if(rr.getDataId() == -1) {
            	Log.d(TAG, "[need add] server id: " + tr.getTimeRecord().server_id);
            	db.addTimeFromServer(tr);
            } else {
            	Log.d(TAG, "[update] server id: " + tr.getTimeRecord().server_id);
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
    
    private static Record getRawRecord(DBManager db, int clientId) {
   
        
        TimeRecord tr = db.queryTimeTheParam(clientId);
        
        return tr.getTimeRecord();
    }
}
