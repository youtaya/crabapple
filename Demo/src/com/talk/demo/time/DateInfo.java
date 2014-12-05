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
		{ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private String month_day;
	private String[] month_array = 
		{ "一月", "二月", "三月", "四月", "五月", "六月", "七月",
			"八月", "九月", "十月", "十一月", "十二月" };
	private String raw_info;
	public DateInfo(String timeInfo) {
		raw_info = timeInfo;
	}
	
	public void parseCreateTime() {
		String str[] = raw_info.split(" ");
		Log.d(TAG, "raw info: "+raw_info);
		String[] strDate = str[0].split("-");
		date = strDate[2];
		Log.d(TAG, "date: "+date);
		if(str.length > 1) {
			String[] strTime = str[1].split(":");
			if(strTime[0].compareTo("12") < 0) {
				time = strTime[0]+":"+strTime[1]+" AM";
			} else {
				time = strTime[0]+":"+strTime[1]+" PM";
			}
		}
		Log.d(TAG, "time: "+time);
		
		Calendar c = Calendar.getInstance();
		String[] test = str[0].split("-");
		int year = Integer.parseInt(test[0]);
		int month = Integer.parseInt(test[1])-1;
		int day = Integer.parseInt(test[2]);
		month_day = month_array[month];
		Log.d(TAG, "my date: "+year+" "+month+" "+day);
		c.set(year, month, day);
		
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		Log.d(TAG, "raw week day: "+dayOfWeek);
		week_day = week_array[dayOfWeek-1];
		
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
	
	public String getTimeHead() {
	    return month_day+"\t"+week_day+"\t"+time;
	}
}
