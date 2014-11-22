package com.talk.demo.talk;

import android.os.Parcel;
import android.os.Parcelable;

public class DialogCache implements Parcelable {
	
	private int id;
	private String link;
	private int direct;
	private String content;
	private String create_date;
	private String create_time;
	private int send_interval_time;
	private String send_done_time;
	private int content_type;
	private String photoPath;
	
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
	
	public void setDirect(int d) {
		direct = d;
	}
	
	public int getDirect() {
	    return direct;
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
   
    // 用来创建自定义的Parcelable的对象
    public static final Parcelable.Creator<DialogCache> CREATOR
            = new Parcelable.Creator<DialogCache>() {
        public DialogCache createFromParcel(Parcel in) {
            DialogCache dc = new DialogCache();
            dc.id = in.readInt();
            dc.link = in.readString();
            dc.direct = in.readInt();
            dc.content = in.readString();
            dc.create_date = in.readString();
            dc.create_time = in.readString();
            dc.send_interval_time = in.readInt();
            dc.send_done_time = in.readString();
            dc.content_type = in.readInt();
            dc.photoPath = in.readString();
            return dc;
        }

        public DialogCache[] newArray(int size) {
            return new DialogCache[size];
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
		parcel.writeInt(direct);
		parcel.writeString(content);
		parcel.writeString(create_date);
		parcel.writeString(create_time);
		parcel.writeInt(send_interval_time);
		parcel.writeString(send_done_time);
		parcel.writeInt(content_type);
		parcel.writeString(photoPath);
	}

}
