package com.talk.demo.share;

import android.app.Activity;
import android.os.Bundle;

import com.talk.demo.R;
import com.talk.demo.persistence.RecordCache;

public class ShareTalkActivity extends Activity {
    private String create_time;
    private RecordCache record_cache;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_share_talk);
        Bundle bundle = getIntent().getExtras();
        create_time = bundle.getString("createtime");
        record_cache = bundle.getParcelable("recordcache");
    }
}
