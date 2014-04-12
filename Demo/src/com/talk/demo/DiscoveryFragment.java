package com.talk.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talk.demo.persistence.DBManager;
import com.talk.demo.util.TalkUtil;

public class DiscoveryFragment extends Fragment {
	private static String TAG = "DiscoveryFragment";
	private TextView tv, tvDate;
	private DBManager mgr;
	private String preDate;
	private int preNum = 0;
    public static final String ARG_SECTION_NUMBER = "section_number";

    public DiscoveryFragment(DBManager db) {
    	mgr = db;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);
        tv = (TextView)rootView.findViewById(R.id.preview_number);
        tvDate = (TextView)rootView.findViewById(R.id.preview_date);
        preNum = getPreviewNumber();
        
        tvDate.setText(preDate);
        
        Log.d(TAG, "num : "+preNum);
        tv.setText(String.valueOf(preNum));
        return rootView;
    }

    public int getPreviewNumber() {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	    SimpleDateFormat pDateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
	    Date date = TalkUtil.Cal_Days(new Date(), 0);
	    Date previewDate = TalkUtil.Cal_Days(new Date(), 1);
	    preDate = pDateFormat.format(previewDate);
	    return mgr.queryWithParams(dateFormat.format(date)).size(); 
    }
}