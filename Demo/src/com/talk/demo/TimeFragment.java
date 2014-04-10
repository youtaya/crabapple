package com.talk.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeFragment extends Fragment implements OnItemClickListener {
    
    private ListView lv;
    private EditText et;
    private Button bt;
    private DBManager mgr;
    private List<TimeRecord> trlist;
    private ArrayList<Map<String, String>> time_record;
    private SimpleAdapter adapter;
    
    public TimeFragment(DBManager db) {
        time_record = new ArrayList<Map<String, String>>();
        trlist = new ArrayList<TimeRecord>();
        mgr = db;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        lv = (ListView)rootView.findViewById(R.id.time_list);
        
        et = (EditText)rootView.findViewById(R.id.fast_record);
        bt = (Button)rootView.findViewById(R.id.ok_fast_record);
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String content = et.getText().toString();
                TimeRecord tr = new TimeRecord(content);  

                mgr.add(tr);
                
                RecordFragment rFragment = RecordFragment.newInstance(mgr);;
                rFragment.update();
            }
            
        });
        
        initListView();
        
        return rootView;
    }
    
    public ArrayList<Map<String, String>> initDataList() {  
        if(!trlist.isEmpty()) {
             trlist.clear();
        }
        trlist = mgr.query(); 
        
        if(!time_record.isEmpty()) {
            time_record.clear();
        }
        
        for (TimeRecord tr : trlist) {  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("content", tr.content);  
            map.put("create_time", tr.create_time);  
            time_record.add(map);  
        }  
  
        return time_record;
    }
    
    private void initListView() {
        if(lv == null)
            return;
        initDataList();
        adapter = new SimpleAdapter(getActivity(),time_record, R.layout.record_listitem,
                new String[]{"content", "create_time"}, new int[]{R.id.content, R.id.create_time});
        lv.setAdapter(adapter);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // TODO Auto-generated method stub
        
        String valueContent = parent.getItemAtPosition(position).toString();
        
        //Intent mIntent = new Intent(getActivity(), VacabPractice.class);
        //startActivity(mIntent);
        
        
    } 
    @Override
    public void onPause() {
    	super.onPause();
    }
    
}
