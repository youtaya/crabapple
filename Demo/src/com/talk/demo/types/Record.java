
package com.talk.demo.types;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

public class Record implements TalkType {

    public int _id;
    public int server_id;
    public String handle;
    public long sync_time;
    /*
     * deleted flag : default : 0 mean don't delete, other: 1 mean need to
     * delete
     */
    public int deleted = 0;
    /*
     * dirty flag : default : 1 mean dirty and need to sync, other: 0 mean not
     * need sync
     */
    public int dirty = 1;

    public String content;
    public String calc_date;
    public String create_time;
    public int send_interval_time;
    public String send_done_time;
    public int content_type;

    public String link;
    public String title = "time";
    public String photo;
    public String audio;
    public String tag;

    public Record() {
    }
    
    public Record(String name, String rLink, String rTitle, String rContent, String createDate,
            String createTime, int contentType, String rPhoto, String rAudio, String rTag,
            int isDeleted, int serverRecordId,
            int rawRecordId, long syncState, int isDirty) {
        handle = name;
        server_id = serverRecordId;
        _id = rawRecordId;
        sync_time = syncState;
        title = rTitle;
        content = rContent;
        calc_date = createDate;
        create_time = createTime;
        content_type = contentType;
        photo = rPhoto;
        audio = rAudio;
        tag = rTag;
        link = rLink;
        deleted = isDeleted;
        dirty = isDirty;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String v) {
        handle = v;
    }

    public int getServerId() {
        return server_id;
    }

    public void setServerId(int v) {
        server_id = v;
    }

    public int getDataId() {
        return _id;
    }

    public void setDataId(int v) {
        _id = v;
    }

    public long getSyncState() {
        return sync_time;
    }

    public void setSyncState(int v) {
        sync_time = v;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String v) {
        title = v;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String v) {
        content = v;
    }

    public String getCreateDate() {
        return calc_date;
    }

    public void setCreateDate(String v) {
        calc_date = v;
    }

    public String getCreateTime() {
        return create_time;
    }

    public void setCreateTime(String v) {
        create_time = v;
    }

    public void setSendInterval(int v) {
        send_interval_time = v;
    }

    public void setSendDoneTime(String v) {
        send_done_time = v;
    }

    public int getContentType() {
        return content_type;
    }

    public void setContentType(int v) {
        content_type = v;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String v) {
        photo = v;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String v) {
        audio = v;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String v) {
        tag = v;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String v) {
        link = v;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int v) {
        deleted = v;
    }

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int v) {
        dirty = v;
    }

    public void getObjectItems(Object[] obj) {
        obj[0] = server_id;
        obj[1] = handle;
        obj[2] = link;
        obj[3] = title;
        obj[4] = content;
        obj[5] = calc_date;
        obj[6] = create_time;
        obj[7] = send_interval_time;
        obj[8] = send_done_time;
        obj[9] = content_type;
        obj[10] = photo;
        obj[11] = audio;
        obj[12] = tag;
        obj[13] = sync_time;
        obj[14] = dirty;
        obj[15] = deleted;

    }

    public void dumpRecord(Cursor c) {
        _id = c.getInt(c.getColumnIndex("id"));
        server_id = c.getInt(c.getColumnIndex("server_id"));
        handle = c.getString(c.getColumnIndex("handle"));
        link = c.getString(c.getColumnIndex("link"));
        title = c.getString(c.getColumnIndex("title"));
        content = c.getString(c.getColumnIndex("content"));
        calc_date = c.getString(c.getColumnIndex("calc_date"));
        create_time = c.getString(c.getColumnIndex("create_time"));
        send_interval_time = c.getInt(c.getColumnIndex("send_interval_time"));
        send_done_time = c.getString(c.getColumnIndex("send_done_time"));
        content_type = c.getInt(c.getColumnIndex("content_type"));
        photo = c.getString(c.getColumnIndex("photo"));
        audio = c.getString(c.getColumnIndex("audio"));
        tag = c.getString(c.getColumnIndex("tag"));
        sync_time = c.getLong(c.getColumnIndex("sync_time"));

    }

    /**
     * Convert the RawRecord object into a JSON string. From the JSONString
     * interface.
     * 
     * @return a JSON string representation of the object
     */
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();

        try {
            if (!TextUtils.isEmpty(handle)) {
                json.put("user", handle);
            }
            if (!TextUtils.isEmpty(link)) {
                json.put("link", link);
            }
            if (!TextUtils.isEmpty(title)) {
                json.put("title", title);
            }
            if (!TextUtils.isEmpty(content)) {
                json.put("content", content);
            }
            if (!TextUtils.isEmpty(calc_date)) {
                json.put("date", calc_date);
            }
            if (!TextUtils.isEmpty(create_time)) {
                json.put("time", create_time);
            }
            if (content_type != -1) {
                json.put("ctx", content_type);
            }
            if (!TextUtils.isEmpty(photo)) {
                json.put("po", photo);
            }
            if (!TextUtils.isEmpty(audio)) {
                json.put("ao", audio);
            }
            if (!TextUtils.isEmpty(tag)) {
                json.put("tag", tag);
            }
            if (server_id != -1) {
                json.put("sid", server_id);
            }
            if (_id != -1) {
                json.put("cid", _id);
            }
            if (deleted != -1) {
                json.put("del", deleted);
            }
        } catch (final Exception ex) {
            Log.i("Record", "Error converting RawContact to JSONObject" + ex.toString());
        }

        return json;
    }

    /**
     * Creates and returns RawRecord instance from all the supplied parameters.
     */
    public static Record create(String name, String link, String title, String content,
            String createDate,
            String createTime, int contentType, String photo, String audio, String tag,
            int deleted, int serverRecordId,
            int rawRecordId, long syncState, int dirty) {
        return new Record(name, link, title, content, createDate,
                createTime, contentType, photo, audio, tag, deleted,
                serverRecordId, rawRecordId, syncState, dirty);
    }

    /**
     * Creates and returns a User instance that represents a deleted user. Since
     * the user is deleted, all we need are the client/server IDs.
     * 
     * @param clientUserId The client-side ID for the record
     * @param serverUserId The server-side ID for the record
     * @return a minimal User object representing the deleted record.
     */
    public static Record createDeletedRecord(int rawRecordId, int serverRecordId)
    {
        return new Record(null, null, null, null, null,
                null, 0, null, null, null, 1,
                serverRecordId, rawRecordId, -1, 1);
    }
    
    public String toString() {
        String isDirty = (dirty ==1)?"yes":"no";
        String info = " id: "+String.valueOf(_id)+
                " server id: "+String.valueOf(server_id)+
                " user name: "+handle+
                " dirty: " +isDirty;
        return info;
    }
}
