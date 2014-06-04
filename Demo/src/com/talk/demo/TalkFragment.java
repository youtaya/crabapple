package com.talk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardBase;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;
import com.afollestad.cardsui.CardListView.CardClickListener;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.share.ShareTalkActivity;

import java.util.ArrayList;
import java.util.Map;

public class TalkFragment extends Fragment {
    
    private static String TAG = "TalkFragment";
    private CardListView cardLv;
    private ArrayList<Map<String, String>> time_record;
    private ArrayList<RecordCache> record_cache;
    private CardAdapter<Card> cardAdapter;
    private RecordManager recordManager;
    
    public TalkFragment(RecordManager recordMgr) {
        time_record = new ArrayList<Map<String, String>>();
        recordManager = recordMgr;
        //talk_cache = new ArrayList<TalkCache>();
        record_cache = new ArrayList<RecordCache>();
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_talk, container, false);
        
        cardLv = (CardListView)rootView.findViewById(R.id.talk_list);
        
        initListView();
        	

        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void initListView() {
        if(cardLv == null)
            return;
        time_record = recordManager.initDataListTalk(record_cache);
        
        cardAdapter = new CardAdapter<Card>(this.getActivity().getApplicationContext(),android.R.color.holo_blue_dark);
        
        // Add a basic header and cards below it
        for(Map<String,String> map : time_record) {
        	cardAdapter.add(new CardHeader(map.get("create_time")));
        	cardAdapter.add(new Card(map.get("content"), map.get("create_time")));
        }
        cardLv.setAdapter(cardAdapter);  
        cardLv.setOnCardClickListener(new CardClickListener() {

			@Override
			public void onCardClick(int index, CardBase card, View view) {
				Log.d(TAG, "index: "+index+" card title: "+card.getTitle()+" card content: "+card.getContent());
		        Intent mIntent = new Intent(getActivity(), ShareTalkActivity.class);
		        Bundle mBundle = new Bundle();
		        //1,3,5,7 ==> 0,1,2,3
		        int position = (index - 1)/2;
		        mBundle.putString("createtime", time_record.get(position).get("create_time"));
		        mBundle.putString("link", time_record.get(position).get("link"));
		        mBundle.putParcelable("recordcache", record_cache.get(position));
		        mIntent.putExtras(mBundle);
		        startActivity(mIntent);				
			}
        	
        });
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
