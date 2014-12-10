package com.talk.demo.intimate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;
import com.talk.demo.talk.DialogItem;
import com.talk.demo.talk.TalkAllItem;

import java.util.ArrayList;
import java.util.List;

public class IntimateActivity extends Activity {
	private static String TAG = "IntimateActivity";
	ArrayList<String> friends;
	IntimateListAdapter adapter;
    ListView view;
    private LinearLayout new_friend;
    private DBManager mgr;
    private List<FriendRecord> frList;
    
    private int friendId;
    private String friendName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intimate);
        
        new_friend = (LinearLayout)findViewById(R.id.new_friend);
        new_friend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(IntimateActivity.this, FindDSourceFriendsActivity.class));
			}
        	
        });
        
        view = (ListView) findViewById(R.id.intimate_list);
        view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.d(TAG, "position : "+position+"id : "+id);
				TextView name = (TextView)view.findViewById(R.id.friend_name);
				friendId = frList.get(position)._id;
				friendName = name.getText().toString();
				//Todo:start daily edit activity
		        Intent mIntent = new Intent(IntimateActivity.this, EditIntimateActivity.class);
		        Bundle mBundle = new Bundle();
		        mBundle.putInt("id", friendId);
		        mBundle.putString("friend", friendName);
		        mIntent.putExtras(mBundle);
			    startActivity(mIntent);	
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
                new int[]{R.id.friend_avatar, R.id.friend_name});
        view.setAdapter(adapter);
    }
}
