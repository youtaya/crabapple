package com.talk.demo.time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.MainActivity.SectionsPagerAdapter;

import java.util.ArrayList;


public class TimeAllItem extends FragmentActivity {
    private static String TAG = "TimeAllItem";
    private String contentValue;
    private ArrayList<String> allContent;
    private TextView mTv;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    
    MyFragmentPagerAdapter mMyPagerAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.time_all_item);
        Bundle bundle = getIntent().getExtras();
        contentValue = bundle.getString("content");
        allContent = bundle.getStringArrayList("allContent");
        initViewPager();
    }
    
    private void initViewPager() {
        mTv = (TextView) findViewById(R.id.time_data);
        mTv.setText(contentValue);
        
        mPager = (ViewPager) findViewById(R.id.vPager);
        

        fragmentsList = new ArrayList<Fragment>();
        
        for(int i=0;i<allContent.size();i++) {
            Fragment activityfragment = TimeItem.newInstance(allContent.get(i));
            fragmentsList.add(activityfragment);
        }
        mMyPagerAdapter =new MyFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentsList);
        mPager.setAdapter(mMyPagerAdapter);
        mPager.setCurrentItem(allContent.indexOf(contentValue));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
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

        @Override
        public void onPageSelected(int arg0) {
            Log.d(TAG, "arg0 is : "+ arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

}
