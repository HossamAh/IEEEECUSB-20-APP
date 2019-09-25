package com.example.firebase.models;

public class Task {
    private  String Topic;
    private String Target;
    private String Details;
    private String DeadlineDate;
    private String TaskID;

    public Task() {
    }

    public Task(String topic, String target, String details, String deadlineDate, String taskID) {
        Topic = topic;
        Target = target;
        Details = details;
        DeadlineDate = deadlineDate;
        TaskID = taskID;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDeadlineDate() {
        return DeadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        DeadlineDate = deadlineDate;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }
}
