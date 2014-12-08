package com.talk.demo.time;

import android.util.Log;

import com.talk.demo.util.TalkUtil;

import java.util.Calendar;

public class DateInfo {

	private static String TAG = "DateInfo";
	private String time;
	private String time_latin;
	private String date;
	private String week_day;
	private String[] week_array = 
		{ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private String month_day;
	private String[] month_array = 
		{ "一月", "二月", "三月", "四月", "五月", "六月", "七月",
			"八月", "九月", "十月", "十一月", "十二月" };
	private String month_day_latin;
	private String[] month_array_latin = 
		{ "1月", "2月", "3月", "4月", "5月", "6月", "7月",
			"8月", "9月", "10月", "11月", "12月" };
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
				time_latin = "上午"+strTime[0]+":"+strTime[1];
			} else {
				time = strTime[0]+":"+strTime[1]+" PM";
				time_latin = "下午"+strTime[0]+":"+strTime[1];
			}
		}
		Log.d(TAG, "time: "+time);
		
		Calendar c = Calendar.getInstance();
		String[] test = str[0].split("-");
		int year = Integer.parseInt(test[0]);
		int month = Integer.parseInt(test[1])-1;
		int day = Integer.parseInt(test[2]);
		month_day = month_array[month];
		month_day_latin = month_array_latin[month];
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
	
	public String getTimeTalk() {
		int result = TalkUtil.isThisDate(raw_info);
		switch(result) {
		case 1:
			return "刚刚";
		case 2:
			return time_latin;
		}
		 
		return month_day_latin+date+"日";
	}
}
