package com.codezero.fireprevention.community.network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;
import com.codezero.fireprevention.activity.MainActivity;

import org.jsoup.Jsoup;

import java.util.HashMap;

/**
 * Created by GyungDal on 2016-07-22.
 */
public class setSensorData extends AsyncTask<Integer, Void, Void>{
    private static final String front = "http://59.26.68.181:8080/AndroidSetSensorInfo.jsp?key=";
    private static final String mid = "&latitude=";
    private static final String end = "&longitude=";
    private DBHelper database;
    private Context context;

    public setSensorData(Context context){
        this.context = context;
        database = new DBHelper(context, DBConfig.DB_NAME, null, 2);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        try {
            HashMap<String, Double> data = getData(params[0]);
            Jsoup.connect(front + params[0] + mid + data.get("lat") + end + data.get("lng"))
                    .get();
            NotificationManager notificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(android.R.drawable.stat_notify_sync)
                    .setTicker("Alert")
                    .setContentTitle("Setting Sensor result")
                    .setContentText("Sensor Setting Success!!!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            notificationManager.notify(0, notification);
        }catch(Exception e){
            Log.e("Set Sensor Value", e.getMessage());
        }
        return null;
    }

    private HashMap<String, Double> getData(int findKey) {
        //select * from data where
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while (c.moveToNext()) {
            int key = c.getInt(c.getColumnIndex("productKey"));
            if (key == findKey) {
                HashMap<String, Double> result = new HashMap<>();
                double lat = c.getDouble(c.getColumnIndex("lat"));
                double lng = c.getDouble(c.getColumnIndex("lng"));
                result.put("lat", lat);
                result.put("lng", lng);
                return result;
            }
        }
        return null;
    }
}

