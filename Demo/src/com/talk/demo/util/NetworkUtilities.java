/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.talk.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.talk.demo.parser.DialogParser;
import com.talk.demo.parser.FriendParser;
import com.talk.demo.parser.GroupParser;
import com.talk.demo.parser.NewsParser;
import com.talk.demo.parser.RecordParser;
import com.talk.demo.parser.ResParser;
import com.talk.demo.types.PrvDialog;
import com.talk.demo.types.Friend;
import com.talk.demo.types.Group;
import com.talk.demo.types.News;
import com.talk.demo.types.Record;
import com.talk.demo.types.TalkType;
import com.talk.demo.util.HttpRequest.HttpRequestException;

/**
 * Provides utility methods for communicating with the server.
 */
final public class NetworkUtilities {
    /** The tag used to log to adb console. */
    private static final String TAG = "NetworkUtilities";
    /** POST parameter name for the user's account name */
    public static final String PARAM_USERNAME = "username";
    /** POST parameter name for the user's password */
    public static final String PARAM_PASSWORD = "password";
    /** POST parameter name for the user's authentication token */
    public static final String PARAM_AUTH_TOKEN = "authtoken";
    /** POST parameter name for the crsf token */
    public static final String PARAM_CSRF_TOKEN = "csrftoken";    
    /** POST parameter name for the client's last-known sync state */
    public static final String PARAM_SYNC_STATE = "syncstate";
    /** POST parameter name for the sending client-edited contact info */
    public static final String PARAM_RECORDS_DATA = "records";
    public static final String PARAM_FRIENDS_DATA = "friends";
    /** Base URL for the v2 Sample Sync Service */
    //public static final String BASE_URL = "http://10.4.65.41/";
    public static final String BASE_URL = "http://192.168.1.101/";
    /** URI for authentication service */
    public static final String AUTH_URI = BASE_URL + "users/login/";
    public static final String SIGNUP_URI = BASE_URL + "users/signup/";
    public static final String SEARCH_PEOPLE_URI = BASE_URL + "users/search_people/";
    /** URI for friend service */
    public static final String RECOMMEND_FRIENDS_URI = BASE_URL + "friends/recommend";
    public static final String SYNC_FRIENDS_URI = BASE_URL + "friends/sync_friend/";
    public static final String ADD_FRIENDS_URI = BASE_URL + "friends/add_friend/";
    public static final String ACCEPT_FRIENDS_URI = BASE_URL + "friends/accept_friend/";
    public static final String UPDATE_FRIENDS_URI = BASE_URL + "friends/update_friend/";
    /** URI for sync service */
    public static final String SYNC_RECORDS_URI = BASE_URL + "times/sync/";
    public static final String VISIT_RECORDS_URI = BASE_URL + "times/visit/";
    /** URI for dialog share */
    public static final String SHARE_RECORDS_URI = BASE_URL + "dialogs/share/";
    public static final String GET_DIALOGS_URI = BASE_URL + "dialogs/getdialog/";
    /** URI for news service */
    public static final String SYNC_NEWS_URI = BASE_URL + "news/today/";
    
    public static final String SYNC_PHOTO_URI = BASE_URL + "times/photo/";
    public static final String DOWNLOAD_PHOTO_URI = BASE_URL + "times/photoView/";
    
    private NetworkUtilities() {
    }

    public static HttpRequest createGet(String url) {
        HttpRequest request = HttpRequest.get(url);
        request.followRedirects(false);
        
        return request;
    }
    
    public static HttpRequest createPost(String url, Map<String, String> formData) {
        HttpRequest request = HttpRequest.post(url);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "text/html");
        request.headers(headers);
        Log.d(TAG, "request: "+formData.toString());
        request.form(formData);
        
        return request;
    }
    
    public static TalkType doHttpRequest(HttpRequest request, ResParser<? extends TalkType> parser) 
    		throws JSONException {
    	try {
	        int statusCode = request.code();
	        switch (statusCode) {
	        case 200:
	        	String response = request.body();
	        	Log.d(TAG, "respone: " + response.toString());
	        	return consume(parser, response);
	        case 400:
	        	Log.d(TAG, "HTTP Code: 400");
	        	break;
	        case 401:
	        	Log.d(TAG, "HTTP Code: 400");
	        	break;  
	        case 404:
	        	Log.d(TAG, "HTTP Code: 404");
	        	break;     
	        case 500:
	        	Log.d(TAG, "HTTP Code: 500");
	        	break;     
	        default:
	        	Log.d(TAG, "Default case");
	        	break;     
	        }
    	} catch (HttpRequestException ex) {
    		Log.d(TAG, "http request error: "+ex.getMessage());
    	}
        
        return null;

    }
    
    public static TalkType consume(ResParser<? extends TalkType> parser, String content)
    		throws JSONException {
        try {
        	JSONObject json = new JSONObject(content);
        	Log.d(TAG, "response json : "+json.toString());
        	Iterator<String> it = (Iterator<String>)json.keys();
        	
        	if(it.hasNext()) {
        		String key = (String)it.next();
        		if(key.equals("error")) {
        			Log.d(TAG, "error is: "+json.getString(key));
        		} else {
        			Object obj = json.get(key);
        			
        			if(obj instanceof JSONArray) {
        				return parser.parse((JSONArray)obj);
        			} else {
        				return parser.parse(json);
        			}
        		}
        	} else {
        		throw new JSONException("Error parsing JSON response, object had no single child key.");
        	}
        } catch (JSONException ex) {
        	throw new JSONException("Error parsing JSON response: "+ ex.getMessage());
        }
        
        return null; 	
    }
    
    public static Friend addFriend(String username, String friend)
    		throws HttpRequestException, JSONException {
        HttpRequest request = createPost(ADD_FRIENDS_URI, PackedFormData.addFriend(username, friend));
        return (Friend)doHttpRequest(request,new FriendParser());
    }
    
    public static Friend acceptFriend(String username, boolean response, String friend)
    		throws HttpRequestException, JSONException {
        HttpRequest request = createPost(ACCEPT_FRIENDS_URI, PackedFormData.acceptFriend(username, response, friend));
        return (Friend)doHttpRequest(request,new FriendParser());
    }
    
    public static Friend updateFriend(String username, String comment, String description, String friend)
    		throws HttpRequestException, JSONException {
        HttpRequest request = createPost(UPDATE_FRIENDS_URI, PackedFormData.updateFriend(username, comment, description, friend));
        return (Friend)doHttpRequest(request,new FriendParser());
    }
    
    public static String signup(String username, String email, String password) {
        
    	try {
			HttpRequest request = HttpRequest.post(SIGNUP_URI);
			// X-CSRFToken
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "text/html");
			//headers.put("Cookie", csrfToken);
			//Log.d(TAG, "our cookie: " + csrfToken);
			request.headers(headers);
			//request.followRedirects(false);
			HttpRequest conn4Session = request.form(PackedFormData.signup(username, email, password));
			conn4Session.code();
			HttpURLConnection sessionConnection = conn4Session.getConnection();
			try {
				int result = sessionConnection.getResponseCode();
				Log.e(TAG, "get response code : "+result);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			
		} catch (HttpRequestException exception) {
			Log.d(TAG, "exception : " + exception.toString());
			return null;
		} finally {
            Log.v(TAG, "signup completing");
        }
		
		return "ok";
		
	}
    
    public static String authenticate(String username, String password) {
		try {
			HttpRequest request = HttpRequest.post(AUTH_URI);
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "text/html");

			request.headers(headers);
			request.followRedirects(false);
			HttpRequest conn4Session = request.form(PackedFormData.login(username, password));
			conn4Session.code();
			HttpURLConnection sessionConnection = conn4Session.getConnection();
			try {
				int result = sessionConnection.getResponseCode();
				Log.e(TAG, "get response code : "+result);
	
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			
		} catch (HttpRequestException exception) {
			Log.d(TAG, "exception : " + exception.toString());
			return null;
		} finally {
            Log.v(TAG, "getAuthtoken completing");
        }
		
		return "ok";
		
	}
    
    @SuppressWarnings("unchecked")
	public static Group<News> todayNews() throws JSONException, HttpRequestException {
        HttpRequest request = createGet(SYNC_NEWS_URI);
        return (Group<News>)doHttpRequest(request,new GroupParser(new NewsParser()));
    }
    
    public static List<String> recommendFriends() throws JSONException {
        List<String> mItems = new LinkedList<String>();
        try {
            
            HttpRequest request = HttpRequest.get(RECOMMEND_FRIENDS_URI);
            //request.followRedirects(false);
            String response = request.body();
            int result = request.code();
            Log.d(TAG,"Response was: " + response);
            final JSONArray serverFriends = new JSONArray(response);
            Log.d(TAG, response);
            for (int i = 0; i < serverFriends.length(); i++) {
                String test = serverFriends.getJSONObject(i).getString("friends");
                if (test != null) {
                    mItems.add(test);
                }
            }
            
            
        } catch (HttpRequestException exception) {
            Log.d(TAG, "exception : " + exception.toString());
        }

        return mItems;
    }
    

    public static void shareRecord(PrvDialog raw, String oring, String target) {
    	
    	try {
    		HttpRequest request = HttpRequest.post(SHARE_RECORDS_URI);
    		request.followRedirects(false);
    		request.form(PackedFormData.shareRecord(raw, oring, target));
    		request.getConnection();
    		int code = request.code();
    		if(code == HttpStatus.SC_OK) {
    			Log.d(TAG, "finish!!");
    		}
    	} catch (HttpRequestException exception) {
    		Log.d(TAG, "exception : "+ exception.toString());
    	}
        
    }
    
    public static PrvDialog getDialog_v2(String username, int id)
            throws HttpRequestException, JSONException {
        HttpRequest request = createPost(ADD_FRIENDS_URI, PackedFormData.getDialog(username, id));
        return (PrvDialog)doHttpRequest(request,new DialogParser());
    }
        

    @SuppressWarnings("unchecked")
    public static Group<Record> updateChannel_v2(String friend, String last_date) throws JSONException, HttpRequestException {
        HttpRequest request = createPost(VISIT_RECORDS_URI, PackedFormData.packedUpdateChannel(friend, last_date));
        return (Group<Record>)doHttpRequest(request,new GroupParser(new RecordParser()));
    }
    
    /**
     * Perform 2-way sync with the server-side contacts. We send a request that
     * includes all the locally-dirty contacts so that the server can process
     * those changes, and we receive (and return) a list of contacts that were
     * updated on the server-side that need to be updated locally.
     *
     * @param account The account being synced
     * @param authtoken The authtoken stored in the AccountManager for this
     *            account
     * @param serverSyncState A token returned from the server on the last sync
     * @param dirtyContacts A list of the contacts to send to the server
     * @return A list of contacts that we need to update locally
     */   
    @SuppressWarnings("unchecked")
    public static Group<Record> syncRecords_v2(
            Account account, String authtoken, long serverSyncState, List<Record> dirtyRecords) throws JSONException, HttpRequestException {
        HttpRequest request = createPost(SYNC_RECORDS_URI, 
                PackedFormData.syncRecords(account, authtoken, serverSyncState, dirtyRecords));
        return (Group<Record>)doHttpRequest(request,new GroupParser(new RecordParser()));
    }
    
    @SuppressWarnings("unchecked")
    public static Group<Friend> syncFriends_v2(
            Account account, String authtoken, long serverSyncState, List<Friend> dirtyFriends) throws JSONException, HttpRequestException {
        HttpRequest request = createPost(SYNC_FRIENDS_URI, 
                PackedFormData.syncFriends(account, authtoken, serverSyncState, dirtyFriends));
        return (Group<Friend>)doHttpRequest(request,new GroupParser(new FriendParser()));
    }
    
    public static void syncPhoto(String imagePath) {
    	
    	Log.d(TAG,"Sync photo to Server");
    	
		String fileKey = "image";
		UploadUtil uploadUtil = UploadUtil.getInstance();;
		
		uploadUtil.uploadFile(imagePath,fileKey, SYNC_PHOTO_URI);
	}

    public static void downloadPhoto(final String photoName) {
        // If there is no photo, we're done
        if (TextUtils.isEmpty(photoName)) {
            return;
        }

        try {
            Log.i(TAG, "Downloading photo: " + DOWNLOAD_PHOTO_URI);
            // Request the photo from the server, and create a bitmap
            // object from the stream we get back.
            URL url = new URL(DOWNLOAD_PHOTO_URI+photoName+"/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                final Bitmap photo = BitmapFactory.decodeStream(connection.getInputStream(),
                        null, options);

                Log.d(TAG, "file name : "+photoName);
                TalkUtil.createDirAndSaveFile(photo, photoName);
                // On pre-Honeycomb systems, it's important to call recycle on bitmaps
                photo.recycle();
            } finally {
                connection.disconnect();
            }
        } catch (MalformedURLException muex) {
            // A bad URL - nothing we can really do about it here...
            Log.e(TAG, "Malformed avatar URL: " + DOWNLOAD_PHOTO_URI);
        } catch (IOException ioex) {
            // If we're unable to download the avatar, it's a bummer but not the
            // end of the world. We'll try to get it next time we sync.
            Log.e(TAG, "Failed to download user avatar: " + DOWNLOAD_PHOTO_URI);
        }
    }
    /**
     * Download the avatar image from the server.
     *
     * @param avatarUrl the URL pointing to the avatar image
     * @return a byte array with the raw JPEG avatar image
     */
    public static byte[] downloadAvatar(final String avatarUrl) {
        // If there is no avatar, we're done
        if (TextUtils.isEmpty(avatarUrl)) {
            return null;
        }

        try {
            Log.i(TAG, "Downloading avatar: " + avatarUrl);
            // Request the avatar image from the server, and create a bitmap
            // object from the stream we get back.
            URL url = new URL(avatarUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                final Bitmap avatar = BitmapFactory.decodeStream(connection.getInputStream(),
                        null, options);

                // Take the image we received from the server, whatever format it
                // happens to be in, and convert it to a JPEG image. Note: we're
                // not resizing the avatar - we assume that the image we get from
                // the server is a reasonable size...
                Log.i(TAG, "Converting avatar to JPEG");
                ByteArrayOutputStream convertStream = new ByteArrayOutputStream(
                        avatar.getWidth() * avatar.getHeight() * 4);
                avatar.compress(Bitmap.CompressFormat.JPEG, 95, convertStream);
                convertStream.flush();
                convertStream.close();
                // On pre-Honeycomb systems, it's important to call recycle on bitmaps
                avatar.recycle();
                return convertStream.toByteArray();
            } finally {
                connection.disconnect();
            }
        } catch (MalformedURLException muex) {
            // A bad URL - nothing we can really do about it here...
            Log.e(TAG, "Malformed avatar URL: " + avatarUrl);
        } catch (IOException ioex) {
            // If we're unable to download the avatar, it's a bummer but not the
            // end of the world. We'll try to get it next time we sync.
            Log.e(TAG, "Failed to download user avatar: " + avatarUrl);
        }
        return null;
    }

}
