package com.codezero.fireprevention.network;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by GyungDal on 2016-07-18.
 */
public class getLocateInfo extends AsyncTask<String, Void, HashMap<String, String>> {
    private static final String TAG = getLocateInfo.class.getName();
    private static final String front = "http://apis.daum.net/local/geo/addr2coord?apikey=" +
            "d82c75b3a6b33cfad136796fbe876e68a518b478&output=xml&callback=pongSearch&q=";
    private static final String temp = "d82c75b3a6b33cfad136796fbe876e68a518b478";

    @Override
    protected HashMap<String, String> doInBackground(String... urls) {
        HashMap<String, String> result = new HashMap<>();
        try {
            String xml = downloadURL(front + urls[0]);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("lng")) {
                            Log.i(TAG, "lng : " + parser.getText());
                        }else if (parser.getName().equals("lat")) {
                            Log.i(TAG, "lat : " + parser.getText());
                        }
                    case XmlPullParser.TEXT:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return null;
    }

    private String downloadURL(String str) {
        String doc = "";
        try {
            URL url = new URL(str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    for (;;) {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        doc = doc + line + "\n";
                    }
                    br.close();
                }
                // conn.disconnect();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        doc = doc.substring("pongSearch(".length(), doc.length() -1);
        Log.i(TAG, doc);
        return doc;

    }
}
