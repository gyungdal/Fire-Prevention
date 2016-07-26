package com.codezero.fireprevention.activity;

/**
 * Created by GyungDal on 2016-07-26.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codezero.fireprevention.R;
import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;

/**
 * Created by GyungDal on 2016-07-26.
 */
public class ManagerActivity extends AppCompatActivity {
    private static final String TAG = ManagerActivity.class.getName();
    private Toolbar toolbar;
    private ImageView statusImage;
    private TextView statusText, numberText;
    private DBHelper database;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setToolbar();
        database = new DBHelper(ManagerActivity.this, DBConfig.DB_NAME, null, 1);

        statusImage = (ImageView)findViewById(R.id.statusImage);
        statusText = (TextView)findViewById(R.id.statusText);
        numberText = (TextView)findViewById(R.id.number);
        setStatus();
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

    private void setStatus(){
        if(DBConfig.isSafe){
            statusImage.setImageResource(R.drawable.safe);
            statusText.setText("상태 : " + "안전");
        }else{
            statusImage.setImageResource(R.drawable.unsafe);
            statusText.setText("상태 : " + "위험");
        }
        numberText.setText("감지 중 : " + select());

    }

    public int select(){
        int result = 0;
        db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()){
            result++;
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
        db.close();
        return result;
    }
}

