package com.codezero.fireprevention;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by GyungDal on 2016-04-04.
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tsignup = (TextView)findViewById(R.id.signup);
        tsignup.setPaintFlags(tsignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tsignup.setOnClickListener(new Click());
    }
    public class Click implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.signup :
                    startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    break;
                default :
                    break;
            }
        }
    }
    public void launchsignup(){
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
