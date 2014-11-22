package com.talk.demo;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.talk.demo.CloudKite.taskListener;
import com.talk.demo.talk.DialogCache;
import com.talk.demo.talk.DialogItem;
import com.talk.demo.talk.TalkViewItem;

import java.util.ArrayList;
import java.util.HashMap;

public class TalkListAdapter extends BaseAdapter {
	private final Context context;
	private CloudKite[] tasks;
	private ViewHolder viewHolder;
	private LayoutInflater inflater;
	private ArrayList<TalkViewItem> mTalkItems;
	private HashMap<String, ArrayList<DialogCache>> mDialogCache;
	final static Handler mHandler = new Handler();

	public TalkListAdapter(Context context, ArrayList<TalkViewItem> items, 
			HashMap<String, ArrayList<DialogCache>> cache) {
		this.context = context;
		this.mTalkItems = items;
		this.mDialogCache = cache;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public TalkListAdapter(Context context, ArrayList<TalkViewItem> items, 
			HashMap<String, ArrayList<DialogCache>> cache, CloudKite[] tasks) {
		this.context = context;
		this.mTalkItems = items;
		this.mDialogCache = cache;
		this.tasks = tasks;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.talk_listitem, parent,
					false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
			
			DialogItem dialog_item = mTalkItems.get(position).getListViewItem().get(0);
			viewHolder.friendName.setText(dialog_item.getLink());
			viewHolder.dialogTime.setText(dialog_item.getCreateTime());
			viewHolder.dialogContent.setText(dialog_item.getContent());
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//final CloudKite aTask = tasks[position];
		//viewHolder.setNewTask(aTask);

		return convertView;
	}

	final static class ViewHolder {
		public ImageView imageType;

		public TextView friendName;
		public TextView dialogTime;
		public TextView dialogContent;

		public ProgressBar pbTask;

		public CloudKite linkTask;
		public CloudKite.taskListener l;

		public void removeListener() {
			if (linkTask != null && l != null)
				linkTask.removeListener(l);
		}

		public void addListener() {
			if (linkTask != null)
				linkTask.addListener(l);
		}

		public void setNewTask(CloudKite t) {
			removeListener();
			this.linkTask = t;
			this.pbTask.setProgress(t.getProgress());
			addListener();
		}

		public ViewHolder(View convertView) {
			this.imageType = (ImageView) convertView.findViewById(R.id.icon);

			this.friendName = (TextView) convertView
					.findViewById(R.id.friend_name);
			this.dialogTime = (TextView) convertView
					.findViewById(R.id.dialog_time);
			this.dialogContent = (TextView) convertView
					.findViewById(R.id.dialog_content);
			this.pbTask = (ProgressBar) convertView.findViewById(R.id.pbTask);
			this.l = new taskListener() {
				@Override
				public void onProgressChanged(final int progress) {

					mHandler.post(new Runnable() {
						@Override
						public void run() {
							pbTask.setProgress(progress);

						}
					});
				}
			};

		}
	}

	@Override
	public int getCount() {

		return mTalkItems.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

}