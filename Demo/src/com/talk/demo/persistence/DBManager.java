package com.talk.demo.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.talk.demo.setting.RichPresent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
  
public class DBManager {  
    private static String TAG = "DBManager";
    private DBHelper helper;  
    private SQLiteDatabase db; 
    // Get rich values
    private RichPresent rp;
    
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        /*
         * 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
         * 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
         */
        db = helper.getWritableDatabase();  
        rp = RichPresent.getInstance(context);
    }  

    public void addTime(TimeRecord tr) {
        new DataOperation(db, "records", tr).insertRecord();
        rp.addRich(2);
    }
    
    public void addFriend(FriendRecord fr) {
    	new DataOperation(db, "friends", fr).insertRecord();
    }
    
    public void addRoom(RoomRecord rr) {
    	new DataOperation(db, "rooms", rr).insertRecord();
    }
    
    public void addDialog(DialogRecord dr) {
    	new DataOperation(db, "dialogs", dr).insertRecord();
    }
    
    public void addTag(TagRecord tagr) {  
    	new DataOperation(db, "tags", tagr).insertRecord();
    }  
    
    public void addTimeFromServer(TimeRecord tr) {
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("server_id", tr.server_id);
        DataOperation doa = new DataOperation(db, "records");
        Cursor c = doa.queryCursorWithCond(sortVar);
        
    	if(c != null && c.moveToFirst()) {
    		Log.d(TAG, "No need to creat new record!");
    		doa.updateServerId(tr);
    	} else {
	        addTime(tr);
    	}
    }  
    
    public void addFriendFromServer(FriendRecord fr) {
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("server_id", fr.server_id);
        DataOperation doa = new DataOperation(db, "records");
        Cursor c = doa.queryCursorWithCond(sortVar);
        
        if(c != null && c.moveToFirst()) {
            Log.d(TAG, "No need to creat new record!");
            doa.updateServerId(fr);
        } else {
        	addFriend(fr);
        }
    }  
 
    /** 
     * add multiple time records 
     * @param TimeRecord 
     */  
    public void addMultipleTimes(List<TimeRecord> tRecord) {
        for (TimeRecord tr : tRecord) {  
        	addTime(tr);
        }  
    }
    
    public void updateContent(TimeRecord tRecord) {
    	new DataOperation(db, "records").updateContent(tRecord, tRecord.content);
        rp.addRich(1);
    }
  
    public void updateTag(int id, String tag) {
    	new DataOperation(db, "records").updateTag(id, tag);
    }
    
    public void updateServerInfo(TimeRecord tRecord) {
    	new DataOperation(db, "records").updateServerId(tRecord);
    }
    
    public void  updateFriendServerInfo(FriendRecord fRecord) {
    	new DataOperation(db, "friends").updateServerId(fRecord);
    }
    
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryTimeWithMultipleParams(String[] params) {  
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
        return trList;  
    }  
    /** 
     * query all content, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryTimeWithParam(String param) {  
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
        return trList;  
    }
    
    public List<TimeRecord> queryTimeFromOthers(String param) {  
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
        return trList;  
    }   
    
    public TimeRecord queryTimeTheParam(int param) {  
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("id", param);
        Cursor c = new DataOperation(db, "records").queryCursorWithCond(sortVar);
        
        TimeRecord tr = new TimeRecord();
        if((c != null) && c.moveToFirst()) {
            tr.dumpRecord(c);
        }
        c.close();  
        return tr;  
    }
    
    public List<TimeRecord> queryTimeTag(String param) {
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
        return trList;  
    }
    
    public FriendRecord queryFriendTheParam(int param) {  
        Map<String, Object> sortVar = new HashMap<String, Object>();
        sortVar.put("id", param);
        Cursor c = new DataOperation(db, "friends").queryCursorWithCond(sortVar);
        
        FriendRecord fr = new FriendRecord();
        if((c != null) && c.moveToFirst()) {
            fr.dumpRecord(c);
        }
        c.close();  
        return fr;  
    }
    
    /** 
     * query all time records, return list 
     * @return List<TimeRecord> 
     */  
    public List<TimeRecord> queryTime() {  
        ArrayList<TimeRecord> trList = new ArrayList<TimeRecord>();  
        String[] sortVar = {"calc_date", "create_time"};
        Cursor c = new DataOperation(db, "records").queryCursorByName(sortVar);
        while (c.moveToNext()) {  
            TimeRecord tr = new TimeRecord();  
            tr.dumpRecord(c);
            trList.add(tr);  
        }  
        c.close();  
        return trList;  
    }
    
    /** 
     * query all friend records, return list 
     * @return List<FriendRecord> 
     */  
    public List<FriendRecord> queryFriend() {  
        ArrayList<FriendRecord> frList = new ArrayList<FriendRecord>();  
        String[] sortVar = {"username"};
        Cursor c = new DataOperation(db, "friends").queryCursorByName(sortVar);
        while (c.moveToNext()) {  
            FriendRecord fr = new FriendRecord();  
            fr.dumpRecord(c);
            frList.add(fr);  
        }  
        c.close();  
        return frList;  
    } 

    public List<DialogRecord> queryDialog() {
        ArrayList<DialogRecord> drList = new ArrayList<DialogRecord>();  
        String[] sortVar = {"calc_date", "create_time"};
        Cursor c = new DataOperation(db, "dialogs").queryCursorByName(sortVar);
        while (c.moveToNext()) {  
        	DialogRecord dr = new DialogRecord();  
            dr.dumpRecord(c);
            drList.add(dr);  
        }  
        c.close();  
        return drList;  
    }
    /** 
     * query all tag records, return list 
     * @return List<TagRecord> 
     */  
    public List<TagRecord> queryTag() {  
        ArrayList<TagRecord> tagrList = new ArrayList<TagRecord>();  
        String[] sortVar = {"tagname"};
        Cursor c = new DataOperation(db, "tags").queryCursorByName(sortVar);
        
        while (c.moveToNext()) {  
        	TagRecord tagr = new TagRecord();  
        	tagr.dumpRecord(c);
        	
            tagrList.add(tagr);  
        }  
        c.close();  
        return tagrList;
    } 
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  
