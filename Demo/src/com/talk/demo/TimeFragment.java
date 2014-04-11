package com.talk.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.time.TimeAllItem;

public class TimeFragment extends Fragment implements OnItemClickListener {
    OnItemChangedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnItemChangedListener {
        public void onItemChanged();
    }
    
    private static String TAG = "TimeFragment";
    private ListView lv;
    private EditText et;
    private Button bt;
    private DBManager mgr;
    private List<TimeRecord> trlist;
    private ArrayList<Map<String, String>> time_record;
    private ArrayList<String> time_content;
    private SimpleAdapter adapter;
    
    public TimeFragment(DBManager db) {
        time_record = new ArrayList<Map<String, String>>();
        trlist = new ArrayList<TimeRecord>();
        time_content = new ArrayList<String>();
        mgr = db;
    }

    private void hideKeyboardAndClearET() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
      	      Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et.setText("");
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        lv = (ListView)rootView.findViewById(R.id.time_list);
        
        et = (EditText)rootView.findViewById(R.id.fast_record);
        bt = (Button)rootView.findViewById(R.id.ok_fast_record);
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String content = et.getText().toString();
                TimeRecord tr = new TimeRecord(content);  

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
    /**  
     * @param   参照日期      
     * @param   天数(之前为负数,之后为正数)          
     * @return  参照日期之前或之后days的日期 
     */  
    public Date Cal_Days(Date date, int days) { 
        
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);  
        return calendar.getTime();  
    }
    
    private ArrayList<Map<String, String>> initDataList() {  
        if(!trlist.isEmpty()) {
             trlist.clear();
        }
        Log.d(TAG, "init data list");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = Cal_Days(new Date(), 0);
        
        trlist = mgr.queryWithParams(dateFormat.format(date)); 
        
        if(!time_record.isEmpty()) {
            time_record.clear();
        }
        
        for (TimeRecord tr : trlist) {  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("content", tr.content); 
            map.put("create_date", tr.create_date);
            map.put("create_time", tr.create_time);  
            time_content.add(tr.content);
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
        mBundle.putString("content", valueContent.split(",")[1].substring(13));
        mBundle.putStringArrayList("allContent", time_content);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
        
        
    } 
    @Override
    public void onPause() {
    	super.onPause();
    }
    
}
