package com.codezero.fireprevention.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codezero.fireprevention.R;
import com.codezero.fireprevention.community.network.getSensorAddress;
import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
/**
 * Created by GyungDal on 2016-04-21.
 */
public class UnSafeActivity extends AppCompatActivity implements
                            MapView.MapViewEventListener, MapView.POIItemEventListener,
                            MapView.OpenAPIKeyAuthenticationResultListener, View.OnClickListener{

    private static final String TAG = "UnSafe Acitivty";
    double lat, lng;
    private String address, name;
    private Toolbar toolbar;
    private Button unsafeButton;
    private TextView textView;
    private DBHelper database;
    private SQLiteDatabase db;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_unsafe);
        database = new DBHelper(UnSafeActivity.this, DBConfig.DB_NAME, null, 2);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setToolbar();
        Log.i(TAG, "Create activity unsafe");
        unsafeButton = (Button)findViewById(R.id.unSafeButton);
        textView = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        if(intent != null){
            lat = intent.getDoubleExtra("lat", -1);
            lng = intent.getDoubleExtra("lng", -1);
            name = intent.getStringExtra("name");
            if(lat == -1 || lng == -1){
                Toast.makeText(getApplicationContext(), "Intent 실패", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        textView.setText(name + "센서에서" + getText(R.string.unsafe));
        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey(this.getString(R.string.DAUM_MAP_KEY));
        mapView.zoomIn(true);
        mapView.zoomOut(true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this); //this에 MapView.MapViewEventListener 구현
        mapView.setPOIItemEventListener(this);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lat, lng), 0, true);

        MapPOIItem mSchool = new MapPOIItem();
        mSchool.setItemName(name);
        mSchool.setTag(0);
        mSchool.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
        mSchool.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        mSchool.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(mSchool);
        unsafeButton.setOnClickListener(this);
        getSensorAddress getAddress = new getSensorAddress(getApplicationContext());
        try {
            address = getAddress.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, lat, lng).get();
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String getName(int get){
        db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()){
            int key = c.getInt(c.getColumnIndex("productKey"));
            String name = c.getString(c.getColumnIndex("name"));
            if(key == get) {
                db.close();
                return name;
            }
        }
        db.close();
        return null;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }


    private void setToolbar(){
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Log.i(TAG,"Unsafe Activity Finish");
                }
            });
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Snackbar.make(getCurrentFocus(), "???", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG, "X : " + mapPoint.getMapPointScreenLocation().x + ", Y : " + mapPoint.getMapPointScreenLocation().y);
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG, "X : " + mapPoint.getMapPointScreenLocation().x + ", Y : " + mapPoint.getMapPointScreenLocation().y);
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.unSafeButton :
                Uri uri = Uri.parse("smsto:01074776900");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", address + "에서 도움을 요청합니다!!!");
                startActivity(it);
                break;
        }
    }
/*
    private void OpenCategoryDialogBox() {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promptView = layoutInflater.inflate(R.layout.addnewcategory, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setTitle("화재 신고");
        alert.setView(promptView);

        final EditText input = (EditText) promptView
                .findViewById(R.id.etCategory);

        input.requestFocus();
        input.setHint("내용을 입력해주세요.");
        input.setTextColor(Color.BLACK);

        alert.setPositiveButton("전송", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newCategoryName = input.getText().toString();

                // Do something with value!

                if (newCategoryName.equals("")) {
                    input.setError("Name Required");
                    OpenCategoryDialogBox();
                } else {
                    String value = input.getText().toString();
                    sendSMS("01074776900", value);


                }
            }

        });
        alert.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.

                        Toast.makeText(getApplicationContext(),
                                "Ok Clicked", Toast.LENGTH_SHORT).show();


                    }
                });

        // create an alert dialog
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void sendSMS(String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }*/


}
