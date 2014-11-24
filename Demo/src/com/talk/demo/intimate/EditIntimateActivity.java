package com.talk.demo.intimate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.talk.demo.R;

public class EditIntimateActivity extends Activity {
	private String friend_name;
	private TextView tvFriendName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_intimate);
        
        Bundle bundle = getIntent().getExtras();
        friend_name = bundle.getString("friend");
        tvFriendName = (TextView) findViewById(R.id.friend_name);
        tvFriendName.setText(friend_name);
    }
}
