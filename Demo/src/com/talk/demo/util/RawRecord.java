package com.talk.demo.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

final public class RawRecord {
    /** The tag used to log to adb console. **/
    private static final String TAG = "RawRecord";

    private final String mUserName;
    private final String mTitle;
    private final String mContent;
    private final String mCreateDate;
    private final String mCreateTime;
    private final int mContentType;
    private final String mPhoto;
    private final String mAudio;
    private final String mStatus;
    private final boolean mDeleted;

    private final boolean mDirty;
    private final long mServerRecordId;
    private final long mRawRecordId;
    private final long mSyncState;
    
    public long getServerContactId() {
        return mServerRecordId;
    }
    public long getRawContactId() {
        return mRawRecordId;
    }
    
    public String getUserName() {
        return mUserName;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getContent() {
        return mContent;
    }
    public String getCreateDate() {
        return mCreateDate;
    }
    public String getCreateTime() {
        return mCreateTime;
    }
    public int getContentType() {
        return mContentType;
    }
    public String getPhoto() {
        return mPhoto;
    }
    public String getAudio() {
        return mAudio;
    }
    public String getStatus() {
        return mStatus;
    }

    public boolean isDeleted() {
        return mDeleted;
    }
    public boolean isDirty() {
        return mDirty;
    }
    public long getSyncState() {
        return mSyncState;
    } 
    
    public RawRecord(String name, String title, String content, String createDate,
            String createTime, int contentType, String photo, String audio,
            String status, boolean deleted, long serverRecordId,
            long rawRecordId, long syncState, boolean dirty) {
        mUserName = name;
        mTitle = title;
        mContent = content;
        mCreateDate = createDate;
        mCreateTime = createTime;
        mContentType = contentType;
        mPhoto = photo;
        mAudio = audio;
        mStatus = status;
        mDeleted = deleted;
        mServerRecordId = serverRecordId;
        mRawRecordId = rawRecordId;
        mSyncState = syncState;
        mDirty = dirty;
    }
    
    /**
     * Convert the RawRecord object into a JSON string.  From the
     * JSONString interface.
     * @return a JSON string representation of the object
     */
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        try {
            if (!TextUtils.isEmpty(mUserName)) {
                json.put("user", mUserName);
            }
            if (!TextUtils.isEmpty(mTitle)) {
                json.put("title", mTitle);
            }
            if (!TextUtils.isEmpty(mContent)) {
                json.put("content", mContent);
            }
            if (!TextUtils.isEmpty(mCreateDate)) {
                json.put("date", mCreateDate);
            }
            if (!TextUtils.isEmpty(mCreateTime)) {
                json.put("time", mCreateTime);
            }
            if (mContentType != 0) {
                json.put("ctx", mContentType);
            }
            if (!TextUtils.isEmpty(mPhoto)) {
                json.put("po", mPhoto);
            }
            if (!TextUtils.isEmpty(mAudio)) {
                json.put("ao", mAudio);
            }
            if (mServerRecordId > 0) {
                json.put("sid", mServerRecordId);
            }
            if (mRawRecordId > 0) {
                json.put("cid", mRawRecordId);
            }
            if (mDeleted) {
                json.put("del", mDeleted);
            }
            } catch (final Exception ex) {
            Log.i(TAG, "Error converting RawContact to JSONObject" + ex.toString());
        }

        return json;
    }
    /**
     * Creates and returns an instance of the RawRecord from the provided JSON data.
     *
     * @param user The JSONObject containing user data
     * @return user The new instance of Sample RawRecord created from the JSON data.
     */
    public static RawRecord valueOf(JSONObject Record) {

        try {
            final String userName = !Record.isNull("user") ? Record.getString("user") : null;
            Log.d(TAG, "user name: "+userName);
            final int serverRecordId = !Record.isNull("sid") ? Record.getInt("sid") : -1;
            // If we didn't get either a username or serverId for the record, then
            // we can't do anything with it locally...
            if ((userName == null) && (serverRecordId <= 0)) {
                throw new JSONException("JSON contact missing required 'u' or 's' fields");
            }

            final int rawRecordId = !Record.isNull("cid") ? Record.getInt("cid") : -1;
            final String title = !Record.isNull("title")  ? Record.getString("title") : null;
            final String content = !Record.isNull("content") ? Record.getString("content") : null;
            final String createDate = !Record.isNull("date") ? Record.getString("date") : null;
            final String createTime = !Record.isNull("time") ? Record.getString("time") : null;
            final int contentType = !Record.isNull("ctx") ? Record.getInt("ctx") : null;
            final String photo = !Record.isNull("po") ? Record.getString("po") : null;
            final String audio = !Record.isNull("ao") ? Record.getString("ao") : null;
            final String status = !Record.isNull("status") ? Record.getString("status") : null;
            final boolean deleted = !Record.isNull("del") ? Record.getBoolean("del") : false;
            final long syncState = !Record.isNull("x") ? Record.getLong("x") : 0;
            return new RawRecord(userName, title, content, createDate,
            		createTime, contentType, photo, audio, status, deleted,
            		serverRecordId, rawRecordId, syncState, false);
        } catch (final Exception ex) {
            Log.i(TAG, "Error parsing JSON contact object" + ex.toString());
        }
        return null;
    }
    
    /**
     * Creates and returns RawRecord instance from all the supplied parameters.
     */
    public static RawRecord create(String name, String title, String content, String createDate,
            String createTime, int contentType, String photo, String audio,
            String status, boolean deleted, long serverRecordId,
            long rawRecordId, long syncState, boolean dirty) {
        return new RawRecord(name, title, content, createDate,
        		createTime, contentType, photo, audio, status, deleted,
        		serverRecordId, rawRecordId, syncState, dirty);
    }
    
    /**
     * Creates and returns a User instance that represents a deleted user.
     * Since the user is deleted, all we need are the client/server IDs.
     * @param clientUserId The client-side ID for the record
     * @param serverUserId The server-side ID for the record
     * @return a minimal User object representing the deleted record.
     */
    public static RawRecord createDeletedRecord(long rawRecordId, long serverRecordId)
    {
        return new RawRecord(null, null, null, null,
                null, 0, null, null, null, true, 
                serverRecordId, rawRecordId, -1, true);
    }
}
