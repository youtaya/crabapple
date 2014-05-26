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
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;
import com.talk.demo.persistence.TimeRecord;

import java.util.ArrayList;
import java.util.List;

public class IntimateActivity extends Activity {
	private static String TAG = "IntimateActivity";
	List<String> friends;
    ArrayAdapter<String> adapter;
    ListView view;
    
    private DBManager mgr;
    private List<FriendRecord> frList;
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
        mgr = new DBManager(this);
        frList = new ArrayList<FriendRecord>();
        friends = new ArrayList<String>();
        
        frList = mgr.queryFriend();
        for (FriendRecord fr : frList) {
        	friends.add(fr.getUserName());
        }
        initData();
    }
    
    public void initData() {
        
        adapter= new ArrayAdapter<String>(this, R.layout.friend_listitem, R.id.friend_name, friends);
        view.setAdapter(adapter);
    }
}
