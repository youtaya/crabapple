package com.talk.demo;

import android.app.Activity;
import android.content.Context;
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

import com.talk.demo.core.RecordManager;
import com.talk.demo.talk.DialogCache;
import com.talk.demo.talk.DialogItem;
import com.talk.demo.talk.TalkAllItem;
import com.talk.demo.talk.TalkViewItem;

import java.util.ArrayList;
import java.util.HashMap;

public class TalkFragment extends Fragment {
    
    private static String TAG = "TalkFragment";
    private ListView mListView;
    private ArrayList<TalkViewItem> talk_record;
    private HashMap<String, ArrayList<DialogCache>> dialog_cache;
    private RecordManager recordManager;
    
    public TalkFragment(RecordManager recordMgr) {
        talk_record = new ArrayList<TalkViewItem>();
        recordManager = recordMgr;
        //talk_cache = new ArrayList<TalkCache>();
        dialog_cache = new HashMap<String, ArrayList<DialogCache>>();
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_talk, container, false);
        
        mListView = (ListView)rootView.findViewById(R.id.talk_list);
        talk_record = recordManager.initDataListTalk(dialog_cache);
        
        //CloudKite[] tasks = initTasks();
        /*
        TalkListAdapter adapter = new TalkListAdapter(this.getActivity().getApplicationContext(), 
        		talk_record, dialog_cache, tasks);
        */
        TalkListAdapter adapter = new TalkListAdapter(this.getActivity(), 
        		talk_record, dialog_cache);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Context ctx = TalkFragment.this.getActivity();
				
		        Intent mIntent = new Intent(ctx, TalkAllItem.class);
		        Bundle mBundle = new Bundle();
		        DialogItem dialog_item = talk_record.get(position).getListViewItem().get(0);
		        Log.d(TAG, "link : "+ dialog_item.getLink());
		        mBundle.putString("link", dialog_item.getLink());
	            mBundle.putString("createdate", dialog_item.getCreateDate());
	            mBundle.putString("createtime", dialog_item.getCreateTime());
		        mBundle.putParcelableArrayList("recordcache", dialog_cache.get(dialog_item.getLink()));
		        Log.d(TAG,"items size: "+ dialog_cache.get(dialog_item.getLink()).size());
			    mIntent.putExtras(mBundle);
			    //mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    ctx.startActivity(mIntent);	
				
			}
        	
        });
        /*
        for (CloudKite t : tasks)
            new Thread(t).start();
		*/
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
    	
        final int count = talk_record.size();
        CloudKite[] result = new CloudKite[count];
        int i = 0;
        for(TalkViewItem talk : talk_record) {
        	result[i] = new CloudKite(talk.getDialogItem().getContent(), 
        	        talk.getDialogItem().getIntervalTime(),
        	        talk.getDialogItem().getDoneTime());
        	i++;
        }

        return result;
    }
}
