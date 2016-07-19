package com.codezero.fireprevention.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by GyungDal on 2016-07-18.
 */
public class getLocateInfo extends AsyncTask<String, Void, HashMap<String, Double>> {
    private static final String TAG = getLocateInfo.class.getName();
    private static final String front = "http://apis.daum.net/local/geo/addr2coord?apikey=" +
            "d82c75b3a6b33cfad136796fbe876e68a518b478&output=json&q=";
    //private final String temp = "d82c75b3a6b33cfad136796fbe876e68a518b478";
    private static final String HEADER_NAME_X_APPID = "x-appid";
    private static final String HEADER_NAME_X_PLATFORM = "x-platform";
    private static final String HEADER_VALUE_X_PLATFORM_ANDROID = "android";
    private static final String HEADER_NAME_USER_AGENT = "User-Agent";
    private static final String HEADER_VALUE_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
    private String appId;
    private Context context;
    protected static String temp;
    public getLocateInfo(Context context){
        this.context = context;
    }
    @Override
    protected HashMap<String, Double> doInBackground(String... urls) {
        HashMap<String, Double> result = null;
        try {
            if (context != null) {
                appId = context.getPackageName();
            }
            String json = fetchData(front + urls[0]);
            result = parse(json);
        }catch(Exception e){
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }
        return result;
    }

    @SuppressLint("JavascriptInterface")
    private String fetchData(String urlString) {
        try {
            WebView web = new WebView(context);
            web.loadUrl(urlString);
            web.setWebViewClient(new HelloWebViewClient());
            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            web.addJavascriptInterface(new MyJavaScriptInterface(),"HTMLOUT");
            //http://apis.daum.net/local/geo/addr2coord?apikey=d82c75b3a6b33cfad136796fbe876e68a518b478&output=json&q=%EA%B0%80%EC%A0%95%EB%B6%81%EB%A1%9C%2076


            /*Log.i(TAG, urlString);
            URL url = new URL(urlString);
            appId = context.getPackageName();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(4000);
            conn.setConnectTimeout(7000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty(HEADER_NAME_X_APPID, appId);
            conn.setRequestProperty(HEADER_NAME_X_PLATFORM, HEADER_VALUE_X_PLATFORM_ANDROID);
            //conn.setRequestProperty(HEADER_NAME_USER_AGENT, HEADER_VALUE_USER_AGENT);
            conn.connect();
            Log.i("Connect Code", "" + conn.getResponseCode());


            InputStream is = conn.getInputStream();
            @SuppressWarnings("resource")
            Scanner s = new Scanner(is);
            s.useDelimiter("\\A");
            String data = s.hasNext() ? s.next() : "";
            is.close();
            Log.i(TAG, data);
            conn.disconnect();*/
            while(temp.isEmpty() || temp.equals("")){
                //
            }
            return temp;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    private HashMap<String, Double> parse(String jsonString) {
        HashMap<String, Double> result = new HashMap<>();
        try {
            JSONObject reader = new JSONObject(jsonString);
            JSONObject channel = reader.getJSONObject("channel");
            if(channel.getInt("totalCount") > 1) {
                Log.i(TAG, "Too many total count value");
                return null;
            }
            JSONArray objects = channel.getJSONArray("item");
            JSONObject object = objects.getJSONObject(0);
            result.put("lat", object.getDouble("lat"));
            result.put("lng", object.getDouble("lng"));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    class MyJavaScriptInterface
    {
        @SuppressWarnings("unused")
        public void showHTML(String html)
        {
            temp = html;
        }
    }
}
