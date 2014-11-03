
package com.talk.demo.time;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.talk.demo.R;

import java.util.ArrayList;
import java.util.List;

public class ViewItemActivity extends Activity {
    private String create_time;
    private String create_date;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Bundle bundle = getIntent().getExtras();
        create_time = bundle.getString("createtime");
        create_date = bundle.getString("createdate");
        content = bundle.getString("content");

    }

}
