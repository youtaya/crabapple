package com.talk.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class RecordFragment extends Fragment {
    private static String TAG="RecordFragment";
    private DBManager mgr;  
    private ListView listView;
    
    public RecordFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        
        listView = (ListView)rootView.findViewById(R.id.record_list);
        mgr = new DBManager(getActivity());
        
        initDataList();
        return rootView;
    }
    
    public void initDataList() {  
        List<TimeRecord> trlist = mgr.query();  
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();  
        for (TimeRecord tr : trlist) {  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("content", tr.content);  
            map.put("create_time", tr.create_time);  
            list.add(map);  
        }  
        
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_2,  
                    new String[]{"content", "create_time"}, new int[]{android.R.id.text1, android.R.id.text2});  
        listView.setAdapter(adapter);  
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
        //应用的最后一个Activity关闭时应释放DB  
        mgr.closeDB();  
    }  
}