package com.talk.demo.sync;

import com.talk.demo.util.RawRecord;

import java.util.ArrayList;
import java.util.List;

public class SyncCompaign {

	/*
	 *  add for get dirty records
	 */
    public static List<RawRecord> getDirtyRecords() {
    	/*
    	 *  get dirty records from db
    	 */
    	List<RawRecord> dirtyTimes = new ArrayList<RawRecord>() ;
    	String name = "jinxp";
    	String title = "hw";
    	String content = "ok,just it";
    	String createDate = "2014-4-26";
    	String createTime = "2014-4-26";
    	int contentType = 1;
    	boolean deleted = false;
    	
    	RawRecord rr = new RawRecord(name, title, content, createDate,
                createTime, contentType, null, null,
                null, deleted, 1,
                1, -1, true);
    	dirtyTimes.add(rr);
    	
    	return dirtyTimes;
    }
    
    /*
     * update records from server
     */
    public static void updateRecords(List<RawRecord> updateRecords) {
    	/*
    	 * clear dirty flag
    	 */
    }
}
