package com.okutu.splash.Activities.Auth.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.okutu.splash.R;

public class QuestionActivity1 extends  AppCompatActivity{
 Button btn_ileri;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soru_bir);
        btn_ileri= findViewById(R.id.btn_next);
        btn_ileri.setOnClickListener(view -> startActivity(new Intent( QuestionActivity1.this, QuestionActivity2.class)));


    }}
