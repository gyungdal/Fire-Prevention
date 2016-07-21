package com.codezero.fireprevention.network;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codezero.fireprevention.DB.DBConfig;
import com.codezero.fireprevention.DB.DBHelper;
import com.codezero.fireprevention.background.NoticeManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by GyungDal on 2016-07-20.
 */
public class getSensorData extends AsyncTask<Void, Void, Void>{
    private static final String SERVER_URL = "http://59.26.68.181:8080/getSensorInfo.jsp?key=";
    private static final int GET_ALL_DATA = 0;
    private static final int GET_ARDUINO_DATA = 1;
    private static final int GET_ANDROID_DATA = 2;
    private static final int GET_NULL = 3;

    private Context context;
    private DBHelper database;
    private NoticeManager noticeManager;
    private int getType;

    public getSensorData(Context context){
        this.context = context;
        database = new DBHelper(context, DBConfig.DB_NAME, null, 1);
        noticeManager = new NoticeManager(context);
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            while (true) {
                TimeUnit.MINUTES.sleep(DBConfig.SLEEP_TIME);

                //온라인이 아닐경우 그냥 대기
                if(!isOnline())
                    continue;

                List<Integer> sensors = getAllProductKey();
                for(int sensor : sensors){
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

                    if(lng.equals("null") || lat.equals("null")) {
                        Log.i(sensor + " Info", "Not init sensor from android");
                        getType = GET_ARDUINO_DATA;
                        setSensorData setSensorData = new setSensorData(context);
                        setSensorData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sensor);
                    }

                    if(smoke.equals("null") || temp.equals("null") || fire.equals("null")) {
                        Log.i(sensor + " Info", "Not init sensor from arduino");
                        getType = (getType == GET_ARDUINO_DATA) ? GET_NULL : GET_ANDROID_DATA;
                    }

                    switch(getType){
                        case GET_ANDROID_DATA :
                            noticeManager.show(sensor + "번 센서", "안드로이드측 값 설정 필요!!!");
                            break;

                        case GET_ARDUINO_DATA :
                            noticeManager.show(sensor + "번 센서", "아두이노측 업로드 데이터가 존재하지 않습니다");
                            break;

                        case GET_NULL :
                            noticeManager.show(sensor + "번 센서", "아무런 설정이 되어있지 않은 센서");
                            break;

                        default:
                            Log.i(sensor + "번 센서", "FINE");
                            break;
                    }

                }
            }
        }catch(InterruptedException ie){
            Log.e("Background Interrupt", ie.getMessage());
        }catch(Exception e){
            Log.e("Other Exception", e.getMessage());
        }
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

    private boolean isOnline(){
        ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING )
            return true;
        else if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED)
            return false;
        return false;
    }
}
