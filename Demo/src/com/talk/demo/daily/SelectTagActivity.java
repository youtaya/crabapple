package com.talk.demo.daily;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.talk.demo.R;

import java.util.List;

public class SelectTagActivity extends Activity {
	private static final String TAG = "SelectTagActivity";
	private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
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
	
    public void addItems(View v) {
        listItems.add("Clicked : "+clickCounter++);
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
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
