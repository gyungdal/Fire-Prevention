package com.codezero.fireprevention;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by GyungDal on 2016-07-05.
 */
public class EditMessageOnKeyListener implements View.OnKeyListener {
    private static final String TAG = EditMessageOnKeyListener.class.getName();
    private Context context;
    public EditMessageOnKeyListener(Context context){
        this.context = context;
    }
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER){
            Toast.makeText(context, "검색중", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "KeyEvent.ENTER");
            return true;
        }
        return false;
    }
}
