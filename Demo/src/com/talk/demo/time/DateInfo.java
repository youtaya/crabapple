package com.talk.demo.time;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateInfo {

	private static String TAG = "DateInfo";
	private String time;
	private String date;
	private String week_day;
	private String[] week_array = 
		{"MONDAY", "TUESDAY", "WENSDAY", "THURSDAY", "FRIDAY", "SATDAY", "SUNDAY" };
	private String raw_info;
	public DateInfo(String timeInfo) {
		raw_info = timeInfo;
	}
	
	public void parseCreateTime() {
		String str[] = raw_info.split(" ");
		String[] strDate = str[0].split("-");
		date = strDate[2];
		Log.d(TAG, "date: "+date);
		String[] strTime = str[1].split(":");
		if(strTime[0].compareTo("12") < 0) {
			time = strTime[0]+":"+strTime[1]+" AM";
		} else {
			time = strTime[0]+":"+strTime[1]+" PM";
		}
		Log.d(TAG, "time: "+time);
		
		Calendar c = Calendar.getInstance();
		try {
			Date myDate = new SimpleDateFormat("yy-mm-dd").parse(str[0]);
			c.setTime(myDate);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			Log.d(TAG, "raw week day: "+dayOfWeek);
			week_day = week_array[dayOfWeek-1];
			Log.d(TAG, "week day: "+week_day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getTime() {
		return time;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getWeekInfo() {
		return week_day;
	}
}
