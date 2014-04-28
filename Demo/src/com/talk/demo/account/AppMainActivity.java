package com.talk.demo.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.talk.demo.MainActivity;
import com.talk.demo.R;
import com.talk.demo.setting.LuckDayActivity;
import com.talk.demo.util.AccountUtils;

public class AppMainActivity extends Activity {
	
	private Button login_btn, signup_btn;
	// whether need to call Login Activity
	private boolean accountExist = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_main);
		login_btn = (Button)findViewById(R.id.login_btn);
		signup_btn = (Button)findViewById(R.id.signup_btn);
        Account existing = AccountUtils.getPasswordAccessibleAccount(this);
        if (existing != null && !TextUtils.isEmpty(existing.name)) {
            accountExist = true;
        }
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	            if (accountExist) {
			        Intent mIntent = new Intent();
			        mIntent.setClass(v.getContext(), LuckDayActivity.class);
			        startActivity(mIntent);
			    } else {
    		        Intent intent = new Intent();
    		        intent.setClass(v.getContext(), LoginActivity.class);
    		        startActivity(intent);
			    }
		        finish();
			}
		});
		
		signup_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 
				// 4 test: jump to mainActivity
		        Intent intent = new Intent();
		        intent.setClass(v.getContext(), LuckDayActivity.class);
		        startActivity(intent);
		        finish();
			}
		});
	}
}
