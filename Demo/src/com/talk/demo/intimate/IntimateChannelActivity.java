package com.talk.demo.intimate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.talk.demo.R;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.RawRecord;

import org.apache.http.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class IntimateChannelActivity extends Activity {
    
    private static final String TAG = IntimateChannelActivity.class.getSimpleName();
    
    private String friend;
    private String last_update_date;
    private SharedPreferences sp;
    private Editor editor;
    private ListView channel_list;
    
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intimate_channel);
        
        channel_list = (ListView) findViewById(R.id.channel_list);
        
        friend = getIntent().getExtras().getString("friend");
        sp = getPreferences(0);
        editor = sp.edit();
        last_update_date = sp.getString("last_date", "1971-1-1");
        new ChannelUpdateTask().execute(friend, last_update_date);
        
        channel_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                
            }
            
        });
    }
    
    public void setChannelData(ArrayList<String> mListItems) {
        
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                mListItems);
        adapter.notifyDataSetChanged();
        
    }
    
    private class ChannelUpdateTask extends AsyncTask<String, String, List<RawRecord>> {
        List<RawRecord> getDataList = new LinkedList<RawRecord>();
        @Override
        protected List<RawRecord> doInBackground(String... params) {
            // Simulates a background job.
            try {
                getDataList = NetworkUtilities.updateChannel(params[0],params[1]);
                
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            return getDataList;
            
        }
        
        @Override
        protected void onPostExecute(List<RawRecord> result) {
        	if(result == null) {
        		return;
        	}
            ArrayList<String> mListItems = new ArrayList<String>();
            //pack raw records to array List
            for(int i=0;i<result.size();i++) {
                mListItems.add(result.get(i).getContent());
            }
            
            setChannelData(mListItems);
            Calendar calendar = Calendar.getInstance();
            last_update_date = String.valueOf(calendar.YEAR)+"-"+
            		String.valueOf(calendar.MONTH)+"-"+
            		String.valueOf(calendar.DAY_OF_MONTH);
            editor.putString("last_date", last_update_date).commit();
            
            super.onPostExecute(result);
        }       
    }
}
