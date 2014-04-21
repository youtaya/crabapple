package com.talk.demo.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.talk.demo.R;

public class AppMainActivity extends Activity {
	
	private Button login_btn, signup_btn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_app_main);
		login_btn = (Button)findViewById(R.id.login_btn);
		signup_btn = (Button)findViewById(R.id.signup_btn);
		
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        Intent intent = new Intent();
		        intent.setClass(v.getContext(), LoginActivity.class);
		        startActivity(intent);
			}
		});
		
		signup_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
