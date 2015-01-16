package com.talk.demo.talk;

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
import com.talk.demo.time.DateInfo;
import com.talk.demo.util.TalkUtil;


public class TalkItem extends Fragment {
    private static String TAG = "TalkItem";
    private String valueContent;
    private String sender;
    private String creatTime;
    private String showTime;
    private int media_type;
    private String photo;
    private ImageView item_bg;
    private TextView item_content;
    private TextView tvTime;
    private TextView linkName;
    
    static TalkItem newInstance(DialogCache content) {
        TalkItem newFragment = new TalkItem(content);
        return newFragment;

    }
    
    public TalkItem(DialogCache content) {
        valueContent = content.getContent();
        sender = content.getSender();
        creatTime = content.getCreateTime();
        DateInfo date = new DateInfo(creatTime);
        date.parseCreateTime();
        showTime = date.getTimeHead();
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
        View view = inflater.inflate(R.layout.fragment_dialog_item, container, false);
        linkName = (TextView) view.findViewById(R.id.item_sign);
        linkName.setText("签名: "+sender);
        
        tvTime = (TextView) view.findViewById(R.id.item_time);
        tvTime.setText(showTime);
        
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
        return view;
    }
    
    private Bitmap loadPhotoAsBitmap(String photoPath) {
    	Bitmap bm = BitmapFactory.decodeFile(photoPath);
    	return bm;
    }
}
