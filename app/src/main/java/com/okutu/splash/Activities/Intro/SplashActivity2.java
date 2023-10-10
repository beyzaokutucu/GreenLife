package com.okutu.splash.Activities.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.okutu.splash.Activities.Auth.LoginActivity;
import com.okutu.splash.R;

public class SplashActivity2 extends AppCompatActivity {

    Button btn_getstarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash2);
        btn_getstarted= findViewById(R.id.btn_login);
        btn_getstarted.setOnClickListener(view -> startActivity(new Intent( SplashActivity2.this, LoginActivity.class)));
    }
}

