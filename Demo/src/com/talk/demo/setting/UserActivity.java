package com.talk.demo.setting;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.util.AccountUtils;

public class UserActivity extends Activity {
	private static String TAG = "UserActivity";
	private TextView tv_rich, user_name;
    // Get rich values
    private RichPresent rp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        
        tv_rich = (TextView)findViewById(R.id.rich_number);
        user_name = (TextView)findViewById(R.id.my_name);
        // Get values
        rp = RichPresent.getInstance(this);
        tv_rich.setText(String.valueOf(rp.getRich()));
        Account accout = AccountUtils.getPasswordAccessibleAccount(this);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"ccount name: "+accout.name);
        	user_name.setText(accout.name);
        }
        
    }

    
    @Override
    public void onResume() {
        super.onResume();
        tv_rich.setText(String.valueOf(rp.getRich()));
    }
}