package com.talk.demo;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordFragment extends ListFragment {
    private static String TAG="RecordFragment";
    
    private DBManager mgr;  
    private List<TimeRecord> trlist;
    private ArrayList<Map<String, String>> list;
    private SimpleAdapter adapter;
    private static RecordFragment instance;
    
    static RecordFragment newInstance(DBManager db) {
        if(instance == null) {
            instance = new RecordFragment(db);
            
        }
        return instance;

    }
    public RecordFragment(DBManager db) {
        list = new ArrayList<Map<String, String>>();
        trlist = new ArrayList<TimeRecord>();
        mgr = db;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        //mgr = new DBManager(getActivity());
        adapter = new SimpleAdapter(getActivity(), initDataList(), android.R.layout.simple_list_item_2,  
                new String[]{"content", "create_time"}, new int[]{android.R.id.text1, android.R.id.text2});
        
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
        return rootView;
    }
    
    private ArrayList<Map<String, String>> initDataList() {  
        if(!trlist.isEmpty()) {
             trlist.clear();
        }
        Log.d(TAG, "init data list");
        trlist = mgr.query(); 
        
        if(!list.isEmpty()) {
            list.clear();
        }
        
        for (TimeRecord tr : trlist) {  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("content", tr.content);  
            map.put("create_time", tr.create_time);  
            list.add(map);  
        }  
  
        return list;
    } 
    
    public void update() {
        initDataList();
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");

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