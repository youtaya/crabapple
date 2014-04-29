package com.talk.demo.daily;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.talk.demo.R;

public class DailyEditActivity extends Activity {
	private static String TAG = "DailyEditActivity";
	private EditText edit_content;
	private String pre_content;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_dailyedit);
        Bundle bundle = getIntent().getExtras();
        pre_content = bundle.getString("precontent");
        edit_content = (EditText)findViewById(R.id.edit_content);
        edit_content.setText(pre_content);
    }
}
