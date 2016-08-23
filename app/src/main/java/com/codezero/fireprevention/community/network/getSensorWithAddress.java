package com.codezero.fireprevention.community.network;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by GyungDal on 2016-08-23.
 */
public class getSensorWithAddress extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = getSensorWithAddress.class.getName();
    private static final String front = "http://59.26.68.181:8080/getSensorInfo.jsp?key=";
    private DBHelper database;
    private Context context;
    public getSensorWithAddress(Context context){
        this.context = context;
        database = new DBHelper(context, DBConfig.DB_NAME, null, 2);
    }

    /**
     *
     * @param [0]key [1]Name
     * @return
     */
    @Override
    protected Boolean doInBackground(String... params) {
        try{
            if(isAlready(Integer.valueOf(params[0]))){
               return false;
            }
            Document doc = Jsoup.connect(front + params[0]).get();
            if(doc.select(".latitude").get(0).text().equals("") ||
                    doc.select(".longitude").get(0).text().equals(""))
                return false;
            insert(Integer.valueOf(params[0]),params[1]
                    ,Double.valueOf(doc.select(".longitude").get(0).text())
                    ,Double.valueOf(doc.select(".latitude").get(0).text()));
            return true;
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    private boolean isAlready(int productKey) {
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while (c.moveToNext()) {
            if(c.getInt(c.getColumnIndex("productKey")) == productKey)
                return true;
        }
        return false;
    }

    public void insert(int key, String name, double lng, double lat){
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productKey", key);
        values.put("name", name);
        values.put("lng", lng);
        values.put("lat", lat);
        values.put("flag", 1);
        db.insert(DBConfig.TABLE_NAME, null, values);
    }
}
