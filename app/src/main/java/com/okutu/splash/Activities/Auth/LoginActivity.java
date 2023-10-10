package com.okutu.splash.Activities.Auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okutu.splash.R;
import com.okutu.splash.Activities.Intro.SplashActivity2;
import com.okutu.splash.Activities.Auth.Login.QuestionActivity1;

public class LoginActivity extends  AppCompatActivity {

 ImageButton btn_back;
 Button btn_giris;
 Button btn_register;

    private FirebaseUser mUser;
    private EditText email,password;
    private String txtemail, txtpassword;
    private FirebaseAuth mAauth;
    private Button loginButton;
    private DatabaseReference mReference;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email=(EditText)findViewById(R.id.Email);
        password=(EditText)findViewById(R.id.Password);
        mAauth=FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.btn_login);
        btn_back= findViewById(R.id.btn_geri);
        btn_register= findViewById(R.id.btn_giris);

        btn_back.setOnClickListener(view -> startActivity(new Intent( LoginActivity.this, SplashActivity2.class)));
        btn_register.setOnClickListener(view -> startActivity(new Intent( LoginActivity.this, RegisterActivity.class)));
        loginButton.setOnClickListener(view -> {
            if (TextUtils.isEmpty(email.getText().toString().trim()) || TextUtils.isEmpty(password.getText().toString().trim())) {
                Toast.makeText(LoginActivity.this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                GirisYap();
                startActivity(new Intent(LoginActivity.this, QuestionActivity1.class));
                finish();
            }
        });
    }
    public void GirisYap() {
        txtemail = email.getText().toString().trim();
        txtpassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpassword)) {
            Toast.makeText(LoginActivity.this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // E-posta adresinin doğru formatta olduğunu kontrol et
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtemail).matches()) {
            Toast.makeText(LoginActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        mAauth.signInWithEmailAndPassword(txtemail, txtpassword)
                .addOnSuccessListener(this, authResult -> {
                    // Başarılı giriş işlemi
                    mUser = mAauth.getCurrentUser();
                    mReference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
                    mReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snp : snapshot.getChildren()) {
                                System.out.println(snp.getKey() + "=" + snp.getValue());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(LoginActivity.this, QuestionActivity1.class));
                    finish();
                })
                .addOnFailureListener(this, e -> {
                    // Giriş işlemi başarısız
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}




