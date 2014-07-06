package com.talk.demo.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.talk.demo.R;
import com.talk.demo.share.FriendsActivity;

public class SettingActivity extends Activity {
	private static String TAG = "SettingActivity";
	private UITableView tableView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        
        tableView = (UITableView) findViewById(R.id.tableView);        
        createList();        
        Log.d(TAG, "total items: " + tableView.getCount());        
        tableView.commit();
    }
    
    private void createList() {
    	CustomClickListener listener = new CustomClickListener();
    	tableView.setClickListener(listener);
    	tableView.addBasicItem("关于西窗话", "版本信息");
    }
    
    private class CustomClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			Log.d(TAG, "item clicked: " + index);
			switch(index) {
			case 0:
				callOtherActivity(AboutActivity.class);
				break;
			}
		}
    }
    
    private void callOtherActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
}