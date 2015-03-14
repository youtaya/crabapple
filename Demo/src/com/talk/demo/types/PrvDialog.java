package com.talk.demo.types;

import org.json.JSONObject;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

public class PrvDialog implements TalkType {

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

	public String room_name;
	public String sender;
	public String link;
	public String photo;
	public String audio;

	public PrvDialog() {
	}

	public PrvDialog(String name, String pdSender, String pdLink,
			String roomName, String pdContent, String createDate,
			String createTime, int contentType, String pdPhoto, String pdAudio,
			int pdDeleted, int serverDialogId, int rawDialogId, long syncState,
			int pdDirty) {
		handle = name;
		server_id = serverDialogId;
		_id = rawDialogId;
		sync_time = syncState;
		room_name = roomName;
		content = pdContent;
		calc_date = createDate;
		create_time = createTime;
		content_type = contentType;
		photo = pdPhoto;
		audio = pdAudio;
		sender = pdSender;
		link = pdLink;
		deleted = pdDeleted;
		dirty = pdDirty;
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

	public void setSyncState(long v) {
		sync_time = v;
	}

	public String getRoomName() {
		return room_name;
	}

	public void setRoomName(String v) {
		room_name = v;
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

	public String getSender() {
		return sender;
	}

	public void setSender(String v) {
		sender = v;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String v) {
		link = v;
	}

	public void setSendInterval(int v) {
		send_interval_time = v;
	}

	public void setSendDoneTime(String v) {
		send_done_time = v;
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
		obj[2] = room_name;
		obj[3] = sender;
		obj[4] = link;
		obj[5] = content;
		obj[6] = calc_date;
		obj[7] = create_time;
		obj[8] = send_interval_time;
		obj[9] = send_done_time;
		obj[10] = content_type;
		obj[11] = photo;
		obj[12] = audio;
		obj[13] = sync_time;
		obj[14] = dirty;
		obj[15] = deleted;

	}

	public void dumpRecord(Cursor c) {
		_id = c.getInt(c.getColumnIndex("id"));
		server_id = c.getInt(c.getColumnIndex("server_id"));
		handle = c.getString(c.getColumnIndex("handle"));
		sender = c.getString(c.getColumnIndex("sender"));
		link = c.getString(c.getColumnIndex("link"));
		room_name = c.getString(c.getColumnIndex("roomname"));
		content = c.getString(c.getColumnIndex("content"));
		calc_date = c.getString(c.getColumnIndex("calc_date"));
		create_time = c.getString(c.getColumnIndex("create_time"));
		send_interval_time = c.getInt(c.getColumnIndex("send_interval_time"));
		send_done_time = c.getString(c.getColumnIndex("send_done_time"));
		content_type = c.getInt(c.getColumnIndex("content_type"));
		photo = c.getString(c.getColumnIndex("photo"));
		audio = c.getString(c.getColumnIndex("audio"));
		sync_time = c.getLong(c.getColumnIndex("sync_time"));

	}

	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();

		try {
			if (!TextUtils.isEmpty(handle)) {
				json.put("user", handle);
			}
			if (!TextUtils.isEmpty(link)) {
				json.put("link", link);
			}
			if (!TextUtils.isEmpty(sender)) {
				json.put("sender", sender);
			}
			if (!TextUtils.isEmpty(room_name)) {
				json.put("room", room_name);
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
			if (content_type != 0) {
				json.put("ctx", content_type);
			}
			if (!TextUtils.isEmpty(photo)) {
				json.put("po", photo);
			}
			if (!TextUtils.isEmpty(audio)) {
				json.put("ao", audio);
			}
			if (server_id > 0) {
				json.put("sid", server_id);
			}
			if (_id > 0) {
				json.put("cid", _id);
			}
			if (deleted != 0) {
				json.put("del", deleted);
			}
		} catch (final Exception ex) {
			Log.i("PrvDialog",
					"Error converting RawContact to JSONObject" + ex.toString());
		}

		return json;
	}

	/**
	 * Creates and returns RawDialog instance from all the supplied parameters.
	 */
	public static PrvDialog create(String name, String sender, String link,
			String roomName, String content, String createDate,
			String createTime, int contentType, String photo, String audio,
			int deleted, int serverDialogId, int rawDialogId, long syncState,
			int dirty) {
		return new PrvDialog(name, sender, link, roomName, content, createDate,
				createTime, contentType, photo, audio, deleted, serverDialogId,
				rawDialogId, syncState, dirty);
	}

	/**
	 * Creates and returns a User instance that represents a deleted user. Since
	 * the user is deleted, all we need are the client/server IDs.
	 * 
	 * @param clientUserId
	 *            The client-side ID for the record
	 * @param serverUserId
	 *            The server-side ID for the record
	 * @return a minimal User object representing the deleted record.
	 */
	public static PrvDialog createDeletedDialog(int rawDialogId,
			int serverDialogId) {
		return new PrvDialog(null, null, null, null, null, null, null, 0, null,
				null, 1, serverDialogId, rawDialogId, -1, 1);
	}
}
