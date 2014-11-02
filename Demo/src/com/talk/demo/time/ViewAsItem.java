package com.talk.demo.time;

public class ViewAsItem {
	private String mCreateDate;
	private String mCreateTime;
	private String mContent;
	private int mContentType;
	private String mPhoto;
	
	public ViewAsItem() {
		
	}
	public ViewAsItem(String cd, String ct, String c, int contt) {
		mCreateDate = cd;
		mCreateTime = ct;
		mContent = c;
		mContentType = contt;
	}
	
	public ViewAsItem(String cd, String ct, String c, int contt, String p) {
		mCreateDate = cd;
		mCreateTime = ct;
		mContent = c;
		mContentType = contt;
		mPhoto = p;
	}
	
	public String getCreateDate() {
		return mCreateDate;
	}
	
	public String getCreateTime() {
		return mCreateTime;
	}
	
	public String getContent() {
		return mContent;
	}
	
	public int getContentType() {
		return mContentType;
	}
	
	public String getPhoto() {
		return mPhoto;
	}
}
