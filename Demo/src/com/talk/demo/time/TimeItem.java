package com.talk.demo.time;

import java.io.File;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.util.TalkUtil;


public class TimeItem extends Fragment {
    private static String TAG = "TimeItem";
    private String valueContent;
    private String creatTime;
    private int media_type;
    private TextView txView;
    private TextView tvTime;
    
    static TimeItem newInstance(RecordCache content) {
        TimeItem newFragment = new TimeItem(content);
        return newFragment;

    }
    
    public TimeItem(RecordCache content) {
        // TODO Auto-generated constructor stub
        valueContent = content.getContent();
        creatTime = content.getCreateTime();
        media_type = content.getMediaType();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "TestFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "TestFragment-----onCreateView");
        View view = inflater.inflate(R.layout.time_item, container, false);
        txView = (TextView) view.findViewById(R.id.item_content);
        tvTime = (TextView) view.findViewById(R.id.item_time);
        
        tvTime.setText(creatTime);
        if(media_type == TalkUtil.MEDIA_TYPE_TEXT)
        	txView.setText(valueContent);
        else if(media_type == TalkUtil.MEDIA_TYPE_PHOTO) {
        	loadImageFromSD(valueContent);
        } else {
        	Log.d(TAG, "audio type not support!");
        }
        return view;
    }
    
    private void loadImageFromSD(String fileDirName) {
    	File imageFile = new File(fileDirName);
    	BitmapDrawable d = new BitmapDrawable(getResources(), imageFile.getAbsolutePath());
    	txView.setBackground(d);
    }
}
