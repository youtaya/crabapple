package com.talk.demo.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

final public class RawDialog extends RawData {
    /** The tag used to log to adb console. **/
    private static final String TAG = "RawDialog";

    private final String mRoomName;
    private final String mLink;
    private final int mDirect;
    private final String mContent;
    private final String mCreateDate;
    private final String mCreateTime;
    private final int mContentType;
    private final String mPhoto;
    private final String mAudio;
    private final boolean mDeleted;

    private final boolean mDirty;
    
    public String getRoomName() {
        return mRoomName;
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
    public int getDirect() {
        return mDirect;
    }    
    public String getLink() {
        return mLink;
    }

    public boolean isDeleted() {
        return mDeleted;
    }
    public boolean isDirty() {
        return mDirty;
    }
    
    public RawDialog(String name, String link, String roomName, String content, String createDate,
            String createTime, int contentType, String photo, String audio, int direct,
            boolean deleted, long serverDialogId,
            long rawDialogId, long syncState, boolean dirty) {
    	super(name,serverDialogId, rawDialogId, syncState);
    	mRoomName = roomName;
        mContent = content;
        mCreateDate = createDate;
        mCreateTime = createTime;
        mContentType = contentType;
        mPhoto = photo;
        mAudio = audio;
        mDirect = direct;
        mLink = link;
        mDeleted = deleted;
        mDirty = dirty;
    }
    
    /**
     * Convert the RawDialog object into a JSON string.  From the
     * JSONString interface.
     * @return a JSON string representation of the object
     */
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        try {
            if (!TextUtils.isEmpty(super.getHandle())) {
                json.put("user", super.getHandle());
            }
            if (!TextUtils.isEmpty(mLink)) {
                json.put("link", mLink);
            }
            if (!TextUtils.isEmpty(mRoomName)) {
                json.put("room", mRoomName);
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
            if (mDirect > 0) {
                json.put("dir", mDirect);
            }            
            if (super.getServerId() > 0) {
                json.put("sid", super.getServerId());
            }
            if (super.getDataId() > 0) {
                json.put("cid", super.getDataId());
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
     * Creates and returns an instance of the RawDialog from the provided JSON data.
     *
     * @param user The JSONObject containing user data
     * @return user The new instance of Sample RawDialog created from the JSON data.
     */
    public static RawDialog valueOf(JSONObject Dialog) {

        try {
            final String userName = !Dialog.isNull("user") ? Dialog.getString("user") : null;
            Log.d(TAG, "user name: "+userName);
            final int serverDialogId = !Dialog.isNull("sid") ? Dialog.getInt("sid") : -1;
            // If we didn't get either a username or serverId for the record, then
            // we can't do anything with it locally...
            if ((userName == null) && (serverDialogId <= 0)) {
                throw new JSONException("JSON contact missing required 'u' or 's' fields");
            }

            final int rawDialogId = !Dialog.isNull("cid") ? Dialog.getInt("cid") : -1;
            final String link = !Dialog.isNull("link") ? Dialog.getString("link") : null;
            final String roomName = !Dialog.isNull("room")  ? Dialog.getString("room") : null;
            final String content = !Dialog.isNull("content") ? Dialog.getString("content") : null;
            final String createDate = !Dialog.isNull("date") ? Dialog.getString("date") : null;
            String createTime = !Dialog.isNull("time") ? Dialog.getString("time") : null;
            if(createTime.length() > 24) {
            	String temp = createTime.substring(0, 24);
            	createTime = temp;
            }
            final int contentType = !Dialog.isNull("ctx") ? Dialog.getInt("ctx") : null;
            final String photo = !Dialog.isNull("po") ? Dialog.getString("po") : null;
            final String audio = !Dialog.isNull("ao") ? Dialog.getString("ao") : null;
            final int direct = !Dialog.isNull("dir") ? Dialog.getInt("dir") : null;
            final boolean deleted = !Dialog.isNull("del") ? Dialog.getBoolean("del") : false;
            final long syncState = !Dialog.isNull("x") ? Dialog.getLong("x") : 0;
            return new RawDialog(userName, link, roomName, content, createDate,
            		createTime, contentType, photo, audio, direct, deleted,
            		serverDialogId, rawDialogId, syncState, false);
        } catch (final Exception ex) {
            Log.i(TAG, "Error parsing JSON contact object" + ex.toString());
        }
        return null;
    }
    
    /**
     * Creates and returns RawDialog instance from all the supplied parameters.
     */
    public static RawDialog create(String name, String link, String roomName, String content, String createDate,
            String createTime, int contentType, String photo, String audio, int direct, 
            boolean deleted, long serverDialogId,
            long rawDialogId, long syncState, boolean dirty) {
        return new RawDialog(name, link, roomName, content, createDate,
        		createTime, contentType, photo, audio, direct, deleted,
        		serverDialogId, rawDialogId, syncState, dirty);
    }
    
    /**
     * Creates and returns a User instance that represents a deleted user.
     * Since the user is deleted, all we need are the client/server IDs.
     * @param clientUserId The client-side ID for the record
     * @param serverUserId The server-side ID for the record
     * @return a minimal User object representing the deleted record.
     */
    public static RawDialog createDeletedDialog(long rawDialogId, long serverDialogId)
    {
        return new RawDialog(null, null, null, null, null,
                null, 0, null, null, 0, true, 
                serverDialogId, rawDialogId, -1, true);
    }
}
