package com.example.firebase.models;

public class FreindlyMessage {
    private String text;
    private String name;
    private String photoUrl;
    private String timestamp;
    private String user_id;
    private String sender_imageUrl;

    public void setSender_imageUrl(String sender_imageUrl) {
        this.sender_imageUrl = sender_imageUrl;
    }

    public String getSender_imageUrl() {
        return sender_imageUrl;
    }

    public FreindlyMessage(String text, String name, String photoUrl, String timestamp, String user_id, String sender_imageUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.timestamp = timestamp;
        this.user_id = user_id;
        this.sender_imageUrl = sender_imageUrl;
    }

    public FreindlyMessage() {
    }

    public FreindlyMessage(String text, String name, String photoUrl,String timestamp , String user_id) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.timestamp = timestamp;
        this.user_id=user_id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}



