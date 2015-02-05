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

import android.accounts.Account;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.talk.demo.util.HttpRequest.HttpRequestException;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Provides utility methods for communicating with the server.
 */
final public class NetworkUtilities {
    /** The tag used to log to adb console. */
    private static final String TAG = "NetworkUtilities";

    private NetworkUtilities() {
    }

    
    public static String signup(String username, String email, String password) {
        
    	try {
			HttpRequest request = HttpRequest.post(ServerInterface.SIGNUP_URI);
			// X-CSRFToken
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "text/html");
			//headers.put("Cookie", csrfToken);
			//Log.d(TAG, "our cookie: " + csrfToken);
			request.headers(headers);
			//request.followRedirects(false);
			HttpRequest conn4Session = request.form(PackedFormData.packedData(username, email, password));
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
			HttpRequest request = HttpRequest.post(ServerInterface.AUTH_URI);
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "text/html");

			request.headers(headers);
			request.followRedirects(false);
			HttpRequest conn4Session = request.form(PackedFormData.packedData(username, password));
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
    
    public static DailyNews syncNews() throws JSONException {
    	DailyNews mItems = new DailyNews();
        try {
            
            HttpRequest request = HttpRequest.get(ServerInterface.SYNC_NEWS_URI);
            request.followRedirects(false);
            String response = request.body();
            int result = request.code();
            Log.d(TAG,"Response was: " + response);
            JSONObject content = new JSONObject(response);
            JSONArray serverNews = content.getJSONArray("news");
            Log.d(TAG, "news are: "+serverNews);
            String cTime = content.getString("create_time");
            String eTime = content.getString("expired_time");
            mItems.parseJSONArray(serverNews);
            mItems.setCreateTime(cTime);
            mItems.setExpiredTime(eTime);
            
        } catch (HttpRequestException exception) {
            Log.d(TAG, "exception : " + exception.toString());
        }

        return mItems;
    }
    
    public static List<String> recommendFriends() throws JSONException {
        List<String> mItems = new LinkedList<String>();
        try {
            
            HttpRequest request = HttpRequest.get(ServerInterface.RECOMMEND_FRIENDS_URI);
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
    

    public static void shareRecord(RawDialog raw, String oring, String target) {
    	
    	try {
    		HttpRequest request = HttpRequest.post(ServerInterface.SHARE_RECORDS_URI);
    		request.followRedirects(false);
    		request.form(PackedFormData.packedShareRecord(raw, oring, target));
    		request.getConnection();
    		int code = request.code();
    		if(code == HttpStatus.SC_OK) {
    			Log.d(TAG, "finish!!");
    		}
    	} catch (HttpRequestException exception) {
    		Log.d(TAG, "exception : "+ exception.toString());
    	}
        
    }
    

        
    public static RawDialog getDialog(String username, int id) {
    	try {
    		HttpRequest request = HttpRequest.post(ServerInterface.GET_DIALOGS_URI);
    		request.followRedirects(false);
    		request.form(PackedFormData.packedDialog(username, id));
    		request.getConnection();
    		int code = request.code();
    		if(code == HttpStatus.SC_OK) {
    			final String response = request.body();
    			Log.d(TAG, "dialog respone : " + response);
    	        final JSONObject dialogItem = new JSONObject(response);
                final RawDialog dialog = RawDialog.valueOf(dialogItem);
                return dialog;
    		}
    	} catch (HttpRequestException exception) {
    		Log.d(TAG, "exception : "+ exception.toString());
    	} catch (JSONException e) {
    		Log.d(TAG, "json exception : "+ e.toString());
		}    

        return null;
    }
    

    
    public static List<RawRecord> updateChannel(String friend, String last_date) {
        
        final List<RawRecord> records = new LinkedList<RawRecord>();
        
        try {
            HttpRequest request = HttpRequest.post(ServerInterface.VISIT_RECORDS_URI);
            request.followRedirects(false);
            request.form(PackedFormData.packedUpdateChannel(friend, last_date));
            request.getConnection();
            int code = request.code();
            if(code == HttpStatus.SC_OK) {
                final String response = request.body();
                Log.d(TAG, "get records respone : " + response);
                final JSONArray jsonRecords = new JSONArray(response);
                for (int i = 0; i < jsonRecords.length(); i++) {
                    RawRecord rawRecord = RawRecord.valueOf(jsonRecords.getJSONObject(i));
                    records.add(rawRecord);
                }
                return records;
            }
        } catch (HttpRequestException exception) {
            Log.d(TAG, "exception : "+ exception.toString());
        } catch (JSONException e) {
            Log.d(TAG, "json exception : "+ e.toString());
        }    

        return null;
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
    public static List<RawRecord> syncRecords(
            Account account, String authtoken, long serverSyncState, List<RawRecord> dirtyRecords) {

        // Create an array that will hold the server-side records
        // that have been changed (returned by the server).
        final ArrayList<RawRecord> serverDirtyList = new ArrayList<RawRecord>();
        
        try {
            // Send the updated friends data to the server
            HttpRequest request = HttpRequest.post(ServerInterface.SYNC_RECORDS_URI);
            request.followRedirects(false);
            request.form(PackedFormData.packedSyncRecords(account, authtoken, serverSyncState, dirtyRecords));
            request.getConnection();
            int code = request.code();
            if(code == HttpStatus.SC_OK) {
                final String response = request.body();
                Log.d(TAG, "get records respone : " + response);
                // Our request to the server was successful - so we assume
                // that they accepted all the changes we sent up, and
                // that the response includes the contacts that we need
                // to update on our side...
                final JSONArray serverRecords = new JSONArray(response);
                Log.d(TAG, serverRecords.toString());
                for (int i = 0; i < serverRecords.length(); i++) {
                    RawRecord rawRecord = RawRecord.valueOf(serverRecords.getJSONObject(i));
                    if (rawRecord != null) {
                        serverDirtyList.add(rawRecord);
                    }
                }

            } else {
                if (code == HttpStatus.SC_UNAUTHORIZED) {
                    Log.e(TAG, "Authentication exception in sending dirty contacts");
                } else {
                    Log.e(TAG, "Server error in sending dirty contacts: " + code);
                }
            }
        } catch (HttpRequestException exception) {
            Log.d(TAG, "exception : "+ exception.toString());
        } catch (JSONException e) {
            Log.d(TAG, "json exception : "+ e.toString());
        }    

        return serverDirtyList;

    }

    public static List<RawFriend> syncFriends(
            Account account, String authtoken, long serverSyncState, List<RawFriend> dirtyFriends) {
        final ArrayList<RawFriend> serverDirtyList = new ArrayList<RawFriend>();
        try {
            // Send the updated friends data to the server
            HttpRequest request = HttpRequest.post(ServerInterface.SYNC_FRIENDS_URI);
            request.followRedirects(false);
            request.form(PackedFormData.packedSyncFriends(account, authtoken, serverSyncState, dirtyFriends));
            request.getConnection();
            int code = request.code();
            if(code == HttpStatus.SC_OK) {
                final String response = request.body();
                Log.d(TAG, "get records respone : " + response);
                final JSONArray serverRecords = new JSONArray(response);
                Log.d(TAG, serverRecords.toString());
                for (int i = 0; i < serverRecords.length(); i++) {
                    RawFriend rawRecord = RawFriend.valueOf(serverRecords.getJSONObject(i));
                    if (rawRecord != null) {
                        serverDirtyList.add(rawRecord);
                    }
                }
            } else {
                if (code == HttpStatus.SC_UNAUTHORIZED) {
                    Log.e(TAG, "Authentication exception in sending dirty contacts");
                } else {
                    Log.e(TAG, "Server error in sending dirty contacts: " + code);
                }
            }
        } catch (HttpRequestException exception) {
            Log.d(TAG, "exception : "+ exception.toString());
        } catch (JSONException e) {
            Log.d(TAG, "json exception : "+ e.toString());
        }    
    

        return serverDirtyList;
    }
    
    public static void syncPhoto(String imagePath) {
    	
    	Log.d(TAG,"Sync photo to Server");
    	
		String fileKey = "image";
		UploadUtil uploadUtil = UploadUtil.getInstance();;
		
		uploadUtil.uploadFile(imagePath,fileKey, ServerInterface.SYNC_PHOTO_URI);
	}

    public static void downloadPhoto(final String photoName) {
        // If there is no photo, we're done
        if (TextUtils.isEmpty(photoName)) {
            return;
        }

        try {
            Log.i(TAG, "Downloading photo: " + ServerInterface.DOWNLOAD_PHOTO_URI);
            // Request the photo from the server, and create a bitmap
            // object from the stream we get back.
            URL url = new URL(ServerInterface.DOWNLOAD_PHOTO_URI+photoName+"/");
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
            Log.e(TAG, "Malformed avatar URL: " + ServerInterface.DOWNLOAD_PHOTO_URI);
        } catch (IOException ioex) {
            // If we're unable to download the avatar, it's a bummer but not the
            // end of the world. We'll try to get it next time we sync.
            Log.e(TAG, "Failed to download user avatar: " + ServerInterface.DOWNLOAD_PHOTO_URI);
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
