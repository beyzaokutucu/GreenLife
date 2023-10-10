package com.okutu.splash.Models;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private String chatId;
    private List<String> userIds;
    private List<Message> messages;

    public Chat() {
        userIds = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
    public void addUserId(String uuid) {
        this.userIds.add(uuid);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message line) {
        this.messages.add(line);
    }

    public void update() {
        FirebaseDatabase.getInstance().getReference().child("Chats").child(this.chatId).setValue(this);
    }

    // Static field
    public static void saveChat(Chat chat) {
        FirebaseDatabase.getInstance().getReference().child("Chats").child(chat.getChatId()).setValue(chat);
    }

    public static Task<DataSnapshot> getChatById(String chatId) {
        return FirebaseDatabase.getInstance().getReference().child("Chats").child(chatId).get();
    }
}
