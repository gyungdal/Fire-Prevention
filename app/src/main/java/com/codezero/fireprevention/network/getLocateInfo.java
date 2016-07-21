package com.codezero.fireprevention.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.google.gson.JsonParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Created by GyungDal on 2016-07-18.
 */
public class getLocateInfo extends AsyncTask<String, Void, HashMap<String, Double>> {
    private static final String TAG = getLocateInfo.class.getName();
    private static final String front = "http://maps.google.com/maps/api/geocode/xml?address=";
    private static final String end = "&sensor=false";
    private String appId;
    private Context context;
    public getLocateInfo(Context context){
        this.context = context;
    }
    @Override
    protected HashMap<String, Double> doInBackground(String... urls) {
        HashMap<String, Double> result = null;
        try {
            appId = context.getPackageName();
            Log.i(TAG, appId);
            result = parse(fetchData(front + urls[0] + end));
        }catch(Exception e){
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }
        return result;
    }
    private String fetchData(String urlString) {
        try {

            urlString = urlString.replaceAll(" ", "%20");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(4000);
            conn.setConnectTimeout(7000);
            conn.setRequestMethod("GET");
            conn.connect();
            Log.i(TAG, "" + conn.getResponseCode());
            InputStream is = conn.getInputStream();
            Scanner s = new Scanner(is);
            s.useDelimiter("\\A");
            String data = s.hasNext() ? s.next() : "";
            is.close();
            Log.i(TAG, data);
            return data;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    private HashMap<String, Double> parse(String input) {
        HashMap<String, Double> result = new HashMap<>();
        try{
            String temp = input.substring(input.indexOf("<location>") + "<location>".length(), input.indexOf("</location>")).trim();
            Double lng = Double.valueOf(temp.substring(temp.indexOf("<lng>") + "<lng>".length(), temp.indexOf("</lng>")));
            Double lat = Double.valueOf(temp.substring(temp.indexOf("<lat>") + "<lat>".length(), temp.indexOf("</lat>")));
            result.put("lat", lat);
            result.put("lng", lng);
            Log.i(TAG, "" + result.get("lat"));
            Log.i(TAG, "" + result.get("lng"));
        } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return result;
    }

}
