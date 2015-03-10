
package com.talk.demo.types;

public class Record implements TalkType {

    private String mLink;
    private String mTitle;
    private String mContent;
    private String mCreateDate;
    private String mCreateTime;
    private int mContentType;
    private String mPhoto;
    private String mAudio;
    private String mTag;
    private boolean mDeleted;

    private boolean mDirty;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String v) {
        mTitle = v;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String v) {
        mContent = v;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(String v) {
        mCreateDate = v;
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String v) {
        mCreateTime = v;
    }

    public int getContentType() {
        return mContentType;
    }

    public void setContentType(int v) {
        mContentType = v;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String v) {
        mPhoto = v;
    }

    public String getAudio() {
        return mAudio;
    }

    public void setAudio(String v) {
        mAudio = v;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String v) {
        mTag = v;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String v) {
        mLink = v;
    }

    public boolean isDeleted() {
        return mDeleted;
    }

    public void setDeleted(boolean v) {
        mDeleted = v;
    }

    public boolean isDirty() {
        return mDirty;
    }

    public void setDirty(boolean v) {
        mDirty = v;
    }
}
