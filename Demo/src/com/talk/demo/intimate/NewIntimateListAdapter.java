package com.talk.demo.intimate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.FriendRecord;

import net.sectorsieteg.avatars.AvatarDrawableFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewIntimateListAdapter extends BaseAdapter {
    private class ViewHolder {
    	ImageView friend_avatar;
    	TextView friend_find_name;
    	ImageView decrator;
    }
    
    private ArrayList<HashMap<String, Object>> mFindIntimateList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private ViewHolder holder;
    
    private DBManager mgr;
    
    public NewIntimateListAdapter(Context c, ArrayList<HashMap<String, Object>> intimateList, int resource,
            String[] from, int[] to) {
        mFindIntimateList = intimateList;
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
        mgr = new DBManager(mContext);
    }
    
    @Override
    public int getCount() {
        return mFindIntimateList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFindIntimateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(int position){
        mFindIntimateList.remove(position);
        this.notifyDataSetChanged();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.friend_find_listitem, null);
            holder = new ViewHolder();
            holder.friend_avatar = (ImageView)convertView.findViewById(valueViewID[0]);
            holder.friend_find_name = (TextView)convertView.findViewById(valueViewID[1]);
            holder.decrator = (ImageView)convertView.findViewById(valueViewID[2]);
            convertView.setTag(holder);
        }
        
        HashMap<String, Object> intimateInfo = mFindIntimateList.get(position);
        if (intimateInfo != null) {
            String friendName = (String) intimateInfo.get(keyString[1]);
            int avatarIcon = (Integer)intimateInfo.get(keyString[0]);
            int decratorIcon = (Integer)intimateInfo.get(keyString[2]);
            holder.friend_find_name.setText(friendName);
            
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = false;
            Bitmap avatar = BitmapFactory.decodeResource(mContext.getResources(), avatarIcon, options);

            AvatarDrawableFactory avatarDrawableFactory = new AvatarDrawableFactory(mContext.getResources());
            Drawable roundedAvatarDrawable = avatarDrawableFactory.getRoundedAvatarDrawable(avatar);
            holder.friend_avatar.setImageDrawable(roundedAvatarDrawable);
            
            //holder.friend_avatar.setImageDrawable(holder.friend_find_name.getResources().getDrawable(avatarIcon));
            //if it's unlock, no need to set listener
            if(avatarIcon == R.drawable.ofm_add_icon)
            	holder.friend_find_name.setOnClickListener(new lvButtonListener(position));
            holder.decrator.setImageDrawable(holder.decrator.getResources().getDrawable(decratorIcon));
            holder.decrator.setOnClickListener(new lvButtonListener(position));
        }        
        return convertView;
    }

    //TODO: should contain more info about friend
    private void addFriendLocal(HashMap<String, Object> map) {
    	String name = (String) map.get(keyString[1]);
    	//check whether have saved
    	List<FriendRecord> frList = mgr.queryFriend();
    	for(FriendRecord temp : frList) {
    		if(temp.userName.equals(name)) {
    			//already add it, skip it
    			return;
    		}
    	}
    	FriendRecord fr = new FriendRecord(name);
    	mgr.addFriend(fr);
    }
    
    class lvButtonListener implements OnClickListener {
        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }
        
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == holder.decrator.getId()) {
            	addFriendLocal(mFindIntimateList.get(position));
            	((ImageView)v).setImageDrawable(holder.decrator.getResources().getDrawable(R.drawable.ofm_profile_icon));
            }
        }
    }
}