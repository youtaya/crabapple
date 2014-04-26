
package com.talk.demo.account;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static com.talk.demo.util.NetData.serverURL;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract.Constants;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.talk.demo.R;
import com.talk.demo.util.HttpRequest;
import com.talk.demo.util.HttpRequest.HttpRequestException;
import com.talk.demo.util.NetData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AccountAuthenticatorActivity {

    /** The Intent flag to confirm credentials. */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";
    /** The Intent extra to store password. */
    public static final String PARAM_PASSWORD = "password";
    /** The Intent extra to store username. */
    public static final String PARAM_USERNAME = "username";
    /** The Intent extra to store username. */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    
	private static final String TAG = "LoginActivity";
    private AccountManager mAccountManager;
    /** Keep track of the login task so can cancel it if requested */
    private UserLoginTask mAuthTask = null;
    /**
     * If set we are just checking that the user knows their credentials; this
     * doesn't cause the user's password or authToken to be changed on the
     * device.
     */
    private Boolean mConfirmCredentials = false;
    
	private AutoCompleteTextView loginText;
	private EditText passwordText;
	private Button loginButton;
	private String password;
	private String username;
	// ToDo: modify according our url
	private static String myUrl = serverURL+"account/login/";
    /** Was the original caller asking for an entirely new account? */
    protected boolean mRequestNewAccount = false;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAccountManager = AccountManager.get(this);
        Log.i(TAG, "loading data from Intent");
        final Intent intent = getIntent();
        username = intent.getStringExtra(PARAM_USERNAME);
        mRequestNewAccount = username == null;
        mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS, false);
        Log.i(TAG, "    request new: " + mRequestNewAccount);
		setContentView(R.layout.activity_login);

		loginText = (AutoCompleteTextView) findViewById(R.id.et_login);
		passwordText = (EditText) findViewById(R.id.et_password);

		loginButton = (Button) findViewById(R.id.bt_login);

		TextWatcher watcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable gitDirEditText) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		loginText.addTextChangedListener(watcher);
		passwordText.addTextChangedListener(watcher);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handleLogin();
			}
		});

		passwordText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event != null && ACTION_DOWN == event.getAction()
						&& keyCode == KEYCODE_ENTER && loginEnabled()) {
					handleLogin();
					return true;
				} else
					return false;
			}
		});

		passwordText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == IME_ACTION_DONE && loginEnabled()) {
					handleLogin();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Authenticate login & password
	 */
	public void handleLogin() {
		username = loginText.getText().toString();
		password = passwordText.getText().toString();
		mAuthTask = new UserLoginTask();
		mAuthTask.execute();
	}

	private boolean loginEnabled() {
		return !TextUtils.isEmpty(loginText.getText())
				&& !TextUtils.isEmpty(passwordText.getText());
	}

	private String authenticate() {
		String authToken = null;
		try {
			HttpURLConnection conn = HttpRequest.get(myUrl)
					.getConnection();

			String cookieHeader = conn.getHeaderFields().get("Set-Cookie")
					.get(0);

			Log.d(TAG, "cookie: " + cookieHeader);

			String csrfToken = cookieHeader.split(";")[0];
			Log.d(TAG, "csrf token : " + csrfToken);

			HttpRequest request = HttpRequest.post(myUrl);
			String name = username;
			String passwd = password;
			Map<String, String> data = new HashMap<String, String>();
			data.put("username", name);
			data.put("password", passwd);
			data.put("csrfmiddlewaretoken", csrfToken.substring(10));
			Log.d(TAG, "name: " + username + " passwd: " + password);
			// X-CSRFToken
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "text/html");
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
                
                for(String resItem : responseList) {
                	Log.d(TAG, "cookie session: " + resItem);
                    if(resItem.contains("sessionid")) {
                    	authToken = resItem.split(";")[0];
                        Log.d(TAG, "session :" + authToken);
                        NetData.setSessionId(authToken);
                    }
                    
                    if(resItem.contains("csrftoken")) {
                        String csrfToken2 = resItem.split(";")[0];
                        Log.d(TAG, "csrf token :" + csrfToken2);
                        NetData.setCsrfToken(csrfToken2);
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
            return authToken;
        } else {
            Log.e(TAG, "Error authenticating");
            return null;
        }
		
	}
    /**
     * Called when response is received from the server for confirm credentials
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller.
     *
     * @param result the confirmCredentials result.
     */
    private void finishConfirmCredentials(boolean result) {
        Log.i(TAG, "finishConfirmCredentials()");
        final Account account = new Account(username, AccountConstants.ACCOUNT_TYPE);
        mAccountManager.setPassword(account, password);
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. We store the
     * authToken that's returned from the server as the 'password' for this
     * account - so we're never storing the user's actual password locally.
     *
     * @param result the confirmCredentials result.
     */
    private void finishLogin(String authToken) {

        Log.i(TAG, "finishLogin()");
        final Account account = new Account(username, AccountConstants.ACCOUNT_TYPE);
        if (mRequestNewAccount) {
            mAccountManager.addAccountExplicitly(account, password, null);
            // Set contacts sync for this account.
            ContentResolver.setSyncAutomatically(account, AccountConstants.PROVIDER_AUTHORITY, true);
        } else {
            mAccountManager.setPassword(account, password);
        }
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountConstants.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
    /**
     * Called when the authentication process completes (see attemptLogin()).
     *
     * @param authToken the authentication token returned by the server, or NULL if
     *            authentication failed.
     */
    public void onAuthenticationResult(String authToken) {

        boolean success = ((authToken != null) && (authToken.length() > 0));
        Log.i(TAG, "onAuthenticationResult(" + success + ")");

        // Our task is complete, so clear it out
        mAuthTask = null;

        if (success) {
            if (!mConfirmCredentials) {
                finishLogin(authToken);
            } else {
                finishConfirmCredentials(success);
            }
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");
        }
    }

    public void onAuthenticationCancel() {
        Log.i(TAG, "onAuthenticationCancel()");
        // Our task is complete, so clear it out
        mAuthTask = null;
    }
    
	private class UserLoginTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			return authenticate();
		}
		
		@Override
		protected void onPostExecute(final String authToken) {
            // On a successful authentication, call back into the Activity to
            // communicate the authToken (or null for an error).
            onAuthenticationResult(authToken);
		}
        @Override
        protected void onCancelled() {
            // If the action was canceled (by the user clicking the cancel
            // button in the progress dialog), then call back into the
            // activity to let it know.
            onAuthenticationCancel();
		}
	}
}
