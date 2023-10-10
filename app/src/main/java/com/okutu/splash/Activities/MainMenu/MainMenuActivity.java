package com.okutu.splash.Activities.MainMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.okutu.splash.Activities.MainMenu.Message.MessageActivity;
import com.okutu.splash.Activities.MainMenu.Profile.EditProfileActivity;
import com.okutu.splash.Models.User;
import com.okutu.splash.R;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainMenuActivity extends AppCompatActivity {
    private static final String TIMER_VALUE_KEY ="TimerValue" ;
    private static final int EDIT_PROFILE_REQUEST = 1;
    private static final int REQUEST_UPDATE_PHOTO = 2;
    TextView timerText;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ImageButton playButton;
    private ImageView thumbnailImageView;
    private TextView textViewAim;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private CircleImageView profileImageView ;

    Button StartButton;
    Timer timer;

    TimerTask timerTask;
    Double time = 0.0;
    private FragmentTransaction fragmentTransaction;
    boolean timerStarted = false;
    private WebView webView;
    private ImageView imageView;

    TextView txtName;

    private static final String PREFS_NAME = "TimerPrefs";
    private static final String START_TIME_KEY = "StartTime";

    private void saveStartTime() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putLong(START_TIME_KEY, System.currentTimeMillis());
        editor.apply();
    }

    private long getSavedStartTime() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getLong(START_TIME_KEY, 0);
    }




    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        profileImageView = findViewById(R.id.profileImageView);
        long savedStartTime = getSavedStartTime();
        if (savedStartTime > 0) {
            long elapsedTime = System.currentTimeMillis() - savedStartTime;
            time += elapsedTime / 1000.0; // Geçen süreyi saniyeye dönüştürerek sayaç değerini güncelle
        }
        saveStartTime(); // Yeni başlangıç zamanını kaydet
        // Diğer kodlar...

        VideoView videoView = findViewById(R.id.videoView);
        thumbnailImageView = findViewById(R.id.thumbnailImageView);
        playButton = findViewById(R.id.playButton);

        String videoUrl = "https://youtu.be/JC_Ixv5Ygvw"; // Video URL'sini buraya ekleyin veya yerel bir dosyaysa dosya yolunu kullanın
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.tedx;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        videoView.setOnPreparedListener(mp -> {
            thumbnailImageView.setVisibility(View.GONE);
            videoView.start();
        });

        playButton.setOnClickListener(v -> {
            thumbnailImageView.setVisibility(View.GONE);
            videoView.start();
        });

        VideoView videoView2 = findViewById(R.id.videoView2);
        String videoPath2 = "android.resource://" + getPackageName() + "/" + R.raw.tedx;
        Uri uri2 = Uri.parse(videoPath2);
        videoView2.setVideoURI(uri2);
        MediaController mediaController2 = new MediaController(this);
        videoView2.setMediaController(mediaController2);
        mediaController2.setAnchorView(videoView2);

        VideoView videoView3 = findViewById(R.id.videoView3);
        String videoPath3 = "android.resource://" + getPackageName() + "/" + R.raw.tedx;
        Uri uri3 = Uri.parse(videoPath3);
        videoView3.setVideoURI(uri3);
        MediaController mediaController3 = new MediaController(this);
        videoView3.setMediaController(mediaController3);
        mediaController3.setAnchorView(videoView3);


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
                    return true;

                case R.id.person:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });


    textViewAim = findViewById(R.id.textViewAim);
        textViewAim.setTextColor(getResources().getColor(android.R.color.white));
        String aim = getIntent().getStringExtra("aim");
        textViewAim.setText("Aim: " + aim);
        webView = findViewById(R.id.webview);
        imageView = findViewById(R.id.imageView4);
        imageView.setOnClickListener(v -> {
            String url = "https://www.yesilay.org.tr/tr/haberler"; // Hedef web sitesinin URL'sini buraya girin
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        webView.getSettings().setJavaScriptEnabled(true);
        imageView.setImageResource(R.drawable.__uluslararasi_yesilay_karikatur_yarismasi_k); // Resmi ayarlayın, "your_image" değerini değiştirin
        timerText = (TextView) findViewById(R.id.timerText);
        StartButton = (Button) findViewById(R.id.startButton);
        timer = new Timer();
        profileImageView.setOnClickListener(v -> {
            // Profil düzenleme sayfasına geçiş yap
            Intent intent = new Intent(MainMenuActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, EDIT_PROFILE_REQUEST);
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Check if the updated photo URI is available
            if (data.hasExtra("updated_photo_uri")) {
                String updatedPhotoUriString = data.getStringExtra("updated_photo_uri");
                Uri updatedPhotoUri = Uri.parse(updatedPhotoUriString);

                // Update the profile image with the updated photo URI
                profileImageView.setImageURI(updatedPhotoUri);
            }
        }
    }


    public void resetTapped(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Are you sure you want to reset the timer?");
        resetAlert.setPositiveButton("Reset", (dialogInterface, i) -> {
            if(timerTask != null)
            {
                timerTask.cancel();
                setButtonUI("START", R.color.white);
                time = 0.0;
                timerStarted = false;
                timerText.setText(formatTime(0,0,0));

            }
        });

        resetAlert.setNeutralButton("Cancel", (dialogInterface, i) -> {
            //do nothing
        });

        resetAlert.show();

    }
    public void startTapped(View view) {
        if (timerStarted == false) {
            timerStarted = true;
            setButtonUI("START", R.color.black);

            startTimer();
        }
    }
    private void setButtonUI(String start, int color)
    {
        StartButton.setText(start);
        StartButton.setTextColor(ContextCompat.getColor(this, color));
    }
    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(() -> {
                    time++;
                    timerText.setText(getTimerText());
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }
    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }
    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveTimerValue(time);
    }
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
        double savedTime = getSavedTimerValue();
        if (savedTime > 0) {
            time = savedTime;
            timerText.setText(getTimerText());
            startTimer();
            setButtonUI("START", R.color.black);
            timerStarted = true;
        }
    }
    private void saveTimerValue(double value) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putFloat(TIMER_VALUE_KEY, (float) value);
        editor.apply();
    }

    // Kaydedilmiş sayaç değerini almak için
    private double getSavedTimerValue() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getFloat(TIMER_VALUE_KEY, 0);
    }



}
