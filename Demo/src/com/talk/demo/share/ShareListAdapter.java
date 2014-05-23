package com.talk.demo.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;

import java.util.List;

public class ShareListAdapter extends BaseAdapter{
	private Context context = null;
	private List<ShareEntity> shareList = null;
	private LayoutInflater inflater = null;
	private int COME_MSG = 0;
	private int TO_MSG = 1;
	
	public ShareListAdapter(Context context,List<ShareEntity> chatList){
		this.context = context;
		this.shareList = chatList;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return shareList.size();
	}

	@Override
	public Object getItem(int position) {
		return shareList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		// 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
		ShareEntity entity = shareList.get(position);
	 	if (entity.isComeMsg())
	 	{
	 		return COME_MSG;
	 	}else{
	 		return TO_MSG;
	 	}
	}

	@Override
	public int getViewTypeCount() {
		// 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShareHolder shareHolder = null;
		if (convertView == null) {
			shareHolder = new ShareHolder();
			if (shareList.get(position).isComeMsg()) {
				convertView = inflater.inflate(R.layout.share_from_item, null);
			}else {
				convertView = inflater.inflate(R.layout.share_to_item, null);
			}
			shareHolder.timeTextView = (TextView) convertView.findViewById(R.id.create_time);
			shareHolder.contentTextView = (TextView) convertView.findViewById(R.id.content);
			shareHolder.userImageView = (ImageView) convertView.findViewById(R.id.iv_user_image);
			convertView.setTag(shareHolder);
		}else {
			shareHolder = (ShareHolder)convertView.getTag();
		}
		
		shareHolder.timeTextView.setText(shareList.get(position).getShareTime());
		shareHolder.contentTextView.setText(shareList.get(position).getContent());
		shareHolder.userImageView.setImageResource(shareList.get(position).getUserImage());
		
		return convertView;
	}
	
	private class ShareHolder{
		private TextView timeTextView;
		private ImageView userImageView;
		private TextView contentTextView;
	}
	
}