package com.talk.demo;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.talk.demo.persistence.DatabaseHandler;
import com.talk.demo.persistence.TimeRecord;

public class TimeFragment extends Fragment {
    
    private EditText et;
    private Button bt;
    private TimeRecord mItem;
    public static final String ARG_ITEM_ID = "item_id";
    
    public TimeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        mItem = DatabaseHandler.getInstance(getActivity()).getTimeRecord(getArguments().getLong(ARG_ITEM_ID));
        
        et = (EditText)rootView.findViewById(R.id.fast_record);
        bt = (Button)rootView.findViewById(R.id.ok_fast_record);
        bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ArrayList<TimeRecord> trlist = new ArrayList<TimeRecord>();  
                String content = et.getText().toString();
                TimeRecord tr1 = new TimeRecord(content);  
                TimeRecord tr2 = new TimeRecord("beautiful girl");  

                trlist.add(tr1);  
                trlist.add(tr2);  

            }
            
        });
        
        return rootView;
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	updatePersonFromUI();
    }
    
    private void updatePersonFromUI() {
    	if (mItem != null) {
    		mItem.title =et.getText().toString();
    		mItem.content = et.getText().toString();
    		mItem.create_time = et.getText().toString();
    		
    		DatabaseHandler.getInstance(getActivity()).putTimeRecord(mItem);
        }
    }
}
