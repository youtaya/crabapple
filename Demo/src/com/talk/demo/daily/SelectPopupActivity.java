package com.talk.demo.daily;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.talk.demo.R;

public class SelectPopupActivity extends Activity implements OnClickListener{

	private static String TAG = "SelectPopupWindow";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_dialog);
        Button meBtn = (Button)findViewById(R.id.share_dialog_send_to_me); 
        meBtn.setOnClickListener(this);          
        Button otherBtn = (Button)findViewById(R.id.share_dialog_send_to_other); 
        otherBtn.setOnClickListener(this);          
        Button tagBtn = (Button)findViewById(R.id.share_dialog_send_to_tag); 
        tagBtn.setOnClickListener(this); 

	}
	
	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.share_dialog_send_to_me:
            Log.d(TAG, "send to me");
            break; 
        case R.id.share_dialog_send_to_other:
            Log.d(TAG, "send to other");
            break; 
        case R.id.share_dialog_send_to_tag:
            Log.d(TAG, "send to tag");
            break; 
        default: 
            break; 
		}
		finish();
	}
	
}