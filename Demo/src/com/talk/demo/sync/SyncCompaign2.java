package com.talk.demo.sync;

import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;
import com.talk.demo.types.Friend;
import com.talk.demo.util.RawFriend;

import java.util.ArrayList;
import java.util.List;

public class SyncCompaign2 {

    private static String TAG = "SyncCompaign2";
    
    private static String myLog(FriendRecord fr) {
        
        String isDirty = (fr.getFriend().getDirty() ==1)?"yes":"no";
    	String info = " id: "+String.valueOf(fr.getFriend().getDataId())+
    			" server id: "+String.valueOf(fr.getFriend().getServerId())+
    			" user name: "+fr.getFriend().getUserName()+
                " dirty: " +isDirty;
    	return info;
    }
	/*
	 *  add for get dirty friends
	 */
    public static List<RawFriend> getDirtyFriends(DBManager db) {

    	List<RawFriend> dirtyFriends = new ArrayList<RawFriend>() ;
        /*
         *  get dirty records from db
         */
    	List<FriendRecord> frlist = db.queryFriend();
    	for(FriendRecord fr: frlist) {
    		Log.d(TAG, "friend record: "+myLog(fr));
    	    //check the dirty and deleted flag
    	    final boolean isDeleted = (1 == fr.getFriend().getDeleted());
    	    final boolean isDirty = (1 == fr.getFriend().getDirty());
            if (isDeleted) {
                Log.i(TAG, "friend is marked for deletion");
                RawFriend rawFriend = RawFriend.createDeletedFriend(fr.getFriend().getDataId(),
                        fr.getFriend().getServerId());
                dirtyFriends.add(rawFriend);
            } else if (isDirty) {
                RawFriend rawFriend = getRawFriend(db, fr.getFriend().getDataId());
                Log.i(TAG, "friend Name: " + rawFriend.getUserName());
                dirtyFriends.add(rawFriend);
            }
    	}
    	 	
    	return dirtyFriends;
    }
    
    /*
     * update records from server
     */
    public static void updateFriends(DBManager db, List<Friend> updateFriends) {
    	/*
    	 * 1: Update server id
    	 * 2: Clear dirty flag
    	 * 3: Delete deleted record
    	 * 4: Get sync state
    	 */
        for(Friend rf: updateFriends) {
            FriendRecord fr = new FriendRecord(rf);
            Log.d(TAG, "server id: " + rf.getServerId());
            Log.d(TAG, "client id: " + rf.getDataId());
            
            if(rf.getDataId() == -1) {
            	Log.d(TAG, "[need add] server id: " + fr.getFriend().server_id);
            	db.addFriendFromServer(fr);
            } else {
            	Log.d(TAG, "[update] server id: " + fr.getFriend().server_id);
            	db.updateFriendServerInfo(fr);
            }

        }
        
    }
    
    private static RawFriend getRawFriend(DBManager db, int clientId) {
        String name = null;
        String handle = null;
        String phone = null;
        String avatar = null;
        String description = null;
        long serverRecordId = -1;;
        long rawRecordId = -1;
        long syncState = -1;
        boolean dirty = false;
        boolean deleted = false;
        
        FriendRecord fr = db.queryFriendTheParam(clientId);
        
        name = fr.getFriend().getUserName();
        handle = fr.getFriend().getHandle();
        phone = fr.getFriend().getPhoneMobile();
        serverRecordId = fr.getFriend().getServerId();
        avatar = fr.getFriend().getAvatar();
        description = fr.getFriend().getDescription();
        rawRecordId = fr.getFriend().getDataId();
        
        RawFriend rf = RawFriend.create(name, handle, phone, avatar, description, 
        		serverRecordId, rawRecordId);
        return rf;
    }
}
