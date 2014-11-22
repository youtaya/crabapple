package com.talk.demo.talk;

import java.util.ArrayList;

public class TalkViewItem {

	private String mLinkName;
	private DialogItem mDialogItem;
	private String mCreateTime;
	private ArrayList<DialogItem> mListViewItem;
	
	public TalkViewItem() {
		
	}
	
	public TalkViewItem(String link, String time) {
		mLinkName = link;
		mCreateTime = time;
	}
	
	public TalkViewItem(String link, DialogItem di) {
		mLinkName = link;
		mDialogItem = di;
	}
	
	public TalkViewItem(String link, String time, ArrayList<DialogItem> ldi) {
		mLinkName = link;
		mCreateTime = time;
		mListViewItem = ldi;
	}
	
	public void setLinkName(String name) {
		mLinkName = name;
	}
	public String getLinkName() {
		return mLinkName;
	}
	
	public void setCreateTime(String t) {
		mCreateTime = t;
	}
	public String getCreateTime() {
		return mCreateTime;
	}
	
	public void setDialogItem(DialogItem di) {
		mDialogItem = di;
	}
	public DialogItem getDialogItem() {
		return mDialogItem;
	}
	

	public void setListViewItem(ArrayList<DialogItem> ldi) {
		mListViewItem = ldi;
	}
	public ArrayList<DialogItem> getListViewItem() {
		return mListViewItem;
	}
}
