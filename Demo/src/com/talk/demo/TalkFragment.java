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
import com.talk.demo.persistence.TalkCache;
import com.talk.demo.share.OptJsonData;
import com.talk.demo.share.ShareTalkActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TalkFragment extends Fragment implements OnItemClickListener {
    
    private static String TAG = "TalkFragment";
    private ListView lv;
    private ArrayList<Map<String, String>> time_record;
    private ArrayList<TalkCache> talk_cache;
    private SimpleAdapter adapter;
    private RecordManager recordManager;
    private OptJsonData ojd;
    
    public TalkFragment(RecordManager recordMgr) {
        time_record = new ArrayList<Map<String, String>>();
        recordManager = recordMgr;
        talk_cache = new ArrayList<TalkCache>();
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_talk, container, false);
        
        lv = (ListView)rootView.findViewById(R.id.talk_list);
        ojd = new OptJsonData(this.getActivity().getApplicationContext());
        
        initListView();
        	

        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
    public ArrayList<Map<String, String>> test4test(ArrayList<Map<String, String>> test) {
    	//ArrayList<Map<String, String>> test = new ArrayList<Map<String, String>>();

    	if(!test.isEmpty()) {
    		test.clear();
    	}
    	
    	if(!talk_cache.isEmpty()) {
    		talk_cache.clear();
    	}
    	
    	try {
    		JSONObject jsonObject = new JSONObject(ojd.readLocalFile("test"));
        	HashMap<String,String> map = new HashMap<String, String>();
        	map.put("create_time", jsonObject.getString("create_time"));
        	map.put("content", jsonObject.getString("content"));
        	test.add(map);
        	JSONArray talkArray = jsonObject.getJSONArray("talk");
        	for(int i = 0; i < talkArray.length(); i++) {
        		JSONObject talkObject = talkArray.getJSONObject(i);
	        	TalkCache tc = new TalkCache();
	        	tc.setContent(talkObject.getString("content"));
	        	tc.setCreateDate(talkObject.getString("time"));
	        	tc.setFrom(talkObject.getString("from"));
	        	tc.setTo(talkObject.getString("to"));
	        	talk_cache.add(tc);
        	}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

    	return test;
    }
    


    private void initListView() {
        if(lv == null)
            return;
        //time_record = recordManager.initDataListTalk(record_cache);
        test4test(time_record);
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
        mBundle.putParcelableArrayList("recordcache", talk_cache);
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
     
        initListView();
    }

}
