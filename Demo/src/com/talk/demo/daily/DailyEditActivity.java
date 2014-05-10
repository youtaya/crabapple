package com.talk.demo.daily;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.talk.demo.MainActivity;
import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.share.FriendsActivity;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.RawRecord;
import com.talk.demo.util.TalkUtil;

import org.apache.http.ParseException;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class DailyEditActivity extends Activity {
	private static String TAG = "DailyEditActivity";
	private EditText edit_content;
	private String pre_content;
	private TextView tv;
	private DBManager mgr;
    private static final int GET_FRIEND = 101;
    private String friend = null;
    private TimeRecord tr = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_dailyedit);
        Bundle bundle = getIntent().getExtras();
        pre_content = bundle.getString("precontent");
        edit_content = (EditText)findViewById(R.id.edit_content);
        edit_content.setText(pre_content);
        edit_content.setSelection(pre_content.length());
        
        tv = (TextView)findViewById(R.id.bluestone);
        tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// call friends, and choose one
			    startFriendActivity();
			}
        	
        });
        mgr = new DBManager(this, this.getApplication());
    }
    
    private void startFriendActivity() {
        Intent mIntent = new Intent(this, FriendsActivity.class);
        this.startActivityForResult(mIntent,GET_FRIEND);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confirm_actions, menu);
        return true;
    }
    
    private String shareToFriend(TimeRecord time, String name) {
        String result = "ok";
    	RawRecord raw = RawRecord.create("jinxp", "test", time.content, time.create_date, time.create_time,
    			time.content_type, null, null, null, false, 11, 12, -1, true);
        try {
            NetworkUtilities.shareRecord(raw, name);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return result;
    
    }
    
    private void confirmDone() {
    	//save to db
        String content = edit_content.getText().toString();
        // Do nothing if content is empty
        
        if(content.length() > 0) {
        	tr = new TimeRecord(content);  
        	tr.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
        	mgr.add(tr);
	    }
        
        // share to friend
        //shareToFriend(tr, friend);
        new ShareRecordTask().execute();
        //goto main activity
        /*
        Intent mIntent = new Intent();
        mIntent.setClass(this, MainActivity.class);
        startActivity(mIntent);
        */
        finish();
    }
    
    private class ShareRecordTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return shareToFriend(tr, friend);
        }
        
        @Override
        protected void onPostExecute(final String authToken) {
        }
        @Override
        protected void onCancelled() {
        }
    } 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_confirm:
                confirmDone();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "got the return :"+requestCode+" :"+resultCode);
        switch(requestCode) {
            case GET_FRIEND:
                if(resultCode == RESULT_OK) {
                    String name = data.getStringExtra("friend_name").toString();
                    tv.setText("Choose Friends: " + name);
                    friend = name;
                }
                break;
        }
    }
}
