package com.talk.demo.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.talk.demo.R;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.DBManager;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreShopActivity extends Activity {
    private static String TAG = "StoreShopActivity";
    
    private ArrayList<HashMap<String, Object>> list;
    private DBManager mgr;
    private RecordManager recordManager;
    private RecordListAdapter adapter;
    
    int[] status = new int[] {
            R.drawable.lock_status,
            R.drawable.unlock_status,
            R.drawable.note_browse_share_normal,
            };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        
        mgr = new DBManager(this);
        recordManager = new RecordManager(mgr, this);
        list = new ArrayList<HashMap<String, Object>>();
        
        list = recordManager.initDataListRecord(status);
        adapter = new RecordListAdapter(this, list, R.layout.record_status_listitem,  
                new String[]{"create_time", "status", "send_knows"}, 
                new int[]{R.id.create_time, R.id.status, R.id.send_knows});
        
        ListView lv = (ListView)findViewById(R.id.record_list);
        lv.setAdapter(adapter);
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
