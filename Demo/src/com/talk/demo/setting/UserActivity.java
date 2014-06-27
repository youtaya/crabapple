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
import com.talk.demo.util.AccountUtils;

public class UserActivity extends Activity {
	private static String TAG = "UserActivity";
	private TextView tv_rich, user_name, tv_luck;
    // Get rich values
    private RichPresent rp;
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
        
        // init example series data
        GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
            new GraphViewData(1, 2.0d)
            , new GraphViewData(2, 1.5d)
            , new GraphViewData(3, 2.5d)
            , new GraphViewData(4, 1.0d)
        });

        GraphView graphView = new BarGraphView(
            this // context
            , "GraphViewDemo" // heading
        );
        graphView.addSeries(exampleSeries); // data

        LinearLayout layout = (LinearLayout) findViewById(R.id.vtable);
        layout.addView(graphView);

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