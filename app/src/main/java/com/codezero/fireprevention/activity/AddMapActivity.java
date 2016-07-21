package com.codezero.fireprevention.activity;

/**
 * Created by GyungDal on 2016-07-18.
 */

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codezero.fireprevention.DB.DBConfig;
import com.codezero.fireprevention.DB.DBHelper;
import com.codezero.fireprevention.R;
import com.codezero.fireprevention.network.getLocateInfo;
import com.codezero.fireprevention.network.setSensorData;

import java.util.HashMap;

/**
 * Created by GyungDal on 2016-07-05.
 */
public class AddMapActivity extends AppCompatActivity {
    private static final String TAG = AddActivity.class.getName();
    private Toolbar toolbar;
    private int key;
    private EditText productName, productLocate;
    private Button productSet;
    private DBHelper database;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_map);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        productLocate = (EditText)findViewById(R.id.productLocate);
        productName = (EditText)findViewById(R.id.productName);
        productSet = (Button)findViewById(R.id.set);
        setToolbar();
        database = new DBHelper(AddMapActivity.this, DBConfig.DB_NAME, null, 1);
        //현재 화면 context, 파일 명, 커서 팩토리, 버전 번호
        Intent intent = getIntent();
        if(intent != null){
            key = intent.getIntExtra("productKey", -1);
            Toast.makeText(getApplicationContext(), "Product Key is " + key, Toast.LENGTH_SHORT).show();
            if(key == -1){
                Toast.makeText(getApplicationContext(), "Intent 실패", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        productSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getLocateInfo getLocateInfo = new getLocateInfo(getApplicationContext());
                    String Name = productName.getText().toString();
                    if(Name.equals("") || Name.isEmpty()){
                        Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ActivityCompat.requestPermissions(AddMapActivity.this,
                            new String[]{Manifest.permission.INTERNET}, 2);
                    HashMap<String, Double> Locate =
                            getLocateInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR
                                    ,productLocate.getText().toString()).get();
                    if(Locate != null) {
                        double lng = Locate.get("lng");
                        double lat = Locate.get("lat");
                        insert(key, Name, lng, lat);
                    }else
                        Toast.makeText(getApplicationContext(), "주소를 자세히 입력해주세요", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public void select(){
        db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()){
            int key = c.getInt(c.getColumnIndex("productKey"));
            String name = c.getString(c.getColumnIndex("name"));
            double lat  = c.getDouble(c.getColumnIndex("lat"));
            double lng = c.getDouble(c.getColumnIndex("lng"));
            Log.i(TAG, "---- DB DATA ----");
            Log.i(TAG, "key : " + key);
            Log.i(TAG, "name : " + name);
            Log.i(TAG, "lat : " + lat);
            Log.i(TAG, "lng : " + lng);
        }
    }

    public void insert(int key, String name, double lng, double lat){
        db = database.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("productKey", key);
        values.put("name", name);
        values.put("lng", lng);
        values.put("lat", lat);
        db.insert(DBConfig.TABLE_NAME, null, values);
        select();
        setSensorData setSensorData = new setSensorData(getApplicationContext());
        setSensorData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, key);
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
                    Log.i(TAG,"Add Activity Finish");
                }
            });
        }
    }

}
