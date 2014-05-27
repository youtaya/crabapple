package com.talk.demo.persistence;

import android.os.Parcel;
import android.os.Parcelable;

public class TalkCache implements Parcelable {
	
	private String content;
	private String create_date;
	private String from;
	private String to;
	
	
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
	
	public void setFrom(String v) {
		from = v;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setTo(String v) {
		to = v;
	}
	
	public String getTo() {
		return to;
	}
	
    // 用来创建自定义的Parcelable的对象
    public static final Parcelable.Creator<TalkCache> CREATOR
            = new Parcelable.Creator<TalkCache>() {
        public TalkCache createFromParcel(Parcel in) {
            TalkCache tc = new TalkCache();
            tc.content = in.readString();
            tc.create_date = in.readString();
            tc.from = in.readString();
            tc.to = in.readString();
            return tc;
        }

        public TalkCache[] newArray(int size) {
            return new TalkCache[size];
        }
    };
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeString(content);
		parcel.writeString(create_date);
		parcel.writeString(from);
		parcel.writeString(to);
		
	}

}
