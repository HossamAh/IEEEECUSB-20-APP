package com.example.firebase.models;

public class Event {
    private String Topic;
    private String Details;
    private String Target;
    private String Date;
    private String Location;
    private String ImageUrl;
    private String EventID;

    public Event(String topic, String details, String target, String date, String location, String imageUrl, String eventID) {
        Topic = topic;
        Details = details;
        Target = target;
        Date = date;
        Location = location;
        ImageUrl = imageUrl;
        EventID = eventID;
    }

    public Event() {
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getTopic() {
        return Topic;
    }

    public String getDetails() {
        return Details;
    }

    public String getTarget() {
        return Target;
    }

    public String getDate() {
        return Date;
    }

    public String getLocation() {
        return Location;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getEventID() {
        return EventID;
    }
}
