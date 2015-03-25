package com.talk.demo;

import android.accounts.Account;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import com.talk.demo.time.DateInfo;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.TalkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TalkListAdapter extends BaseAdapter {
	private static String TAG = "TalkListAdapter";
	private final Context context;
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
	
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.talk_listitem, parent,
					false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
			
			DialogItem dialog_item = mTalkItems.get(position).getListViewItem().get(0);
			viewHolder.friendName.setText(dialog_item.getLink());
			DateInfo dateinfo = new DateInfo(dialog_item.getCreateTime());
			dateinfo.parseCreateTime();
			viewHolder.dialogTime.setText(dateinfo.getTimeTalk());
			viewHolder.dialogContent.setText(dialog_item.getContent());

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final List<CloudKite> tasks = initTasks(mTalkItems.get(position));
		
        for (CloudKite t : tasks) {
            new Thread(t).start();
        }
        
		viewHolder.setNewTask(tasks);
		
		return convertView;
	}
	
    List<CloudKite> initTasks(TalkViewItem views) {
        
        ArrayList<DialogItem> items = views.getListViewItem();
        List<CloudKite> result = new ArrayList<CloudKite>();
        Account accout = AccountUtils.getPasswordAccessibleAccount(context);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"ccount name: "+accout.name);
        }
        for(DialogItem item : items) {
        	if(item.getSender().equals(accout.name) && !TalkUtil.isSendDone(item.getDoneTime())) {
	        	CloudKite ck = new CloudKite(item.getContent(), 
	                    item.getIntervalTime(),
	                    item.getDoneTime());
	        	result.add(ck);
        	}
        	
        }

        return result;
    }
    
	final static class ViewHolder {
		public ImageView imageType;

		public TextView friendName;
		public TextView dialogTime;
		public TextView dialogContent;

		public ProgressBar pbTask;

		public List<CloudKite> linkTask;
		public CloudKite.taskListener listener;

		public void removeListener() {
			if (linkTask != null && listener != null) {
				for(CloudKite ck: linkTask) {
					ck.removeListener(listener);
				}
			}
		}

		public void addListener() {
			if (linkTask != null) {
				for(CloudKite ck: linkTask) {
					ck.addListener(listener);
				}
			}
		}

		public void setNewTask(List<CloudKite> ts) {
			removeListener();
			this.linkTask = ts;
			for(CloudKite ck: linkTask) {
				this.pbTask.setProgress(ck.getProgress());
			}
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
			this.listener = new taskListener() {
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