package com.okutu.splash.Activities.Auth;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.okutu.splash.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ImageButton btn_back;
    Button btn_regis;
   private EditText email,password,name;
    private String txtEmail, txtPassword,txtName;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mReference;
    private HashMap <String,Object> mData;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("...Loading");

        email=(EditText)findViewById(R.id.Email);
        password=(EditText)findViewById(R.id.Password);
        name=(EditText)findViewById(R.id.Name);
        firebaseAuth =FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();
        btn_back= findViewById(R.id.btn_geri2);
        btn_regis= findViewById(R.id.btn_register);
        btn_back.setOnClickListener(view -> {
            progressDialog.show();

            startActivity(new Intent( RegisterActivity.this, LoginActivity.class));
        });
        btn_regis.setOnClickListener(view -> kayitol());
    }


        public void kayitol(){

            txtEmail=email.getText().toString();
            txtPassword=password.getText().toString();
            txtName=name.getText().toString();

            if(!TextUtils.isEmpty(txtName)&&!TextUtils.isEmpty(txtEmail)&&!TextUtils.isEmpty(txtPassword)){
                firebaseAuth.createUserWithEmailAndPassword(txtEmail,txtPassword)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                firebaseUser = firebaseAuth.getCurrentUser();

                                mData = new HashMap<>();
                                mData.put("userName", txtName);
                                mData.put("userEmail", txtEmail);
                                mData.put("userPassword", txtPassword);
                                mData.put("userId", firebaseUser.getUid());
                                mData.put("matchedUserId", "");
                                mData.put("chatId", "");

                                mReference.child("Users").child(firebaseUser.getUid()).setValue(mData)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();

                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed to save data to database.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }else
            Toast.makeText(RegisterActivity.this, "Email and Password cannot be empty ", Toast.LENGTH_SHORT).show();
        }
}
