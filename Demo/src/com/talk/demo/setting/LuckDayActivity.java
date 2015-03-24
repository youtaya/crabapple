package com.talk.demo.setting;

import android.R.integer;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.talk.demo.MainActivity;
import com.talk.demo.R;

import java.util.Calendar;


public class LuckDayActivity extends Activity {
    private static String TAG = "LuckDayActivity";
    private TextView tv;
    private TextView date;
    private int actualMonth, actualDay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_luckday);

        tv = (TextView)findViewById(R.id.setting_date);
        
        date = (TextView) findViewById(R.id.date);

    }
    
    public void showDateDialog(View v) {
        DialogFragment newFragment = new DialogFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    
    public void setDate(int year, int month, int day) {
        Log.d(TAG, "Chosen Date: "+year+month+day);
        actualMonth = month;
        actualDay = day;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirm_actions, menu);
        return true;
    }
    
    private void confirmDate() {
        //save setting date
        SharedPreferences sp = getSharedPreferences("luck_day", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("Month", actualMonth);
        editor.putInt("Day", actualDay);
        editor.commit();
        //set first use time
        Calendar calendar = Calendar.getInstance();
        SharedPreferences spFUT = getSharedPreferences("first_use_time", Context.MODE_PRIVATE);
        Editor editorFUT = spFUT.edit();
        Log.d(TAG, "first use time :"+calendar.get(calendar.YEAR)+"/"+calendar.get(calendar.MONTH)+"/"+calendar.get(calendar.DAY_OF_MONTH));
        editorFUT.putInt("year", calendar.get(calendar.YEAR));
        editorFUT.putInt("month", calendar.get(calendar.MONTH));
        editorFUT.putInt("day", calendar.get(calendar.DAY_OF_MONTH));
        editorFUT.commit();
        
        //go to Main Activity
        Intent mIntent = new Intent();
        mIntent.setClass(this, MainActivity.class);
        startActivity(mIntent);
        finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_confirm:
                confirmDate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}