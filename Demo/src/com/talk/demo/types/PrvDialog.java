
package com.talk.demo.types;

public class PrvDialog implements TalkType {

    private String mRoomName;
    private String mSender;
    private String mLink;
    private String mContent;
    private String mCreateDate;
    private String mCreateTime;
    private int mContentType;
    private String mPhoto;
    private String mAudio;
    private boolean mDeleted;
    private boolean mDirty;
    
    private String mHandle;
    private long mServerId;
    private long mDataId;
    private long mSyncState;
    

    public String getHandle() {
        return mHandle;
    }
    public void setHandle(String v) {
        mHandle = v;
    }
    public long getServerId() {
        return mServerId;
    }
    public void setServerId(long v) {
        mServerId = v;
    }
    public long getDataId() {
        return mDataId;
    }
    public void setDataId(long v) {
        mDataId = v;
    }
    public long getSyncState() {
        return mSyncState;
    } 
    public void setSyncState(long v) {
        mSyncState = v;
    }

    public String getRoomName() {
        return mRoomName;
    }
    
    public void setRoomName(String v) {
        mRoomName = v;
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

    public String getSender() {
        return mSender;
    }
    
    public void setSender(String v) {
        mSender = v;
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
