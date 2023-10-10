package com.okutu.splash.Activities.MainMenu.Message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okutu.splash.Activities.MainMenu.FindFriendActivity;
import com.okutu.splash.Activities.MainMenu.MainMenuActivity;
import com.okutu.splash.Activities.MainMenu.ProfileActivity;
import com.okutu.splash.Adapter.MessageAdapter;
import com.okutu.splash.Models.Chat;
import com.okutu.splash.Models.Message;
import com.okutu.splash.Models.User;
import com.okutu.splash.R;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private ListView messageListView;
    private EditText messageInput;
    private Button sendButton;

    private List<Message> messages;
    private ArrayAdapter<Message> messageAdapter;
    private TextView targetName;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        auth = FirebaseAuth.getInstance();

        targetName = findViewById(R.id.titleText);

        // Mesaj listesini ve adaptörünü oluşturun
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messages);


        // Mesaj listesini ve adaptörünü görüntüleyiciye bağlayın
        messageListView = findViewById(R.id.messageList);
        messageListView.setAdapter(messageAdapter);

        // Mesaj gönderme alanını ve düğmeyi alın
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        // Gönder düğmesine tıklama olayını ayarlayın
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Mesajları alma işlemini başlatmak için bir metodu çağırın
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
                    return true;
                case R.id.person:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
    }

    ValueEventListener chatListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Chat chat = snapshot.getValue(Chat.class);
            messages.clear();
            messages.addAll(chat.getMessages());
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        User.getChatIdByUserId(auth.getCurrentUser().getUid()).addOnSuccessListener( chatIdSnapshot -> {
            String chatId = chatIdSnapshot.getValue(String.class);

            FirebaseDatabase.getInstance().getReference().child("Chats").child(chatId).addValueEventListener(chatListener);
        });
        refreshChat();
    }

    @Override
    protected void onPause() {
        super.onPause();
        User.getChatIdByUserId(auth.getCurrentUser().getUid()).addOnSuccessListener( chatIdSnapshot -> {
            String chatId = chatIdSnapshot.getValue(String.class);
            FirebaseDatabase.getInstance().getReference().child("Chats").child(chatId).removeEventListener(chatListener);
        });
    }

    private void refreshChat() {
        User.getChatIdByUserId(auth.getCurrentUser().getUid()).addOnSuccessListener( chatIdSnapshot -> {
            String chatId = chatIdSnapshot.getValue(String.class);
            if (chatId == null || chatId.isEmpty()) {
                Log.e("Debug", "chatId is null");
                return;
            }
            Chat.getChatById(chatId).addOnSuccessListener( chatSnapshot -> {

                Chat chat = chatSnapshot.getValue(Chat.class);
                if (chat == null) {
                    Log.e("Debug", "chat is null");
                        return;
                }
                messages.clear();
                messages.addAll(chat.getMessages());
                String matchId = "";
                for (String userId : chat.getUserIds()) {
                    if (!userId.equals(auth.getCurrentUser().getUid())) {
                        matchId = userId;
                        break;
                    }
                }
                User.getUserById(matchId).addOnSuccessListener( matchSnapshot -> {
                    User match = matchSnapshot.getValue(User.class);
                    if (match == null) {
                        Log.e("Debug", "match is null");
                        return;
                    }
                    targetName.setText(match.getUserName());
                }).addOnFailureListener(t -> Log.e("Debug", "getUserById failed"));
            }).addOnFailureListener(t -> Log.e("Debug", "getChatById failed"));
        }).addOnFailureListener(t -> Log.e("Debug", "getChatIdByUser failed"));
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (message.isEmpty()) return;

        FirebaseUser user = auth.getCurrentUser();
        User.getUserById(user.getUid()).addOnSuccessListener( userSnapshot -> {
            User kullanici = userSnapshot.getValue(User.class);
            Chat.getChatById(kullanici.getChatId()).addOnSuccessListener( chatSnapshot -> {
                Chat chat = chatSnapshot.getValue(Chat.class);
                if (chat == null) {
                    Log.e("Debug", "chat is null");
                    return;
                }
                Message newMessage = new Message();
                newMessage.setMessage(message);
                newMessage.setSenderId(user.getUid());
                chat.addMessage(newMessage);
                chat.update();
            }).addOnFailureListener(t -> Log.e("Debug", "getChatById failed"));
        }).addOnFailureListener(t -> Log.e("Debug", "getUserById failed"));

        // Mesaj gönderildikten sonra giriş alanını temizleyin
        messageInput.setText("");
        refreshChat();

    }
}
