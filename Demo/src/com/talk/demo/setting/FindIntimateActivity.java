package com.talk.demo.setting;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.talk.demo.R;
import com.talk.demo.util.NetworkUtilities;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FindIntimateActivity extends Activity {
	private static String TAG = "FindIntimateActivity";
	List<String> friends;
    ArrayAdapter<String> adapter;
    ListView view;
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
        
        friends = new ArrayList<String>();
        new loadListFromHtml().execute();
    }
    
    public void initData() {
        
        adapter= new ArrayAdapter<String>(this, R.layout.friend_find_listitem, R.id.friend_find_name, friends);
        view.setAdapter(adapter);
    }
    
	private class loadListFromHtml extends AsyncTask<Void, Void, List<String>> {
		protected List<String> doInBackground(Void... params) {
			try {
				friends = NetworkUtilities.syncFriends();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return friends;
		}

		protected void onPostExecute(List<String> result) {
			initData();
		}

	}
}
