
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
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.time.TimeAllItem;

import java.util.ArrayList;
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
    //private ImageSize targetSize = new ImageSize(120, 80); // result Bitmap will be fit to this size
    
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
        if (convertView == null) {  
            holder = new ViewHolder();  
            convertView = LayoutInflater.from(context).inflate(R.layout.record_listitem, null);  
            holder.image = (ImageView) convertView.findViewById(R.id.time_pic);  
            holder.content = (TextView) convertView.findViewById(R.id.content);  
            holder.create_time = (TextView) convertView.findViewById(R.id.create_time); 
            // 将holder绑定到convertView  
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }  
  
        // 向ViewHolder中填入的数据 

        /*
        if(null != values.get(position).get("photo")) {
        	holder.image.setImageBitmap(ImageUtils.decodeBitmap(values.get(position).get("photo").toString()));
        	holder.image.setOnClickListener(new lvButtonListener(position));
        }
        */
        if(null != values.get(position).get("content")) {
        	holder.content.setText(values.get(position).get("content").toString()); 
        	holder.create_time.setText(values.get(position).get("create_time").toString());
        	int media_type = (Integer)values.get(position).get("content_type");
        	if(2 == media_type || 4 == media_type) {
        		Uri uri = Uri.parse("file://"+values.get(position).get("photo").toString());
        		Log.d(TAG, " image uri: "+uri.toString());
        		
        		imageLoader.displayImage(uri.toString(), holder.image, animateFirstListener);
        		//holder.image.setImageDrawable(ImageUtils.decodeDrawable(values.get(position).get("content").toString()));
        	}
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
}
