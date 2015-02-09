package com.talk.demo.intimate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;

import net.sectorsieteg.avatars.AvatarDrawableFactory;

import java.util.ArrayList;

public class IntimateListAdapter extends BaseAdapter {
    private class ViewHolder {
    	ImageView friend_avatar;
    	TextView friend_name;
    }
    
    private ArrayList<String> mIntimateList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private ViewHolder holder;
    
    public IntimateListAdapter(Context c, ArrayList<String> intimateList, int resource,
            String[] from, int[] to) {
        mIntimateList = intimateList;
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }
    
    @Override
    public int getCount() {
        return mIntimateList.size();
    }

    @Override
    public Object getItem(int position) {
        return mIntimateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position){
        mIntimateList.remove(position);
        this.notifyDataSetChanged();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.friend_listitem, null);
            holder = new ViewHolder();
            holder.friend_avatar = (ImageView)convertView.findViewById(valueViewID[0]);
            holder.friend_name = (TextView)convertView.findViewById(valueViewID[1]);
            convertView.setTag(holder);
        }
        
        String intimateInfo = mIntimateList.get(position);
        if (intimateInfo != null) {
            String friendName = intimateInfo;
            holder.friend_name.setText(friendName);
            
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = false;
            Bitmap avatar = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.avatar, options);

            AvatarDrawableFactory avatarDrawableFactory = new AvatarDrawableFactory(mContext.getResources());
            Drawable roundedAvatarDrawable = avatarDrawableFactory.getRoundedAvatarDrawable(avatar);
            holder.friend_avatar.setImageDrawable(roundedAvatarDrawable);
            
            //holder.friend_avatar.setImageDrawable(holder.friend_find_name.getResources().getDrawable(avatarIcon));
        }        
        return convertView;
    }
}