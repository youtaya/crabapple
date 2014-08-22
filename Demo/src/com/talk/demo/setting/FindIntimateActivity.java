package com.talk.demo.setting;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.talk.demo.R;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.NetworkUtilities;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindIntimateActivity extends Activity {
	private static String TAG = "FindIntimateActivity";
	ArrayList<HashMap<String, Object>> friends;
    FindIntimateListAdapter adapter;
    ListView view;
    String ourName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_intimate);
        view = (ListView) findViewById(R.id.find_intimate_list);
        view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Log.d(TAG, "position : "+position+"id : "+id);
            }
        });
        
        friends = new ArrayList<HashMap<String, Object>>();
        new loadFriendList().execute();
        
        Account accout = AccountUtils.getPasswordAccessibleAccount(this);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"ccount name: "+accout.name);
        	ourName = accout.name;
        }
    }
    
    public void initData() {
        
        adapter= new FindIntimateListAdapter(this, friends, R.layout.friend_find_listitem,
        		new String[]{"avatar", "friend_name", "add"}, 
                new int[]{R.id.friend_avatar, R.id.friend_find_name, R.id.decrator});
        view.setAdapter(adapter);
    }
    
	private class loadFriendList extends AsyncTask<Void, Void, List<String>> {
		private List<String> getFriendList;
		protected List<String> doInBackground(Void... params) {
			try {
				getFriendList = new ArrayList<String>();
				getFriendList = NetworkUtilities.recommendFriends();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return getFriendList;
		}

		protected void onPostExecute(List<String> result) {
			for(String name: getFriendList) {
				//not need to contain our user name
				if(ourName.equals(name)) {
					continue;
				}
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("avatar", R.drawable.default_avatar);
				map.put("friend_name", name);
				map.put("add", R.drawable.ofm_add_icon);
				friends.add(map);
			}
			initData();
		}

	}
}
