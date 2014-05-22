package com.talk.demo;

import android.app.Activity;
import android.content.Intent;
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

import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.share.ShareTalkActivity;

import java.util.ArrayList;
import java.util.Map;

public class TalkFragment extends Fragment implements OnItemClickListener {
    
    private static String TAG = "TalkFragment";
    private ListView lv;
    private ArrayList<Map<String, String>> time_record;
    private ArrayList<RecordCache> record_cache;
    private SimpleAdapter adapter;
    private RecordManager recordManager;
    
    public TalkFragment(RecordManager recordMgr) {
        time_record = new ArrayList<Map<String, String>>();
        recordManager = recordMgr;
        record_cache = new ArrayList<RecordCache>();
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_talk, container, false);
        
        lv = (ListView)rootView.findViewById(R.id.talk_list);
               
        initListView();
        
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void initListView() {
        if(lv == null)
            return;
        time_record = recordManager.initDataListTalk(record_cache);
        adapter = new SimpleAdapter(getActivity(),time_record, R.layout.talk_listitem,
                new String[]{"content", "create_time"}, new int[]{R.id.content, R.id.create_time});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        
        String valueContent = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "value content: "+valueContent);
        Log.d(TAG, "time record: "+time_record.get(position).values().toString());
        Intent mIntent = new Intent(getActivity(), ShareTalkActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("createtime", time_record.get(position).get("create_time"));
        mBundle.putParcelable("recordcache", record_cache.get(position));
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
        
        
    } 
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
        time_record = recordManager.initDataListTalk(record_cache);
        adapter.notifyDataSetChanged();

    }

}
