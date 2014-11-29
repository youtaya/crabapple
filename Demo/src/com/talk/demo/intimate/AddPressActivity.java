package com.talk.demo.intimate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.talk.demo.R;

public class AddPressActivity extends Activity {
	private static String TAG = "AddPressActivity";
	private TextView wall;
	private Button me_write, me_step;
	private GridView gridView;
	private MentGridViewAdapter mentAdapter;
	String[] contents = {
			"I Miss You",
			"I Love You",
			"A U OK",
			"All Right",
			"Think It",
			"More, More"
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_press);
        
        gridView = (GridView) findViewById(R.id.ment_content);
        mentAdapter = new MentGridViewAdapter(AddPressActivity.this, contents);
        gridView.setAdapter(mentAdapter);
    }
}
