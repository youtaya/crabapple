
package com.talk.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeListAdapter extends BaseAdapter {

	private static String TAG = "TimeListAdapter";
    private final Context context;
    private ArrayList<Map<String, Object>> values;
    private ViewHolder holder;
    private ArrayList<RecordCache> record_cache;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    
    private static final int TYPE_CATEGORY_ITEM = 0;  
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_TAG_ITEM = 2; 
    private TextView list_section;
    private TextView tag_title, tag_item1, tag_item2, tag_item3;
    
    public TimeListAdapter(Context context, ArrayList<Map<String, Object>> data, ArrayList<RecordCache> recordCache) {
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
    	List<String> items = new ArrayList<String>();
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
	
	        if(null != values.get(position).get("content")) {
	        	holder.content.setText(values.get(position).get("content").toString());
	        	String time_info = values.get(position).get("create_time").toString();
	        	DateInfo mDateInfo = new DateInfo(time_info);
	        	mDateInfo.parseCreateTime();
	        	holder.create_time.setText(mDateInfo.getTime());
	        	holder.create_date.setText(mDateInfo.getDate());
	        	holder.create_week.setText(mDateInfo.getWeekInfo());
	        	int media_type = (Integer)values.get(position).get("content_type");
	        	if(2 == media_type || 4 == media_type) {
	        		Uri uri = null;
	        		if(null != values.get(position).get("photo")) {
		        		uri = Uri.parse("file://"+"/sdcard/Demo/"+values.get(position).get("photo").toString());
	        		} else {
	        			uri = Uri.parse("file://"+"/sdcard/Demo/"+"20140810231230");
	        		}
	        		Log.d(TAG, " image uri: "+uri.toString());
	        		holder.image.setVisibility(View.VISIBLE);
	        		imageLoader.displayImage(uri.toString(), holder.image, animateFirstListener);
	        	}
	        }
	        break;
    	case TYPE_TAG_ITEM:
            convertView = LayoutInflater.from(context).inflate(R.layout.time_list_tags, null);
            tag_title = (TextView) convertView.findViewById(R.id.time_tag_title);
            tag_item1 = (TextView) convertView.findViewById(R.id.time_tag_1);
            tag_item2 = (TextView) convertView.findViewById(R.id.time_tag_2);
            tag_item3 = (TextView) convertView.findViewById(R.id.time_tag_3);
            tag_title.setText(values.get(position).get("title").toString());
            items = (List<String>) values.get(position).get("tags");
            int nums = items.size();
            if (nums == 1) {
            	tag_item1.setText(items.get(0).toString());
            } else if (nums == 2) {
            	tag_item1.setText(items.get(0).toString());
            	tag_item2.setText(items.get(1).toString());
            } else {
            	tag_item1.setText(items.get(0).toString());
            	tag_item2.setText(items.get(1).toString());
            	tag_item3.setText(items.get(2).toString());
            }
    	    break;
    	case TYPE_CATEGORY_ITEM:
            convertView = LayoutInflater.from(context).inflate(R.layout.time_list_header, null);
            list_section = (TextView) convertView.findViewById(R.id.time_list_header);
            list_section.setText(values.get(position).get("header").toString());
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
        
        Map<String, Object> item = values.get(position);
        if (1 == (Integer)item.get("isSection")) {
            return TYPE_CATEGORY_ITEM;
        } else if (2 == (Integer)item.get("isSection")) {
            return TYPE_TAG_ITEM;
        } else {
        	return TYPE_ITEM;
        }
    }
    
    @Override
    public int getViewTypeCount() {
        return 3;
    }
}
