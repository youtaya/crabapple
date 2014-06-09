package com.talk.demo.persistence;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordCache implements Parcelable {
	
	private int id;
	private String content;
	private String create_date;
	private String create_time;
	private int content_type;
	private String photoPath;
	
	public void setId(int v) {
		id = v;
	}
	
	public int getId() {
		return id;
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
	
	public void setPhotoPath(String v) {
		photoPath = v;
	}
	
	public String getPhotoPath() {
		return photoPath;
	}
	
    // 用来创建自定义的Parcelable的对象
    public static final Parcelable.Creator<RecordCache> CREATOR
            = new Parcelable.Creator<RecordCache>() {
        public RecordCache createFromParcel(Parcel in) {
            RecordCache rc = new RecordCache();
            rc.id = in.readInt();
            rc.content = in.readString();
            rc.create_date = in.readString();
            rc.create_time = in.readString();
            rc.content_type = in.readInt();
            rc.photoPath = in.readString();
            return rc;
        }

        public RecordCache[] newArray(int size) {
            return new RecordCache[size];
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
		parcel.writeString(content);
		parcel.writeString(create_date);
		parcel.writeString(create_time);
		parcel.writeInt(content_type);
		parcel.writeString(photoPath);
	}

}
