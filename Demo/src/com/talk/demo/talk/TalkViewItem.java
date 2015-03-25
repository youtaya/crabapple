package com.talk.demo.talk;

import java.util.ArrayList;

public class TalkViewItem {

	private String mTalkName;
	private DialogItem mDialogItem;
	private String mCreateTime;
	private ArrayList<DialogItem> mListViewItem;
	
	public TalkViewItem() {
		
	}
	
	public TalkViewItem(String name, String time) {
		mTalkName = name;
		mCreateTime = time;
	}
	
	public TalkViewItem(String name, DialogItem di) {
		mTalkName = name;
		mDialogItem = di;
	}
	
	public TalkViewItem(String name, String time, ArrayList<DialogItem> ldi) {
		mTalkName = name;
		mCreateTime = time;
		mListViewItem = ldi;
	}
	
	public void setTalkName(String name) {
		mTalkName = name;
	}
	public String getTalkName() {
		return mTalkName;
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
