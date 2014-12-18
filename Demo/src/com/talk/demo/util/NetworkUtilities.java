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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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
    /** Timeout (in ms) we specify for each http request */
    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
    /** Base URL for the v2 Sample Sync Service */
    //public static final String BASE_URL = "http://114.215.208.170/";
    public static final String BASE_URL = "http://192.168.1.180/";
    /** URI for authentication service */
    public static final String AUTH_URI = BASE_URL + "account/login/";
    public static final String SIGNUP_URI = BASE_URL + "account/signup/";
    public static final String SYNC_NEWS_URI = BASE_URL + "news/today/";
    public static final String RECOMMEND_FRIENDS_URI = BASE_URL + "friends/recommend";
    public static final String SYNC_FRIENDS_URI = BASE_URL + "friends/sync/";
    /** URI for sync service */
    public static final String SYNC_RECORDS_URI = BASE_URL + "times/sync/";
    /** URI for dialog share */
    public static final String SHARE_RECORDS_URI = BASE_URL + "dialogs/share/";
    public static final String GET_DIALOGS_URI = BASE_URL + "dialogs/getdialog/";
    
    public static final String SYNC_PHOTO_URI = BASE_URL + "times/photo/";
    public static final String DOWNLOAD_PHOTO_URI = BASE_URL + "times/photoView/";
    
    private NetworkUtilities() {
    }

    /**
     * Configures the httpClient to connect to the URL provided.
     */
    public static HttpClient getHttpClient() {
        HttpClient httpClient = new DefaultHttpClient();
        final HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
        return httpClient;
    }

    /**
     * Connects to the SampleSync test server, authenticates the provided
     * username and password.
     *
     * @param username The server account username
     * @param password The server account password
     * @return String The authentication token returned by the server (or null)
     */
    /*
    public static String authenticate(String username, String password) {

        final HttpResponse resp;
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, username));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
        final HttpEntity entity;
        try {
            entity = new UrlEncodedFormEntity(params);
        } catch (final UnsupportedEncodingException e) {
            // this should never happen.
            throw new IllegalStateException(e);
        }
        Log.i(TAG, "Authenticating to: " + AUTH_URI);
        final HttpPost post = new HttpPost(AUTH_URI);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        try {
            resp = getHttpClient().execute(post);
            String authToken = null;
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream istream = (resp.getEntity() != null) ? resp.getEntity().getContent()
                        : null;
                if (istream != null) {
                    BufferedReader ireader = new BufferedReader(new InputStreamReader(istream));
                    authToken = ireader.readLine().trim();
                }
            }
            if ((authToken != null) && (authToken.length() > 0)) {
                Log.v(TAG, "Successful authentication");
                return authToken;
            } else {
                Log.e(TAG, "Error authenticating" + resp.getStatusLine());
                return null;
            }
        } catch (final IOException e) {
            Log.e(TAG, "IOException when getting authtoken", e);
            return null;
        } finally {
            Log.v(TAG, "getAuthtoken completing");
        }
    }
    */
    
    public static String signup(String username, String email, String password) {
    	String authToken = null;
		String csrfToken2 = null;
		try {
			HttpURLConnection conn = HttpRequest.get(SIGNUP_URI)
					.getConnection();
			//ToDo: cookie header may be null, should fix it.
			List<String> cookieHeader = conn.getHeaderFields().get("Set-Cookie");
            for(String resItem : cookieHeader) {
            	Log.d(TAG, "cookie session: " + resItem);
                if(resItem.contains("sessionid")) {
                	authToken = resItem.split(";")[0];
                    Log.d(TAG, "session :" + authToken);
                }
                
                if(resItem.contains("csrftoken")) {
                    csrfToken2 = resItem.split(";")[0];
                    Log.d(TAG, "csrf token :" + csrfToken2);
                }
                
            }
			Log.d(TAG, "cookie: " + cookieHeader);

			String csrfToken = csrfToken2;
			Log.d(TAG, "csrf token : " + csrfToken);

			HttpRequest request = HttpRequest.post(SIGNUP_URI);
			String name = username;
			String mail = email;
			String passwd = password;
			Map<String, String> data = new HashMap<String, String>();
			data.put("username", name);
			data.put("email", mail);
			data.put("password", passwd);
			data.put("password_confirm",passwd);
			data.put("csrfmiddlewaretoken", csrfToken.substring(10));
			Log.d(TAG, "name: " + username + " passwd: " + password);
			// X-CSRFToken
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "text/html");
			headers.put("Cookie", csrfToken);
			Log.d(TAG, "our cookie: " + csrfToken);
			request.headers(headers);
			//request.followRedirects(false);
			HttpRequest conn4Session = request.form(data);
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
		String authToken = null;
		String csrfToken2 = null;
		try {
			HttpURLConnection conn = HttpRequest.get(AUTH_URI)
					.getConnection();
			//ToDo: cookie header may be null, should fix it.
			List<String> temp  = conn.getHeaderFields().get("Set-Cookie");
			String cookieHeader = null;
			String csrfToken = null;
			if (null !=temp && !temp.isEmpty()) {
				cookieHeader = temp.get(0);
			
				Log.d(TAG, "cookie: " + cookieHeader);

				csrfToken = cookieHeader.split(";")[0];
				Log.d(TAG, "csrf token : " + csrfToken);
			}

			HttpRequest request = HttpRequest.post(AUTH_URI);
			String name = username;
			String passwd = password;
			Map<String, String> data = new HashMap<String, String>();
			data.put("username", name);
			data.put("password", passwd);
			if(csrfToken != null)
				data.put("csrfmiddlewaretoken", csrfToken.substring(10));
			Log.d(TAG, "name: " + username + " passwd: " + password);
			// X-CSRFToken
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "text/html");
			if(csrfToken != null)
				headers.put("Cookie", csrfToken);

			request.headers(headers);
			request.followRedirects(false);
			HttpRequest conn4Session = request.form(data);
			conn4Session.code();
			HttpURLConnection sessionConnection = conn4Session.getConnection();
			try {
				int result = sessionConnection.getResponseCode();
				Log.e(TAG, "get response code : "+result);
                List<String> responseList = sessionConnection.getHeaderFields().get("Set-Cookie");
                
                if(null != responseList) {
	                for(String resItem : responseList) {
	                	Log.d(TAG, "cookie session: " + resItem);
	                    if(resItem.contains("sessionid")) {
	                    	authToken = resItem.split(";")[0];
	                        Log.d(TAG, "session :" + authToken);
	                        NetData.setSessionId(authToken);
	                    }
	                    
	                    if(resItem.contains("csrftoken")) {
	                        csrfToken2 = resItem.split(";")[0];
	                        Log.d(TAG, "csrf token :" + csrfToken2);
	                        NetData.setCsrfToken(csrfToken2);
	                    }
	                    
	                }
                }
                
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
		
        if ((authToken != null) && (authToken.length() > 0)) {
            Log.v(TAG, "Successful authentication");
            return authToken+";"+csrfToken2;
        } else {
            Log.e(TAG, "Error authenticating");
            return null;
        }
		
	}
    
    public static DailyNews syncNews() throws JSONException {
    	DailyNews mItems = new DailyNews();
        try {
            
            HttpRequest request = HttpRequest.get(SYNC_NEWS_URI);
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
    
    public static void shareRecord(RawDialog raw, String oring, String target) 
            throws JSONException, ParseException, IOException {
        HttpURLConnection conn = HttpRequest.get(AUTH_URI)
                .getConnection();

        if (null == conn || null == conn.getHeaderFields()) {
        	return;
        }
        /*
         * cookieHeader may be null cause NullPointerException
         * ToDo: write the whole code completely
         */
		List<String> temp  = conn.getHeaderFields().get("Set-Cookie");
		String cookieHeader = null;
		String csrfToken = null;
		if (null !=temp && !temp.isEmpty()) {
			cookieHeader = temp.get(0);
		
			Log.d(TAG, "cookie: " + cookieHeader);

			csrfToken = cookieHeader.split(";")[0];
			Log.d(TAG, "csrf token : " + csrfToken);
		}

        JSONObject jsonRecord = raw.toJSONObject();
        // Prepare our POST data
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, oring));
        params.add(new BasicNameValuePair("records", jsonRecord.toString()));
        params.add(new BasicNameValuePair("target", target));
        if(csrfToken != null)
        	params.add(new BasicNameValuePair("csrfmiddlewaretoken", csrfToken.substring(10)));
        HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        final HttpPost post = new HttpPost(SHARE_RECORDS_URI);
        post.addHeader(entity.getContentType());
        if(csrfToken != null)
        	post.addHeader("Cookie", csrfToken);
        post.setEntity(entity);
        final HttpResponse resp = getHttpClient().execute(post);
        final String response = EntityUtils.toString(resp.getEntity());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Log.d(TAG, "finish!!");
        } 
    }
    
    public static RawDialog getDialog(String username, int id) 
            throws JSONException, ParseException, IOException {
        
        HttpURLConnection conn = HttpRequest.get(AUTH_URI)
                .getConnection();

        if (null == conn || null == conn.getHeaderFields()) {
            return null;
        }
        /*
         * cookieHeader may be null cause NullPointerException
         * ToDo: write the whole code completely
         */
        List<String> temp  = conn.getHeaderFields().get("Set-Cookie");
        String cookieHeader = null;
        String csrfToken = null;
        if (null !=temp && !temp.isEmpty()) {
            cookieHeader = temp.get(0);
        
            Log.d(TAG, "cookie: " + cookieHeader);

            csrfToken = cookieHeader.split(";")[0];
            Log.d(TAG, "csrf token : " + csrfToken);
        }

        // Prepare our POST data
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("id", String.valueOf(id)));
        if(csrfToken != null)
            params.add(new BasicNameValuePair("csrfmiddlewaretoken", csrfToken.substring(10)));
        HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        final HttpPost post = new HttpPost(GET_DIALOGS_URI);
        post.addHeader(entity.getContentType());
        if(csrfToken != null)
            post.addHeader("Cookie", csrfToken);
        post.setEntity(entity);
        final HttpResponse resp = getHttpClient().execute(post);
        final String response = EntityUtils.toString(resp.getEntity());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            final JSONObject dialogItem = new JSONObject(response);
            Log.d(TAG, "dialog item: "+ dialogItem.toString());
            final RawDialog dialog = RawDialog.valueOf(dialogItem);
            return dialog;
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
            Account account, String authtoken, long serverSyncState, List<RawRecord> dirtyRecords)
            throws JSONException, ParseException, IOException, AuthenticationException {
        // Convert our list of User objects into a list of JSONObject
        List<JSONObject> jsonRecords = new ArrayList<JSONObject>();
        for (RawRecord rawRecord : dirtyRecords) {
        	jsonRecords.add(rawRecord.toJSONObject());
        }

        // Create a special JSONArray of our JSON contacts
        JSONArray buffer = new JSONArray(jsonRecords);

        // Create an array that will hold the server-side records
        // that have been changed (returned by the server).
        final ArrayList<RawRecord> serverDirtyList = new ArrayList<RawRecord>();

        // Prepare our POST data
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, account.name));
        //params.add(new BasicNameValuePair(PARAM_AUTH_TOKEN, authtoken));
        params.add(new BasicNameValuePair(PARAM_RECORDS_DATA, buffer.toString()));
        String tempBuffer = null;
        if(authtoken.split(";").length > 1) {
        	tempBuffer = authtoken.split(";")[1];
        }
        if(tempBuffer.length() > 10) {
        	params.add(new BasicNameValuePair("csrfmiddlewaretoken", tempBuffer.substring(10)));
        }
        Log.d(TAG, "auth toke: "+authtoken);
        
        if (serverSyncState > 0) {
            params.add(new BasicNameValuePair(PARAM_SYNC_STATE, Long.toString(serverSyncState)));
        }
        Log.i(TAG, params.toString());
        HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);

        // Send the updated friends data to the server
        Log.i(TAG, "Syncing to: " + SYNC_RECORDS_URI);
        final HttpPost post = new HttpPost(SYNC_RECORDS_URI);
        post.addHeader(entity.getContentType());
        post.addHeader("Cookie", authtoken);
        post.setEntity(entity);
        final HttpResponse resp = getHttpClient().execute(post);
        final String response = EntityUtils.toString(resp.getEntity());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
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
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                Log.e(TAG, "Authentication exception in sending dirty contacts");
                throw new AuthenticationException();
            } else {
                Log.e(TAG, "Server error in sending dirty contacts: " + resp.getStatusLine());
                throw new IOException();
            }
        }

        return serverDirtyList;
    }

    public static List<RawFriend> syncFriends(
            Account account, String authtoken, long serverSyncState, List<RawFriend> dirtyFriends)
            throws JSONException, ParseException, IOException, AuthenticationException {
        // Convert our list of User objects into a list of JSONObject
        List<JSONObject> jsonRecords = new ArrayList<JSONObject>();
        for (RawFriend rawFriend : dirtyFriends) {
            jsonRecords.add(rawFriend.toJSONObject());
        }

        // Create a special JSONArray of our JSON contacts
        JSONArray buffer = new JSONArray(jsonRecords);

        // Create an array that will hold the server-side records
        // that have been changed (returned by the server).
        final ArrayList<RawFriend> serverDirtyList = new ArrayList<RawFriend>();

        // Prepare our POST data
        final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAM_USERNAME, account.name));
        //params.add(new BasicNameValuePair(PARAM_AUTH_TOKEN, authtoken));
        params.add(new BasicNameValuePair(PARAM_RECORDS_DATA, buffer.toString()));
        String tempBuffer = null;
        if(authtoken.split(";").length > 1) {
        	tempBuffer = authtoken.split(";")[1];
        }
        if(tempBuffer.length() > 10) {
        	params.add(new BasicNameValuePair("csrfmiddlewaretoken", tempBuffer.substring(10)));
        }
        Log.d(TAG, "auth toke: "+authtoken);
        
        if (serverSyncState > 0) {
            params.add(new BasicNameValuePair(PARAM_SYNC_STATE, Long.toString(serverSyncState)));
        }
        Log.i(TAG, params.toString());
        HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);

        // Send the updated friends data to the server
        Log.i(TAG, "Syncing to: " + SYNC_FRIENDS_URI);
        final HttpPost post = new HttpPost(SYNC_FRIENDS_URI);
        post.addHeader(entity.getContentType());
        post.addHeader("Cookie", authtoken);
        post.setEntity(entity);
        final HttpResponse resp = getHttpClient().execute(post);
        final String response = EntityUtils.toString(resp.getEntity());
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // Our request to the server was successful - so we assume
            // that they accepted all the changes we sent up, and
            // that the response includes the contacts that we need
            // to update on our side...
            final JSONArray serverRecords = new JSONArray(response);
            Log.d(TAG, serverRecords.toString());
            for (int i = 0; i < serverRecords.length(); i++) {
                RawFriend rawRecord = RawFriend.valueOf(serverRecords.getJSONObject(i));
                if (rawRecord != null) {
                    serverDirtyList.add(rawRecord);
                }
            }
        } else {
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                Log.e(TAG, "Authentication exception in sending dirty contacts");
                throw new AuthenticationException();
            } else {
                Log.e(TAG, "Server error in sending dirty friends: " + resp.getStatusLine());
                throw new IOException();
            }
        }

        return serverDirtyList;
    }
    
    public static void syncPhoto(String imagePath) {
    	
    	Log.d(TAG,"Sync photo to Server");
    	/*
    	HttpRequest request = HttpRequest.post(SYNC_PHOTO_URI);
    	request.part("body", "Making a multipart request");
    	request.part("image", new File(imagePath));
    	
    	if (request.ok())
    	  Log.d(TAG,"Status was updated");
    	*/
    	
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
