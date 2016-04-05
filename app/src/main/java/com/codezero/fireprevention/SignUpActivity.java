package com.codezero.fireprevention;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by GyungDal on 2016-04-04.
 */
public class SignUpActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new Click());
    }
    public class Click implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.cancel :
                    finish();
                    break;
                default :
                    break;
            }
        }
    }
}
