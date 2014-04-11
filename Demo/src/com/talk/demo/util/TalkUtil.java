package com.talk.demo.util;

import java.util.Calendar;
import java.util.Date;

public class TalkUtil {
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
