package com.talk.demo.core;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.DialogRecord;
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
	private List<DialogRecord> drlist;
	private DBManager dbMgr;
	private Context context;
	public RecordManager(DBManager mgr, Context ctx) {
		trlist = new ArrayList<TimeRecord>();
		dbMgr = mgr;
		context = ctx;
	}

	public ArrayList<Map<String, String>> initDataListTalk(ArrayList<RecordCache> record_cache,
	        boolean isStore) {
		ArrayList<Map<String, String>> dialog_record = new ArrayList<Map<String, String>>();
		if (!trlist.isEmpty()) {
			trlist.clear();
		}
		Log.d(TAG, "init data list");
		
	    drlist = dbMgr.queryDialog();

		if (!dialog_record.isEmpty()) {
		    dialog_record.clear();
		}
		
		if(!record_cache.isEmpty()) {
			record_cache.clear();
		}

		for (DialogRecord dr : drlist) {
			HashMap<String, String> map = new HashMap<String, String>();
			RecordCache rc = new RecordCache();
			if (dr.content_type == TalkUtil.MEDIA_TYPE_PHOTO)
				map.put("content", "惊鸿一瞥");
			else if (dr.content_type == TalkUtil.MEDIA_TYPE_AUDIO)
				map.put("content", "口若兰花");
			else
				map.put("content", dr.content);

			rc.setId(dr._id);
			rc.setContent(dr.content);
			map.put("calc_date", dr.calc_date);
			rc.setCreateDate(dr.calc_date);
			map.put("create_time", dr.create_time);
			map.put("send_interval_time", String.valueOf(dr.send_interval_time));
			map.put("send_done_time", dr.send_done_time);
			map.put("link", dr.link);
			rc.setCreateTime(dr.create_time);
			rc.setMediaType(dr.content_type);
			record_cache.add(rc);
			dialog_record.add(map);
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
	public ArrayList<TimeViewItem> initDataListTime(HashMap<String, ArrayList<RecordCache>> record_cache, boolean isLuckDay) {
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
			
			if(tr.tag != null && !exsitTag(ourTagSet, tr.tag)) {
				ourTagSet.add(tr.tag);
				tvi.setType(2);
				Log.d(TAG, "tag is: "+tr.tag);
				
				ArrayList<RecordCache> listCache = new ArrayList<RecordCache>();
				ArrayList<ViewAsItem> listViewAsItem = new ArrayList<ViewAsItem>();
				
				tvi.setTagTitle(tr.tag);
				tag_records = dbMgr.queryTimeTag(tr.tag);
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
		dbMgr.addTime(tr);
	}
	
	public void addDialog(DialogRecord dr) {
		dbMgr.addDialog(dr);
	}
}
