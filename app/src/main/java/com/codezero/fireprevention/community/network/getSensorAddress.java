package com.codezero.fireprevention.community.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.codezero.fireprevention.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by GyungDal on 2016-07-27.
 */
public class getSensorAddress extends AsyncTask<Double, Void, String> {
    private static final String TAG = getSensorAddress.class.getName();
    private static final String front = "https://apis.daum.net/local/geo/coord2addr?apikey=";
    private static final String lng = "&longitude=";
    private static final String lat = "&latitude=";
    private static final String input = "&inputCoordSystem=WGS84&appid=";
    private static final String output = "&output=json";
    private Context context;
    public getSensorAddress(Context context){
        this.context = context;
    }

    /**
     * Param : lat, lng
     */
    @Override
    protected String doInBackground(Double... params) {
        try {
            Document doc = Jsoup.connect(front + context.getString(R.string.DAUM_MAP_KEY)
                    + lat + params[0] + lng + params[1] + input + "com.codezero.fireprevention")
                    .get();
            String fullData = doc.toString();
            Log.i("get Address", fullData);
            String data = fullData.substring(fullData.indexOf("fullname=\"") + "fullname=\"".length());
            data = data.substring(0, data.indexOf("\""));
            return data;
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

}
