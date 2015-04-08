package com.talk.demo.intimate;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.talk.demo.R;
import com.talk.demo.share.FriendsActivity;
import com.talk.demo.types.Candidate;
import com.talk.demo.types.Friend;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.NetworkUtilities;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewIntimateActivity extends Activity {
	private static String TAG = "NewIntimateActivity";
    private ImageView from_contact, from_internet;
    NewIntimateListAdapter adapter;
    ListView mListView;
    ArrayList<HashMap<String, Object>> friends;
    String ourName;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_intimate);
        
        from_contact = (ImageView)findViewById(R.id.from_contact);
        from_internet = (ImageView)findViewById(R.id.from_internet);
        
        from_contact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(NewIntimateActivity.this, FindIntimateActivity.class);
                startActivity(mIntent);         
            }
            
        });
        
        from_internet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(NewIntimateActivity.this, FriendsActivity.class);
                startActivity(mIntent);           
            }
            
        });
        
        mListView = (ListView) findViewById(R.id.new_friend_list);
        friends = new ArrayList<HashMap<String, Object>>();
        new loadFriendList().execute();
        
        Account accout = AccountUtils.getPasswordAccessibleAccount(this);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
            Log.d(TAG,"ccount name: "+accout.name);
            ourName = accout.name;
        }
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if(id == R.id.action_add_intimate) {
            Intent mIntent = new Intent(this, FindDSourceFriendsActivity.class);
            this.startActivity(mIntent);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_intimate_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "got the return :" + requestCode + " :" + resultCode);
	}
	
    public void initData() {

        adapter = new NewIntimateListAdapter(this, friends, R.layout.friend_find_listitem,
                new String[] {
                        "avatar", "friend_name", "add"
                },
                new int[] {
                        R.id.friend_avatar, R.id.friend_find_name, R.id.decrator
                });
        mListView.setAdapter(adapter);
    }

    private class loadFriendList extends AsyncTask<Void, Void, List<Candidate>> {
        private List<Candidate> getFriendList;

        protected List<Candidate> doInBackground(Void... params) {
            try {
                getFriendList = new ArrayList<Candidate>();
                getFriendList = NetworkUtilities.newFriends();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return getFriendList;
        }

        protected void onPostExecute(List<Candidate> result) {
        	if(null != result) {
	            for (Candidate friend : getFriendList) {
	                // not need to contain our user name
	                if (ourName.equals(friend.getUserName())) {
	                    continue;
	                }
	                HashMap<String, Object> map = new HashMap<String, Object>();
	                map.put("avatar", R.drawable.avatar);
	                map.put("friend_name", friend.getUserName());
	                map.put("add", R.drawable.ofm_add_icon);
	                friends.add(map);
	            }
	            initData();
        	}
        }

    }
}
