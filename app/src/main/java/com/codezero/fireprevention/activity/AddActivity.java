package com.codezero.fireprevention.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codezero.fireprevention.R;
import com.codezero.fireprevention.community.network.getSensorWithAddress;

/**
 * Created by GyungDal on 2016-07-05.
 */
public class AddActivity extends AppCompatActivity {
    private static final String TAG = AddActivity.class.getName();

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private Toolbar toolbar;
    private EditText esearch;
    private Button bscan;
    private Button bsearch;
    private EditText searchName;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_add);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setToolbar();
        bscan = (Button)findViewById(R.id.QRCodeScan);
        esearch = (EditText)findViewById(R.id.SearchText);
        bsearch = (Button)findViewById(R.id.searchButton);
        searchName = (EditText)findViewById(R.id.SearchName);
        bscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this,FullScannerActivity.class));
            }
        });
        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = esearch.getText().toString();
                String name = searchName.getText().toString();
                new getSensorWithAddress(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, key, name);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
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

    public void launchFullActivity(View v) {
        launchActivity(FullScannerActivity.class);
    }


    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
