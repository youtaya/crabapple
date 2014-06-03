package com.talk.demo.share;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.util.TalkUtil;

import java.util.ArrayList;

public class ShareTalkActivity extends Activity {
	private static String TAG = "ShareTalkActivity";
    private String create_time;
    private String link_name;
    private RecordCache talk_cache;
    //private OptJsonData ojd;
    private ListView lv;
    private TextView link_tv;
    private TextView time_tv;
    private TextView tv;
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
        link_name = bundle.getString("link");
        talk_cache = bundle.getParcelable("recordcache");
        //ojd = new OptJsonData(this.getApplicationContext());
        //lv = (ListView)findViewById(R.id.share_list);
        //initListView();
        link_tv = (TextView)findViewById(R.id.link_name);
        link_tv.setText(link_name);
        time_tv = (TextView)findViewById(R.id.create_time);
        time_tv.setText(create_time);
        tv = (TextView)findViewById(R.id.share_content);
        tv.setText(talk_cache.getContent());
        
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
			    	/*
			    	 *  load json data from file
			    	 *  add new talk item
			    	 *  save new json data
			    	 */
			    	//ojd.appendJsonData(shareEntity);
			    	adapter.notifyDataSetChanged();
			    	lv.setSelection(share_record.size() - 1);
			    	share_comment.setText("");
				}
			}
        	
        });
    }
    

    /*
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
    */
}
