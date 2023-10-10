package com.okutu.splash.Models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private String userId, userName, userEmail, matchedUserId, chatId, image;


    public User(String userId, String userName, String userEmail){
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.matchedUserId = "";
        this.chatId = "";

    }
    public User(){

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMatchedUserId() {
        return matchedUserId;
    }

    public void setMatchedUserId(String matchedUserId) {
        this.matchedUserId = matchedUserId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Static field
    public static void saveUser(User user){
        FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUserId()).setValue(user);
    }

    public static Task<DataSnapshot> getUserById(String userId){
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(userId).get();
    }

    public static Task<DataSnapshot> getChatIdByUserId(String userId){
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(userId).child("chatId").get();
    }

}