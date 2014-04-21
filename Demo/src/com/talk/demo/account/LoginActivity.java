
package com.talk.demo.account;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static com.talk.demo.util.NetData.serverURL;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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

import com.talk.demo.R;
import com.talk.demo.util.NetData;
import com.talk.demo.util.HttpRequest;
import com.talk.demo.util.HttpRequest.HttpRequestException;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private AutoCompleteTextView loginText;
	private EditText passwordText;
	private Button loginButton;
	private String password;
	private String username;
	// ToDo: modify according our url
	private static String myUrl = serverURL+"account/login/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		loginText = (AutoCompleteTextView) findViewById(R.id.et_login);
		passwordText = (EditText) findViewById(R.id.et_password);

		loginButton = (Button) findViewById(R.id.bt_login);

		TextWatcher watcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable gitDirEditText) {
				// updateEnablement();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
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

		new DownloadTask().execute(myUrl, username, password);
	}

	private boolean loginEnabled() {
		return !TextUtils.isEmpty(loginText.getText())
				&& !TextUtils.isEmpty(passwordText.getText());
	}

	private class DownloadTask extends AsyncTask<String, Long, Integer> {
		protected Integer doInBackground(String... urls) {
			Integer result = 403;
			try {
				HttpURLConnection conn = HttpRequest.get(urls[0])
						.getConnection();

				String cookieHeader = conn.getHeaderFields().get("Set-Cookie")
						.get(0);

				Log.d(TAG, "cookie: " + cookieHeader);

				String csrfToken = cookieHeader.split(";")[0];
				Log.d(TAG, "csrf token : " + csrfToken);

				HttpRequest request = HttpRequest.post(urls[0]);
				String name = urls[1];
				String passwd = urls[2];
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
                    result = sessionConnection.getResponseCode();
                    List<String> responseList = sessionConnection.getHeaderFields().get("Set-Cookie");
                    
                    for(String resItem : responseList) {
                    	Log.d(TAG, "cookie session: " + resItem);
                        if(resItem.contains("sessionid")) {
                            String sessionidString = resItem.split(";")[0];
                            Log.d(TAG, "session :" + sessionidString);
                            NetData.setSessionId(sessionidString);
                        }
                        
                        if(resItem.contains("csrftoken")) {
                            String csrfTokenString = resItem.split(";")[0];
                            Log.d(TAG, "csrf token :" + csrfTokenString);
                            NetData.setCsrfToken(csrfTokenString);
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
				Log.d(TAG, "result : " + result);
			} catch (HttpRequestException exception) {
				Log.d(TAG, "exception : " + exception.toString());
			}

			return result;
		}

		protected void onPostExecute(Integer var) {
			if (var != 302)
				Log.d(TAG, "login fail :" + var);
			else {
				Log.d(TAG, "login sucess :" + var);
				finish();
			}

		}
	}
}
