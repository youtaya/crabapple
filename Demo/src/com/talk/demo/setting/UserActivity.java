package com.talk.demo.setting;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.talk.demo.R;
import com.talk.demo.util.AccountUtils;

public class UserActivity extends Activity {
	private static String TAG = "UserActivity";
	private TextView tv_rich, user_name, tv_luck;
	private UITableView tableView;
    // Get rich values
    private RichPresent rp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        
        tableView = (UITableView) findViewById(R.id.tableView);        
        createList();        
        Log.d("MainActivity", "total items: " + tableView.getCount());        
        tableView.commit();
        
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

    }
    
    private void createList() {
    	CustomClickListener listener = new CustomClickListener();
    	tableView.setClickListener(listener);
    	tableView.addBasicItem("Example 1 - UITableView", "without images");
    	tableView.addBasicItem("Example 5 - UITableViewActivity", "a sample activity");
    	tableView.addBasicItem("Example 7 - UIButton", "some floating buttons");
    	tableView.addBasicItem("Example 8 - Clear List", "this button will clear the list");
    	
    }
    private class CustomClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			Log.d("MainActivity", "item clicked: " + index);
		}
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