package com.talk.demo.setting;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.util.TalkUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreviewActivity extends Activity {
	private static String TAG = "PreviewActivity";
	private TextView tv, tvDate;
	private DBManager mgr;
	private PreDate preNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mgr = new DBManager(this);
        
        tv = (TextView)findViewById(R.id.preview_number);
        tvDate = (TextView)findViewById(R.id.preview_date);
        preNum = getDayNumber(1, true);
        
        tvDate.setText(preNum.getPreDate());
        tvDate.setTextColor(Color.parseColor("blue"));
        
        Log.d(TAG, "num : "+preNum.getRecordNums());
        tv.setText(String.valueOf(preNum.getRecordNums()));
        tv.setTextColor(Color.parseColor("magenta"));
        
       
        // init example series data
        List<PreDate> dailyNum = new ArrayList<PreDate>();
        for(int i=0;i<4;i++) {
        	dailyNum.add(getDayNumber(0-i, false));
        }
        GraphViewData[] gvd = new GraphViewData[dailyNum.size()];
        int maxNum = 0;
        for(int j=(dailyNum.size()-1), i=0;j>=0;j--,i++) {
        	gvd[i] = new GraphViewData(i+1, dailyNum.get(j).getRecordNums());
        	if(maxNum < dailyNum.get(j).getRecordNums() ) {
        		maxNum = dailyNum.get(j).getRecordNums();
        	}
        }
        
        GraphViewSeries exampleSeries = new GraphViewSeries(gvd);

        GraphView graphView = new BarGraphView(
            this // context
            , "日记生成统计" // heading
        );
        graphView.getGraphViewStyle().setNumHorizontalLabels(4);
        graphView.getGraphViewStyle().setNumVerticalLabels(maxNum+2);
        graphView.setManualYAxisBounds(maxNum+1, 0);
        //graphView.getGraphViewStyle().setVerticalLabelsWidth(300);
        graphView.setHorizontalLabels(new String[] {"3天前", "2天前", "昨天", "今天"});
        
        graphView.addSeries(exampleSeries); // data

        LinearLayout layout = (LinearLayout) findViewById(R.id.vtable);
        layout.addView(graphView);
    }

    public PreDate getDayNumber(int v, boolean isPreview) {
	    SimpleDateFormat pDateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
	    Date previewDate = TalkUtil.Cal_Days(new Date(), v);
	    String preDate = pDateFormat.format(previewDate);
	    PreDate pd = new PreDate(preDate);
	    int number = 0;
	    if(isPreview)
	    	number = getPreviewNumber(previewDate);
	    else
	    	number = getDailyNumber(previewDate);
	    
	    pd.setRecordNums(number);
	    return pd; 
    }
    
    public int getDailyNumber(Date v) {
	    int numbers = mgr.queryTimeWithParam(TalkUtil.dailyDate(v)).size();
	    return numbers; 
    }
    
    public int getPreviewNumber(Date v) {
	    int numbers = mgr.queryTimeWithMultipleParams(TalkUtil.preConditonDates(v)).size();
	    return numbers; 
    }
    
    class PreDate {
    	private String mPreDate;
    	private int mRecordNums;
    	
    	public PreDate(String v1) {
    		mPreDate = v1;
    	}  	
    	
    	public PreDate(String v1, int v2) {
    		mPreDate = v1;
    		mRecordNums = v2;
    	}
    	
    	public void setRecordNums(int v2) {
    		mRecordNums = v2;
    	}
    	
    	public String getPreDate() {
    		return mPreDate;
    	}
    	
    	public int getRecordNums() {
    		return mRecordNums;
    	}
    	
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
}