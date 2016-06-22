package com.codezero.fireprevention.activity;

/**
 * Created by GyungDal on 2016-06-20.
 */
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codezero.fireprevention.R;

/**
 * Created by GyungDal on 2016-03-31.
 */
public class EnrollmentActivity extends AppCompatActivity{

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.enrollment_activity);
        setupToolbar();
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}