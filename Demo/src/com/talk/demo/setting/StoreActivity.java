package com.talk.demo.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardBase;
import com.afollestad.cardsui.CardListView;
import com.afollestad.cardsui.CardListView.CardClickListener;
import com.talk.demo.R;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.share.ShareTalkActivity;
import com.talk.demo.time.TimeCache;
import com.talk.demo.time.TimeViewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreActivity extends Activity {
    private static String TAG = "StoreActivity";
    
    private DBManager mgr;
    private RecordManager recordManager;
    
    private CardListView cardLv;
    private ArrayList<TimeViewItem> time_record;
    private ArrayList<TimeCache> record_cache;
    private CardAdapter<Card> cardAdapter;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        
        mgr = new DBManager(this);
        recordManager = new RecordManager(mgr, this);
        
        time_record = new ArrayList<TimeViewItem>();
        record_cache = new ArrayList<TimeCache>();
        cardLv = (CardListView)findViewById(R.id.store_list);
        initListView();
        
    }
    private void initListView() {
        if(cardLv == null)
            return;
        time_record = recordManager.initStoreListTime(record_cache);
        
        cardAdapter = new CardAdapter<Card>(this,android.R.color.holo_blue_dark);
        
        // Add a basic header and cards below it
        for(TimeViewItem item : time_record) {
            //cardAdapter.add(new CardHeader(map.get("create_time")));
            cardAdapter.add(new Card(item.getViewItem().getContent(), item.getViewItem().getCreateTime()));
        }
        cardLv.setAdapter(cardAdapter);  
        cardLv.setOnCardClickListener(new CardClickListener() {

            @Override
            public void onCardClick(int index, CardBase card, View view) {
                Log.d(TAG, "index: "+index+" card title: "+card.getTitle()+" card content: "+card.getContent());
                Intent mIntent = new Intent(StoreActivity.this, ShareTalkActivity.class);
                Bundle mBundle = new Bundle();
                //1,3,5,7 ==> 0,1,2,3
                int position = (index - 1)/2;
                mBundle.putString("createtime", time_record.get(position).getViewItem().getCreateTime());
                mBundle.putString("link", time_record.get(position).getViewItem().getCreateDate());
                mBundle.putParcelable("recordcache", record_cache.get(position));
                mIntent.putExtras(mBundle);
                startActivity(mIntent);             
            }
            
        });
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
        initListView();
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
