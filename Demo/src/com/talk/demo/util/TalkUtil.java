package com.talk.demo.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TalkUtil {
	
	public static final int REQUEST_IMAGE_CAPTURE = 1;
	public static final int REQUEST_SELECT_PICTURE = 2;
	public static final int REQUEST_AUDIO_CAPTURE = 3;
	public static final int REQUEST_PHOTO_CROPPER = 3;// 结果
	public static final int REQUEST_SEND_TO_WHAT = 4;
	/**
	 *  media_type: 1: text; 2: photo; 3: audio
	 */
	
	public static final int MEDIA_TYPE_TEXT = 1;
	public static final int MEDIA_TYPE_PHOTO = 2;
	public static final int MEDIA_TYPE_AUDIO = 3;
	public static final int MEDIA_TYPE_PHOTO_TEXT = 4;
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
	/*
	 * get dates for database query
	 * as ruler: 1, 3, 5, 7...
	 */
	public static String[] conditonDates() {
	    String[] condDate = new String[4];
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	    Date date1 = Cal_Days(new Date(), -1);
        Date date3 = Cal_Days(new Date(), -3);
        Date date5 = Cal_Days(new Date(), -5);
        Date date7 = Cal_Days(new Date(), -7);
        condDate[0] = dateFormat.format(date1);
        condDate[1] = dateFormat.format(date3);
        condDate[2] = dateFormat.format(date5);
        condDate[3] = dateFormat.format(date7);
        return condDate;
	}
	
	public static String[] preConditonDates(Date d) {
	    String[] condDate = new String[4];
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	    Date date1 = Cal_Days(d, -1);
        Date date3 = Cal_Days(d, -3);
        Date date5 = Cal_Days(d, -5);
        Date date7 = Cal_Days(d, -7);
        condDate[0] = dateFormat.format(date1);
        condDate[1] = dateFormat.format(date3);
        condDate[2] = dateFormat.format(date5);
        condDate[3] = dateFormat.format(date7);
        return condDate;
	}
	
	public static String dailyDate(Date d) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	    Date date1 = Cal_Days(d, 0);
	    return dateFormat.format(date1);
	}
	public static String currentDate() {
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 return dateFormat.format(new Date());
	}
	
    public static void createDirAndSaveFile(Bitmap imageToSave, String fileName) {
        File directory = new File(Environment.getExternalStorageDirectory() + "/Demo");
        if(!directory.exists()) {
        	directory = new File("/sdcard/Demo/");
        	directory.mkdirs();
        }
        
        File file = new File(directory, fileName);
        if(file.exists())
            file.delete();
        
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getTimeAsFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
        Date date = new Date();
        return dateFormat.format(date);
    }
}
