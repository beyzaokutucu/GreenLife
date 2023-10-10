package com.okutu.splash.Activities.MainMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.okutu.splash.Activities.MainMenu.Message.MessageActivity;
import com.okutu.splash.Models.Chat;
import com.okutu.splash.Models.User;
import com.okutu.splash.R;

import java.util.Iterator;

public class FindFriendActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mUserReference;
    private DatabaseReference mChatsReference;

    private ImageButton btnEslesme;
    private TextView textView11;
    private TextView userName;
    private TextView matchedUserName;
    private ImageView userImage;
    private ImageView matchedUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        firebaseAuth = FirebaseAuth.getInstance();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users");
        mChatsReference = FirebaseDatabase.getInstance().getReference().child("chats");

        btnEslesme = findViewById(R.id.btnEslesme);
        textView11 = findViewById(R.id.StartMessage);
        userName = findViewById(R.id.UserName);
        matchedUserName = findViewById(R.id.matchedUserName);
        userImage = findViewById(R.id.UserImage);
        matchedUserImage = findViewById(R.id.matchedUserImage);

        btnEslesme.setOnClickListener(v -> {
            // Rastgele bir kullanıcı eşleşmesi yap
            getRandomUser();
        });

        textView11.setOnClickListener(v -> {
            // Chat sayfasına geçiş yap
            openChatPage();
        });

        TextView textView = findViewById(R.id.StartMessage);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(FindFriendActivity.this, MessageActivity.class);
            startActivity(intent);
        });

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
    protected void onResume() {
        super.onResume();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //StorageReference imagesReference = FirebaseStorage.getInstance().getReference().child("User").child("Profile Pic");

        User.getUserById(firebaseUser.getUid()).addOnSuccessListener(userSnapshot -> {
            User user = userSnapshot.getValue(User.class);
            userName.setText(user.getUserName());
            String pfpUri = user.getImage();
            if (pfpUri != null && !pfpUri.isEmpty()) {
                userImage.setImageURI(Uri.parse(user.getImage()));
            } else {
                //Picasso.get().load(t).into(userImage)
            }

            String matchedUserId = user.getMatchedUserId();
            if (matchedUserId != null && !matchedUserId.isEmpty()) {
                User.getUserById(matchedUserId).addOnSuccessListener(matchedUserSnapshot -> {
                    User matchedUser = matchedUserSnapshot.getValue(User.class);
                    matchedUserName.setText(matchedUser.getUserName());
                    String matchedPfpUri = matchedUser.getImage();
                    if (matchedPfpUri != null && !matchedPfpUri.isEmpty()) {
                        matchedUserImage.setImageURI(Uri.parse(user.getImage()));
                    } else {
                        //Picasso.get().load(t).into(userImage)
                    }
                });
            } else {
                //Eslesilen kisi null ve ciktilarinin atanmasi
            }

        });

    }


    private void getRandomUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        Task<DataSnapshot> querySnapshotTask = FirebaseDatabase.getInstance().getReference().child("Users").get();
        querySnapshotTask.addOnSuccessListener(querySnapshot -> {
            int totalUsers = (int)querySnapshot.getChildrenCount();

            String randomUserId = null;
            while (randomUserId == null) {
                int randomIndex = (int)(Math.random() * totalUsers);
                Iterator<DataSnapshot> iterator = querySnapshot.getChildren().iterator();
                while (randomIndex > 0) {
                    iterator.next();
                    randomIndex--;
                }
                DataSnapshot snapshot = iterator.next();

                randomUserId = snapshot.child("userId").getValue(String.class);
                if (currentUser.getUid().equals(randomUserId)) randomUserId = null;
            }
            updateMatchedUser(randomUserId);
        });
    }


    private void updateMatchedUser(String matchedUuid) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(firebaseUser.getUid()).child("matchedUserId").setValue(matchedUuid);
        reference.child(matchedUuid).child("matchedUserId").setValue(firebaseUser.getUid());


        DatabaseReference newRef = mChatsReference.push();
        Chat chat = new Chat();
        chat.addUserId(firebaseUser.getUid());
        chat.addUserId(matchedUuid);
        chat.setChatId(newRef.getKey());
        chat.update();
        reference.child(firebaseUser.getUid()).child("chatId").setValue(chat.getChatId());
        reference.child(matchedUuid).child("chatId").setValue(chat.getChatId());

        onResume();

    }

    private void openChatPage() {
        Intent intent = new Intent(FindFriendActivity.this, MessageActivity.class);
        startActivity(intent);
    }

}








