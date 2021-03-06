package com.codezero.fireprevention.background;

/**
 * Created by GyungDal on 2016-07-20.
 */
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.codezero.fireprevention.activity.MainActivity;
import com.codezero.fireprevention.activity.UnSafeActivity;

public class StartReceiver extends BroadcastReceiver {
    private static final String TAG = "Background Work";
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, intent.getAction());
        //setup(context);
        //test(context);
        if(!isServiceRunning(context, "com.codezero.fireprevention.background")) {
            Intent i = new Intent("com.codezero.fireprevention.background");
            i.setPackage(context.getPackageName());
            context.startService(i);
        }
    }

    private Boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setup(Context context){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainIntent = new Intent(context, UnSafeActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 8, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setTicker("Fire Prevention")
            .setContentTitle("동기화 시작")
            .setContentText("동기화를 시작 합니다.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.notify(0, notification);

    }

}
