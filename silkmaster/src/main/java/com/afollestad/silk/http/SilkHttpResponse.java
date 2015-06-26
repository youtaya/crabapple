package com.afollestad.silk.http;

import ch.boye.httpclientandroidlib.Header;
import ch.boye.httpclientandroidlib.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the response to an HTTP request.
 *
 * @author Aidan Follestad (afollestad)
 */
public class SilkHttpResponse {

    private final List<SilkHttpHeader> mHeaders;
    private final byte[] mContent;

    SilkHttpResponse(HttpResponse response, byte[] content) {
        mHeaders = new ArrayList<SilkHttpHeader>();
        for (Header header : response.getAllHeaders())
            mHeaders.add(new SilkHttpHeader(header));
        mContent = content;
    }

    /**
     * Gets all headers with a specified name from the response.
     */
    public SilkHttpHeader[] getHeaders(String name) {
        List<SilkHttpHeader> headers = new ArrayList<SilkHttpHeader>();
        for (SilkHttpHeader h : mHeaders) {
            if (h.getName().equalsIgnoreCase(name))
                headers.add(h);
        }
        return headers.toArray(new SilkHttpHeader[headers.size()]);
    }

    /**
     * Gets all response headers.
     */
    public SilkHttpHeader[] getHeaders() {
        return mHeaders.toArray(new SilkHttpHeader[mHeaders.size()]);
    }

    /**
     * Gets the response content.
     */
    public byte[] getContent() {
        return mContent;
    }

    /**
     * Gets the response content as a string.
     */
    public String getContentString() {
        if (mContent == null) return null;
        try {
            return new String(mContent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the response content as a JSONObject.
     */
    public JSONObject getContentJSON() throws InvalidJSONException {
        try {
            return new JSONObject(getContentString());
        } catch (JSONException e) {
            throw new InvalidJSONException();
        }
    }

    /**
     * Gets the response content as a JSONArray.
     */
    public JSONArray getContentJSONArray() throws InvalidJSONException {
        try {
            return new JSONArray(getContentString());
        } catch (JSONException e) {
            throw new InvalidJSONException();
        }
    }

    @Override
    public String toString() {
        try {
            return getContentString();
        } catch (Exception e) {
            return super.toString();
        }
    }

    public static class InvalidJSONException extends SilkHttpException {
        public InvalidJSONException() {
            super("The server did not return JSON.");
        }
    }
}