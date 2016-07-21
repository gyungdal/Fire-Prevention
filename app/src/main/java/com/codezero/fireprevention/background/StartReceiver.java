package com.codezero.fireprevention.background;

/**
 * Created by GyungDal on 2016-07-20.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.codezero.fireprevention.network.getSensorData;

public class StartReceiver extends BroadcastReceiver {
    private static final String TAG = "Background Work";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, intent.getAction());
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i(TAG, "Start");
            getSensorData thread = new getSensorData(context);
            thread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            Log.i(TAG, "Success");
        }
    }

}
