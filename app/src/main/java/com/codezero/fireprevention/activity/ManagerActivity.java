package com.codezero.fireprevention.activity;

/**
 * Created by GyungDal on 2016-07-26.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codezero.fireprevention.R;
import com.codezero.fireprevention.activity.listview.Item;
import com.codezero.fireprevention.activity.listview.ListViewAdapter;
import com.codezero.fireprevention.database.DBConfig;
import com.codezero.fireprevention.database.DBHelper;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by GyungDal on 2016-07-26.
 */
public class ManagerActivity extends AppCompatActivity {
    private static final String TAG = ManagerActivity.class.getName();
    private Toolbar toolbar;
    private ImageView statusImage;
    private TextView statusText, numberText;
    private ListView productList;
    private DBHelper database;
    private SQLiteDatabase db;
    private ListViewAdapter listviewAdapter;
    private static final int ENABLE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setToolbar();
        database = new DBHelper(ManagerActivity.this, DBConfig.DB_NAME, null, 2);
        productList = (ListView)findViewById(R.id.productList);
        statusImage = (ImageView)findViewById(R.id.statusImage);
        statusText = (TextView)findViewById(R.id.statusText);
        numberText = (TextView)findViewById(R.id.number);
        setStatus();
        listviewAdapter = new ListViewAdapter(this);
        HashMap<String, Boolean> Data = getListData();
        Iterator<String> itr = Data.keySet().iterator();
        while(itr.hasNext()) {
            String name = itr.next();
            Boolean state = Data.get(name);
            listviewAdapter.addItem(name, state);
        }
        productList.setAdapter(listviewAdapter);

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Item mData = listviewAdapter.items.get(position);
                Toast.makeText(getApplicationContext(), mData.getName(), Toast.LENGTH_SHORT).show();
            }
        });
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
}

