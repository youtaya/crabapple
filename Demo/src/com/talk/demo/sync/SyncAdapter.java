package com.talk.demo.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.talk.demo.account.AccountConstants;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.RawRecord;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SyncAdapter extends AbstractThreadedSyncAdapter  {
	private static final String TAG = "SyncAdapter";
	
    private static final String SYNC_MARKER_KEY = "com.talk.demo.sync.marker";
    private static final boolean NOTIFY_AUTH_FAILURE = true;
    
    private final AccountManager mAccountManager;
    private final Context mContext;
    
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
		mAccountManager = AccountManager.get(context);
	}
	
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    private List<RawRecord> getDirtyRecords() {
    	List<RawRecord> dirtyTimes = new ArrayList<RawRecord>() ;
    	String name = "jinxp";
    	String title = "hw";
    	String content = "ok,just it";
    	String createDate = "2014-4-26";
    	String createTime = "2014-4-26";
    	int contentType = 1;
    	boolean deleted = false;
    	
    	RawRecord rr = new RawRecord(name, title, content, createDate,
                createTime, contentType, null, null,
                null, deleted, 1,
                1, -1, true);
    	dirtyTimes.add(rr);
    	
    	return dirtyTimes;
    }
    
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Log.d(TAG, "onPerformSync");
		try {
			List<RawRecord> dirtyRecords;
	        List<RawRecord> updatedRecords;
	        // see if we already have a sync-state attached to this account. By handing
	        // This value to the server, we can just get the contacts that have
	        // been updated on the server-side since our last sync-up
	        long lastSyncMarker = getServerSyncMarker(account);
	        // Use the account manager to request the AuthToken we'll need
	        // to talk to our sample server.  If we don't have an AuthToken
	        // yet, this could involve a round-trip to the server to request
	        // and AuthToken.
	        String authtoken = mAccountManager.blockingGetAuthToken(account,
			        AccountConstants.AUTHTOKEN_TYPE, NOTIFY_AUTH_FAILURE);
	        dirtyRecords = getDirtyRecords();
	        Log.d(TAG, "sync record start");
			updatedRecords = NetworkUtilities.syncRecords(account, authtoken, lastSyncMarker, dirtyRecords);
		} catch (final AuthenticatorException e) {
            Log.e(TAG, "AuthenticatorException", e);
            syncResult.stats.numParseExceptions++;
        } catch (final OperationCanceledException e) {
            Log.e(TAG, "OperationCanceledExcetpion", e);
        } catch (final IOException e) {
            Log.e(TAG, "IOException", e);
            syncResult.stats.numIoExceptions++;
        } catch (final AuthenticationException e) {
            Log.e(TAG, "AuthenticationException", e);
            syncResult.stats.numAuthExceptions++;
        } catch (final ParseException e) {
            Log.e(TAG, "ParseException", e);
            syncResult.stats.numParseExceptions++;
        } catch (final JSONException e) {
            Log.e(TAG, "JSONException", e);
            syncResult.stats.numParseExceptions++;
        }
	}
	
    /**
     * This helper function fetches the last known high-water-mark
     * we received from the server - or 0 if we've never synced.
     * @param account the account we're syncing
     * @return the change high-water-mark
     */
    private long getServerSyncMarker(Account account) {
        String markerString = mAccountManager.getUserData(account, SYNC_MARKER_KEY);
        if (!TextUtils.isEmpty(markerString)) {
            return Long.parseLong(markerString);
        }
        return 0;
    }

    /**
     * Save off the high-water-mark we receive back from the server.
     * @param account The account we're syncing
     * @param marker The high-water-mark we want to save.
     */
    private void setServerSyncMarker(Account account, long marker) {
        mAccountManager.setUserData(account, SYNC_MARKER_KEY, Long.toString(marker));
    }

}
