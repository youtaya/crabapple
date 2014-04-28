
package com.talk.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.TalkUtil;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, DailyFragment.OnItemChangedListener {

    private static String TAG = "MainActivity";
    
    private DBManager mgr;
    private DailyFragment guideFragment;
    private TimeFragment timeFragment;
    private RecordFragment recordFragment;
    private DiscoveryFragment discoveryFragment;
    private ArrayList<Fragment> fragmentList;
    
    private boolean forTest = true;
    
	@Override
	public void onItemChanged() {
		// change to record fragment
		getActionBar().setSelectedNavigationItem(2);
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

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mgr = new DBManager(this);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        SharedPreferences sPreferences = getSharedPreferences("for_test", Context.MODE_PRIVATE);
        forTest = sPreferences.getBoolean("test", true);
        if(forTest) {
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    	    Date date1 = TalkUtil.Cal_Days(new Date(), -1);
    	    Date date2 = TalkUtil.Cal_Days(new Date(), -2);
            Date date3 = TalkUtil.Cal_Days(new Date(), -3);
            Date date4 = TalkUtil.Cal_Days(new Date(), -4);
            Date date5 = TalkUtil.Cal_Days(new Date(), -5);
            Date date6 = TalkUtil.Cal_Days(new Date(), -6);
            Date date7 = TalkUtil.Cal_Days(new Date(), -7);
            String c1 = "Hello darkness, my old friend";
            TimeRecord tr1 = new TimeRecord(c1, dateFormat.format(date1));
            tr1.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            mgr.add(tr1);
            String c2 = "I come to talk with you again";
            TimeRecord tr2 = new TimeRecord(c2, dateFormat.format(date2));
            tr2.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            mgr.add(tr2);
            String c3 = "Because a vision softly creeping";
            TimeRecord tr3 = new TimeRecord(c3, dateFormat.format(date3));
            tr3.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            mgr.add(tr3);
            String c4 = "Left its seeds while I was sleeping";
            TimeRecord tr4 = new TimeRecord(c4, dateFormat.format(date4));
            tr4.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            mgr.add(tr4);
            String c5 = "And the vision that was planted in my brain";
            TimeRecord tr5 = new TimeRecord(c5, dateFormat.format(date5));
            tr5.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            mgr.add(tr5);
            String c6 = "Still remains ";
            TimeRecord tr6 = new TimeRecord(c6, dateFormat.format(date6));
            tr6.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            mgr.add(tr6);
            String c7 = "Within the sound of silence";
            TimeRecord tr7 = new TimeRecord(c7, dateFormat.format(date7));
            tr7.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            mgr.add(tr7);
            
            SharedPreferences sp = getSharedPreferences("for_test", Context.MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putBoolean("test", false);
            editor.commit();
        }
        
        fragmentList = new ArrayList<Fragment>();
        //add guide fragment
        guideFragment = new DailyFragment(mgr);
        fragmentList.add(guideFragment);
        //add time fragment
        timeFragment = new TimeFragment(mgr);
        fragmentList.add(timeFragment);
        //add record fragment
        recordFragment = RecordFragment.newInstance(mgr);
        fragmentList.add(recordFragment);
        //add statistics fragment
        discoveryFragment = new DiscoveryFragment(mgr);
        //Bundle args3 = new Bundle();
        //args3.putInt(DiscoveryFragment.ARG_SECTION_NUMBER, 3);
        //discoveryFragment.setArguments(args3);
        fragmentList.add(discoveryFragment);
        
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
