package com.okutu.splash.Activities.Auth.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.okutu.splash.Activities.MainMenu.MainMenuActivity;
import com.okutu.splash.R;

public class QuestionActivity2 extends AppCompatActivity {

    private EditText editTextAim;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.soru_iki);
        super.onCreate(savedInstanceState);

        editTextAim = findViewById(R.id.editTextAim);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(v -> {
            String aim = editTextAim.getText().toString();
            Intent intent = new Intent(QuestionActivity2.this, MainMenuActivity.class);
            intent.putExtra("aim", aim);
            startActivity(intent);
        });
    }
}