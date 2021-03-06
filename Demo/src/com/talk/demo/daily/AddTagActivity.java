package com.talk.demo.daily;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TagRecord;

public class AddTagActivity extends Activity {
	private static final String TAG = "AddTagActivity";
	private EditText mEditText;
	private DBManager mgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tag);
		mEditText = (EditText)findViewById(R.id.edittext);
		mgr = new DBManager(this);
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
        	    //add tag item to database
        		TagRecord tagr = new TagRecord();
        		tagr.getTag().setTagName(mTagName);
        		tagr.getTag().setDirty(1);
        	    mgr.addTag(tagr);
        	    
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
