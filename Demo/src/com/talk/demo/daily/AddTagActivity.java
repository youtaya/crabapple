package com.talk.demo.daily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.talk.demo.R;

public class AddTagActivity extends Activity {
	private static final String TAG = "AddTagActivity";
	private EditText mEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tag);
		mEditText = (EditText)findViewById(R.id.edittext);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_tag_actions, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if(id == R.id.action_save) {
        	String mTagName = mEditText.getText().toString();
        	if(mTagName.length() > 0) {
	            Intent mIntent = new Intent();
	            mIntent.putExtra("tag_name", mTagName);
	            setResult(RESULT_OK, mIntent);
	    		finish();
        	}
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
