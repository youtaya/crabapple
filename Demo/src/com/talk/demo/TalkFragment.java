package com.talk.demo;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.talk.demo.core.RecordManager;
import com.talk.demo.talk.DialogCache;
import com.talk.demo.talk.DialogItem;
import com.talk.demo.talk.TalkAllItem;
import com.talk.demo.talk.TalkViewItem;
import com.talk.demo.util.AccountUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class TalkFragment extends Fragment {
    
    private static String TAG = "TalkFragment";
    private Context ctx;
    private ListView mListView;
    private ArrayList<TalkViewItem> talk_record;
    private TalkListAdapter talk_adapter;
    private HashMap<String, ArrayList<DialogCache>> dialog_cache;
    private RecordManager recordManager;
    private String ownUser;
    
    public TalkFragment(RecordManager recordMgr, Context ctx) {
        talk_record = new ArrayList<TalkViewItem>();
        recordManager = recordMgr;
        dialog_cache = new HashMap<String, ArrayList<DialogCache>>();
        
        Account accout = AccountUtils.getPasswordAccessibleAccount(ctx);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"account name: "+accout.name);
        	ownUser = accout.name;
        }
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_talk, container, false);
        ctx = TalkFragment.this.getActivity();
        
        mListView = (ListView)rootView.findViewById(R.id.talk_list);
        talk_record = recordManager.initDataListTalk(dialog_cache);
        
        talk_adapter = new TalkListAdapter(this.getActivity(), 
        		talk_record, dialog_cache);
        mListView.setAdapter(talk_adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
		        Intent mIntent = new Intent(ctx, TalkAllItem.class);
		        Bundle mBundle = new Bundle();
		        DialogItem dialog_item = talk_record.get(position).getListViewItem().get(0);
		        
		        String talkObj = dialog_item.getLink();
		        if(talkObj.equalsIgnoreCase(ownUser)) {
		        	talkObj = dialog_item.getSender();
		        }
		        Log.d(TAG, "talk object is : "+ talkObj);
		        mBundle.putString("link", talkObj);
	            mBundle.putString("createdate", dialog_item.getCreateDate());
	            mBundle.putString("createtime", dialog_item.getCreateTime());
		        mBundle.putParcelableArrayList("recordcache", dialog_cache.get(talkObj));
		        Log.d(TAG,"items size: "+ dialog_cache.get(talkObj).size());
			    mIntent.putExtras(mBundle);
			    //mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    ctx.startActivity(mIntent);	
				
			}
        	
        });
        
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				ShowTalkDialog().show();
				return true;
			}
        	
        });
        
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
        talk_record = recordManager.initDataListTalk(dialog_cache);
        talk_adapter.notifyDataSetChanged();
    }
    
	private Dialog ShowTalkDialog() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
	    
	    builder.setItems(R.array.talk_opt, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               switch(which) {
	               case 0:
	            	   Toast.makeText(ctx, "移除", 2000).show();
	            	   break;
	               case 1:
	            	   Toast.makeText(ctx, "置顶", 2000).show();
	            	   break;
	               case 2:
	            	   Toast.makeText(ctx, "导出", 2000).show();
	            	   break;
	               }
	           }
	    });
	    return builder.create();
	}
    
}
