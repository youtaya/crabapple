package com.talk.demo.share;

public class ShareEntity {
	private int userImage;
	private String content;
	private String chatTime;
	private boolean isComeMsg;

	public int getUserImage() {
		return userImage;
	}
	public void setUserImage(int userImage) {
		this.userImage = userImage;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getChatTime() {
		return chatTime;
	}
	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public boolean isComeMsg() {
		return isComeMsg;
	}
	public void setComeMsg(boolean isComeMsg) {
		this.isComeMsg = isComeMsg;
	}
}
