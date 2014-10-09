package com.talk.demo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.talk.demo.R;
import java.util.ArrayList;
import java.util.HashMap;

public class TalkListAdapter extends BaseAdapter {
    private final Context context;
    private CloudKite[] tasks;
    private LayoutInflater inflater;

    final static Handler mHandler = new Handler();

    public TalkListAdapter(Context context, CloudKite[] tasks) {
        this.context = context;
        this.tasks = tasks;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.talk_listitem, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CloudKite aTask = tasks[position];
        viewHolder.setNewTask(aTask);

        return convertView;
    }

    final static class ViewHolder {
        public ImageView imageType;
        public TextView tvTaskDesc;
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
            this.tvTaskDesc.setText(t.getDesc());
            this.pbTask.setProgress(t.getProgress());
            addListener();
        }

        public ViewHolder(View convertView) {
            this.imageType = (ImageView) convertView
                    .findViewById(R.id.icon);
            this.tvTaskDesc = (TextView) convertView
                    .findViewById(R.id.tvTaskDesc);
            this.pbTask = (ProgressBar) convertView
                    .findViewById(R.id.pbTask);
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

        return tasks.length;
    }

    @Override
    public CloudKite getItem(int position) {

        return tasks[position];
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

}