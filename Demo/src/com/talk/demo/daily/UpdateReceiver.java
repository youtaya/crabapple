package com.talk.demo.daily;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UpdateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
         Toast.makeText(context, "消息送达", Toast.LENGTH_LONG).show();

    }
}
