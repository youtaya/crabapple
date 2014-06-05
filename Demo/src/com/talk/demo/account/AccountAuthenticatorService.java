package com.talk.demo.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountAuthenticatorService extends Service {

    private AccountAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        mAuthenticator = new AccountAuthenticator(this);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}
