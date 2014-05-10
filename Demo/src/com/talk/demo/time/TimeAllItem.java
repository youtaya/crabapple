package com.talk.demo.time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
    private EditText time_comment;
    private ImageView comment_save;
    private DBManager mgr;
    private MyOnPageChangeListener pageListener;
    
    MyFragmentPagerAdapter mMyPagerAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.time_all_item);
        Bundle bundle = getIntent().getExtras();
        create_date = bundle.getString("createdate");
        create_time = bundle.getString("createtime");
        record_cache = bundle.getParcelableArrayList("recordcache");
        initViewPager();
        
        mgr = new DBManager(this, this.getApplication());
        time_comment = (EditText)findViewById(R.id.time_comment);
        comment_save = (ImageView)findViewById(R.id.comment_send);
        comment_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String comment = time_comment.getText().toString();
				if(comment.length() > 0) {
					int index = pageListener.getCurrentPageIndex();
					Log.d(TAG, "page index: "+index);
					String origStr = record_cache.get(index).getContent();
					TimeRecord tr = new TimeRecord(record_cache.get(index));
					tr.setContent(origStr+comment);
					Log.d(TAG, "content: "+tr.content);
					mgr.updateContent(tr);
					
					finish();
				}
			}
        	
        });
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
            return fragmentsList.get(arg0);
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

        public int getCurrentPageIndex() {
        	return current;
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

}
