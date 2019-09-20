package com.example.firebase.models;

public class ChatNode {
    private String ReceiverName;
    private FreindlyMessage LastMessage;
    private String ReceiverImageUrl;
    private boolean status;//seen or not.
    private String ChatID;

    public ChatNode(String receiverName, String receiverImageUrl, boolean status, String chatID) {
        ReceiverName = receiverName;
        ReceiverImageUrl = receiverImageUrl;
        this.status = status;
        ChatID = chatID;
    }

    public void setChatID(String chatID) {
        ChatID = chatID;
    }

    public String getChatID() {
        return ChatID;
    }

    public void setLastMessage(FreindlyMessage lastMessage) {
        LastMessage = lastMessage;
    }

    public FreindlyMessage getLastMessage() {
        return LastMessage;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverImageUrl(String receiverImageUrl) {
        ReceiverImageUrl = receiverImageUrl;
    }

    public String getReceiverImageUrl() {
        return ReceiverImageUrl;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public ChatNode(){}

    public boolean isStatus() {
        return status;
    }

    public ChatNode(String receiverName, FreindlyMessage lastMessage, String receiverImageUrl, boolean status) {
        ReceiverName = receiverName;
        LastMessage = lastMessage;
        ReceiverImageUrl = receiverImageUrl;
        this.status = status;
    }
}