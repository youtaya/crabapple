package com.talk.demo.util;

public class ServerInterface {
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

    /** Base URL for the v2 Sample Sync Service */
    //public static final String BASE_URL = "http://114.215.208.170/";
    public static final String BASE_URL = "http://192.168.1.104/";
    /** URI for authentication service */
    public static final String AUTH_URI = BASE_URL + "users/login/";
    public static final String SIGNUP_URI = BASE_URL + "users/signup/";
    public static final String SYNC_NEWS_URI = BASE_URL + "news/today/";
    public static final String RECOMMEND_FRIENDS_URI = BASE_URL + "friends/recommend";
    public static final String SYNC_FRIENDS_URI = BASE_URL + "friends/sync_friend/";
    /** URI for sync service */
    public static final String SYNC_RECORDS_URI = BASE_URL + "times/sync/";
    public static final String VISIT_RECORDS_URI = BASE_URL + "times/visit/";
    /** URI for dialog share */
    public static final String SHARE_RECORDS_URI = BASE_URL + "dialogs/share/";
    public static final String GET_DIALOGS_URI = BASE_URL + "dialogs/getdialog/";
    
    public static final String SYNC_PHOTO_URI = BASE_URL + "times/photo/";
    public static final String DOWNLOAD_PHOTO_URI = BASE_URL + "times/photoView/";
}
