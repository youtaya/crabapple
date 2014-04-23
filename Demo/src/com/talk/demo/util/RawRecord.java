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
                json.put("u", mUserName);
            }
            if (!TextUtils.isEmpty(mTitle)) {
                json.put("t", mTitle);
            }
            if (!TextUtils.isEmpty(mContent)) {
                json.put("c", mContent);
            }
            if (!TextUtils.isEmpty(mCreateDate)) {
                json.put("d", mCreateDate);
            }
            if (!TextUtils.isEmpty(mCreateTime)) {
                json.put("i", mCreateTime);
            }
            if (!TextUtils.isEmpty(mPhoto)) {
                json.put("p", mPhoto);
            }
            if (!TextUtils.isEmpty(mAudio)) {
                json.put("a", mAudio);
            }
            if (mServerRecordId > 0) {
                json.put("s", mServerRecordId);
            }
            if (mRawRecordId > 0) {
                json.put("r", mRawRecordId);
            }
            if (mDeleted) {
                json.put("x", mDeleted);
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
    public static RawRecord valueOf(JSONObject contact) {

        try {
            final String userName = !contact.isNull("u") ? contact.getString("u") : null;
            final int serverRecordId = !contact.isNull("s") ? contact.getInt("s") : -1;
            // If we didn't get either a username or serverId for the contact, then
            // we can't do anything with it locally...
            if ((userName == null) && (serverRecordId <= 0)) {
                throw new JSONException("JSON contact missing required 'u' or 's' fields");
            }

            final int rawContactId = !contact.isNull("r") ? contact.getInt("r") : -1;
            final String title = !contact.isNull("t")  ? contact.getString("t") : null;
            final String content = !contact.isNull("c") ? contact.getString("c") : null;
            final String createDate = !contact.isNull("d") ? contact.getString("d") : null;
            final String createTime = !contact.isNull("i") ? contact.getString("i") : null;
            final int contentType = !contact.isNull("y") ? contact.getInt("y") : null;
            final String photo = !contact.isNull("p") ? contact.getString("p") : null;
            final String audio = !contact.isNull("a") ? contact.getString("a") : null;
            final String status = !contact.isNull("s") ? contact.getString("s") : null;
            final boolean deleted = !contact.isNull("x") ? contact.getBoolean("x") : false;
            final long syncState = !contact.isNull("ss") ? contact.getLong("ss") : 0;
            return new RawRecord(userName, title, content, createDate,
            		createTime, contentType, photo, audio, status, deleted,
            		serverRecordId, rawContactId, syncState, false);
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
