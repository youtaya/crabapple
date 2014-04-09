package com.talk.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;

import java.util.ArrayList;

public class TimeFragment extends Fragment {
    
    private EditText et;
    private Button bt;
    private DBManager mgr;
    
    public TimeFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        
        mgr = new DBManager(getActivity()); 
        
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

                mgr.add(trlist);
            }
            
        });
        
        return rootView;
    }
}
