package com.talk.demo.share;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.talk.demo.R;
import com.talk.demo.persistence.TalkCache;
import com.talk.demo.util.TalkUtil;

import java.util.ArrayList;

public class ShareTalkActivity extends Activity {
    private String create_time;
    private ArrayList<TalkCache> talk_cache;
    
    private ListView lv;
    private ShareListAdapter adapter;
    private ArrayList<ShareEntity> share_record;
    
    private EditText share_comment;
    private ImageView share_save;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_share_talk);
        
        share_record = new ArrayList<ShareEntity>();
        Bundle bundle = getIntent().getExtras();
        create_time = bundle.getString("createtime");
        talk_cache = bundle.getParcelableArrayList("recordcache");
        
        lv = (ListView)findViewById(R.id.share_list);
        initListView();
        
        share_comment = (EditText)findViewById(R.id.share_comment);
        share_save = (ImageView)findViewById(R.id.share_send);
        share_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String comment = share_comment.getText().toString();
				if(comment.length() > 0) {
					ShareEntity shareEntity = new ShareEntity();
					shareEntity.setShareTime(TalkUtil.currentDate());
					shareEntity.setContent(comment);
					shareEntity.setComeMsg(false);
			    	share_record.add(shareEntity);
			    	adapter.notifyDataSetChanged();
			    	lv.setSelection(share_record.size() - 1);
			    	share_comment.setText("");
				}
			}
        	
        });
    }
    
    private void initListView() {
        if(lv == null)
            return;
        
        for(TalkCache tc : talk_cache) {
	        ShareEntity map = new ShareEntity();
	        map.setShareTime(tc.getCreateDate());
	        map.setContent(tc.getContent());
	        if(tc.getFrom().equals("bob"))
	        	map.setComeMsg(true);
	        else
	        	map.setComeMsg(false);
	        share_record.add(map);
        }
        
        adapter = new ShareListAdapter(this,share_record);
        lv.setAdapter(adapter);
    }
}
