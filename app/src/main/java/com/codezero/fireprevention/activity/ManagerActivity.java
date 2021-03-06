package com.codezero.fireprevention.activity;

/**
 * Created by GyungDal on 2016-07-26.
 */

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codezero.fireprevention.R;
import com.codezero.fireprevention.activity.listview.Item;
import com.codezero.fireprevention.activity.listview.ListViewAdapter;
import com.codezero.fireprevention.community.network.getSensorData;
import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by GyungDal on 2016-07-26.
 */
public class ManagerActivity extends AppCompatActivity {
    private static final String TAG = ManagerActivity.class.getName();
    private static final int ENABLE = 1;
    private static final int ALL_DELETE = 1;
    private static final int SELECT_DELETE = 2;
    private static final int ALL_ALARM_STOP = 3;

    private Toolbar toolbar;
    private TextView numberText;
    private ListView productList;
    private DBHelper database;
    private SQLiteDatabase db;
    private ListViewAdapter listviewAdapter;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_manager);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setToolbar();
        database = new DBHelper(ManagerActivity.this, DBConfig.DB_NAME, null, 2);
        productList = (ListView)findViewById(R.id.productList);
        numberText = (TextView)findViewById(R.id.number);
        setStatus();
        listviewAdapter = new ListViewAdapter(this);
        HashMap<String, Boolean> Data = getListData();
        Iterator<String> itr = Data.keySet().iterator();
        while(itr.hasNext()) {
            String name = itr.next();
            boolean state = Data.get(name);
            listviewAdapter.addItem(name, state);
        }
        productList.setAdapter(listviewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ALL_DELETE, Menu.NONE, "전체 제품 삭제");
        menu.add(0, SELECT_DELETE, Menu.NONE, "선택 제품 삭제");
        menu.add(0, ALL_ALARM_STOP, Menu.NONE, "전체 알람 OFF");
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ALL_DELETE :
                AllDelete();
                Toast.makeText(getApplicationContext(), "전체 삭제 완료", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case SELECT_DELETE :
                selectDelete();
                Toast.makeText(getApplicationContext(), "선택 삭제 완료", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case ALL_ALARM_STOP :
                setAllState();
                Toast.makeText(getApplicationContext(), "전체 알람 OFF 완료", Toast.LENGTH_SHORT).show();
                finish();
                break;

            default :
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectDelete(){
        for(int i = 0;i < listviewAdapter.getCount();i++){
            if(listviewAdapter.getItem(i).getIsdel())
                delete(listviewAdapter.getItem(i).getName());
        }
    }

    private void setState(String name, boolean state){
        Log.i(TAG, name + ", State : " + state);
        int key = 0;
        db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex("name")).equals(name)) {
                key = c.getInt(c.getColumnIndex("productKey"));
                break;
            }
        }
        db.close();
        db = database.getWritableDatabase();
        db.execSQL("update " + DBConfig.TABLE_NAME + " set flag = "
                + (state ? 1 : 0) + " where productKey = " + key + ";");
        db.close();
        new getSensorData(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        numberText.setText("감지 중 : " + getNumber());
    }


    private void setAllState(){
        db = database.getWritableDatabase();
        db.execSQL("update " + DBConfig.TABLE_NAME + " set flag = 0");
        db.close();
        new getSensorData(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        numberText.setText("감지 중 : " + getNumber());
        ManagerActivity.this.finish();
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
        numberText.setText("감지 중 : " + getNumber());
    }

    public int getNumber(){
        int result = 0;
        db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()){
            int key = c.getInt(c.getColumnIndex("productKey"));
            String name = c.getString(c.getColumnIndex("name"));
            double lat  = c.getDouble(c.getColumnIndex("lat"));
            double lng = c.getDouble(c.getColumnIndex("lng"));
            int flag = c.getInt(c.getColumnIndex("flag"));
            Log.i(TAG, "---- DB DATA ----");
            Log.i(TAG, "key : " + key);
            Log.i(TAG, "name : " + name);
            Log.i(TAG, "lat : " + lat);
            Log.i(TAG, "lng : " + lng);
            Log.i(TAG, "flag : " + flag);
            if(flag == ENABLE)
                result++;
        }
        db.close();
        return result;
    }

    public HashMap<String, Boolean> getListData(){
        HashMap<String, Boolean> result = new HashMap<>();
        db = database.getReadableDatabase();
        Cursor c = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()){
            int key = c.getInt(c.getColumnIndex("productKey"));
            String name = c.getString(c.getColumnIndex("name"));
            double lat  = c.getDouble(c.getColumnIndex("lat"));
            double lng = c.getDouble(c.getColumnIndex("lng"));
            int flag = c.getInt(c.getColumnIndex("flag"));
            Log.i(TAG, "---- DB DATA ----");
            Log.i(TAG, "key : " + key);
            Log.i(TAG, "name : " + name);
            Log.i(TAG, "lat : " + lat);
            Log.i(TAG, "lng : " + lng);
            Log.i(TAG, "flag : " + flag);
            if(flag == ENABLE)
                result.put(name, true);
            else
                result.put(name, false);
        }
        db.close();
        return result;
    }


    private void delete(String name){
        db = database.getWritableDatabase();
        db.execSQL("delete from " + DBConfig.TABLE_NAME + " where name = \"" + name + "\";");
        db.close();
    }

    private void AllDelete(){
        db = database.getWritableDatabase();
        db.execSQL("delete from " + DBConfig.TABLE_NAME);
        db.close();
    }

    @Override
    public void onResume(){
        super.onResume();
        setStatus();
    }
}

