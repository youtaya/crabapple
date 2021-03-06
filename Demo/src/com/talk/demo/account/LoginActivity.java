
package com.talk.demo.account;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.talk.demo.MainActivity;
import com.talk.demo.R;
import com.talk.demo.setting.LuckDayActivity;
import com.talk.demo.util.NetworkUtilities;

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
        
        /*
         * if date has set, go to main activity
         * else go to set lucky day.
         */
        SharedPreferences sPreferences = getSharedPreferences("luck_day", Context.MODE_PRIVATE);
        int setMonth = sPreferences.getInt("Month", 0);
        int setDay = sPreferences.getInt("Day", 0);
        Log.d(TAG, "month is : "+setMonth+ "day is : "+setDay);
        if(setMonth > 0 || setDay > 0) {
            Intent mIntent = new Intent();
            mIntent.setClass(this, MainActivity.class);
            startActivity(mIntent);
            finish();
        } else {
	        Intent mIntent = new Intent();
	        mIntent.setClass(this, LuckDayActivity.class);
	        startActivity(mIntent);
	        finish();
        }
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
        
		// auth done, go to set lucky day.
        Intent mIntent = new Intent();
        mIntent.setClass(this, LuckDayActivity.class);
        startActivity(mIntent);
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
			return NetworkUtilities.authenticate(username, password);
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
	
	@Override
	public void onResume() {
		super.onResume();
	}
}
