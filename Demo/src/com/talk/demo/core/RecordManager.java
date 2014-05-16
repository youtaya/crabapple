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

	public ArrayList<Map<String, String>> initDataListRecord(int[] status) {
		ArrayList<Map<String, String>> time_record = new ArrayList<Map<String, String>>();
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
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("create_time", tr.create_time);
			if (tr.calc_date.equalsIgnoreCase(revealDate1)
					|| tr.calc_date.equalsIgnoreCase(revealDate2)
					|| tr.calc_date.equalsIgnoreCase(revealDate3)
					|| tr.calc_date.equalsIgnoreCase(revealDate4))
				map.put("status", Integer.toString(status[1]));
			else
				map.put("status", Integer.toString(status[0]));
			map.put("send_knows", Integer.toString(status[2]));
			time_record.add(map);
		}

		return time_record;
	}

	public ArrayList<Map<String, String>> initDataListTalk(ArrayList<RecordCache> record_cache) {
		return initDataListTime(record_cache, true);
	}

	public ArrayList<Map<String, String>> initDataListTime(ArrayList<RecordCache> record_cache, boolean isLuckDay) {
		ArrayList<Map<String, String>> time_record = new ArrayList<Map<String, String>>();
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
			rc.setCreateTime(tr.create_time);
			rc.setMediaType(tr.content_type);
			record_cache.add(rc);
			time_record.add(map);
		}

		return time_record;
	}
	
	public void addRecord(TimeRecord tr) {
		dbMgr.add(tr);
	}
}
