package com.talk.demo.setting;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.TalkUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserActivity extends Activity {
	private static String TAG = "UserActivity";
	private TextView tv_rich, user_name, tv_luck;
    // Get rich values
    private RichPresent rp;
    private DBManager mgr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        
        tv_rich = (TextView)findViewById(R.id.rich_number);
        user_name = (TextView)findViewById(R.id.my_name);
        tv_luck = (TextView)findViewById(R.id.my_luck_day);
        // Get values
        rp = RichPresent.getInstance(this);
        tv_rich.setText(String.valueOf(rp.getRich()));
        Account accout = AccountUtils.getPasswordAccessibleAccount(this);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"ccount name: "+accout.name);
        	user_name.setText(accout.name);
        }
        
        mgr = new DBManager(this);
        
        // init example series data
        int[] dailyNum = new int[4];
        for(int i=0;i<4;i++) {
        	dailyNum[i] = getDailyNumber(0-i);
        }
        GraphViewData[] gvd = new GraphViewData[dailyNum.length];
        int maxNum = 0;
        for(int j=(dailyNum.length-1), i=0;j>=0;j--,i++) {
        	gvd[i] = new GraphViewData(i+1, dailyNum[j]);
        	if(maxNum < dailyNum[j] ) {
        		maxNum = dailyNum[j];
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
        graphView.setHorizontalLabels(new String[] {"3 days ago", "2 days ago", "yesterday", "today"});
        
        graphView.addSeries(exampleSeries); // data

        LinearLayout layout = (LinearLayout) findViewById(R.id.vtable);
        layout.addView(graphView);

    }
    
    public int getDailyNumber(int v) {
	    SimpleDateFormat pDateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
	    Date previewDate = TalkUtil.Cal_Days(new Date(), v);
	    String preDate = pDateFormat.format(previewDate);
	    return mgr.queryWithParam(TalkUtil.dailyDate(previewDate)).size(); 
    }
    
    @Override
    public void onResume() {
        super.onResume();
        tv_rich.setText(String.valueOf(rp.getRich()));
        // Get luck day
        SharedPreferences sPreferences = getSharedPreferences("luck_day", Context.MODE_PRIVATE);
        int sMonth = sPreferences.getInt("Month", 0);
        int sDay = sPreferences.getInt("Day", 0);
        tv_luck.setText(sMonth+" 月 "+sDay+" 日 ");
    }
}