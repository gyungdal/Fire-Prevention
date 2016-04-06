package com.codezero.fireprevention;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by GyungDal on 2016-04-04.
 */
public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText email;
    private EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tsignup = (TextView)findViewById(R.id.signup);
        tsignup.setPaintFlags(tsignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tsignup.setOnClickListener(new Click());
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new Click());
        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);
    }
    public class Click implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.signup :
                    startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    break;
                case R.id.login :
                    if(email.getText().toString().equals("9274aa@gmail.com") &&
                            pass.getText().toString().equals("test"))
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    else
                        Snackbar.make(getCurrentFocus(), "로그인 실패", Snackbar.LENGTH_SHORT).show();
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
