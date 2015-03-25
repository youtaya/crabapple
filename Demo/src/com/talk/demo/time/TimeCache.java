package com.talk.demo.time;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeCache implements Parcelable {
	
	private int id;
	private String link;
	private String title;
	private String content;
	private String create_date;
	private String create_time;
	private int send_interval_time;
	private String send_done_time;
	private int content_type;
	private String photoPath;
	private String tag;
	
	public void setId(int v) {
		id = v;
	}
	
	public int getId() {
		return id;
	}
	
	public void setLink(String v) {
	    link = v;
	}
	
	public String getLink() {
	    return link;
	}
	
	public void setTitle(String v) {
	    title = v;
	}
	
	public String getTitle() {
	    return title;
	}
	
	public void setContent(String v) {
		content = v;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setCreateDate(String v) {
		create_date = v;
	}
	
	public String getCreateDate() {
		return create_date;
	}
	
	public void setCreateTime(String v) {
		create_time = v;
	}
	
	public String getCreateTime() {
		return create_time;
	}
	
    public void setMediaType(int v) {
        content_type = v;
    }
    
    public int getMediaType() {
        return content_type;
    }	
    
	public void setIntervalTime(int v) {
	    send_interval_time = v;
	}
	
	public int getIntervalTime() {
		return send_interval_time;
	}
	
	public void setDoneTime(String v) {
	    send_done_time = v;
    }
    
    public String getDoneTime() {
        return send_done_time;
    }
    
	public void setPhotoPath(String v) {
		photoPath = v;
	}
	
	public String getPhotoPath() {
		return photoPath;
	}
   
	public void setTag(String v) {
        tag = v;
    }
    
    public String getTag() {
        return tag;
    }
    // 用来创建自定义的Parcelable的对象
    public static final Parcelable.Creator<TimeCache> CREATOR
            = new Parcelable.Creator<TimeCache>() {
        public TimeCache createFromParcel(Parcel in) {
            TimeCache rc = new TimeCache();
            rc.id = in.readInt();
            rc.link = in.readString();
            rc.title = in.readString();
            rc.content = in.readString();
            rc.create_date = in.readString();
            rc.create_time = in.readString();
            rc.send_interval_time = in.readInt();
            rc.send_done_time = in.readString();
            rc.content_type = in.readInt();
            rc.photoPath = in.readString();
            rc.tag = in.readString();
            return rc;
        }

        public TimeCache[] newArray(int size) {
            return new TimeCache[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeInt(id);
		parcel.writeString(link);
		parcel.writeString(title);
		parcel.writeString(content);
		parcel.writeString(create_date);
		parcel.writeString(create_time);
		parcel.writeInt(send_interval_time);
		parcel.writeString(send_done_time);
		parcel.writeInt(content_type);
		parcel.writeString(photoPath);
		parcel.writeString(tag);
	}

}
