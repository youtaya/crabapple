package com.talk.demo.setting;

import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talk.demo.MainActivity;
import com.talk.demo.R;
import com.talk.demo.util.TalkUtil;


public class LuckDayActivity extends Activity {
    private static String TAG = "LuckDayActivity";
    private TextView tv;
    private WheelView month;
    private WheelView day;
    private int actualMonth, actualDay;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //if date has set, go to main activity
        SharedPreferences sPreferences = getSharedPreferences("luck_day", Context.MODE_PRIVATE);
        int setMonth = sPreferences.getInt("Month", 0);
        int setDay = sPreferences.getInt("Day", 0);
        if(setMonth > 0 || setDay > 0) {
            Log.d(TAG, "month is : "+setMonth+ "day is : "+setDay);
            Intent mIntent = new Intent();
            mIntent.setClass(this, MainActivity.class);
            startActivity(mIntent);
            finish();
        }
        setContentView(R.layout.activity_luckday);

        tv = (TextView)findViewById(R.id.setting_date);
        Calendar calendar = Calendar.getInstance();

        month = (WheelView) findViewById(R.id.month);
        day = (WheelView) findViewById(R.id.day);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(month, day);
                actualMonth = month.getCurrentItem();
                actualDay = day.getCurrentItem() + 1;
                tv.setText("Month "+(actualMonth + 1)+" Day "+actualDay);
            }
        };

        // month
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[] {"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"};
        month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);
        
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        day.addChangingListener(listener);
        //day
        updateDays(month, day);

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
        //goto main activity
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
    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(26);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}