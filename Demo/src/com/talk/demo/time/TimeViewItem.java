package com.talk.demo.time;

import java.util.ArrayList;

public class TimeViewItem {
	/*
	 *  0: head, 1: item, 2: tag item
	 */
	private int type; 
	private String mHeadContent;
	private ViewAsItem mViewItem;
	private String mTagTitle;
	private ArrayList<ViewAsItem> mListViewItem;
	
	public TimeViewItem() {
		
	}
	
	public TimeViewItem(int t, String hc) {
		type = t;
		mHeadContent = hc;
	}
	
	public TimeViewItem(int t, ViewAsItem vi) {
		type = t;
		mViewItem = vi;
	}
	
	public TimeViewItem(int t, String tt, ArrayList<ViewAsItem> lvi) {
		type = t;
		mTagTitle = tt;
		mListViewItem = lvi;
	}
	
	public void setType(int t) {
		type = t;
	}
	public int getType() {
		return type;
	}
	
	public void setHeadContent(String hc) {
		mHeadContent = hc;
	}
	public String getHeadContent() {
		return mHeadContent;
	}
	
	public void setViewItem(ViewAsItem vai) {
		mViewItem = vai;
	}
	public ViewAsItem getViewItem() {
		return mViewItem;
	}
	
	public void setTagTitle(String tt) {
		mTagTitle = tt;
	}
	public String getTagTitle() {
		return mTagTitle;
	}

	public void setListViewItem(ArrayList<ViewAsItem> lvsi) {
		mListViewItem = lvsi;
	}
	public ArrayList<ViewAsItem> getListViewItem() {
		return mListViewItem;
	}
}
