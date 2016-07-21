package com.codezero.fireprevention.network;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.codezero.fireprevention.DB.DBConfig;
import com.codezero.fireprevention.DB.DBHelper;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
        database = new DBHelper(context, DBConfig.DB_NAME, null, 1);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        try {
            HashMap<String, Double> data = getData(params[0]);
            URL url = new URL(front + params[0] + mid + data.get("lat") + end + data.get("lng"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(4000);
            conn.setConnectTimeout(7000);
            conn.setRequestMethod("GET");
            conn.connect();
            if(conn.getResponseCode()!=HttpURLConnection.HTTP_OK)
                Log.i("Set Sensor Value", "Http Code is..." + conn.getResponseCode());
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

