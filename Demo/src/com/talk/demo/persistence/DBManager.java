package com.talk.demo.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

import com.talk.demo.setting.RichPresent;
import com.talk.demo.types.Friend;
import com.talk.demo.types.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
  
public class DBManager {  
    private static String TAG = "DBManager";
    private DBHelper helper;  
    // Get rich values
    private RichPresent rp;
    
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        rp = RichPresent.getInstance(context);
    }  

    public void addTime(TimeRecord tr) {
        /*
         * 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
         * 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
         */
        SQLiteDatabase db = helper.getWritableDatabase();
    	new DataOperation(db, "records", tr).insertRecord();
    	Log.d(TAG, "record insert: "+tr.getTimeRecord().toString());
        rp.addRich(2);
        db.close();
    }
    
    public void addFriend(FriendRecord fr) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	new DataOperation(db, "friends", fr).insertRecord();
    	db.close();
    }
    
    public void addRoom(RoomRecord rr) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	new DataOperation(db, "rooms", rr).insertRecord();
    	db.close();
    }
    
    public void addDialog(DialogRecord dr) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	new DataOperation(db, "dialogs", dr).insertRecord();
    	db.close();
    }
    
    public void addTag(TagRecord tagr) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	new DataOperation(db, "tags", tagr).insertRecord();
    	db.close();
    }  
    
    public void addTimeFromServer(TimeRecord tr) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("server_id", tr.getTimeRecord().getServerId());
        DataOperation doa = new DataOperation(db, "records");
        Cursor c = doa.queryCursorWithCond(sortVar);
        
    	if(c != null && c.moveToFirst()) {
    		Log.d(TAG, "No need to creat new record!");
    		doa.updateServerId(tr.getTimeRecord().getServerId(),tr.getTimeRecord().getDataId(),tr.getTimeRecord().getSyncState());
    	} else {
	        addTime(tr);
    	}
    	db.close();
    }  
    
    public void addFriendFromServer(FriendRecord fr) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("server_id", fr.getFriend().getServerId());
        DataOperation doa = new DataOperation(db, "friends");
        Cursor c = doa.queryCursorWithCond(sortVar);
        
        if(c != null && c.moveToFirst()) {
            Log.d(TAG, "No need to creat new record!");
            doa.updateServerId(fr.getFriend().getServerId(),fr.getFriend().getDataId(),fr.getFriend().getSyncState());
        } else {
        	addFriend(fr);
        }
        db.close();
    }  
 
    /** 
     * add multiple time records 
     * @param TimeRecord 
     */  
    public void addMultipleTimes(List<TimeRecord> tRecord) {
        SQLiteDatabase db = helper.getWritableDatabase();
        for (TimeRecord tr : tRecord) {  
        	addTime(tr);
        }
        db.close();
    }
    
    public void updateContent(TimeRecord tRecord) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	new DataOperation(db, "records").updateContent(tRecord.getTimeRecord().getDataId(), tRecord.getTimeRecord().getContent());
        rp.addRich(1);
        db.close();
    }
  
    public void updateTag(int id, String tag) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	new DataOperation(db, "records").updateTag(id, tag);
    	db.close();
    }
    
    public void updateDescription(int id, String des) {
        SQLiteDatabase db = helper.getWritableDatabase();
        new DataOperation(db, "friends").updateDescription(id, des);
        db.close();
    }
    
    public void updateServerInfo(Record record) {
        SQLiteDatabase db = helper.getWritableDatabase();
        DataOperation dop = new DataOperation(db, "records", new TimeRecord(record));
    	dop.updateServerId(
    			record.getServerId(),record.getDataId(),record.getSyncState());
    	db.close();
    }
    
    public void  updateFriendServerInfo(Friend friend) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	new DataOperation(db, "friends").updateServerId(
    	        friend.getServerId(),friend.getDataId(),friend.getSyncState());
    	db.close();
    }
    
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryTimeWithMultipleParams(String[] params) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        String[] whereVar = {"calc_date","calc_date","calc_date","calc_date"};
        String[] sortVar = {"calc_date", "create_time"};
        Cursor c = new DataOperation(db, "records").queryCursorWithComplexCond(whereVar,sortVar, params);
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr.dumpRecord(c);
            trList.add(tr);  
        }  
        c.close();
        db.close();
        return trList;  
    }  
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryTimeWithParam(String param) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        String[] whereVar = {"calc_date"};
        String[] sortVar = {"calc_date", "create_time"};
        Cursor c = new DataOperation(db, "records").queryCursorWithComplexCond(whereVar,sortVar, new String[]{param});
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr.dumpRecord(c);
            trList.add(tr);  
        }  
        c.close();
        db.close();
        return trList;  
    }
    
    public List<TimeRecord> queryTimeFromOthers(String param) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        String[] whereVar = {"link!"};
        String[] sortVar = {"calc_date", "create_time"};
        Cursor c = new DataOperation(db, "records").queryCursorWithComplexCond(whereVar,sortVar, new String[]{param});
        
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr.dumpRecord(c);
            trList.add(tr);  
        }  
        c.close();
        db.close();
        return trList;  
    }   
    
    public TimeRecord queryTimeTheParam(int param) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("id", param);
        Cursor c = new DataOperation(db, "records").queryCursorWithCond(sortVar);
        
        TimeRecord tr = new TimeRecord();
        if((c != null) && c.moveToFirst()) {
            tr.dumpRecord(c);
        }
        c.close();
        db.close();
        return tr;  
    }
    
    public List<TimeRecord> queryTimeTag(String param) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("tag", param);
        Cursor c = new DataOperation(db, "records").queryCursorWithCond(sortVar);
        
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr.dumpRecord(c);
            trList.add(tr);  
        }  
        c.close();
        db.close();
        return trList;  
    }
    
    public FriendRecord queryFriendTheParam(int param) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("id", param);
        Cursor c = new DataOperation(db, "friends").queryFriendCursorWithCond(sortVar);
        
        FriendRecord fr = new FriendRecord();
        if((c != null) && c.moveToFirst()) {
            fr.dumpRecord(c);
        }
        c.close();
        db.close();
        return fr;  
    }
    
    public List<DialogRecord> queryDialogTalkPeople(String param) {
        SQLiteDatabase db = helper.getWritableDatabase();
    	ArrayList<DialogRecord> drList = new ArrayList<DialogRecord>();  
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("link", param);
        sortVar.put("sender", param);
        Cursor c = new DataOperation(db, "dialogs").queryCursorWithCondOR(sortVar);
        
        while (c.moveToNext()) {  
        	DialogRecord dr = new DialogRecord();  
            dr.dumpRecord(c);
            drList.add(dr);  
        }  
        c.close();
        db.close();
        return drList;  
    }
    
    /** 
     * query all time records, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryTime() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        String[] sortVar = {"calc_date", "create_time"};
        Cursor c = new DataOperation(db, "records").queryCursorByName(sortVar);
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr.dumpRecord(c);
            Log.d(TAG, "record query: "+tr.getTimeRecord().toString());
            trList.add(tr);  
        }  
        c.close();
        db.close();
        return trList;  
    }
    
    /** 
     * query all friend records, return list 
     * @return List<FriendRecord> 
     */  
    public List<FriendRecord> queryFriend() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<FriendRecord> frList = new ArrayList<FriendRecord>();  
        String[] sortVar = {"username"};
        Cursor c = new DataOperation(db, "friends").queryCursorByName(sortVar);
        while (c.moveToNext()) {  
            FriendRecord fr = new FriendRecord();  
            fr.dumpRecord(c);
            frList.add(fr);  
        }  
        c.close();
        db.close();
        return frList;  
    } 

    public List<DialogRecord> queryDialog() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<DialogRecord> drList = new ArrayList<DialogRecord>();  
        String[] sortVar = {"calc_date", "create_time"};
        Cursor c = new DataOperation(db, "dialogs").queryCursorByName(sortVar);
        while (c.moveToNext()) {  
        	DialogRecord dr = new DialogRecord();  
            dr.dumpRecord(c);
            drList.add(dr);  
        }  
        c.close(); 
        db.close();
        return drList;  
    }
    /** 
     * query all tag records, return list 
     * @return List<TagRecord> 
     */  
    public List<TagRecord> queryTag() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<TagRecord> tagrList = new ArrayList<TagRecord>();  
        String[] sortVar = {"tagname"};
        Cursor c = new DataOperation(db, "tags").queryCursorByName(sortVar);
        
        while (c.moveToNext()) {  
        	TagRecord tagr = new TagRecord();  
        	tagr.dumpRecord(c);
        	
            tagrList.add(tagr);  
        }  
        c.close();
        db.close();
        return tagrList;
    } 
}  
