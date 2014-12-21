package com.talk.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.talk.demo.intimate.EditIntimateActivity;
import com.talk.demo.intimate.FindDSourceFriendsActivity;
import com.talk.demo.intimate.IntimateListAdapter;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;

public class PeopleFragment extends Fragment {
	private static String TAG = "IntimateActivity";
	ArrayList<String> friends;
	IntimateListAdapter adapter;
    ListView view;
    private LinearLayout new_friend;
    private DBManager mgr;
    private List<FriendRecord> frList;
    
    private int friendId;
    private String friendName;
    
    private Context mContext;
    
    public PeopleFragment(Context ctx) {
    	mContext = ctx;
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.activity_intimate, container, false);
        
        new_friend = (LinearLayout)rootView.findViewById(R.id.new_friend);
        new_friend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mContext, FindDSourceFriendsActivity.class));
			}
        	
        });
        
        view = (ListView) rootView.findViewById(R.id.intimate_list);
        view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.d(TAG, "position : "+position+"id : "+id);
				TextView name = (TextView)view.findViewById(R.id.friend_name);
				friendId = frList.get(position)._id;
				friendName = name.getText().toString();
				//Todo:start daily edit activity
		        Intent mIntent = new Intent(mContext, EditIntimateActivity.class);
		        Bundle mBundle = new Bundle();
		        mBundle.putInt("id", friendId);
		        mBundle.putString("friend", friendName);
		        mIntent.putExtras(mBundle);
			    startActivity(mIntent);	
            }
        });
        mgr = new DBManager(mContext);
        frList = new ArrayList<FriendRecord>();
        friends = new ArrayList<String>();
        
        frList = mgr.queryFriend();
        for (FriendRecord fr : frList) {
        	friends.add(fr.getUserName());
        }
        initData();
        
        return rootView;
        
    }
    
    public void initData() {
        
        //adapter= new ArrayAdapter<String>(this, R.layout.friend_listitem, R.id.friend_name, friends);
        adapter= new IntimateListAdapter(mContext, friends, R.layout.friend_listitem,
        		new String[]{"avatar", "friend_name"}, 
                new int[]{R.id.friend_avatar, R.id.friend_name});
        view.setAdapter(adapter);
    }
}
