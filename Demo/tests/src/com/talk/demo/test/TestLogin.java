package com.talk.demo.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.talk.demo.R;
import com.talk.demo.account.LoginActivity;

public class TestLogin extends ActivityInstrumentationTestCase2<LoginActivity> {
    private LoginActivity activity;
    private AutoCompleteTextView loginText;
    private EditText passwordText;
    private Button loginButton;
    public TestLogin(Class<LoginActivity> activityClass) {
        super(activityClass);
    }
    
    public TestLogin(String name) { 
          super(LoginActivity.class);   
          setName(name);   
    }
    
    @Override
    protected void setUp() throws Exception {
      super.setUp();
      activity = this.getActivity();
      loginText = (AutoCompleteTextView)activity.findViewById(R.id.et_login);
      passwordText = (EditText)activity.findViewById(R.id.et_password);
      loginButton = (Button)activity.findViewById(R.id.bt_login);
    }
    
    @UiThreadTest  
    public void testInputLogin() {  
        loginText.setText("2");  
        passwordText.setText("3");  
        //模拟点击按钮  
        loginButton.performClick();  
    }  
    
    @Override  
    protected void tearDown() throws Exception {  
        super.tearDown();  
    }
}
