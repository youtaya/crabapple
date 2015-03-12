package com.talk.demo.core;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.DialogRecord;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.talk.DialogCache;
import com.talk.demo.talk.DialogItem;
import com.talk.demo.talk.TalkViewItem;
import com.talk.demo.time.TimeCache;
import com.talk.demo.time.TimeViewItem;
import com.talk.demo.time.ViewAsItem;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.TalkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RecordManager {
	private static String TAG = "RecordManager";
	private List<TimeRecord> trlist;
	private List<DialogRecord> drlist;
	private DBManager dbMgr;
	private Context context;
	private String ownUser;
	
	public RecordManager(DBManager mgr, Context ctx) {
		trlist = new ArrayList<TimeRecord>();
		drlist = new ArrayList<DialogRecord>();
		dbMgr = mgr;
		context = ctx;
		
        Account accout = AccountUtils.getPasswordAccessibleAccount(context);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"account name: "+accout.name);
        	ownUser = accout.name;
        }
	}

	private boolean exsitRoom(List<String> room, String link) {
		if(room.isEmpty()) {
			return false;
		}
		
		for(String str: room) {
			if(str.equalsIgnoreCase(link)) {
				return true;
			}
		}
		
		return false;
	}
	
	private String getTalkObject(String sender, String link) {

        String result = new String();
        if(ownUser.equalsIgnoreCase(sender)) {
        	result = link;
        } else {
        	result = sender;
        }
        Log.d(TAG,"talk object : "+result);
        return result;
	}
	
	public ArrayList<TalkViewItem> initDataListTalk(HashMap<String, ArrayList<DialogCache>> dialog_cache) {
		ArrayList<TalkViewItem> dialog_record = new ArrayList<TalkViewItem>();
		if (!drlist.isEmpty()) {
			drlist.clear();
		}
		Log.d(TAG, "init talk list");
		
	    drlist = dbMgr.queryDialog();

		if (!dialog_record.isEmpty()) {
		    dialog_record.clear();
		}
		
		if(!dialog_cache.isEmpty()) {
			dialog_cache.clear();
		}

		List<String> roomSet = new LinkedList<String>();
		List<DialogRecord> roomlist = new ArrayList<DialogRecord>();
		
		for (DialogRecord dr : drlist) {
			String talkObj = getTalkObject(dr.getPrvDialog().getSender(), dr.getPrvDialog().getLink());
			if(!exsitRoom(roomSet, talkObj)) {
				roomSet.add(talkObj);
				
				TalkViewItem tvi = new TalkViewItem();
				
				roomlist = dbMgr.queryDialogTalkPeople(talkObj);
				
				ArrayList<DialogCache> cache = new ArrayList<DialogCache>();
				ArrayList<DialogItem> dItems = new ArrayList<DialogItem>();
				
				tvi.setTalkName(talkObj);
				
				for(DialogRecord r: roomlist) {
					DialogItem di = new DialogItem(r.getPrvDialog()._id, r.getPrvDialog().sender, r.getPrvDialog().link, 
							r.getPrvDialog().calc_date, r.getPrvDialog().create_time, r.getPrvDialog().content, r.getPrvDialog().content_type);

					di.setIntervalTime(r.getPrvDialog().send_interval_time);
					di.setDoneTime(r.getPrvDialog().send_done_time);
					
					DialogCache dc = new DialogCache();
					dc.setId(r.getPrvDialog()._id);
					dc.setSender(r.getPrvDialog().sender);
					dc.setLink(r.getPrvDialog().link);
					dc.setContent(r.getPrvDialog().content);
					dc.setCreateDate(r.getPrvDialog().calc_date);
					dc.setCreateTime(r.getPrvDialog().create_time);
					dc.setMediaType(r.getPrvDialog().content_type);
					
					dItems.add(di);
					cache.add(dc);
				}
				
				tvi.setListViewItem(dItems);
				dialog_cache.put(talkObj, cache);
				dialog_record.add(tvi);
			}
		}

		return dialog_record;
	}

	private boolean exsitDateItem(List<String> list, String date) {
		if(list.isEmpty()) {
			return false;
		}
		
		for(String str: list) {
			if(str.equalsIgnoreCase(date)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean exsitTag(List<String> list, String tag) {
		if(list.isEmpty()) {
			return false;
		}
		
		for(String str: list) {
			if(str.equalsIgnoreCase(tag)) {
				return true;
			}
		}
		
		return false;
	}
	
	String month12[] = {"Jan", "Feb", "Mar","Apr", "May", 
			"June", "July","Aug", "Sept", "Oct", "Nov", "Dec"
	};
	
	String month12_zh[] = {"1月", "2月", "3月","4月", "5月", 
			"6月", "7月","8月", "9月", "10月", "11月", "12月"
	};
	private String coverYearMonth(String yearMonth) {
		String[] temp = yearMonth.split("-");
		Log.d(TAG, "convert to : "+Integer.parseInt(temp[1]));
		return month12_zh[Integer.parseInt(temp[1])-1]+" "+temp[0];
	}
	public ArrayList<TimeViewItem> initDataListTime(HashMap<String, ArrayList<TimeCache>> record_cache, 
			boolean isLuckDay) {
		ArrayList<TimeViewItem> time_records = new ArrayList<TimeViewItem>();
		if (!trlist.isEmpty()) {
			trlist.clear();
		}
		Log.d(TAG, "init data list");
		
		if (true) {
			trlist = dbMgr.queryTime();
		} else
			trlist = dbMgr.queryTimeWithMultipleParams(TalkUtil.conditonDates());

		if (!time_records.isEmpty()) {
			time_records.clear();
		}
		
		if(!record_cache.isEmpty()) {
			record_cache.clear();
		}
		List<String> ourDateSet = new LinkedList<String>();
		List<String> ourTagSet = new LinkedList<String>();
		List<TimeRecord> tag_records = new ArrayList<TimeRecord>();
		
		for (int i = 0; i< trlist.size(); i ++) {
			TimeViewItem tvi = new TimeViewItem();
			
			TimeRecord tr = trlist.get(i);
			
			String mYearMonth = tr.getTimeRecord().calc_date.substring(0,7);
			
			if(!exsitDateItem(ourDateSet, mYearMonth)) {
				ourDateSet.add(mYearMonth);
				TimeViewItem tvi_head = new TimeViewItem();
				tvi_head.setType(0);
				tvi_head.setHeadContent(coverYearMonth(mYearMonth));
				Log.d(TAG, "put header: "+mYearMonth);
				time_records.add(tvi_head);
			}
			
			if(tr.getTimeRecord().tag != null) {
				if(!exsitTag(ourTagSet, tr.getTimeRecord().tag)) {
					ourTagSet.add(tr.getTimeRecord().tag);
					tvi.setType(2);
					Log.d(TAG, "tag is: "+tr.getTimeRecord().tag);
					
					ArrayList<TimeCache> listCache = new ArrayList<TimeCache>();
					ArrayList<ViewAsItem> listViewAsItem = new ArrayList<ViewAsItem>();
					
					tvi.setTagTitle(tr.getTimeRecord().tag);
					tag_records = dbMgr.queryTimeTag(tr.getTimeRecord().tag);
					for(TimeRecord item : tag_records) {
						TimeCache rc = new TimeCache();
						ViewAsItem vi = new ViewAsItem(item.getTimeRecord()._id, item.getTimeRecord().calc_date,item.getTimeRecord().create_time,
								item.getTimeRecord().content,item.getTimeRecord().content_type,item.getTimeRecord().photo);
						vi.setTitle(item.getTimeRecord().title);
						
						rc.setId(item.getTimeRecord()._id);
						rc.setContent(item.getTimeRecord().content);
						rc.setCreateDate(item.getTimeRecord().calc_date);
						rc.setCreateTime(item.getTimeRecord().create_time);
						rc.setMediaType(item.getTimeRecord().content_type);
						rc.setPhotoPath(item.getTimeRecord().photo);
						
						listCache.add(rc);
						listViewAsItem.add(vi);
					}
					
					record_cache.put(tr.getTimeRecord().tag, listCache);
					tvi.setListViewItem(listViewAsItem);
					time_records.add(tvi);
				}
				continue;
			}
			tvi.setType(1);
			ViewAsItem vai = new ViewAsItem(tr.getTimeRecord()._id, tr.getTimeRecord().calc_date,
					tr.getTimeRecord().create_time,tr.getTimeRecord().content,tr.getTimeRecord().content_type,tr.getTimeRecord().photo);
			vai.setTitle(tr.getTimeRecord().title);
			
			tvi.setViewItem(vai);
			time_records.add(tvi);

		}

		return time_records;
	}
	
	public ArrayList<TimeViewItem> initStoreListTime(ArrayList<TimeCache> record_cache) {
		ArrayList<TimeViewItem> time_records = new ArrayList<TimeViewItem>();
		if (!trlist.isEmpty()) {
			trlist.clear();
		}
		Log.d(TAG, "init data list");
		
		trlist = dbMgr.queryTime();

		if (!time_records.isEmpty()) {
			time_records.clear();
		}
		
		if(!record_cache.isEmpty()) {
			record_cache.clear();
		}
		
		for (int i = 0; i< trlist.size(); i ++) {
			
			TimeRecord tr = trlist.get(i);
			TimeViewItem tvi = new TimeViewItem();
			TimeCache rc = new TimeCache();
			ViewAsItem vi = new ViewAsItem(tr.getTimeRecord()._id, tr.getTimeRecord().calc_date,tr.getTimeRecord().create_time,
					tr.getTimeRecord().content,tr.getTimeRecord().content_type,tr.getTimeRecord().photo);
			rc.setId(tr.getTimeRecord()._id);
			rc.setContent(tr.getTimeRecord().content);
			rc.setCreateDate(tr.getTimeRecord().calc_date);
			rc.setCreateTime(tr.getTimeRecord().create_time);
			rc.setMediaType(tr.getTimeRecord().content_type);
			rc.setPhotoPath(tr.getTimeRecord().photo);
			
			record_cache.add(rc);
			tvi.setViewItem(vi);
			time_records.add(tvi);

		}

		return time_records;
	}
	
	public void addRecord(TimeRecord tr) {
		dbMgr.addTime(tr);
	}
	
	public void addDialog(DialogRecord dr) {
		dbMgr.addDialog(dr);
	}
}
