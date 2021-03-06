package com.codezero.fireprevention.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codezero.fireprevention.community.network.getSensorData;
import com.codezero.fireprevention.database.DBConfig;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by GyungDal on 2016-08-16.
 */
public class ReceiveService extends Service {
    private static final String TAG = ReceiveService.class.getName();
    private static final long TIME = 2000;
    private getSensorData get;
    private int count, count2;
    private Timer timer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId){
        Log.i(TAG, "Start Service");
        super.onStart(intent, startId);
        get = new getSensorData(getApplicationContext());

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                 System.gc();
                Log.i(TAG, get.getStatus().name());
                if(get.getStatus() == AsyncTask.Status.FINISHED)
                    get = new getSensorData(getApplicationContext());
                if(get.getStatus() == AsyncTask.Status.PENDING) {
                    if (count2++ > 5) {
                        get = new getSensorData(getApplicationContext());
                        count2 = 0;
                    }
                    get.execute();
                }
                if(get.getStatus() == AsyncTask.Status.RUNNING) {
                    if(count++ > 5) {
                        count = 0;
                        get.cancel(true);
                        get = new getSensorData(getApplicationContext());
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, TIME, TIME);
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "Service destroy!!!");
        timer.cancel();
        super.onDestroy();
    }
}
