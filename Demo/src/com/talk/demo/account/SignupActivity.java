package com.talk.demo.account;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.talk.demo.R;
import com.talk.demo.util.NetworkUtilities;

public class SignupActivity extends Activity {
	private static String TAG = "SignupActivity";
	private EditText username;
	private AutoCompleteTextView email;
	private EditText password;
	private Button signupButton;
	
	private UserRegisterTask mRegisterTask = null;
	private String mUserName;
	private String mEmail;
	private String mPassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        username = (EditText)findViewById(R.id.et_username);
        email = (AutoCompleteTextView)findViewById(R.id.et_mail);
        password = (EditText)findViewById(R.id.et_password);
        signupButton = (Button)findViewById(R.id.bt_signup);
        
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
		
		username.addTextChangedListener(watcher);
		email.addTextChangedListener(watcher);
		password.addTextChangedListener(watcher);
		
		signupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				handleSignup();
			}
		});
    }
    
	/**
	 * Register with username, email and password
	 */
	public void handleSignup() {
		mUserName = username.getText().toString();
		mEmail = email.getText().toString();
		mPassword = password.getText().toString();
		mRegisterTask = new UserRegisterTask();
		mRegisterTask.execute();
	}
	
    public void onRegisterResult(String authToken) {
        boolean success = ((authToken != null) && (authToken.length() > 0));
        Log.i(TAG, "onRegisterResult(" + success + ")");
        finish();
    }
    public void onRegisterCancel() {
        Log.i(TAG, "onRegisterCancel()");
        // Our task is complete, so clear it out
        mRegisterTask = null;
    }
	private class UserRegisterTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			return NetworkUtilities.signup(mUserName, mEmail, mPassword);
		}
		
		@Override
		protected void onPostExecute(final String authToken) {
            // On a successful authentication, call back into the Activity to
            // communicate the authToken (or null for an error).
            onRegisterResult(authToken);
		}
        @Override
        protected void onCancelled() {
            // If the action was canceled (by the user clicking the cancel
            // button in the progress dialog), then call back into the
            // activity to let it know.
            onRegisterCancel();
		}
	}

}
