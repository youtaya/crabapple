
package com.talk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;

import com.talk.demo.account.AppEnterActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {
    private static final long DELAY = 3000;
    private boolean scheduled = false;
    private Timer splashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanuch);

        splashTimer = new Timer();
        splashTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
            	SplashActivity.this.finish();
                startActivity(new Intent(SplashActivity.this, AppEnterActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        }, DELAY);
        scheduled = true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (scheduled)
            splashTimer.cancel();
        splashTimer.purge();
    }
    
	
    @Override
    public void onResume() {
    	super.onResume();
    	JPushInterface.onResume(this);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	JPushInterface.onPause(this);
    }
}
