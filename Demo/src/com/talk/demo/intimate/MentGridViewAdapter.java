package com.talk.demo.intimate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;

public class MentGridViewAdapter extends BaseAdapter {
 
	private Context mContext;
	private final String[] values;
	private int[] imageIds;
	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	
	public MentGridViewAdapter(Context c, String[] str) {
		mContext = c;
		values = str;
		
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public MentGridViewAdapter(Context c, String[] str, int[] ids) {
		mContext = c;
		values = str;
		imageIds = ids;
		
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return values.length;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.ment_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView.findViewById(R.id.ment_item_image);
			viewHolder.content = (TextView) convertView.findViewById(R.id.ment_item_content);
			convertView.setTag(viewHolder);
			

			viewHolder.content.setText(values[position]);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

    /** 
     * ViewHolder类用以储存item中控件的引用 
     */  
    final class ViewHolder {  
        ImageView image;
        TextView content;
    }
}
