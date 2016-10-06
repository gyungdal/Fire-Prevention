package com.codezero.fireprevention.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.codezero.fireprevention.activity.UnSafeActivity;

/**
 * Created by GyungDal on 2016-07-22.
 */
public class NoticeManager {
    private Context context;
    private String name;
    private double lat, lng;
    private static int i = 5632;
    public NoticeManager(Context context, String name, double lat, double lng){
        this.context = context;
        Log.i("NoticeManager", String.valueOf(context));
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public void show(String title, String text){
        try {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent mainIntent = new Intent(context, UnSafeActivity.class);
            mainIntent.putExtra("name", name);
            mainIntent.putExtra("lat", lat);
            mainIntent.putExtra("lng", lng);
            mainIntent.putExtra("extra", text);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, (i - (int) lat), mainIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setTicker("화재센서 알림")
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            notificationManager.notify((i - (int) lat), notification);
        }catch(Exception e){
            Log.e("NoticeManager", e.getMessage());

        }
    }
}
