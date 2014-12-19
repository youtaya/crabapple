package com.talk.demo.core;

import android.content.Context;
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
	
	public RecordManager(DBManager mgr, Context ctx) {
		trlist = new ArrayList<TimeRecord>();
		drlist = new ArrayList<DialogRecord>();
		dbMgr = mgr;
		context = ctx;
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
			if(!exsitRoom(roomSet, dr.link)) {
				roomSet.add(dr.link);
				
				TalkViewItem tvi = new TalkViewItem();
				ArrayList<DialogCache> cache = new ArrayList<DialogCache>();
				roomlist = dbMgr.queryDialogLink(dr.link);
				
				ArrayList<DialogItem> dItems = new ArrayList<DialogItem>();
				
				tvi.setLinkName(dr.link);
				
				for(DialogRecord r: roomlist) {
					DialogItem di = new DialogItem(r._id, r.sender, r.link, r.direct,  
							r.calc_date, r.create_time, r.content, r.content_type);

					di.setIntervalTime(r.send_interval_time);
					di.setDoneTime(r.send_done_time);
					
					DialogCache dc = new DialogCache();
					dc.setId(r._id);
					dc.setLink(r.link);
					dc.setDirect(r.direct);
					dc.setContent(r.content);
					dc.setCreateDate(r.calc_date);
					dc.setCreateTime(r.create_time);
					dc.setMediaType(r.content_type);
					
					dItems.add(di);
					cache.add(dc);
				}
				
				tvi.setListViewItem(dItems);
				dialog_cache.put(dr.link, cache);
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
			
			String mYearMonth = tr.calc_date.substring(0,7);
			
			if(!exsitDateItem(ourDateSet, mYearMonth)) {
				ourDateSet.add(mYearMonth);
				TimeViewItem tvi_head = new TimeViewItem();
				tvi_head.setType(0);
				tvi_head.setHeadContent(coverYearMonth(mYearMonth));
				Log.d(TAG, "put header: "+mYearMonth);
				time_records.add(tvi_head);
			}
			
			if(tr.tag != null) {
				if(!exsitTag(ourTagSet, tr.tag)) {
					ourTagSet.add(tr.tag);
					tvi.setType(2);
					Log.d(TAG, "tag is: "+tr.tag);
					
					ArrayList<TimeCache> listCache = new ArrayList<TimeCache>();
					ArrayList<ViewAsItem> listViewAsItem = new ArrayList<ViewAsItem>();
					
					tvi.setTagTitle(tr.tag);
					tag_records = dbMgr.queryTimeTag(tr.tag);
					for(TimeRecord item : tag_records) {
						TimeCache rc = new TimeCache();
						ViewAsItem vi = new ViewAsItem(item._id, item.calc_date,item.create_time,
								item.content,item.content_type,item.photo);
						rc.setId(item._id);
						rc.setContent(item.content);
						rc.setCreateDate(item.calc_date);
						rc.setCreateTime(item.create_time);
						rc.setMediaType(item.content_type);
						rc.setPhotoPath(item.photo);
						
						listCache.add(rc);
						listViewAsItem.add(vi);
					}
					
					record_cache.put(tr.tag, listCache);
					tvi.setListViewItem(listViewAsItem);
					time_records.add(tvi);
				}
				continue;
			}
			tvi.setType(1);
			ViewAsItem vai = new ViewAsItem(tr._id, tr.calc_date,tr.create_time,tr.content,tr.content_type,tr.photo);
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
			ViewAsItem vi = new ViewAsItem(tr._id, tr.calc_date,tr.create_time,
					tr.content,tr.content_type,tr.photo);
			rc.setId(tr._id);
			rc.setContent(tr.content);
			rc.setCreateDate(tr.calc_date);
			rc.setCreateTime(tr.create_time);
			rc.setMediaType(tr.content_type);
			rc.setPhotoPath(tr.photo);
			
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
