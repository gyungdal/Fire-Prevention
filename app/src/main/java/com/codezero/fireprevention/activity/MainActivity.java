package com.codezero.fireprevention.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codezero.fireprevention.R;
import com.codezero.fireprevention.community.network.getSensorData;
import com.codezero.fireprevention.database.DBConfig;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private static final boolean DANGER = true;
    private static final boolean SAFE = false;
    public static boolean check; // true : danger
    private ImageView imageView;
    private TextView textView, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            getSensorData get = new getSensorData(getApplicationContext());
            get.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        status = getStatusTextView();
        imageView = (ImageView)findViewById(R.id.statusImage);
        textView = (TextView)findViewById(R.id.statusText);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        check = !check;
        setStatus();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public TextView getStatusTextView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        return (TextView)header.findViewById(R.id.status);

    }
    private void setStatus(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManagerActivity.class));
            }
        });
        if(DBConfig.isSafe){
            imageView.setImageResource(R.drawable.safe);
            textView.setText(getText(R.string.safe));
            status.setText("상태 : " + "안전");
        }else{
            imageView.setImageResource(R.drawable.unsafe);
            textView.setText(getText(R.string.unsafe));
            status.setText("상태 : " + (DBConfig.NotSafeNumber >= 2 ? "위험" : "주의"));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void click(View v){
        switch(v.getId()) {
            case R.id.insert:
                startActivity(new Intent(this, AddActivity.class));
                Log.i(TAG, "등록");
                break;
            case R.id.management:
                startActivity(new Intent(this, ManagerActivity.class));
                Log.i(TAG, "관리");
                break;
            case R.id.runWhenFire:
                startActivity(new Intent(this, RunWhenFireActivity.class));
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=K64g7dqK9FE")));
                Log.i(TAG, "대피요령");
                break;
            /*case R.id.SOS:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                String smsBody = "HELP!!!";
                sendIntent.putExtra("sms_body", smsBody); // 보낼 문자
                sendIntent.putExtra("address", "01074776900"); // 받는사람 번호
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
                Log.i(TAG, "긴급 SOS");
                break;*/
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*
    private void OpenCategroyDialogBox() {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promptView = layoutInflater.inflate(R.layout.addnewcategory, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setTitle("신고");
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
                    OpenCategroyDialogBox();
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
    }
*/
}
