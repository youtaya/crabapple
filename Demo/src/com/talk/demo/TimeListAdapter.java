
package com.talk.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class TimeListAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<Map<String, Object>> values;
    public TimeListAdapter(Context context, ArrayList<Map<String, Object>> data) {
    	this.context = context;
    	this.values = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;  
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
        }
        if(null != values.get(position).get("content")) {
        	holder.content.setText(values.get(position).get("content").toString()); 
        	//holder.create_time.setText(values.get(position).get("create_time").toString()); 
        }
        return convertView; 
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
		// TODO Auto-generated method stub
		return 0;
	} 
}
