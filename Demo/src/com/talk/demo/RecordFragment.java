package com.talk.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.talk.demo.core.RecordManager;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordFragment extends Fragment {
    private static String TAG = "RecordFragment";
    
    private ArrayList<HashMap<String, Object>> list;
    private RecordManager recordManager;
    private RecordListAdapter adapter;
    private static RecordFragment instance;
    
    int[] status = new int[] {
            R.drawable.lock_status,
            R.drawable.unlock_status,
            R.drawable.send_knows,
            };
    
    static RecordFragment newInstance(RecordManager recordMgr) {
        if(instance == null) {
            instance = new RecordFragment(recordMgr);
            
        }
        return instance;

    }
    public RecordFragment(RecordManager recordMgr) {
        list = new ArrayList<HashMap<String, Object>>();
        recordManager = recordMgr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        list = recordManager.initDataListRecord(status);
        adapter = new RecordListAdapter(getActivity(), list, R.layout.record_status_listitem,  
                new String[]{"create_time", "status", "send_knows"}, 
                new int[]{R.id.create_time, R.id.status, R.id.send_knows});
        
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        ListView lv = (ListView)rootView.findViewById(R.id.record_list);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return rootView;
    }
    
    public void update() {
    	list = recordManager.initDataListRecord(status);
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
        list = recordManager.initDataListRecord(status);
        adapter.notifyDataSetChanged();
    }

    
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "on Pause");
    }
    
    @Override
    public void onDestroy() {  
        super.onDestroy();  

    }  
}