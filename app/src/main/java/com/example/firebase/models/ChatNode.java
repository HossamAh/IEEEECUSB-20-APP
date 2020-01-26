package com.example.firebase.models;

public class ChatNode {
    private String Name1;
    private FreindlyMessage LastMessage;
    private String ImageUrl1;
    private String Name2;
    private String ImageUrl2;
    private boolean status;//seen or not.
    private String ChatID;

    public ChatNode() {
    }

    public ChatNode(String name1, FreindlyMessage lastMessage, String imageUrl1, String name2, String imageUrl2, boolean status, String chatID) {
        Name1 = name1;
        LastMessage = lastMessage;
        ImageUrl1 = imageUrl1;
        Name2 = name2;
        ImageUrl2 = imageUrl2;
        this.status = status;
        ChatID = chatID;
    }

    public String getName1() {
        return Name1;
    }

    public void setLastMessage(FreindlyMessage lastMessage) {
        LastMessage = lastMessage;
    }

    public void setName1(String name1) {
        Name1 = name1;
    }

    public FreindlyMessage getLastMessage() {
        return LastMessage;
    }

    public String getImageUrl1() {
        return ImageUrl1;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setImageUrl1(String imageUrl1) {
        ImageUrl1 = imageUrl1;
    }

    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        Name2 = name2;
    }

    public String getImageUrl2() {
        return ImageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        ImageUrl2 = imageUrl2;
    }

    public String getChatID() {
        return ChatID;
    }

    public void setChatID(String chatID) {
        ChatID = chatID;
    }

    public boolean isStatus() {
        return status;
    }
}