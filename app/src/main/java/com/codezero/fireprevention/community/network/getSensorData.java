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

import com.codezero.fireprevention.activity.MainActivity;
import com.codezero.fireprevention.activity.UnSafeActivity;
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
public class getSensorData extends AsyncTask<Void, Void, Void> {
    private static final String SERVER_URL = "http://59.26.68.181:8080/getSensorInfo.jsp?key=";
    private static final int NORMAL = 0;
    private static final int FIRE_SMOKE_TEMP = 7;
    private static final int FIRE_SMOKE = 6;
    private static final int FIRE_TEMP = 5;
    private static final int FIRE = 3;
    private static final int SMOKE_TEMP = 4;
    private static final int SMOKE = 2;
    private static final int TEMP = 1;
    private static final int NEAR = 10;
    private boolean isSafe;
    private Context context;
    private DBHelper database;
    private boolean isNear;
    private static final long TIME = 1000;
    public getSensorData(Context context){
        this.context = context;
        database = new DBHelper(context, DBConfig.DB_NAME, null, 2);
    }

    @Override
    protected Void doInBackground(Void... params) {
        isSafe = DBConfig.isSafe = true;
        DBConfig.NotSafeNumber = 0;
        isNear = false;
        Log.i("Get Sensor Data Start", "YEAH");
        try {
            //온라인이 아닐경우 그냥 대기
            while (!isOnline()) {
                Thread.sleep(TIME);
            }

            List<Integer> sensors = getAllProductKey();
            for (int sensor : sensors) {
                Document doc = Jsoup.connect(SERVER_URL + sensor)
                        .get();

                //getData
                String lat = doc.select(".latitude").get(0).text();
                String lng = doc.select(".longitude").get(0).text();
                String state = doc.select(".state").get(0).text();
                String time = doc.select(".time").get(0).text();

                //Debug
                Log.i(sensor + " Data", "lat : " + lat);
                Log.i(sensor + " Data", "lng : " + lng);
                Log.i(sensor + " Data", "state : " + state);
                Log.i(sensor + " Data", "time : " + time);
                int result = Integer.valueOf(state);
                if(result > NEAR) {
                    isNear = true;
                    result = result - NEAR;
                }
                DBConfig.NotSafeNumber++;
                isSafe = DBConfig.isSafe = false;
                NoticeManager noticeManager =
                        new NoticeManager(context, getName(sensor)
                                , Double.valueOf(lat), Double.valueOf(lng));
                switch (result) {
                    case FIRE_SMOKE_TEMP:
                        noticeManager.show(sensor + "번 센서", (isNear ? "주변에 불이나고" : "") +
                                "불꽃, 연기, 온도 센서 작동");
                        break;
                    case FIRE_SMOKE:
                        //noticeManager.show(sensor + "번 센서", "연기가 나고 화재가 감지 되었습니다.");
                        noticeManager.show(sensor + "번 센서", (isNear ? "주변에 불이나고" : "") +
                                "불꽃, 연기 센서 작동");
                        break;
                    case FIRE_TEMP:
                        //noticeManager.show(sensor + "번 센서", "온도가 올라가고 화재가 감지 되었습니다.");
                        noticeManager.show(sensor + "번 센서", (isNear ? "주변에 불이나고" : "") +
                                "불꽃, 온도 센서 작동");
                        break;
                    case FIRE:
                        //noticeManager.show(sensor + "번 센서", "화재가 감지 되었습니다.");
                        noticeManager.show(sensor + "번 센서",(isNear ? "주변에 불이나고" : "") +
                                "불꽃 센서 작동");
                        break;
                    case SMOKE_TEMP:
                        //noticeManager.show(sensor + "번 센서", "연기와 온도가 올라갔습니다.");
                        noticeManager.show(sensor + "번 센서",(isNear ? "주변에 불이나고" : "") +
                                "연기, 온도 센서 작동");
                        break;
                    case SMOKE:
                        //noticeManager.show(sensor + "번 센서", "연기가 납니다.");
                        noticeManager.show(sensor + "번 센서", (isNear ? "주변에 불이나고" : "") +
                                "연기 센서 작동");
                        break;
                    case TEMP:
                        //noticeManager.show(sensor + "번 센서", "온도가 올라갔습니다.");
                        noticeManager.show(sensor + "번 센서", (isNear ? "주변에 불이나고" : "") +
                                "온도 센서 작동");
                        break;
                    case NORMAL :
                        if(isNear)
                            noticeManager.show(sensor + "번 센서", "주변에 불이났습니다.");
                        break;
                    default:
                        noticeManager.show(sensor + "번 센서",(isNear ? "주변에 불이나고" : "") +
                                "센서 오류");
                        Log.i("" + sensor, "" + result);
                        break;
                }
                Log.i(sensor + "번 센서", "FINE");
            }
        } catch (Exception e) {
            Log.e("Other Exception", e.getMessage());
        }
        if (isSafe)
            DBConfig.isSafe = true;
        Log.i("getSensorData", "Finish");
        return null;
    }

    private byte BoolToByte(boolean b){
        return (b ? (byte)1 : (byte)0);
    }
    private List<Integer> getAllProductKey() {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while (c.moveToNext()) {
            if(c.getInt(c.getColumnIndex("flag")) == 1)
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
    public Boolean isOnline() {
        Log.i("Online???", "check");
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
