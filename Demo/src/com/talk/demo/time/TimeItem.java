package com.talk.demo.time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talk.demo.R;


public class TimeItem extends Fragment {
    private static String TAG = "TimeItem";
    private String valueContent;
    
    static TimeItem newInstance(String content) {
        TimeItem newFragment = new TimeItem(content);
        return newFragment;

    }
    
    public TimeItem(String content) {
        // TODO Auto-generated constructor stub
        valueContent = content;
        
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "TestFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "TestFragment-----onCreateView");
        View view = inflater.inflate(R.layout.time_item, container, false);
        return view;
    }
}
