package com.codezero.fireprevention.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codezero.fireprevention.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private static final boolean DANGER = true;
    private static final boolean SAFE = false;
    public static boolean check; // true : danger
    private ImageView imageView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    public void setStatus(){
        if(check){
            imageView.setImageResource(R.drawable.unsafe);
            textView.setText(getText(R.string.unsafe));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, UnSafeActivity.class));
                    Toast.makeText(getApplicationContext(), "클릭", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            imageView.setImageResource(R.drawable.safe);
            textView.setText(getText(R.string.safe));
            imageView.setOnClickListener(null);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void click(View v){
        switch(v.getId()) {
            case R.id.alarm:
                Log.i(TAG, "alarm");
                break;
            case R.id.status:
                Log.i(TAG, "status");
                break;
            case R.id.home:
                Toast.makeText(getApplicationContext(), "이미 홈입니다.", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "HOME");
                break;
            case R.id.insert:
                startActivity(new Intent(this, AddActivity.class));
                Log.i(TAG, "등록");
                break;
            case R.id.management:
                Log.i(TAG, "관리");
                break;
            case R.id.runWhenFire:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=K64g7dqK9FE")));
                Log.i(TAG, "대피요령");
                break;
            case R.id.SOS:
                Log.i(TAG, "긴급 SOS");
                break;
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


}
