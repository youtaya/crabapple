package com.talk.demo;

import android.R.integer;
import android.app.Activity;
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
import java.util.Map;

public class TalkFragment extends Fragment {
    
    private static String TAG = "TalkFragment";
    private ListView cardLv;
    private ArrayList<Map<String, String>> time_record;
    private ArrayList<RecordCache> record_cache;
    private RecordManager recordManager;
    
    public TalkFragment(RecordManager recordMgr) {
        time_record = new ArrayList<Map<String, String>>();
        recordManager = recordMgr;
        //talk_cache = new ArrayList<TalkCache>();
        record_cache = new ArrayList<RecordCache>();
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_talk, container, false);
        
        cardLv = (ListView)rootView.findViewById(R.id.talk_list);
        time_record = recordManager.initDataListTalk(record_cache, false);
        
        CloudKite[] tasks = initTasks();
        TalkListAdapter adapter = new TalkListAdapter(this.getActivity().getApplicationContext(), tasks);
        cardLv.setAdapter(adapter);

        for (CloudKite t : tasks)
            new Thread(t).start();

        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
     
    }
    
    CloudKite[] initTasks() {
    	
        final int count = time_record.size();
        CloudKite[] result = new CloudKite[count];
        int i = 0;
        for(Map<String,String> map : time_record) {
        	result[i] = new CloudKite(map.get("content"), 
        	        Integer.parseInt(map.get("send_interval_time")),
        	        map.get("send_done_time"));
        	i++;
        }

        return result;
    }
}
