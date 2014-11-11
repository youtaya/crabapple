package com.talk.demo.time;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
    private String photo;
    private ImageView item_bg;
    private TextView item_content;
    //private WebView wvView;
    private TextView tvTime;
    
    static TimeItem newInstance(RecordCache content) {
        TimeItem newFragment = new TimeItem(content);
        return newFragment;

    }
    
    public TimeItem(RecordCache content) {
        valueContent = content.getContent();
        creatTime = content.getCreateTime();
        media_type = content.getMediaType();
        photo = content.getPhotoPath();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "TestFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "TestFragment-----onCreateView");
        View view = inflater.inflate(R.layout.activity_view_item, container, false);
        tvTime = (TextView) view.findViewById(R.id.item_time);
        tvTime.setText(creatTime);
        
        item_bg = (ImageView)view.findViewById(R.id.item_bg);
        item_content = (TextView)view.findViewById(R.id.item_content);
        //得到AssetManager
        AssetManager mgr = this.getActivity().getApplicationContext().getAssets();
        //根据路径得到Typeface
        Typeface tf=Typeface.createFromAsset(mgr, "Roboto-Thin.ttf");
        //设置字体
        item_content.setTypeface(tf);
        
        switch(media_type) {
        case TalkUtil.MEDIA_TYPE_TEXT:
        	item_content.setText(valueContent);
        	break;
        case TalkUtil.MEDIA_TYPE_PHOTO:
        	Bitmap bm = loadPhotoAsBitmap(photo);
        	item_bg.setImageBitmap(bm);
        	break;
        case TalkUtil.MEDIA_TYPE_AUDIO:
        	Log.d(TAG, "not support now!");
        	break;
        case TalkUtil.MEDIA_TYPE_PHOTO_TEXT:
        	Bitmap bm2 = loadPhotoAsBitmap(photo);
        	item_bg.setImageBitmap(bm2);
        	item_content.setText(valueContent);
        	break;
        default:
        	Log.d(TAG, "unknow now!");
        	break;
        }
        /*
        wvView = (WebView) view.findViewById(R.id.item_content);
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
        */
        return view;
    }
    
    private Bitmap loadPhotoAsBitmap(String photoPath) {
    	Bitmap bm = BitmapFactory.decodeFile(photoPath);
    	return bm;
    }
}
