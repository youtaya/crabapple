package com.talk.demo.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.talk.demo.MainActivity;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.TalkUtil;

import java.util.Date;

public class TestRecord extends ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity activity;
	private DBManager mgr;
	
	public TestRecord(Class<MainActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}
	
    public TestRecord(String name) { 
        super(MainActivity.class);   
        setName(name);   
  }
    
    @Override
    protected void setUp() throws Exception {
      super.setUp();
      activity = this.getActivity();
      
      mgr = new DBManager(activity);
    }
    
    @UiThreadTest  
    public void testInputLogin() {
        
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
    	
	    Date date1 = TalkUtil.Cal_Days(new Date(), -1);
	    Date date2 = TalkUtil.Cal_Days(new Date(), -2);
        Date date3 = TalkUtil.Cal_Days(new Date(), -3);
        Date date4 = TalkUtil.Cal_Days(new Date(), -4);
        Date date5 = TalkUtil.Cal_Days(new Date(), -5);
        Date date6 = TalkUtil.Cal_Days(new Date(), -6);
        Date date7 = TalkUtil.Cal_Days(new Date(), -7);
        String c1 = "Hello darkness, my old friend";
        TimeRecord tr1 = new TimeRecord(c1, date1);
        tr1.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        tr1.setLink("abc");
        mgr.add(tr1);
        
        String c2 = "I come to talk with you again";
        TimeRecord tr2 = new TimeRecord(c2, date2);
        tr2.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        tr2.setLink("abc");
        mgr.add(tr2);
        
        String c3 = "Because a vision softly creeping";
        TimeRecord tr3 = new TimeRecord(c3, date3);
        tr3.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        tr3.setLink("abc");
        mgr.add(tr3);
        
        String c4 = "Left its seeds while I was sleeping";
        TimeRecord tr4 = new TimeRecord(c4, date4);
        tr4.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        tr4.setLink("abc");
        mgr.add(tr4);
        
        String c5 = "And the vision that was planted in my brain";
        TimeRecord tr5 = new TimeRecord(c5, date5);
        tr5.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        tr5.setLink("abc");
        mgr.add(tr5);
        
        String c6 = "Still remains ";
        TimeRecord tr6 = new TimeRecord(c6, date6);
        tr6.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        tr6.setLink("abc");
        mgr.add(tr6);
        
        String c7 = "Within the sound of silence";
        TimeRecord tr7 = new TimeRecord(c7, date7);
        tr7.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        tr7.setLink("abc");
        mgr.add(tr7);
        
    }  
    
    @Override  
    protected void tearDown() throws Exception {  
        super.tearDown(); 
        //TODO: clear added items
    }

}
