package com.talk.demo;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.talk.demo.account.AccountConstants;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.time.TimeViewItem;
import com.talk.demo.util.AccountUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TimeFragment extends Fragment {
    
    private static String TAG = "TimeFragment";
    private SwipeListView lv;
    private ArrayList<TimeViewItem> time_record;
    private HashMap<String, ArrayList<RecordCache>> record_cache;
    private TimeListAdapter tAdapter;
    private RecordManager recordManager;
    
    public TimeFragment(RecordManager recordMgr) {
        time_record = new ArrayList<TimeViewItem>();
        record_cache = new HashMap<String, ArrayList<RecordCache>>();
        recordManager = recordMgr;
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        
        lv = (SwipeListView)rootView.findViewById(R.id.time_list);

        initListView();
        
        lv.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onChoiceChanged(int position, boolean selected) {
                Log.d(TAG, "onChoiceChanged:" + position + ", " + selected);
            }

            @Override
            public void onChoiceEnded() {
                Log.d(TAG, "onChoiceEnded");
            }

            @Override
            public void onChoiceStarted() {
                Log.d(TAG, "onChoiceStarted");
            }

            @Override
            public void onClickBackView(int position) {
                Log.d(TAG, "onClickBackView:" + position);
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d(TAG, "onClickFrontView:" + position);
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
                Log.d(TAG, "onClosed:" + position + "," + fromRight);
            }

            @Override
            public void onDismiss(int[] arg0) {
                Log.d(TAG, "onDismiss");
            }

            @Override
            public void onFirstListItem() {
                Log.d(TAG, "onFirstListItem");
            }

            @Override
            public void onLastListItem() {
                Log.d(TAG, "onLastListItem");
            }

            @Override
            public void onListChanged() {
                Log.d(TAG, "onListChanged");
            }

            @Override
            public void onMove(int position, float x) {
                Log.d(TAG, "onMove:" + position + "," + x);
            }

            @Override
            public void onOpened(int position, boolean toRight) {
                Log.d(TAG, "onOpened:" + position + "," + toRight);
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d(TAG, "onStartClose:" + position + "," + right);
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d(TAG, "onStartOpen:" + position + "," + action + "," + right);
            }
        });
        
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
