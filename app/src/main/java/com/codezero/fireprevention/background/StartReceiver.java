package com.codezero.fireprevention.background;

/**
 * Created by GyungDal on 2016-07-20.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.codezero.fireprevention.activity.MainActivity;
import com.codezero.fireprevention.activity.UnSafeActivity;
import com.codezero.fireprevention.community.network.getSensorData;
import com.codezero.fireprevention.database.DBConfig;

import java.util.Timer;
import java.util.TimerTask;

public class StartReceiver extends BroadcastReceiver {
    private static final String TAG = "Background Work";
    private TimerTask mTask;
    private Timer mTimer;
    private getSensorData thread;
    private static final int TIME = 1000 * 60 * 5;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, intent.getAction());
        setup(context);
        //test(context);
        DBConfig.isSafe = true;
        Log.i(TAG, "Start");
        thread = new getSensorData(context);

        mTask = new TimerTask() {
            @Override
            public void run() {
                Log.i("Start Receive", "Get Sensor Data");
                thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };
        mTimer = new Timer(true);
        mTimer.schedule(mTask, 0, TIME);
        Log.i(TAG, "Success");
        while(true);
    }

    private void setup(Context context){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 8, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setTicker("Fire Prevention")
                .setContentTitle("동기화 시작")
                .setContentText("5분 간격으로 동기화 합니다.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.notify(0, notification);

    }

    private void test(Context context){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainIntent = new Intent(context, UnSafeActivity.class);
        mainIntent.putExtra("name", "TEST");
        mainIntent.putExtra("lat", 36.392024979576206);
        mainIntent.putExtra("lng", 127.36304090241377);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 4, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setTicker("Fire Prevention")
                .setContentTitle("불이야!")
                .setContentText("으아아아아ㅏㅇ")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);

    }
}
