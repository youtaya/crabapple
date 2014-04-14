package com.talk.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.time.TimeAllItem;
import com.talk.demo.util.TalkUtil;

public class TimeFragment extends Fragment implements OnItemClickListener {
    OnItemChangedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnItemChangedListener {
        public void onItemChanged();
    }
    
    private static String TAG = "TimeFragment";
    private ListView lv;
    private EditText et;
    private ImageView iv,ivSpring, ivPhoto;
    private DBManager mgr;
    private List<TimeRecord> trlist;
    private ArrayList<Map<String, String>> time_record;
    private ArrayList<RecordCache> record_cache;
    private SimpleAdapter adapter;
    private LinearLayout take_snap;
    private boolean snap_on = false;
    public TimeFragment(DBManager db) {
        time_record = new ArrayList<Map<String, String>>();
        trlist = new ArrayList<TimeRecord>();
        record_cache = new ArrayList<RecordCache>();
        mgr = db;
    }

    private void hideKeyboardAndClearET() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
      	      Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et.setText("");
        iv.setImageResource(R.drawable.btn_check_off_normal);
    }
    
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        	getActivity().startActivityForResult(takePictureIntent, TalkUtil.REQUEST_IMAGE_CAPTURE);
        }
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        take_snap = (LinearLayout)rootView.findViewById(R.id.take_snap);
        
        lv = (ListView)rootView.findViewById(R.id.time_list);
        et = (EditText)rootView.findViewById(R.id.fast_record);
        ivSpring = (ImageView)rootView.findViewById(R.id.tool_spring);
        ivPhoto = (ImageView)rootView.findViewById(R.id.take_photo);
        ivPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dispatchTakePictureIntent();
				take_snap.setVisibility(View.GONE);
			}
        	
        });
        ivSpring.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!snap_on) {
					take_snap.setVisibility(View.VISIBLE);
					snap_on = true;
				} else {
					take_snap.setVisibility(View.GONE);
					snap_on = false;
				}
			}
        	
        });
        iv = (ImageView)rootView.findViewById(R.id.ok_fast_record);
        TextWatcher watcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				iv.setImageResource(R.drawable.btn_check_on_normal);
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if( s.length() > 0) {
					iv.setImageResource(R.drawable.btn_check_on_normal);
				} else {
					iv.setImageResource(R.drawable.btn_check_off_normal);
				}
				
			}
        	
        };
        et.addTextChangedListener(watcher);
        
        iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String content = et.getText().toString();
                TimeRecord tr = new TimeRecord(content);  
                tr.setMediaType(TalkUtil.MEDIA_TYPE_TEXT);
                mgr.add(tr);
                
                RecordFragment rFragment = RecordFragment.newInstance(mgr);;
                rFragment.update();
                // update time list view
                initDataList();
                adapter.notifyDataSetChanged();
                
                hideKeyboardAndClearET();
                
                mCallback.onItemChanged();
            }
            
        });
        
        initListView();
        
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnItemChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemChangedListener");
        }
    }

   
    private ArrayList<Map<String, String>> initDataList() {  
        if(!trlist.isEmpty()) {
             trlist.clear();
        }
        Log.d(TAG, "init data list");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = TalkUtil.Cal_Days(new Date(), 0);
        
        trlist = mgr.queryWithParams(dateFormat.format(date)); 
        
        if(!time_record.isEmpty()) {
            time_record.clear();
        }
        
        for (TimeRecord tr : trlist) {  
            HashMap<String, String> map = new HashMap<String, String>(); 
            RecordCache rc = new RecordCache();
            map.put("content", tr.content); 
            rc.setContent(tr.content);
            map.put("create_date", tr.create_date);
            rc.setCreateDate(tr.create_date);
            map.put("create_time", tr.create_time);  
            rc.setCreateTime(tr.create_time);
            rc.setMediaType(tr.media_type);
            record_cache.add(rc);
            time_record.add(map);  
        }  
  
        return time_record;
    }
    
    private void initListView() {
        if(lv == null)
            return;
        initDataList();
        adapter = new SimpleAdapter(getActivity(),time_record, R.layout.record_listitem,
                new String[]{"content", "create_date", "create_time"}, new int[]{R.id.content, R.id.create_date, R.id.create_time});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // TODO Auto-generated method stub
        
        String valueContent = parent.getItemAtPosition(position).toString();
        
        Intent mIntent = new Intent(getActivity(), TimeAllItem.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("createdate", valueContent.split(",")[1].substring(13));
        mBundle.putString("createtime", valueContent.split(",")[0].substring(13));
        mBundle.putParcelableArrayList("recordcache", record_cache);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
        
        
    } 
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
        initDataList();
        adapter.notifyDataSetChanged();

    }

}
