package com.talk.demo.sync;

import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;
import com.talk.demo.types.Friend;
import com.talk.demo.types.Group;

import java.util.ArrayList;
import java.util.List;

public class SyncCompaign2 {

    private static String TAG = "SyncCompaign2";
    
    private static String myLog(FriendRecord fr) {
        
        return fr.getFriend().toString();
    }
	/*
	 *  add for get dirty friends
	 */
    public static List<Friend> getDirtyFriends(DBManager db) {

    	List<Friend> dirtyFriends = new ArrayList<Friend>() ;
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
                Friend friend = Friend.createDeletedFriend(fr.getFriend().getDataId(),
                        fr.getFriend().getServerId());
                dirtyFriends.add(friend);
            } else if (isDirty) {
                Friend rawFriend = getRawFriend(db, fr.getFriend().getDataId());
                Log.i(TAG, "friend Name: " + rawFriend.getUserName());
                dirtyFriends.add(rawFriend);
            }
    	}
    	 	
    	return dirtyFriends;
    }
    
    /*
     * update records from server
     */
    public static void updateFriends(DBManager db, Group<Friend> updateFriends) {
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
    
    private static Friend getRawFriend(DBManager db, int clientId) {
        
        FriendRecord fr = db.queryFriendTheParam(clientId);
        
        return fr.getFriend();
    }
}
