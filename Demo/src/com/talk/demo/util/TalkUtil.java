package com.talk.demo.util;

import java.util.Calendar;
import java.util.Date;

public class TalkUtil {
	
	public static final int REQUEST_IMAGE_CAPTURE = 1;
	
	/**
	 *  media_type: 1: text; 2: photo; 3: audio
	 */
	
	public static final int MEDIA_TYPE_TEXT = 1;
	public static final int MEDIA_TYPE_PHOTO = 2;
	public static final int MEDIA_TYPE_AUDIO = 3;
    /**  
     * @param   参照日期      
     * @param   天数(之前为负数,之后为正数)          
     * @return  参照日期之前或之后days的日期 
     */  
	public static Date Cal_Days(Date date, int days) { 
        
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);  
        return calendar.getTime();  
    }
}
