package com.talk.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.talk.demo.core.RecordManager;
import com.talk.demo.daily.DailyEditActivity;
import com.talk.demo.prewrite.PreWrite;
import com.talk.demo.util.Constant;
import com.talk.demo.util.DailyNews;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.TalkUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class DailyFragment extends Fragment implements OnItemClickListener {
    private static String TAG = "DailyFragment";
    //private ListView lv;
    private PullToRefreshListView pullToRefreshView;
    private FloatingActionButton btn_new;
    private RecordManager recordManager;
    private LinkedList<String> daily_record;
    private DailyListAdapter adapter;
    private PreWrite pw;
    private LinkedList<String> mListItems;
    private SharedPreferences mass_sp;
    private Editor editor;
    
    public DailyFragment(RecordManager recordMgr, PreWrite prewrite) {
        daily_record = new LinkedList<String>();
        recordManager = recordMgr;
        pw = prewrite;
        
    }

    /*
    private void diamondDialog() {
        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setTitle("新进宝石一枚");
        builder.setMessage("宝石可以用于解锁锁定时光:)");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    */
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);
        
        
        // Set a listener to be invoked when the list should be refreshed.
        pullToRefreshView = (PullToRefreshListView)rootView.findViewById(R.id.daily_list);
        //lv = (ListView)rootView.findViewById(R.id.daily_list);
        mass_sp = getActivity().getSharedPreferences(Constant.NEWS_ID, getActivity().MODE_PRIVATE);
        editor = mass_sp.edit();
        
        if(isExpired()) {
            // async to load news
            new GetDataTask().execute();
        }
        
        btn_new = (FloatingActionButton)rootView.findViewById(R.id.btn_new);
        btn_new.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
			    switch (action) {
			    case MotionEvent.ACTION_MOVE:
			    case MotionEvent.ACTION_DOWN:
			    	btn_new.setPressed(true);
			    	break;
			    case MotionEvent.ACTION_UP:
			    	if(!btn_new.isFocusable()) {
			    		Log.d(TAG, "test....");
				    	Intent intent = new Intent(getActivity(),DailyEditActivity.class);  
		            	getActivity().startActivity(intent);  
		            	getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.keep_unchanage);
			    	}
			    	btn_new.setPressed(false);
	            	break;
			    case MotionEvent.ACTION_CANCEL:
			    	btn_new.setPressed(false);
			    default:
			    	break;
			    }
			    
				return true;
			}
        	
        });
   
        TextWatcher watcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//btn_maximize.setImageResource(R.drawable.btn_maximize_active);
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if( s.length() > 0) {
					btn_new.setFocusable(true);
				} else {
					btn_new.setFocusable(false);
				}
			}
        };
        
        
        pullToRefreshView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        
        initListView();
        
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    /*
     * expire conditions : 1. first use; 2. news are out of date
     */
    private boolean isExpired() {
        boolean expire = true;
        String default_time = "2012-6-30";
        String eTime = mass_sp.getString(Constant.EXPIRED_TIME, default_time);
        if(!eTime.equals(default_time) && !TalkUtil.isOutDate(eTime)) {
        	Log.d(TAG, "news are refresh, not need to update");
            expire = false;
        }
        return expire;
    }
    
    public LinkedList<String> initDataList() {  
        Log.d(TAG, "init data list");

        if(!daily_record.isEmpty()) {
            daily_record.clear();
        }
      
        // check today title, and update
        Set<String> allNews = mass_sp.getStringSet(Constant.NEWS_CONTENT, null);
        if(allNews != null) {
	    	daily_record.clear();
	    	Iterator<String> it = allNews.iterator(); 
	    	while (it.hasNext()) {
	        	String value = it.next();
	            Log.d(TAG, "temp is "+value);
	            daily_record.add(value);
	        }
        }
        return daily_record;
    }

    
    public void initListView() {
    	
        if(pullToRefreshView == null)
            return;
        mListItems = initDataList();
        adapter = new DailyListAdapter(getActivity(), mListItems);
        pullToRefreshView.setAdapter(adapter);
        pullToRefreshView.setOnItemClickListener(this);

    }
    
    private class GetDataTask extends AsyncTask<Void, Void, DailyNews> {
    	DailyNews getDataList = new DailyNews();
        @Override
		protected DailyNews doInBackground(Void... params) {
            // Simulates a background job.
            try {
                getDataList = NetworkUtilities.syncNews();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            return getDataList;
            
		}
		
        @Override
        protected void onPostExecute(DailyNews result) {
        	HashMap<String, String> news = result.getNews();
        	Set<String> keys = news.keySet();
        	Set<String> values = new HashSet<String>();
            for(Iterator<String> iter = keys.iterator(); iter.hasNext();) {
            	String value = iter.next();
                Log.d(TAG, "temp is "+value);
                values.add(news.get(value));
            }
        	editor.putStringSet(Constant.NEWS_CONTENT, values);
            editor.putString(Constant.CREATE_TIME, result.getCreateTime());
            editor.putString(Constant.EXPIRED_TIME, result.getExpiredTime());
            editor.commit();
            
            adapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            pullToRefreshView.onRefreshComplete();
            super.onPostExecute(result);
        }		
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        
        String valueContent = parent.getItemAtPosition(position).toString();
        
        Intent mIntent = new Intent(getActivity(), DailyEditActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("precontent", valueContent);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
        
    } 
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "on Resume");
        initDataList();
        adapter.notifyDataSetChanged();

        //Todo:get today latest
        
    }
    

}
