package com.talk.demo.setting;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.talk.demo.R;

public class UserActivity extends Activity {
	private static String TAG = "UserActivity";
	private TextView tv_rich;
    // Get rich values
    private RichPresent rp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        
        tv_rich = (TextView)findViewById(R.id.rich_number);
        // Get values
        rp = RichPresent.getInstance(this);
        tv_rich.setText(String.valueOf(rp.getRich()));
        
    }

    
    @Override
    public void onResume() {
        super.onResume();
        tv_rich.setText(String.valueOf(rp.getRich()));
    }
}