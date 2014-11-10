
package com.talk.demo.time;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.daily.AddTagActivity;
import com.talk.demo.daily.SelectTagActivity;
import com.talk.demo.persistence.TagRecord;

public class ViewItemActivity extends Activity {
    private static String TAG = "ViewItemActivity";
    private String create_time;
    private String create_date;
    private String content;

    private TextView item_time;
    private TextView item_content;
    private static final int GET_TAG = 107;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Bundle bundle = getIntent().getExtras();
        create_time = bundle.getString("createtime");
        create_date = bundle.getString("createdate");
        content = bundle.getString("content");
        
        item_time = (TextView)findViewById(R.id.item_time);
        item_content = (TextView)findViewById(R.id.item_content);
        
        item_time.setText(create_date+" "+create_time);
        item_content.setText(content);

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if(id == R.id.action_add) {
            Intent mIntent = new Intent(this, SelectTagActivity.class);
            this.startActivityForResult(mIntent, GET_TAG);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tag_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    private void updateTag(String tag) {
        //TODO: update tag item in db
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "got the return :" + requestCode + " :" + resultCode);

        switch (requestCode) {
        case GET_TAG:
            if (resultCode == RESULT_OK) {
                String res_tag = data.getStringExtra("tag_name").toString();
                Log.d(TAG, "tag is : "+res_tag);
                updateTag(res_tag);
                finish();
            }
            break;
        }
    }

}
