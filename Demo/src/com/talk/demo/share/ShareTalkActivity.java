package com.talk.demo.share;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.TalkUtil;

public class ShareTalkActivity extends Activity {
	private static String TAG = "ShareTalkActivity";
    private String create_time;
    private String link_name;
    private RecordCache talk_cache;
    private TextView link_tv;
    private TextView time_tv;
    private TextView tv;
    
    private DBManager mgr;
    private LinearLayout share_comment_layout;
    private EditText share_comment;
    private ImageView share_save;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_share_talk);
        
        Bundle bundle = getIntent().getExtras();
        create_time = bundle.getString("createtime");
        link_name = bundle.getString("link");
        talk_cache = bundle.getParcelable("recordcache");
        link_tv = (TextView)findViewById(R.id.link_name);
        link_tv.setText(link_name);
        time_tv = (TextView)findViewById(R.id.create_time);
        time_tv.setText(create_time);
        tv = (TextView)findViewById(R.id.share_content);
        tv.setText(talk_cache.getContent());
        tv.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				//Todo:bring user to write
				if(share_comment_layout.getVisibility() == View.VISIBLE) {
					share_comment_layout.setVisibility(View.GONE);
				} else {
					share_comment_layout.setVisibility(View.VISIBLE);
				}
				return false;
			}
        	
        });

        mgr = new DBManager(this);
        share_comment_layout = (LinearLayout)findViewById(R.id.share_comment_layout);
        share_comment = (EditText)findViewById(R.id.share_comment);
        share_save = (ImageView)findViewById(R.id.share_send);
        share_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String comment = share_comment.getText().toString();
				if(comment.length() > 0) {
				     
					TimeRecord tr = new TimeRecord(comment);  
					tr.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
					mgr.add(tr);
			    	share_comment.setText("");
				}
			}
        	
        });
    }

}
