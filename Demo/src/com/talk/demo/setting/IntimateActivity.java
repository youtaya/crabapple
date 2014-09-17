package com.talk.demo.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;

import java.util.ArrayList;
import java.util.List;

public class IntimateActivity extends Activity {
	private static String TAG = "IntimateActivity";
	ArrayList<String> friends;
	IntimateListAdapter adapter;
    ListView view;
    
    private DBManager mgr;
    private List<FriendRecord> frList;
    
    private String friendName;
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
				TextView name = (TextView)view.findViewById(R.id.friend_name);
				friendName = name.getText().toString();
				//Todo:start daily edit activity
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
        
        //adapter= new ArrayAdapter<String>(this, R.layout.friend_listitem, R.id.friend_name, friends);
        adapter= new IntimateListAdapter(this, friends, R.layout.friend_listitem,
        		new String[]{"avatar", "friend_name"}, 
                new int[]{R.id.friend_avatar, R.id.friend_find_name});
        view.setAdapter(adapter);
    }
}
