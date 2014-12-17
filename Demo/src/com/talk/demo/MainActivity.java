
package com.talk.demo;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewConfiguration;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.talk.demo.core.RecordManager;
import com.talk.demo.jpush.JPushUtil;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.DialogRecord;
import com.talk.demo.prewrite.PreWrite;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.RawDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static String TAG = "MainActivity";
    private PreWrite pw;
    private DBManager mgr;
    private RecordManager recordManager;
    private DailyFragment guideFragment;
    private TimeFragment timeFragment;
    private TalkFragment talkFragment;
    private ArrayList<Fragment> fragmentList;
    
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
    
    /**
     * Receiver: for receive custom message frome jpush server
     */
    private MessageReceiver mMessageReceiver;
    
    public static final String MESSAGE_RECEIVED_ACTION = "com.talk.demo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final int MSG_SET_TAGS = 1002;
    public static boolean isForeground = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // set tags for push service
        Account existing = AccountUtils.getPasswordAccessibleAccount(this);
        if (existing != null && !TextUtils.isEmpty(existing.name)) {
            setTag(existing.name);
        }
        // used for receive msg
        registerMessageReceiver();
        
        getOverflowMenu();
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mgr = new DBManager(this);
        recordManager = new RecordManager(mgr, this);
        pw = new PreWrite(this.getApplicationContext());
        pw.startPosition();

        fragmentList = new ArrayList<Fragment>();
        //add guide fragment
        guideFragment = new DailyFragment(recordManager);
        fragmentList.add(guideFragment);
        //add time fragment
        timeFragment = new TimeFragment(recordManager);
        fragmentList.add(timeFragment);
        //add talk fragment
        talkFragment = new TalkFragment(recordManager);
        fragmentList.add(talkFragment);        
        
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

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }
    
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
           
            if(MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String message = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                
                //updateDialog(username, id);
            }
            
        }
    }
    
    private void updateDialog(String user, int id) {
        RawDialog dialog = NetworkUtilities.getDialog(user, id);
        DialogRecord record = new DialogRecord(dialog);
        mgr.addDialog(record);
    }
    
    private void setTag(String tag) {
        Set<String> tagSet = new LinkedHashSet<String>();
        if(!JPushUtil.isValidTagAndAlias(tag)) {
            Toast.makeText(this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        tagSet.add(tag);
        
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
        
    }
    
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in hanlder");
                    JPushInterface.setAliasAndTags(MainActivity.this, null, (Set<String>)msg.obj, mTagsCallback);
                    break;
            }
        }
    };
    
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch(code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.d(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout, Try again after 60s.";
                    Log.d(TAG, logs);
                    if(JPushUtil.isConnected(MainActivity.this)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000*60);
                    } else {
                        Log.d(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorcode = "+code;
                    Log.d(TAG, logs);
            }
            
            JPushUtil.showToast(logs, MainActivity.this);
        }
    };
    
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
    	super.onCreateOptionsMenu(menu);
    	
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
                /*
                case 3:
                    return getString(R.string.title_section3).toUpperCase(l);
                */
            }
            return null;
        }
    }
    
    @Override
    public void onResume() {
        isForeground = true;
    	super.onResume();
    	JPushInterface.onResume(MainActivity.this);
    }
    
    @Override
    public void onPause() {
        isForeground = false;
    	super.onPause();
    	JPushInterface.onPause(MainActivity.this);
    }
   
    @Override
    public void onDestroy() {  
        super.onDestroy();  
        Log.d(TAG, "onDestroy");
        //mgr.closeDB();  
        
        if(mMessageReceiver != null)
            unreigsterReceiver(mMessageReceiver);
    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {
	    long currentTime = System.currentTimeMillis();
	    if((currentTime-touchTime) >= waitTime) {
	    	Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
		    touchTime = currentTime;
	    } else {
		    finish();
	    }
    }
    
}
