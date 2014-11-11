package com.talk.demo.time;

public class ViewAsItem {
    private int item_id;
	private String mCreateDate;
	private String mCreateTime;
	private String mContent;
	private int mContentType;
	private String mPhoto;
	
	public ViewAsItem() {
		
	}
	public ViewAsItem(int id, String cd, String ct, String c, int contt) {
	    item_id = id;
		mCreateDate = cd;
		mCreateTime = ct;
		mContent = c;
		mContentType = contt;
	}
	
	public ViewAsItem(int id, String cd, String ct, String c, int contt, String p) {
	    item_id = id;
		mCreateDate = cd;
		mCreateTime = ct;
		mContent = c;
		mContentType = contt;
		mPhoto = p;
	}
	
	public int getItemId() {
	    return item_id;
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
