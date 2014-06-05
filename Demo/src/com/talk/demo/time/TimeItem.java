package com.talk.demo.time;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.util.TalkUtil;


public class TimeItem extends Fragment {
    private static String TAG = "TimeItem";
    private String valueContent;
    private String creatTime;
    private int media_type;
    private WebView wvView;
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
        wvView = (WebView) view.findViewById(R.id.item_content);
        tvTime = (TextView) view.findViewById(R.id.item_time);
        
        tvTime.setText(creatTime);
        if(media_type == TalkUtil.MEDIA_TYPE_TEXT) {
        	String textHtml = "<html><body><p style=\"font-family: times, Times New Roman, times-roman, georgia, serif;" +
        			"color: #444;margin: 0;padding: 0px 0px 6px 0px;" +
        			"font-size: 51px;line-height: 44px;" +
        			"letter-spacing: -2px;" +
        			"font-weight: bold;" +
        			"text-align:center;font-size:30px\">"+valueContent+"</p></body></html>";
        	wvView.loadDataWithBaseURL(null, textHtml, "text/html", "UTF-8","");
        } else if(media_type == TalkUtil.MEDIA_TYPE_PHOTO) {
        	wvView.getSettings().setLoadWithOverviewMode(true);
        	wvView.getSettings().setUseWideViewPort(true);
        	String imagePath = "file://"+ valueContent;
        	String imageHtml = "<html><head></head><body><img src="+ imagePath + " width=\"100%\"></body></html>";
        	wvView.loadDataWithBaseURL(null, imageHtml, "text/html", "UTF-8","");
        } else if(media_type == TalkUtil.MEDIA_TYPE_AUDIO) {
        	String audioPath = "file://"+ valueContent;
        	String decoratedTag = "<audio width=\"100%\" height=\"10%\" controls>" +
        			"<source src="+audioPath+" type=\"audio/mpeg\"></audio>";
        	String audioHtml = "<html><head></head><body>"+decoratedTag+"</body></html>";
        	
        	wvView.loadDataWithBaseURL(null, audioHtml, "text/html", "UTF-8","");
        } else {
        	Log.d(TAG, "unknown type not support!");
        }
        return view;
    }
    
}
