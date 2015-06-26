package com.afollestad.silk.http;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.StatusLine;

import java.net.SocketTimeoutException;

/**
 * @author Aidan Follestad (afollestad)
 */
public class SilkHttpException extends Exception {

    private int mStatus = -1;
    private String mReason;
    private boolean mIsResponse;
    private String mResponseBody;

    SilkHttpException(String msg) {
        super(msg);
    }

    SilkHttpException(Exception e) {
        super((e instanceof SocketTimeoutException) ? "Connection timed out" : e.getMessage());
    }

    SilkHttpException(HttpResponse response, String body) {
        mIsResponse = true;
        StatusLine stat = response.getStatusLine();
        mStatus = stat.getStatusCode();
        mReason = stat.getReasonPhrase();
        mResponseBody = body;
    }

    /**
     * Gets the status code returned from the HTTP request, this will only be set if {@link #isServerResponse()} returns true.
     */
    public int getStatusCode() {
        return mStatus;
    }

    /**
     * Gets the reason phrase for the value of {@link #getStatusCode()}. this will only be set if {@link #isServerResponse()} returns true.
     */
    public String getReasonPhrase() {
        return mReason;
    }

    /**
     * Gets whether or not this exception was thrown for a non-200 HTTP response code, or if it was thrown for a code level Exception.
     */
    public boolean isServerResponse() {
        return mIsResponse;
    }

    public String getResponseBody() {
        return mResponseBody;
    }

    private final static int RESPONSE_BODY_LOG_THRESHOLD = 150;

    @Override
    public String getMessage() {
        if (isServerResponse()) {
            String message = getStatusCode() + " " + getReasonPhrase();
            if (mResponseBody != null) {
                if (mResponseBody.length() > RESPONSE_BODY_LOG_THRESHOLD)
                    message += "\n" + mResponseBody.substring(0, RESPONSE_BODY_LOG_THRESHOLD) + "\n... (response body truncated for log)";
                else message += "\n" + mResponseBody;
            }
            return message;
        }
        return super.getMessage();
    }
}