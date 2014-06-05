package com.talk.demo.setting;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.util.TalkUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PreviewActivity extends Activity {
	private static String TAG = "PreviewActivity";
	private TextView tv, tvDate;
	private DBManager mgr;
	private String preDate;
	private int preNum = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mgr = new DBManager(this);
        
        tv = (TextView)findViewById(R.id.preview_number);
        tvDate = (TextView)findViewById(R.id.preview_date);
        preNum = getPreviewNumber();
        
        tvDate.setText(preDate);
        tvDate.setTextColor(Color.parseColor("blue"));
        
        Log.d(TAG, "num : "+preNum);
        tv.setText(String.valueOf(preNum));
        tv.setTextColor(Color.parseColor("magenta"));
    }

    public int getPreviewNumber() {
	    SimpleDateFormat pDateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
	    Date previewDate = TalkUtil.Cal_Days(new Date(), 1);
	    preDate = pDateFormat.format(previewDate);
	    return mgr.queryWithMultipleParams(TalkUtil.preConditonDates(previewDate)).size(); 
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
}