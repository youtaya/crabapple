package com.talk.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardBase;
import com.afollestad.cardsui.CardListView;
import com.afollestad.cardsui.CardListView.CardClickListener;
import com.dj.listviewsample01.R;
import com.dj.listviewsample01.Task;
import com.dj.listviewsample01.MainActivity.MyAdapter;
import com.dj.listviewsample01.MainActivity.MyAdapter.ViewHolder;
import com.dj.listviewsample01.Task.taskListener;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.RecordCache;
import com.talk.demo.share.ShareTalkActivity;

import java.util.ArrayList;
import java.util.Map;

public class TalkFragment extends Fragment {
    
    private static String TAG = "TalkFragment";
    private ListView cardLv;
    private ArrayList<Map<String, String>> time_record;
    private ArrayList<RecordCache> record_cache;
    private RecordManager recordManager;
    
    public TalkFragment(RecordManager recordMgr) {
        time_record = new ArrayList<Map<String, String>>();
        recordManager = recordMgr;
        //talk_cache = new ArrayList<TalkCache>();
        record_cache = new ArrayList<RecordCache>();
    }

     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_talk, container, false);
        
        cardLv = (ListView)rootView.findViewById(R.id.talk_list);
        
        CloudKite[] tasks = initTasks();
        MyAdapter adapter = new MyAdapter(this, tasks);
        cardLv.setAdapter(adapter);

        for (CloudKite t : tasks)
            new Thread(t).start();

        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "on Resume");
     
    }
    
    CloudKite[] initTasks() {
        final int count = 10;
        CloudKite[] result = new CloudKite[count];
        for (int i = 0; i < count; i++) {
            result[i] = new CloudKite("TASK::" + i);
        }

        return result;
    }
    
    public static class MyAdapter extends BaseAdapter {
        private final Context context;
        private CloudKite[] tasks;
        private LayoutInflater inflater;

        final static Handler mHandler = new Handler();

        public MyAdapter(Context context, CloudKite[] tasks) {
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
}
