package com.talk.demo.time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.RecordCache;


public class TimeItem extends Fragment {
    private static String TAG = "TimeItem";
    private String valueContent;
    private String creatTime;
    private TextView tv, tvTime;
    
    static TimeItem newInstance(RecordCache content) {
        TimeItem newFragment = new TimeItem(content);
        return newFragment;

    }
    
    public TimeItem(RecordCache content) {
        // TODO Auto-generated constructor stub
        valueContent = content.getContent();
        creatTime = content.getCreateTime();
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
        tvTime = (TextView) view.findViewById(R.id.item_time);
        
        tvTime.setText(creatTime);
        tv.setText(valueContent);
        return view;
    }
}
