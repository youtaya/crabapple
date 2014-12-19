package com.talk.demo.talk;

public class DialogItem {
    private int item_id;
    private String mSender;
    private String mLink;
    private int mDirect;
    private int mSendIntervalTime;
    private String mDoneSendTime;
	private String mCreateDate;
	private String mCreateTime;
	private String mContent;
	private int mContentType;
	private String mPhoto;
	
	public DialogItem() {
		
	}
	public DialogItem(int id, String sender, String link, int dir, 
			String cd, String ct, String c, int contt) {
	    item_id = id;
	    mSender = sender;
	    mLink = link;
	    mDirect = dir;
		mCreateDate = cd;
		mCreateTime = ct;
		mContent = c;
		mContentType = contt;
	}
	
	public DialogItem(int id, String sender, String link, int dir, int interval, 
			 String doneTime, String cd, String ct, String c, int contt) {
	    item_id = id;
	    mSender = sender;
	    mLink = link;
	    mDirect = dir;
	    mSendIntervalTime = interval;
	    mDoneSendTime = doneTime;
		mCreateDate = cd;
		mCreateTime = ct;
		mContent = c;
		mContentType = contt;
	}
	
	public DialogItem(int id, String sender, String link, int dir, int interval,
		String doneTime, String cd, String ct, String c, int contt, String p) {
		
	    item_id = id;
	    mSender = sender;
	    mLink = link;
	    mDirect = dir;
	    mSendIntervalTime = interval;
	    mDoneSendTime = doneTime;
		mCreateDate = cd;
		mCreateTime = ct;
		mContent = c;
		mContentType = contt;
		mPhoto = p;
	}
	
	public int getItemId() {
	    return item_id;
	}
	
	public String getSender() {
		return mSender;
	}
	
	public String getLink() {
		return mLink;
	}
	
	public int getDirect() {
		return mDirect;
	}
	
	public int getIntervalTime() {
		return mSendIntervalTime;
	}
	public void setIntervalTime(int v) {
		mSendIntervalTime = v;
	}
	
	public String getDoneTime() {
		return mDoneSendTime;
	}
	public void setDoneTime(String v) {
		mDoneSendTime = v;
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
