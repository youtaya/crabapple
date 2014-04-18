package com.talk.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.TalkUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordFragment extends Fragment {
    private static String TAG = "RecordFragment";
    
    private DBManager mgr;  
    private List<TimeRecord> trlist;
    private ArrayList<Map<String, String>> list;
    private SimpleAdapter adapter;
    private static RecordFragment instance;
    
    int[] status = new int[] {
            R.drawable.lock_status,
            R.drawable.unlock_status,

        };
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
        adapter = new SimpleAdapter(getActivity(), initDataList(), R.layout.record_status_listitem,  
                new String[]{"create_date", "create_time", "status"}, new int[]{R.id.create_date, R.id.create_time, R.id.status});
        
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        ListView lv = (ListView)rootView.findViewById(R.id.record_list);
        lv.setAdapter(adapter);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date1 = TalkUtil.Cal_Days(new Date(), -1);
        Date date2 = TalkUtil.Cal_Days(new Date(), -3);
        Date date3 = TalkUtil.Cal_Days(new Date(), -5);
        Date date4 = TalkUtil.Cal_Days(new Date(), -7);
        String revealDate1 = dateFormat.format(date1);
        String revealDate2 = dateFormat.format(date2);
        String revealDate3 = dateFormat.format(date3);
        String revealDate4 = dateFormat.format(date4);
        
        for (TimeRecord tr : trlist) {  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("create_date", tr.create_date);  
            map.put("create_time", tr.create_time); 
            if(tr.create_date.equalsIgnoreCase(revealDate1)||tr.create_date.equalsIgnoreCase(revealDate2)
            		||tr.create_date.equalsIgnoreCase(revealDate3)||tr.create_date.equalsIgnoreCase(revealDate4))
            	map.put("status", Integer.toString(status[1]));
            else
            	map.put("status", Integer.toString(status[0]));
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
        initDataList();
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