
package com.talk.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    
    public TimeListAdapter(Context context, ArrayList<Map<String, Object>> data, ArrayList<RecordCache> recordCache) {
    	this.context = context;
    	this.values = data;
    	this.record_cache = recordCache;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {  
            holder = new ViewHolder();  
            convertView = LayoutInflater.from(context).inflate(R.layout.record_listitem, null);  
            holder.lf_image = (ImageView) convertView.findViewById(R.id.lf_time_pic);  
            holder.lf_content = (TextView) convertView.findViewById(R.id.lf_content);  
            holder.lf_create_time = (TextView) convertView.findViewById(R.id.lf_create_time);  
            holder.image = (ImageView) convertView.findViewById(R.id.time_pic);  
            holder.content = (TextView) convertView.findViewById(R.id.content);  
            holder.create_time = (TextView) convertView.findViewById(R.id.create_time); 
            // 将holder绑定到convertView  
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }  
  
        // 向ViewHolder中填入的数据 
        if(null != values.get(position).get("lf_content")) {
        	holder.lf_content.setText(values.get(position).get("lf_content").toString());  
        	//holder.lf_create_time.setText(values.get(position).get("lf_create_time").toString());
        	holder.lf_image.setOnClickListener(new lvButtonListener(position));
        }
        if(null != values.get(position).get("content")) {
        	holder.content.setText(values.get(position).get("content").toString()); 
        	//holder.create_time.setText(values.get(position).get("create_time").toString()); 
        	holder.image.setOnClickListener(new lvButtonListener(position));
        }
        return convertView; 
    }
    private void callOtherActivity(int index, int position, boolean left) {
    	Log.d(TAG, "index : "+ index +" position: "+position+" is left: "+left);
        Intent mIntent = new Intent(context, TimeAllItem.class);
        Bundle mBundle = new Bundle();
        Log.d(TAG, "create date : "+(String)values.get(position).get(left?"lf_calc_date":"calc_date"));
        mBundle.putString("createdate", (String)values.get(position).get(left?"lf_calc_date":"calc_date"));
        mBundle.putString("createtime", (String)values.get(position).get(left?"lf_create_time":"create_time"));
        Log.d(TAG,"cache size: "+record_cache.size());
        mBundle.putParcelableArrayList("recordcache", record_cache);
        mIntent.putExtras(mBundle);
        context.startActivity(mIntent);	
    }
    
    class lvButtonListener implements OnClickListener {
        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }
        
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            int index = 0;
            boolean isleft = true;
            /*
             * {0:[0,1],1:[2,3],2:[4,5],3:[6,7]...}
             */
            if (vid == holder.lf_image.getId()) {
            	index = 2*position;
            	isleft = true;
            	Log.d(TAG, "left view id");
            	callOtherActivity(index, position, isleft);
            } else if (vid == holder.image.getId()) {
            	index = 2*position+1;
            	isleft = false;
            	Log.d(TAG, "right view id");
            	callOtherActivity(index, position, isleft);
            } else {
            	Log.d(TAG, "unknow view id");
            }
        }
    }
    /** 
     * ViewHolder类用以储存item中控件的引用 
     */  
    final class ViewHolder {  
        ImageView lf_image;  
        TextView lf_content;
        TextView lf_create_time;
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
