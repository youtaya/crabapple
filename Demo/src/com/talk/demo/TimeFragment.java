package com.talk.demo;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.talk.demo.account.AccountConstants;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.time.TimeAllItem;
import com.talk.demo.util.AccountUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class TimeFragment extends Fragment {
    
    private static String TAG = "TimeFragment";
    private ListView lv;
    private ArrayList<Map<String, Object>> time_record;
    private ArrayList<RecordCache> record_cache;
    private TimeListAdapter tAdapter;
    private RecordManager recordManager;
    
    public TimeFragment(RecordManager recordMgr) {
        time_record = new ArrayList<Map<String, Object>>();
        record_cache = new ArrayList<RecordCache>();
        recordManager = recordMgr;
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        
        lv = (ListView)rootView.findViewById(R.id.time_list);
        
        /*
        lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                new ManualSyncTask().execute();
            }
        });
        */
        
        initListView();
        
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    // Calculate the difference days after first use time
    private void CalDiffDays() {
        Calendar calendar = Calendar.getInstance();
        Calendar savedCalendar = Calendar.getInstance();
        SharedPreferences sPreferences = getActivity().getSharedPreferences("first_use_time", Context.MODE_PRIVATE);
        int sYear = sPreferences.getInt("year", 0);
        int sMonth = sPreferences.getInt("month", 0);
        int sDay = sPreferences.getInt("day", 0);
        savedCalendar.set(sYear, sMonth, sDay);
        long last = calendar.getTimeInMillis()-savedCalendar.getTimeInMillis();
        long diffDays = last / (24 * 60 * 60 * 1000);
        Log.d(TAG, "last day : "+diffDays);
    }
    
    // Calculate whether luck day
    private boolean isLuckDay() {
        Calendar calendar = Calendar.getInstance();
        Calendar savedCalendar = Calendar.getInstance();
        SharedPreferences sPreferences = getActivity().getSharedPreferences("luck_day", Context.MODE_PRIVATE);
        int sMonth = sPreferences.getInt("Month", 0);
        int sDay = sPreferences.getInt("Day", 0);
        savedCalendar.set(calendar.get(Calendar.YEAR), sMonth, sDay);
        int result = calendar.compareTo(savedCalendar);
        return  (result == 0)?true:false;
    }
 
    private void initListView() {
        if(lv == null)
            return;
        time_record = recordManager.initDataListTime(record_cache, isLuckDay());
        
    	// This sets the color displayed for card titles and header actions by default
        tAdapter = new TimeListAdapter(this.getActivity(),time_record, record_cache);
        lv.setAdapter(tAdapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
		        Intent mIntent = new Intent(view.getContext(), TimeAllItem.class);
		        Bundle mBundle = new Bundle();
		        Log.d(TAG, "create date : "+(String)time_record.get(position).get("calc_date"));
		        mBundle.putString("createdate", (String)time_record.get(position).get("calc_date"));
		        mBundle.putString("createtime", (String)time_record.get(position).get("create_time"));
		        Log.d(TAG,"cache size: "+record_cache.size());
		        mBundle.putParcelableArrayList("recordcache", record_cache);
		        mIntent.putExtras(mBundle);
		        startActivity(mIntent);	
				
			}
       	
        });
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
        time_record = recordManager.initDataListTime(record_cache, isLuckDay());
        tAdapter.notifyDataSetChanged();

    }
    
    private class ManualSyncTask extends AsyncTask<Void, Void, Void> {
    	
		@Override
		protected Void doInBackground(Void... params) {
			onRefreshButtonClick();
			//Todo: trigger talk list to update!!
			return null;
		}			
        @Override
        protected void onPostExecute(Void result) {
            // Call onRefreshComplete when the list has been refreshed.
            //lv.onRefreshComplete();
            super.onPostExecute(result);
        }

    }
    
    /**
     * Respond to a button click by calling requestSync(). This is an
     * asynchronous operation.
     *
     * This method is attached to the refresh button in the layout
     * XML file
     *
     * @param v The View associated with the method call,
     * in this case a Button
     */
    public void onRefreshButtonClick() {
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        Account accout = AccountUtils.getPasswordAccessibleAccount(getActivity());
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"ccount name: "+accout.name);
        	ContentResolver.requestSync(accout, AccountConstants.PROVIDER_AUTHORITY, settingsBundle);
        }
        
    }
}
