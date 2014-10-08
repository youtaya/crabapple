package com.talk.demo.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.talk.demo.Card;
import com.talk.demo.CardAdapter;
import com.talk.demo.CardBase;
import com.talk.demo.CardClickListener;
import com.talk.demo.CardListView;
import com.talk.demo.R;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.share.ShareTalkActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreActivity extends Activity {
    private static String TAG = "StoreActivity";
    
    private DBManager mgr;
    private RecordManager recordManager;
    
    private CardListView cardLv;
    private ArrayList<Map<String, String>> time_record;
    private ArrayList<RecordCache> record_cache;
    private CardAdapter<Card> cardAdapter;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        
        mgr = new DBManager(this);
        recordManager = new RecordManager(mgr);
        
        time_record = new ArrayList<Map<String, String>>();
        record_cache = new ArrayList<RecordCache>();
        cardLv = (CardListView)findViewById(R.id.store_list);
        initListView();
        
    }
    private void initListView() {
        if(cardLv == null)
            return;
        time_record = recordManager.initDataListTalk(record_cache);
        
        cardAdapter = new CardAdapter<Card>(this.getActivity().getApplicationContext(),android.R.color.holo_blue_dark);
        
        // Add a basic header and cards below it
        for(Map<String,String> map : time_record) {
            //cardAdapter.add(new CardHeader(map.get("create_time")));
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
