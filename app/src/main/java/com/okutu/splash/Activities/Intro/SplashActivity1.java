package com.okutu.splash.Activities.Intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.okutu.splash.R;

public class SplashActivity1 extends AppCompatActivity {

    private Button customerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        customerButton = findViewById(R.id.customerButton);
        customerButton.setBackgroundResource(R.drawable.button_shadow);
        customerButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                customerButton.setBackgroundColor(Color.parseColor("#7BC4B2"));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                customerButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            return false;
        });

        customerButton.setOnClickListener(v -> startActivity(new Intent(SplashActivity1.this, SplashActivity2.class)));
    }
}

