package com.talk.demo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.RecordCache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class TimeFragment extends Fragment {
    
    private static String TAG = "TimeFragment";
    private ListView lv;
    private ArrayList<Map<String, Object>> time_record;
    private ArrayList<RecordCache> record_cache;
    private TimeListAdapter tAdapter;
    private RecordManager recordManager;
    
    public TimeFragment(RecordManager recordMgr) {
        time_record = new ArrayList<Map<String, Object>>();
        record_cache = new ArrayList<RecordCache>();
        recordManager = recordMgr;
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        
        lv = (ListView)rootView.findViewById(R.id.time_list);
               
        initListView();
        
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    // Calculate the difference days after first use time
    private void CalDiffDays() {
        Calendar calendar = Calendar.getInstance();
        Calendar savedCalendar = Calendar.getInstance();
        SharedPreferences sPreferences = getActivity().getSharedPreferences("first_use_time", Context.MODE_PRIVATE);
        int sYear = sPreferences.getInt("year", 0);
        int sMonth = sPreferences.getInt("month", 0);
        int sDay = sPreferences.getInt("day", 0);
        savedCalendar.set(sYear, sMonth, sDay);
        long last = calendar.getTimeInMillis()-savedCalendar.getTimeInMillis();
        long diffDays = last / (24 * 60 * 60 * 1000);
        Log.d(TAG, "last day : "+diffDays);
    }
    
    // Calculate whether luck day
    private boolean isLuckDay() {
        Calendar calendar = Calendar.getInstance();
        Calendar savedCalendar = Calendar.getInstance();
        SharedPreferences sPreferences = getActivity().getSharedPreferences("luck_day", Context.MODE_PRIVATE);
        int sMonth = sPreferences.getInt("Month", 0);
        int sDay = sPreferences.getInt("Day", 0);
        savedCalendar.set(calendar.get(Calendar.YEAR), sMonth, sDay);
        int result = calendar.compareTo(savedCalendar);
        return  (result == 0)?true:false;
    }
 
    private void initListView() {
        if(lv == null)
            return;
        time_record = recordManager.initDataListTime(record_cache, isLuckDay());
        
    	// This sets the color displayed for card titles and header actions by default
        tAdapter = new TimeListAdapter(this.getActivity(),time_record, record_cache);

        lv.setAdapter(tAdapter);

    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
        time_record = recordManager.initDataListTime(record_cache, isLuckDay());
        tAdapter.notifyDataSetChanged();

    }

}
