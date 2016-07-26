package com.codezero.fireprevention.community.network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.codezero.fireprevention.activity.MainActivity;
import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;
import com.codezero.fireprevention.background.NoticeManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GyungDal on 2016-07-20.
 */
public class getSensorData extends AsyncTask<Void, Void, Void>{
    private static final String SERVER_URL = "http://59.26.68.181:8080/getSensorInfo.jsp?key=";
    private static final int GET_ALL_DATA = 0;
    private static final int GET_ARDUINO_DATA = 1;
    private static final int GET_ANDROID_DATA = 2;
    private static final int GET_NULL = 3;
    private boolean isSafe;
    private Context context;
    private DBHelper database;
    private int getType;

    public getSensorData(Context context){
        this.context = context;
        database = new DBHelper(context, DBConfig.DB_NAME, null, 1);
    }

    @Override
    protected Void doInBackground(Void... params) {
        isSafe = true;
        Log.i("Get Sensor Data Start", "YEAH");
        try {
            //온라인이 아닐경우 그냥 대기
            while(!isOnline());

            List<Integer> sensors = getAllProductKey();
            for(int sensor : sensors) {
                getType = GET_ALL_DATA;
                Document doc = Jsoup.connect(SERVER_URL + sensor)
                        .get();

                //getData
                String lat = doc.select(".latitude").get(0).text();
                String lng = doc.select(".longitude").get(0).text();
                String smoke = doc.select(".smoke").get(0).text();
                String temp = doc.select(".temp").get(0).text();
                String fire = doc.select(".fire").get(0).text();
                String time = doc.select(".time").get(0).text();

                //Debug
                Log.i(sensor + " Data", "lat : " + lat);
                Log.i(sensor + " Data", "lng : " + lng);
                Log.i(sensor + " Data", "smoke : " + smoke);
                Log.i(sensor + " Data", "temp : " + temp);
                Log.i(sensor + " Data", "fire : " + fire);
                Log.i(sensor + " Data", "time : " + time);

                if (lng.equals("null") || lat.equals("null")) {
                    Log.i(sensor + " Info", "Not init sensor from android");
                    getType = GET_ARDUINO_DATA;
                }

                if (smoke.equals("null") || temp.equals("null") || fire.equals("null")) {
                    Log.i(sensor + " Info", "Not init sensor from arduino");
                    getType = (getType == GET_ARDUINO_DATA) ? GET_NULL : GET_ANDROID_DATA;
                }

                switch (getType) {
                    case GET_ANDROID_DATA:
                        alert(sensor + "번 센서", "안드로이드측 값 설정 필요!!!");

                        break;

                    case GET_ARDUINO_DATA:
                        alert(sensor + "번 센서", "아두이노측 업로드 데이터가 존재하지 않습니다");
                        break;

                    case GET_NULL:
                        alert(sensor + "번 센서", "아무런 설정이 되어있지 않은 센서");
                        break;
                    default: {
                        if (Double.valueOf(fire) > 60) {
                            isSafe = DBConfig.isSafe = false;
                            NoticeManager noticeManager =
                                    new NoticeManager(context, getName(sensor)
                                            , Double.valueOf(lat), Double.valueOf(lng));
                            noticeManager.show(sensor + "번 센서", "경보");
                        }
                        Log.i(sensor + "번 센서", "FINE");
                        break;
                    }
                }

            }
        } catch(Exception e){
            Log.e("Other Exception", e.getMessage());
        }
        if(isSafe)
            DBConfig.isSafe = true;
        return null;
    }

    private List<Integer> getAllProductKey() {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        int i = 0;
        while (c.moveToNext()) {
            result.add(c.getInt(c.getColumnIndex("productKey")));
        }
        return result;
    }

    private String getName(int productKey) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while (c.moveToNext()) {
            if(c.getInt(c.getColumnIndex("productKey")) == productKey);
                return c.getString(c.getColumnIndex("name"));
        }
        return null;
    }
    private boolean isOnline(){
        Log.i("GET info", "Is Online???");
        ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING )
            return true;
        else if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED)
            return false;
        return false;
    }

    private void alert(String title, String text){
        Log.i("Fire Alert", title);
        Log.i("Fire Alert", text);
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setTicker("Alert")
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }
}
