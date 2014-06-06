
package com.talk.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DailyListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> values;
    private int[] colors = new int[] { 0xff3cb371, 0xffa0a0a0 };
    
    public DailyListAdapter(Context context, List<String> values) {
        super(context, R.layout.daily_listitem, values);
        this.context = context;
        this.values = values;
    }
    
    public void removeItem(int position){
    	values.remove(position);
        this.notifyDataSetChanged();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;  
        if (convertView == null) {  
            holder = new ViewHolder();  
            convertView = LayoutInflater.from(context).inflate(R.layout.daily_listitem, null);  
            holder.image = (ImageView)convertView.findViewById(R.id.pre_image);  
            holder.text = (TextView)convertView.findViewById(R.id.pre_content);
            holder.feed_like = (ImageView)convertView.findViewById(R.id.feed_like);
            holder.feed_comment = (ImageView)convertView.findViewById(R.id.feed_comment);
            // 将holder绑定到convertView  
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }  
  
        // 向ViewHolder中填入的数据  
        //holder.image.setBackgroundColor(0xff3cb371);;  
        holder.text.setText(values.get(position));  
        
        int colorPos = position % colors.length;  
        convertView.setBackgroundColor(colors[colorPos]);
        if(0 == colorPos) {
        	holder.feed_like.setImageResource(R.drawable.ic_feed_like_white);
        	holder.feed_comment.setImageResource(R.drawable.ic_feed_comment_white);
        }
        
        holder.feed_like.setOnClickListener(new FeedListener(position));
        
        return convertView; 
    }
    
    class FeedListener implements OnClickListener {
        private int position;

        FeedListener(int pos) {
            position = pos;
        }
        
        @Override
        public void onClick(View v) {
			//ToDo:save this record
			//remove this item from list
			removeItem(position);
        }
    }
    /** 
     * ViewHolder类用以储存item中控件的引用 
     */  
    final class ViewHolder {  
        ImageView image;  
        TextView text;
        ImageView feed_like;
        ImageView feed_comment;
    } 
}
