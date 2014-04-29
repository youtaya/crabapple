package com.talk.demo.daily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.talk.demo.MainActivity;
import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.TalkUtil;

public class DailyEditActivity extends Activity {
	private static String TAG = "DailyEditActivity";
	private EditText edit_content;
	private String pre_content;
	private DBManager mgr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_dailyedit);
        Bundle bundle = getIntent().getExtras();
        pre_content = bundle.getString("precontent");
        edit_content = (EditText)findViewById(R.id.edit_content);
        edit_content.setText(pre_content);
        
        mgr = new DBManager(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirm_actions, menu);
        return true;
    }
    
    private void confirmDone() {
    	//save to db
        String content = edit_content.getText().toString();
        // Do nothing if content is empty
        if(content.length() > 0) {
        	TimeRecord tr = new TimeRecord(content);  
        	tr.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        	mgr.add(tr);
	    }
        //goto main activity
        Intent mIntent = new Intent();
        mIntent.setClass(this, MainActivity.class);
        startActivity(mIntent);
        finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_confirm:
                confirmDone();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
