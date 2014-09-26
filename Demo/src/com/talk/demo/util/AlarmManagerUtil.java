
package com.talk.demo.util;

import android.R.integer;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.talk.demo.daily.UpdateReceiver;

public class AlarmManagerUtil {
    private static String TAG = "AlarmManagerUtil";
    
    public static AlarmManager getAlarmManager(Context ctx) {
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 指定时间后进行消息发送 注意: Receiver记得在manifest.xml中注册
     * 
     * @param ctx
     */
    public static void sendUpdateBroadcast(Context ctx, int milseconds) {
        Log.i(TAG, "send to start update broadcase,delay time :" + milseconds);

        AlarmManager am = getAlarmManager(ctx);
        // milseconds 后将产生广播,触发UpdateReceiver的执行,这个方法才是真正的更新数据的操作主要代码
        Intent mIntent = new Intent(ctx, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, mIntent, 0);
        am.set(AlarmManager.RTC, System.currentTimeMillis() + milseconds, pendingIntent);
    }

    /**
     * 取消定时执行(有如闹钟的取消)
     * 
     * @param ctx
     */
    public static void cancelUpdateBroadcast(Context ctx) {
        AlarmManager am = getAlarmManager(ctx);
        Intent mIntent = new Intent(ctx, UpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, mIntent, 0);
        am.cancel(pendingIntent);
    }
    
}
