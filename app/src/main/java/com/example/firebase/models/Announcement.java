package com.example.firebase.models;

public class Announcement {
private int Type; //task or envent
private String Topic;
private String Location;
private String Details;
private String Target;
private String imageURL;
private  String notificationID;

    public Announcement(int type, String topic, String location, String details, String target, String imageURL) {
        Type = type;
        Topic = topic;
        Location = location;
        Details = details;
        Target = target;
        this.imageURL = imageURL;
    }

    public Announcement() {
    }

    public int getType() {
        return Type;
    }

    public String getTopic() {
        return Topic;
    }

    public String getLocation() {
        return Location;
    }

    public String getDetails() {
        return Details;
    }

    public String getTarget() {
        return Target;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setType(int type) {
        Type = type;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
