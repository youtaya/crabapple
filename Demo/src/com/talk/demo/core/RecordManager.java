package com.talk.demo.core;

import android.util.Log;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.TalkUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecordManager {
	private static String TAG = "RecordManager";
	private List<TimeRecord> trlist;
	private DBManager dbMgr;
	public RecordManager(DBManager mgr) {
		trlist = new ArrayList<TimeRecord>();
		dbMgr = mgr;
	}

	public ArrayList<HashMap<String, Object>> initDataListRecord(int[] status) {
		ArrayList<HashMap<String, Object>> time_record = new ArrayList<HashMap<String, Object>>();
		if (!trlist.isEmpty()) {
			trlist.clear();
		}
		Log.d(TAG, "init data list");
		trlist = dbMgr.query();

		if (!time_record.isEmpty()) {
			time_record.clear();
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = TalkUtil.Cal_Days(new Date(), -1);
		Date date2 = TalkUtil.Cal_Days(new Date(), -3);
		Date date3 = TalkUtil.Cal_Days(new Date(), -5);
		Date date4 = TalkUtil.Cal_Days(new Date(), -7);
		String revealDate1 = dateFormat.format(date1);
		String revealDate2 = dateFormat.format(date2);
		String revealDate3 = dateFormat.format(date3);
		String revealDate4 = dateFormat.format(date4);

		for (TimeRecord tr : trlist) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("create_time", tr.create_time);
			if (tr.calc_date.equalsIgnoreCase(revealDate1)
					|| tr.calc_date.equalsIgnoreCase(revealDate2)
					|| tr.calc_date.equalsIgnoreCase(revealDate3)
					|| tr.calc_date.equalsIgnoreCase(revealDate4))
				map.put("status", status[1]);
			else
				map.put("status", status[0]);
			map.put("send_knows", status[2]);
			time_record.add(map);
		}

		return time_record;
	}

	public ArrayList<Map<String, String>> initDataListTalk(ArrayList<RecordCache> record_cache) {
		ArrayList<Map<String, String>> time_record = new ArrayList<Map<String, String>>();
		if (!trlist.isEmpty()) {
			trlist.clear();
		}
		Log.d(TAG, "init data list");

		trlist = dbMgr.queryFromOthers("jinxp");

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
	private String coverYearMonth(String yearMonth) {
		String[] temp = yearMonth.split("-");
		Log.d(TAG, "convert to : "+Integer.parseInt(temp[1]));
		return month12[Integer.parseInt(temp[1])-1]+" "+temp[0];
	}
	public ArrayList<Map<String, Object>> initDataListTime(ArrayList<RecordCache> record_cache, boolean isLuckDay) {
		ArrayList<Map<String, Object>> time_record = new ArrayList<Map<String, Object>>();
		if (!trlist.isEmpty()) {
			trlist.clear();
		}
		Log.d(TAG, "init data list");
		
		if (isLuckDay) {
			trlist = dbMgr.query();
		} else
			trlist = dbMgr.queryWithMultipleParams(TalkUtil.conditonDates());

		if (!time_record.isEmpty()) {
			time_record.clear();
		}
		
		if(!record_cache.isEmpty()) {
			record_cache.clear();
		}
		List<String> ourDateSet = new LinkedList<String>();
		List<String> ourTagSet = new LinkedList<String>();
		List<TimeRecord> tag_records = new ArrayList<TimeRecord>();
		
		for (int i = 0; i< trlist.size(); i ++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			HashMap<String, Object> section_map = new HashMap<String, Object>();
			HashMap<String, Object> tag_map = new HashMap<String, Object>();
			List<String> tag_items = new ArrayList<String>();
			
			RecordCache rc = new RecordCache();
			TimeRecord tr = trlist.get(i);
			
			String mYearMonth = tr.calc_date.substring(0,7);
			
			if(!exsitDateItem(ourDateSet, mYearMonth)) {
				ourDateSet.add(mYearMonth);
				section_map.put("isSection", 1);
				
				section_map.put("header", coverYearMonth(mYearMonth));
				Log.d(TAG, "put header: "+mYearMonth);
				time_record.add(section_map);
			}
			
			if(tr.tag != null && !exsitTag(ourTagSet, tr.tag)) {
				ourTagSet.add(tr.tag);
				tag_map.put("isSection", 2);
				Log.d(TAG, "tag is: "+tr.tag);
				tag_map.put("title", tr.tag);
				tag_records = dbMgr.queryTag(tr.tag);
				for(TimeRecord item : tag_records) {
					tag_items.add(item.content);
				}
				tag_map.put("tags", tag_items);
				time_record.add(tag_map);
				continue;
			}
			map.put("isSection", 0);
			rc.setId(tr._id);
			rc.setContent(tr.content);
			map.put("calc_date", tr.calc_date);
			map.put("content_type", tr.content_type);
			rc.setCreateDate(tr.calc_date);
			map.put("content", tr.content);
			map.put("create_time", tr.create_time);
			rc.setCreateTime(tr.create_time);
			rc.setMediaType(tr.content_type);
			map.put("photo", tr.photo);
			rc.setPhotoPath(tr.photo);
			
			record_cache.add(rc);
			time_record.add(map);

		}

		return time_record;
	}
	
	public void addRecord(TimeRecord tr) {
		dbMgr.add(tr);
	}
}
