package com.talk.demo.time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.persistence.TimeRecord;

import java.util.ArrayList;


public class TimeAllItem extends FragmentActivity {
    private static String TAG = "TimeAllItem";
    private String create_date;
    private String create_time;
    private ArrayList<RecordCache> record_cache;
    private TextView mTv;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private DBManager mgr;
    private MyOnPageChangeListener pageListener;
    
    MyFragmentPagerAdapter mMyPagerAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.time_all_item);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bar_title_item);
        
        Bundle bundle = getIntent().getExtras();
        create_date = bundle.getString("createdate");
        create_time = bundle.getString("createtime");
        record_cache = bundle.getParcelableArrayList("recordcache");
        initViewPager();
        
        mgr = new DBManager(this);

    }
    private int getIndexOf() {
    	int i = 0;
    	Log.d(TAG, "content value : "+create_time);
    	for(;i<record_cache.size();i++) {
    		Log.d(TAG, "cache content value : "+record_cache.get(i).getCreateTime());
    		if(create_time.equalsIgnoreCase(record_cache.get(i).getCreateTime()))
    			return i;
    	}
    	// should not call it.
    	Log.d(TAG, "Can't found in record cache");
    	i = 0;
    	return i;
    }
    
    private void initViewPager() {
        mTv = (TextView) findViewById(R.id.time_data);
        mTv.setText(create_date);
        
        mPager = (ViewPager) findViewById(R.id.vPager);
        

        fragmentsList = new ArrayList<Fragment>();
        
        for(int i=0;i<record_cache.size();i++) {
            Fragment activityfragment = TimeItem.newInstance(record_cache.get(i));
            fragmentsList.add(activityfragment);
        }
        mMyPagerAdapter =new MyFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentsList);
        mPager.setAdapter(mMyPagerAdapter);
        int index = getIndexOf();
        Log.d(TAG, "found in record cache :"+index);
        mPager.setCurrentItem(index);
        pageListener = new MyOnPageChangeListener();
        mPager.setOnPageChangeListener(pageListener);
    }
    
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentsList;
        private int current;
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
        	current = arg0;
            return fragmentsList.get(arg0);
        }
        
        public int getCurrentPageIndex() {
        	return current;
        }
        
        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }
    
    public class MyOnPageChangeListener implements OnPageChangeListener {

    	private int current;
        @Override
        public void onPageSelected(int arg0) {
            Log.d(TAG, "arg0 is : "+ arg0);
            current = arg0;
        }

        public int getSelectedPageIndex() {
        	return current;
        }
        
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.add_tag_actions, menu);
    	return super.onCreateOptionsMenu(menu);
    }

}
