package com.talk.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.share.ShareTalkActivity;

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
        
        CloudKite[] tasks = initTasks();
        TalkListAdapter adapter = new TalkListAdapter(this, tasks);
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
        final int count = 10;
        CloudKite[] result = new CloudKite[count];
        for (int i = 0; i < count; i++) {
            result[i] = new CloudKite("TASK::" + i);
        }

        return result;
    }
}
