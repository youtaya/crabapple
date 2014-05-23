package com.talk.demo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.talk.demo.setting.RichPresent;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordListAdapter extends BaseAdapter {
    private class ViewHolder {
    	TextView create_time;
    	ImageButton buy;
        ImageButton share;
    }
    
    private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private ViewHolder holder;
    private RichPresent rp;
    
    public RecordListAdapter(Context c, ArrayList<HashMap<String, Object>> appList, int resource,
            String[] from, int[] to) {
        mAppList = appList;
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
        
        rp = RichPresent.getInstance(c.getApplicationContext());
    }
    
    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position){
        mAppList.remove(position);
        this.notifyDataSetChanged();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.record_status_listitem, null);
            holder = new ViewHolder();
            holder.create_time = (TextView)convertView.findViewById(valueViewID[0]);
            holder.buy = (ImageButton)convertView.findViewById(valueViewID[1]);
            holder.share = (ImageButton)convertView.findViewById(valueViewID[2]);
            convertView.setTag(holder);
        }
        
        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String cTime = (String) appInfo.get(keyString[0]);
            int buyIcon = (Integer)appInfo.get(keyString[1]);
            int shareIcon = (Integer)appInfo.get(keyString[2]);
            holder.create_time.setText(cTime);
            holder.buy.setImageDrawable(holder.buy.getResources().getDrawable(buyIcon));
            holder.buy.setOnClickListener(new lvButtonListener(position));
            holder.share.setImageDrawable(holder.share.getResources().getDrawable(shareIcon));
            holder.share.setOnClickListener(new lvButtonListener(position));
        }        
        return convertView;
    }
    /*
     * ToDo: change to support DialogFragment
     */
    protected void buyDialog(final View v) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage("确定适用蓝宝石");
        builder.setTitle("购买提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            	rp.minusRich(2);
            	chooseConfirm(v);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                
            }
        });
        builder.create().show();
    }
    
    protected void shareDialog() {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage("确定分享给好友");
        builder.setTitle("分享好友");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                
            }
        });
        builder.create().show();
    }
    
    
    private void chooseConfirm(View v) {
    	((ImageButton)v).setImageDrawable(holder.buy.getResources().getDrawable(R.drawable.unlock_status));
    }
    
    class lvButtonListener implements OnClickListener {
        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }
        
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == holder.share.getId())
            	shareDialog();
            else if (vid == holder.buy.getId()) {
            	buyDialog(v);
            } else {
            }
        }
    }
}