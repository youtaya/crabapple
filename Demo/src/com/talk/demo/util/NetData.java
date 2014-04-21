package com.talk.demo.util;

public class NetData {

	public static String serverURL = "http://192.168.1.104/";
    //public static String serverURL = "http://10.4.65.41/";
    
    private static String sessionid;
    private static String csrfToken;
    
    public static void setSessionId(String param) {
        sessionid = param;
    }
    
    public static String getSessionId() {
        return sessionid;
    }
    
    public static void setCsrfToken(String param) {
        csrfToken = param;
    }
    
    public static String getCsrfToken() {
        return csrfToken;
    }
}
