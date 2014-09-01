package com.talk.demo.daily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.talk.demo.R;
import com.talk.demo.share.AddFriendsActivity;

public class SelectPopupActivity extends Activity implements OnClickListener{

	private static String TAG = "SelectPopupWindow";
	private static final int GET_FRIEND = 101;
	private static final int GET_TAG = 102;
	private String friend = null;
	private String tag = null;
    private int to_what  = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_dialog);
        Button meBtn = (Button)findViewById(R.id.share_dialog_send_to_me); 
        meBtn.setOnClickListener(this);          
        Button otherBtn = (Button)findViewById(R.id.share_dialog_send_to_friend); 
        otherBtn.setOnClickListener(this);
        Button strangerBtn = (Button)findViewById(R.id.share_dialog_send_to_stranger); 
        strangerBtn.setOnClickListener(this);    
        Button tagBtn = (Button)findViewById(R.id.share_dialog_send_to_tag); 
        tagBtn.setOnClickListener(this); 
        
        Button cancelBtn = (Button)findViewById(R.id.share_dialog_cancel);
        cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });

	}
	
	/*
	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	*/
	private void startFriendActivity() {
		Intent mIntent = new Intent(this, AddFriendsActivity.class);
		this.startActivityForResult(mIntent, GET_FRIEND);
	}
	
	private void startTagActivity() {
		Intent mIntent = new Intent(this, TagActivity.class);
		this.startActivityForResult(mIntent, GET_TAG);
	}
	
	public void onClick(View v) {

		switch (v.getId()) {
        case R.id.share_dialog_send_to_me:
            Log.d(TAG, "send to me");
            to_what = 1;
            Intent resultIntent = new Intent();
            Bundle bund = new Bundle();
            bund.putInt("TO_WHAT", to_what);
            resultIntent.putExtras(bund);
            setResult(RESULT_OK, resultIntent);
    		finish();
            break; 
        case R.id.share_dialog_send_to_friend:
            Log.d(TAG, "send to friend");
            startFriendActivity();
            break;
        case R.id.share_dialog_send_to_stranger:
            Log.d(TAG, "send to stranger");
            to_what = 3;
            Intent resIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("TO_WHAT", to_what);
            bundle.putString("TARGET", "anonymous");
            resIntent.putExtras(bundle);
            setResult(RESULT_OK, resIntent);
    		finish();
            break;             
        case R.id.share_dialog_send_to_tag:
            Log.d(TAG, "send to tag");
            startTagActivity();
            break; 
        default: 
            break; 
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "got the return :" + requestCode + " :" + resultCode);
		switch (requestCode) {
		case GET_FRIEND:
			if (resultCode == RESULT_OK) {
				String name = data.getStringExtra("friend_name").toString();
				friend = name;
	            Log.d(TAG, "friend is : "+friend);
	            to_what = 2;
		        Intent resultIntent = new Intent();
	            Bundle bundle = new Bundle();
	            bundle.putInt("TO_WHAT", to_what);
	            bundle.putString("TARGET", friend);
	            resultIntent.putExtras(bundle);
		        setResult(RESULT_OK, resultIntent);
				finish();
			}
			break;
		case GET_TAG:
			if (resultCode == RESULT_OK) {
	            Log.d(TAG, "tag is : "+tag);
				finish();
			}
			break;			
		}
	}
}