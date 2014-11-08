
package com.talk.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.time.DateInfo;
import com.talk.demo.time.TimeAllItem;
import com.talk.demo.time.TimeViewItem;
import com.talk.demo.time.ViewAsItem;
import com.talk.demo.time.ViewItemActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeListAdapter extends BaseAdapter {

	private static String TAG = "TimeListAdapter";
    private final Context context;
    private ArrayList<TimeViewItem> values;
    private ViewHolder holder;
    private ViewTagHolder mTagHolder;
    private ViewHeaderHolder mHeaderHolder;
    private HashMap<String, ArrayList<RecordCache>> record_cache;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    
    private static final int TYPE_CATEGORY_ITEM = 0;  
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_TAG_ITEM = 2; 
    
    public TimeListAdapter(Context context, ArrayList<TimeViewItem> data, HashMap<String, ArrayList<RecordCache>> recordCache) {
    	this.context = context;
    	this.values = data;
    	this.record_cache = recordCache;

		options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20))
			.build();
    	ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
    		.defaultDisplayImageOptions(options)
    		.build();
    	imageLoader.init(config);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	int itemViewType = getItemViewType(position);
    	
    	switch(itemViewType) {
    	case TYPE_ITEM:
            holder = new ViewHolder();  
            convertView = LayoutInflater.from(context).inflate(R.layout.record_listitem, null);  
            holder.image = (ImageView) convertView.findViewById(R.id.time_pic);  
            holder.content = (TextView) convertView.findViewById(R.id.content);  
            holder.create_time = (TextView) convertView.findViewById(R.id.create_time);
            holder.create_date = (TextView) convertView.findViewById(R.id.create_date); 
            holder.create_week = (TextView) convertView.findViewById(R.id.create_week); 
            // 将holder绑定到convertView  
            convertView.setTag(holder);  
	  
	        // 向ViewHolder中填入的数据 
	
            ViewAsItem view_item = values.get(position).getViewItem();
        	holder.content.setText(view_item.getContent());
        	String time_info = view_item.getCreateTime();
        	DateInfo mDateInfo = new DateInfo(time_info);
        	mDateInfo.parseCreateTime();
        	final String createDate = mDateInfo.getDate();
        	final String createTime = mDateInfo.getTime();
        	holder.create_time.setText(mDateInfo.getTime());
        	holder.create_date.setText(mDateInfo.getDate());
        	holder.create_week.setText(mDateInfo.getWeekInfo());
        	int media_type = view_item.getContentType();
        	final String itemContent = view_item.getContent();
        	if(2 == media_type || 4 == media_type) {
        		Uri uri = null;
        		if(null != view_item.getPhoto()) {
	        		uri = Uri.parse("file://"+"/sdcard/Demo/"+view_item.getPhoto());
        		} else {
        			uri = Uri.parse("file://"+"/sdcard/Demo/"+"20140810231230");
        		}
        		Log.d(TAG, " image uri: "+uri.toString());
        		holder.image.setVisibility(View.VISIBLE);
        		imageLoader.displayImage(uri.toString(), holder.image, animateFirstListener);
        	}
	        
            convertView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                	//TODO
    		        Intent mIntent = new Intent(context, ViewItemActivity.class);
    		        Bundle mBundle = new Bundle();
    		        Log.d(TAG, "create date : "+createDate);
    		        mBundle.putString("createdate", createDate);
    		        mBundle.putString("createtime", createTime);
    		        mBundle.putString("content", itemContent);
    		        mIntent.putExtras(mBundle);
    		        context.startActivity(mIntent);
                }
            }); 
	        break;
    	case TYPE_TAG_ITEM:
    	    mTagHolder = new ViewTagHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.record_listitem, null);
            mTagHolder.image = (ImageView) convertView.findViewById(R.id.time_pic);
            mTagHolder.tag = (TextView) convertView.findViewById(R.id.tag);
            mTagHolder.tag.setVisibility(View.VISIBLE);
            mTagHolder.content = (TextView) convertView.findViewById(R.id.content);  
            mTagHolder.create_time = (TextView) convertView.findViewById(R.id.create_time);
            mTagHolder.create_date = (TextView) convertView.findViewById(R.id.create_date); 
            mTagHolder.create_week = (TextView) convertView.findViewById(R.id.create_week); 
            convertView.setTag(mTagHolder);
            
            TimeViewItem time_view_item = values.get(position);
            mTagHolder.tag.setText(time_view_item.getTagTitle());
            final String tag_title = time_view_item.getTagTitle();
            
            // 向ViewHolder中填入的数据 
            // get first item from tag items
            ViewAsItem view_as_item = time_view_item.getListViewItem().get(0);
            mTagHolder.content.setText(view_as_item.getContent());
        	String timeInfo = view_as_item.getCreateTime();
        	DateInfo mDateInfo2 = new DateInfo(timeInfo);
        	mDateInfo2.parseCreateTime();
        	final String createDate2 = mDateInfo2.getDate();
        	final String createTime2 = mDateInfo2.getTime();
        	mTagHolder.create_time.setText(mDateInfo2.getTime());
        	mTagHolder.create_date.setText(mDateInfo2.getDate());
        	mTagHolder.create_week.setText(mDateInfo2.getWeekInfo());
        	int media_type2 = view_as_item.getContentType();
        	if(2 == media_type2 || 4 == media_type2) {
        		Uri uri = null;
        		if(null != view_as_item.getPhoto()) {
	        		uri = Uri.parse("file://"+"/sdcard/Demo/"+view_as_item.getPhoto());
        		} else {
        			uri = Uri.parse("file://"+"/sdcard/Demo/"+"20140810231230");
        		}
        		Log.d(TAG, " image uri: "+uri.toString());
        		mTagHolder.image.setVisibility(View.VISIBLE);
        		imageLoader.displayImage(uri.toString(), holder.image, animateFirstListener);
        	}
        	
            convertView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    //TODO: fix bug
    		        Intent mIntent = new Intent(context, TimeAllItem.class);
    		        Bundle mBundle = new Bundle();
    		        Log.d(TAG, "tag title : "+ tag_title);
    		        mBundle.putString("tag_title", tag_title);
                    mBundle.putString("createdate", createDate2);
                    mBundle.putString("createtime", createTime2);
    		        mBundle.putParcelableArrayList("recordcache", record_cache.get(tag_title));
    		        Log.d(TAG,"items size: "+ record_cache.get(tag_title).size());
        		    mIntent.putExtras(mBundle);
    		        context.startActivity(mIntent);	
                    
                }
            });
    	    break;
    	case TYPE_CATEGORY_ITEM:
    	    mHeaderHolder = new ViewHeaderHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.time_list_header, null);
            mHeaderHolder.list_section = (TextView) convertView.findViewById(R.id.time_list_header);
            convertView.setTag(mHeaderHolder);
            
            mHeaderHolder.list_section.setText(values.get(position).getHeadContent());
    		break;
    	}
        return convertView; 
    }
    
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {


		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				FadeInBitmapDisplayer.animate(imageView, 500);
				imageView.setImageBitmap(loadedImage);
			}
		}
	}
    /** 
     * ViewHolder类用以储存item中控件的引用 
     */  
    final class ViewHolder {  
        ImageView image;
        TextView content;
        TextView create_time;
        TextView create_date;
        TextView create_week;
    }
    
    final class ViewTagHolder {  
        ImageView image;
        TextView tag;
        TextView content;
        TextView create_time;
        TextView create_date;
        TextView create_week;
    }
    
    final class ViewHeaderHolder {  
        TextView list_section;
    }

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	} 
	
    @Override
    public int getItemViewType(int position) {
        // 异常情况处理  
        if (null == values || position <  0|| position > getCount()) {
            return TYPE_ITEM;
        } 
        
        int type = values.get(position).getType();
        switch(type) {
        case 0:
        	return TYPE_CATEGORY_ITEM;
        case 1:
        	return TYPE_ITEM;
        case 2:
        	return TYPE_TAG_ITEM;
        }
        
        return TYPE_ITEM;
    }
    
    @Override
    public int getViewTypeCount() {
        return 3;
    }
}
