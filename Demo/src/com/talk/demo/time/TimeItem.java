package com.talk.demo.time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talk.demo.R;

import java.util.ArrayList;


public class TimeItem extends Fragment {
    private static String TAG = "TimeItem";
    private String valueContent;
    private TextView tv;
    
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
        tv = (TextView) view.findViewById(R.id.item_content);
        tv.setText(valueContent);
        return view;
    }
}
