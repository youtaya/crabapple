package com.talk.demo.types;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;


public class Friend implements TalkType {

    public int _id;
    public int server_id;
    public String handle;
    public long sync_time;
    /*
     * deleted flag :
     * default : 0 mean don't delete, other: 1 mean need to delete
     */
    public int deleted = 0; 
    /*
     * dirty flag :
     * default : 1 mean dirty and need to sync, other: 0 mean not need sync
     */
    public int dirty = 1;
    
    public String userName;
    public String phoneMobile;
    public String avatar;
    public String description;
    
    public Friend() {
    }
    
    public Friend(String name, String fHandle, String phone, String fAvatar,
            String fDescription, int serverFriendId,int rawFriendId, long syncState) {
        handle = fHandle;
        server_id = serverFriendId;
        _id = rawFriendId;
        sync_time = syncState;
        userName = name;
        phoneMobile = phone;
        avatar = fAvatar;
        description = fDescription;
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
    
    public void setUsername(String v) {
        userName = v;
    }
    public String getUserName() {
        return userName;
    }
    
    public void setPhoneMobile(String v) {
        phoneMobile = v;
    }
    public String getPhoneMobile() {
        return phoneMobile;
    }
    
    public void setAvatar(String v) {
        avatar = v;
    }
    public String getAvatar() {
        return avatar;
    }
    
    public void setDescription(String v) {
        description = v;
    }
    public String getDescription() {
        return description;
    }    
    
    public void setDirty(int v) {
        dirty = v;
    }
    
    public int getDirty() {
        return dirty;
    }
    
    public int getDeleted() {
    	return deleted;
    }
    
    public void getObjectItems(Object[] obj) {
        obj[0] = server_id;
        obj[1] = handle;
        obj[2] = userName;
        obj[3] = phoneMobile;
        obj[4] = avatar;
        obj[5] = description;
        obj[6] = sync_time;
        obj[7] = dirty;
        obj[8] = deleted;
        
    }

    public void dumpRecord(Cursor c) {
        _id = c.getInt(c.getColumnIndex("id"));
        server_id = c.getInt(c.getColumnIndex("server_id"));
        userName = c.getString(c.getColumnIndex("username"));
        handle = c.getString(c.getColumnIndex("handle"));
        phoneMobile = c.getString(c.getColumnIndex("phone_mobile"));
        avatar = c.getString(c.getColumnIndex("avatar"));
        description = c.getString(c.getColumnIndex("description"));
        sync_time = c.getLong(c.getColumnIndex("sync_time"));
        dirty = c.getInt(c.getColumnIndex("dirty"));
        deleted = c.getInt(c.getColumnIndex("deleted"));
    }
    
    
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(userName)) {
                json.put("u", userName);
            }
            if (!TextUtils.isEmpty(handle)) {
                json.put("h", handle);
            }
            if (!TextUtils.isEmpty(phoneMobile)) {
                json.put("p", phoneMobile);
            }
            if (!TextUtils.isEmpty(avatar)) {
                json.put("a", avatar);
            } 
            if (!TextUtils.isEmpty(description)) {
                json.put("d", description);
            }                  
            if (server_id > 0) {
                json.put("s", server_id);
            }
            if (_id > 0) {
                json.put("f", _id);
            }
            if (deleted != -1) {
                json.put("del", deleted);
            }
            if (dirty != -1) {
                json.put("dirty", dirty);
            }
            if (sync_time != -1) {
                json.put("x", sync_time);
            }

        } catch (final Exception ex) {
            Log.d("Friend", "Error conveting to JSONObject"+ex.toString());
        }
        
        return json;
    }
    
    public static Friend create(String name, String handle, String phone, String avatar,
            String description, int serverFriendId,int rawFriendId) {
        return new Friend(name, handle, phone, avatar, description, serverFriendId, rawFriendId, -1);
    }
    
    public static Friend createDeletedFriend(int serverFriendId, int rawFriendId) {
        return new Friend(null, null, null, null, null, serverFriendId, rawFriendId, -1);
    }
    
    public String toString() {
        String isDirty = (dirty ==1)?"yes":"no";
        String info = " id: "+String.valueOf(_id)+
                " server id: "+String.valueOf(server_id)+
                " user name: "+userName+
                " dirty: " +isDirty;
        return info;
    }

}