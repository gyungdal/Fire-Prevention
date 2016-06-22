package com.codezero.fireprevention.network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GyungDal on 2016-06-13.
 */
public class getData extends AsyncTask<String, Integer, Map<String, String>> {
    private static final String TAG = getData.class.getName();
    //latitude : 위도
    //longitude : 경도
    @Override
    protected Map<String, String> doInBackground(String... params) {
        HashMap<String, String> result = new HashMap<>();
        try{
            String udata = getDataFromURL(params[0]);
            JSONObject jsonObject = new JSONObject(udata);
            result.put("id", jsonObject.getString("id"));
            result.put("latitude", jsonObject.getString("latitude"));
            result.put("longitude", jsonObject.getString("longitude"));
            result.put("isFire", String.valueOf(jsonObject.getBoolean("isFire")));
            result.put("isSmoke", String.valueOf(jsonObject.getBoolean("isSmoke")));
            result.put("temp", String.valueOf(jsonObject.getDouble("temp")));
        }catch(JSONException e){
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    public String getDataFromURL(String Url){
        BufferedReader bufreader = null;
        HttpURLConnection urlConnection = null;
        StringBuffer page = new StringBuffer(); //읽어온 데이터를 저장할 StringBuffer객체 생성
        try {
            URL url= new URL(Url);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

            bufreader = new BufferedReader(new InputStreamReader(contentStream,"UTF-8"));
            String line = null;

            //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
            while((line = bufreader.readLine())!=null){
                Log.d("line:",line);
                page.append(line);
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }finally{
            //자원해제
            try {
                bufreader.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return page.toString();
    }// getStringFromUrl()----
}
