package com.okutu.splash.Activities.MainMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.okutu.splash.Activities.MainMenu.Message.MessageActivity;
import com.okutu.splash.Activities.MainMenu.Profile.AboutUsActivity;
import com.okutu.splash.Activities.MainMenu.Profile.ChangePasswordActivity;
import com.okutu.splash.Activities.MainMenu.Profile.ChangePhotoActivity;
import com.okutu.splash.Models.User;
import com.okutu.splash.R;
import com.squareup.picasso.Picasso;

public  class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Button editProfileButton;
    private Button settingsButton;
    private ImageView profileImageView;
    private Button aboutUsButton;
    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private static final int REQUEST_UPDATE_PHOTO = 1;


    @Override
    protected void onResume() {
        super.onResume();
        User.getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnSuccessListener(
                userSnapshot -> {
                    User user = userSnapshot.getValue(User.class);
                    if (user.getImage() != null && !user.getImage().isEmpty()) {
                        Picasso.get().load(user.getImage()).into(profileImageView);
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        profileImageView = findViewById(R.id.profileImageView);
        editProfileButton = findViewById(R.id.editProfileButton);
        settingsButton = findViewById(R.id.settingsButton);
        aboutUsButton = findViewById(R.id.aboutUsButton);

        editProfileButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        aboutUsButton.setOnClickListener(this);
        if (getIntent().hasExtra("updated_photo_uri")) {
            String updatedPhotoUriString = getIntent().getStringExtra("updated_photo_uri");
            Uri updatedPhotoUri = Uri.parse(updatedPhotoUriString);

            // Set the updated photo URI to the profileImageView
            profileImageView.setImageURI(updatedPhotoUri);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.home:
                            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.friend:
                            startActivity(new Intent(getApplicationContext(), FindFriendActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.chat:
                            startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.person:
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                });
            }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.editProfileButton:
                intent = new Intent(this, ChangePhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.settingsButton:
                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutUsButton:
                intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                break;
        }
    }

            public void logout(View view) {

                finishAffinity();

            }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE_PHOTO && resultCode == RESULT_OK && data != null) {
            // Get the updated photo URI from the intent
            String updatedPhotoUriString = data.getStringExtra("updated_photo_uri");
            Uri updatedPhotoUri = Uri.parse(updatedPhotoUriString);

            // Update the profile photo
            profileImageView.setImageURI(updatedPhotoUri);
        }
    }

        }