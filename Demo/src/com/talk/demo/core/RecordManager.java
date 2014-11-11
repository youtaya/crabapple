package com.talk.demo.core;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.time.TimeViewItem;
import com.talk.demo.time.ViewAsItem;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.TalkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecordManager {
	private static String TAG = "RecordManager";
	private List<TimeRecord> trlist;
	private DBManager dbMgr;
	private Context context;
	public RecordManager(DBManager mgr, Context ctx) {
		trlist = new ArrayList<TimeRecord>();
		dbMgr = mgr;
		context = ctx;
	}

	public ArrayList<Map<String, String>> initDataListTalk(ArrayList<RecordCache> record_cache,
	        boolean isStore) {
		ArrayList<Map<String, String>> time_record = new ArrayList<Map<String, String>>();
		if (!trlist.isEmpty()) {
			trlist.clear();
		}
		Log.d(TAG, "init data list");
		
		if(isStore) { 
		    trlist = dbMgr.query();
		} else {
            Account accout = AccountUtils.getPasswordAccessibleAccount(context);
            if (accout != null && !TextUtils.isEmpty(accout.name)) {
            	Log.d(TAG,"ccount name: "+accout.name);
            }
            
    		trlist = dbMgr.queryFromOthers(accout.name);
		}

		if (!time_record.isEmpty()) {
			time_record.clear();
		}
		
		if(!record_cache.isEmpty()) {
			record_cache.clear();
		}

		for (TimeRecord tr : trlist) {
			HashMap<String, String> map = new HashMap<String, String>();
			RecordCache rc = new RecordCache();
			if (tr.content_type == TalkUtil.MEDIA_TYPE_PHOTO)
				map.put("content", "惊鸿一瞥");
			else if (tr.content_type == TalkUtil.MEDIA_TYPE_AUDIO)
				map.put("content", "口若兰花");
			else
				map.put("content", tr.content);

			rc.setId(tr._id);
			rc.setContent(tr.content);
			map.put("calc_date", tr.calc_date);
			rc.setCreateDate(tr.calc_date);
			map.put("create_time", tr.create_time);
			map.put("send_interval_time", String.valueOf(tr.send_interval_time));
			map.put("send_done_time", tr.send_done_time);
			map.put("link", tr.link);
			rc.setCreateTime(tr.create_time);
			rc.setMediaType(tr.content_type);
			record_cache.add(rc);
			time_record.add(map);
		}

		return time_record;
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
	public ArrayList<TimeViewItem> initDataListTime(HashMap<String, ArrayList<RecordCache>> record_cache, boolean isLuckDay) {
		ArrayList<TimeViewItem> time_records = new ArrayList<TimeViewItem>();
		if (!trlist.isEmpty()) {
			trlist.clear();
		}
		Log.d(TAG, "init data list");
		
		if (true) {
			trlist = dbMgr.query();
		} else
			trlist = dbMgr.queryWithMultipleParams(TalkUtil.conditonDates());

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
			
			if(tr.tag != null && !exsitTag(ourTagSet, tr.tag)) {
				ourTagSet.add(tr.tag);
				tvi.setType(2);
				Log.d(TAG, "tag is: "+tr.tag);
				
				ArrayList<RecordCache> listCache = new ArrayList<RecordCache>();
				ArrayList<ViewAsItem> listViewAsItem = new ArrayList<ViewAsItem>();
				
				tvi.setTagTitle(tr.tag);
				tag_records = dbMgr.queryTag(tr.tag);
				for(TimeRecord item : tag_records) {
					RecordCache rc = new RecordCache();
					ViewAsItem vi = new ViewAsItem(item._id, item.calc_date,item.create_time,item.content,item.content_type,item.photo);
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
				continue;
			}
			tvi.setType(1);
			ViewAsItem vai = new ViewAsItem(tr._id, tr.calc_date,tr.create_time,tr.content,tr.content_type,tr.photo);
			tvi.setViewItem(vai);
			time_records.add(tvi);

		}

		return time_records;
	}
	
	public void addRecord(TimeRecord tr) {
		dbMgr.add(tr);
	}
}
