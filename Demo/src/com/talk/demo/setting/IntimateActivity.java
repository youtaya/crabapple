package com.talk.demo.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.talk.demo.R;

import java.util.ArrayList;
import java.util.List;

public class IntimateActivity extends Activity {
	private static String TAG = "IntimateActivity";
	List<String> friends;
    ArrayAdapter<String> adapter;
    ListView view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intimate);
        view = (ListView) findViewById(R.id.intimate_list);
        view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.d(TAG, "position : "+position+"id : "+id);
            }
        });
        
        friends = new ArrayList<String>();
        // for test
        String r1 = "wanghaia";
        friends.add(r1);
        String r2 = "test1";
        friends.add(r2);
        String r3 = "test2";
        friends.add(r3);
        
        initData();
    }
    
    public void initData() {
        
        adapter= new ArrayAdapter<String>(this, R.layout.friend_listitem, R.id.friend_name, friends);
        view.setAdapter(adapter);
    }
}
