
package com.talk.demo.time;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.talk.demo.R;

public class ViewItemActivity extends Activity {
    private String create_time;
    private String create_date;
    private String content;

    private TextView item_time;
    private TextView item_content;
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

}
