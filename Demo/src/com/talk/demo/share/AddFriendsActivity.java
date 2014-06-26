package com.talk.demo.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends Activity {
	private static String TAG = "AddFriendsActivity";
	List<String> friends;
    ArrayAdapter<String> adapter;
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
				passChooseName(friendName);
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
    
	private void passChooseName(String name) {
	    Log.d(TAG, "pass choose name: "+name);
        if(name != null) {
            Intent resultIntent = new Intent();
            Log.d(TAG, "friend name : "+name);
            resultIntent.putExtra("friend_name", name);
            this.setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
	}
	
    public void initData() {
        
        adapter= new ArrayAdapter<String>(this, R.layout.friend_listitem, R.id.friend_name, friends);
        view.setAdapter(adapter);
    }
}
