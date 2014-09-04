package com.talk.demo.daily;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.talk.demo.R;

public class AddTagActivity extends Activity {
	private static final String TAG = "AddTagActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tag);

	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_tag, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if(id == R.id.action_save) {
            //TODO: retuen tag
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
