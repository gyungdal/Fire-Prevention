package com.codezero.fireprevention.background;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codezero.fireprevention.DB.DBConfig;
import com.codezero.fireprevention.DB.DBHelper;

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
    private Context context;
    private DBHelper database;
    private NoticeManager noticeManager;

    public getSensorData(Context context){
        this.context = context;
        database = new DBHelper(context, DBConfig.DB_NAME, null, 1);
        noticeManager = new NoticeManager(context);
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            while (true) {
                List<Integer> sensors = getAllProductKey();
                for(int sensor : sensors){
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

                    

                }
                TimeUnit.MINUTES.sleep(DBConfig.SLEEP_TIME);
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
}
