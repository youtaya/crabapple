package com.talk.demo.daily;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.talk.demo.R;

import java.util.List;

public class SelectTagActivity extends Activity {
	private static final String TAG = "SelectTagActivity";
	private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private static final int ADD_TAG = 87;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tag);
        
        ListView myListView = (ListView)findViewById(R.id.ListView);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        myListView.setAdapter(adapter);
	
	}
	
    public void addItems(String params) {
        listItems.add(params);
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tag, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if(id == android.R.id.action_add) {
            //TODO: add tag
            Intent mIntent = new Intent(this, AddTagActivity.class);
            startActivityForResult(mIntent, ADD_TAG);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "got the return :" + requestCode + " :" + resultCode);
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        switch (requestCode) {
        case ADD_TAG:
            if (resultCode == RESULT_OK) {
            }
            break;
        }
    }
}
