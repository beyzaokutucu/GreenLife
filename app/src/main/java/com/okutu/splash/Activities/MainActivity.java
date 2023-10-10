package com.okutu.splash.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.okutu.splash.Activities.Intro.SplashActivity1;
import com.okutu.splash.R;

public class MainActivity  extends AppCompatActivity {

    Button btn_splash1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acilis);
        new Handler().postDelayed(() -> {
            setContentView(R.layout.activity_main);

            btn_splash1= findViewById(R.id.btn_splash);
            btn_splash1.setOnClickListener(view -> startActivity(new Intent( MainActivity.this, SplashActivity1.class)));
        }, 4000);




    }
}