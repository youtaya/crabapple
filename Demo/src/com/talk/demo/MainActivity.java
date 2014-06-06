
package com.talk.demo;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.prewrite.PreWrite;
import com.talk.demo.setting.FindIntimateActivity;
import com.talk.demo.setting.IntimateActivity;
import com.talk.demo.setting.PreviewActivity;
import com.talk.demo.setting.UserActivity;
import com.talk.demo.util.TalkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, DailyFragment.OnItemChangedListener {

    private static String TAG = "MainActivity";
    private PreWrite pw;
    private DBManager mgr;
    private RecordManager recordManager;
    private DailyFragment guideFragment;
    private TimeFragment timeFragment;
    private RecordFragment recordFragment;
    private TalkFragment talkFragment;
    private ArrayList<Fragment> fragmentList;
    
    private boolean forTest = true;

	@Override
	public void onItemChanged() {
		// change to record fragment
		getActionBar().setSelectedNavigationItem(3);
	}  
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getOverflowMenu();
       // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mgr = new DBManager(this);
        recordManager = new RecordManager(mgr);
        pw = new PreWrite(this.getApplicationContext());
        pw.startPosition();
        
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        SharedPreferences sPreferences = getSharedPreferences("for_test", Context.MODE_PRIVATE);
        forTest = sPreferences.getBoolean("test", true);
        if(forTest) {
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
            
            SharedPreferences sp = getSharedPreferences("for_test", Context.MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putBoolean("test", false);
            editor.commit();
            
        }
        
        
        fragmentList = new ArrayList<Fragment>();
        //add guide fragment
        guideFragment = new DailyFragment(recordManager, pw);
        fragmentList.add(guideFragment);
        //add time fragment
        timeFragment = new TimeFragment(recordManager);
        fragmentList.add(timeFragment);
        //add talk fragment
        talkFragment = new TalkFragment(recordManager);
        fragmentList.add(talkFragment);        
        //add record fragment
        recordFragment = RecordFragment.newInstance(recordManager);
        fragmentList.add(recordFragment);
        
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragmentList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setMenuIconEnable(menu,true);
        return super.onPrepareOptionsMenu(menu);
    }
    
    // fake hardware menu
    private void getOverflowMenu() {
        try {
           ViewConfiguration config = ViewConfiguration.get(this);
           Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
           if(menuKeyField != null) {
               menuKeyField.setAccessible(true);
               menuKeyField.setBoolean(config, false);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
    	//inflater.inflate(R.menu.main_actions, menu);
    	inflater.inflate(R.menu.setting_actions, menu);
        return true;
    }
    
    private void setMenuIconEnable(Menu menu, boolean enabled) {
        Log.d(TAG, "setMenuIconEnable");
        try {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            m.invoke(menu, enabled);
        } catch (Exception e) {
            Log.d(TAG, "get method fail!!");
            e.printStackTrace();
        }
    }
    
    private void callOtherActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_user:
        	callOtherActivity(UserActivity.class);
            return true;
        case R.id.action_preview:
        	callOtherActivity(PreviewActivity.class);
            return true;
        case R.id.action_friend:
        	callOtherActivity(IntimateActivity.class);
            return true;  
        case R.id.action_add_friend:
        	callOtherActivity(FindIntimateActivity.class);
            return true;              
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> flist;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fl) {
            super(fm);
            flist = fl;
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Log.d(TAG, "getItem");
            return flist.get(position);
        }
        
        @Override
        public int getItemPosition(Object object) {
            Log.d(TAG, "getItemPostion");
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return flist.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section0).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
    
   
    @Override
    public void onDestroy() {  
        super.onDestroy();  
        Log.d(TAG, "onDestroy");
        //mgr.closeDB();  
    }
    
}
