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
import com.talk.demo.persistence.TalkCache;
import com.talk.demo.share.ShareTalkActivity;

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
    
    public TalkFragment(RecordManager recordMgr) {
        time_record = new ArrayList<Map<String, String>>();
        recordManager = recordMgr;
        talk_cache = new ArrayList<TalkCache>();
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
    
    public ArrayList<Map<String, String>> test4test() {
    	ArrayList<Map<String, String>> test = new ArrayList<Map<String, String>>();
    	/*
    	 <content
    	 	time=20120514
    	 	title=hello>
    	 	<item
    	 		from=bob
    	 		to=alice>
    	 		<time>20120514</time>
    	 		<content>how are you</content>
    	 	</item>
    	 	<item
    	 		from=alice
    	 		to=bob>
    	 		<time>20120515</time>
    	 		<content>I am ok</content>
			</item>
    	 </content>
    	 */
    	HashMap<String,String> map = new HashMap<String, String>();
    	map.put("create_time", "20120514");
    	map.put("content", "hello");
    	test.add(map);
    	
    	TalkCache tc1 = new TalkCache();
    	tc1.setContent("how are you");
    	tc1.setCreateDate("20120514");
    	tc1.setFrom("bob");
    	tc1.setTo("alice");
    	talk_cache.add(tc1);
    	TalkCache tc2 = new TalkCache();
    	tc2.setContent("i am ok");
    	tc2.setCreateDate("20120515");
    	tc2.setFrom("alice");
    	tc2.setTo("bob");
    	talk_cache.add(tc2);
    	return test;
    }

    private void initListView() {
        if(lv == null)
            return;
        //time_record = recordManager.initDataListTalk(record_cache);
        time_record = test4test();
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
        //time_record = recordManager.initDataListTalk(talk_cache);
        adapter.notifyDataSetChanged();

    }

}
