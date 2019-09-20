package com.example.firebase.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private String User_Name;
    private String User_Committee;
    private String User_Position;
    private String Committee_position;
    private String Uid;
    private String Url;
    private ArrayList<String> Chats_IDs;

    public Users(){}
    public Users(String user_name,String User_Committee,String User_position )
    {
        setUser_Committee(User_Committee);
        setUser_Name(user_name);
        setUser_Position(User_position);
        setCommittee_position(User_Committee+"_"+User_position);
    }
    public Users(String user_name, String User_Committee, String User_position, String url)
    {
        setUser_Committee(User_Committee);
        setUser_Name(user_name);
        setUser_Position(User_position);
        setCommittee_position(User_Committee+"_"+User_position);
        setUrl(url);
    }

    public Users(String user_Name, String user_Committee, String user_Position, String committee_position, String uid, String url, ArrayList<String> chats_IDs) {
        User_Name = user_Name;
        User_Committee = user_Committee;
        User_Position = user_Position;
        Committee_position = committee_position;
        Uid = uid;
        Url = url;
        Chats_IDs = chats_IDs;
    }

    public void setChats_IDs(ArrayList<String> chats_IDs) {
        Chats_IDs = chats_IDs;
    }

    public ArrayList<String> getChats_IDs() {
        return Chats_IDs;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getUrl() {
        return Url;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUser_Committee(String user_Committee) {
        User_Committee = user_Committee;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public void setUser_Position(String user_Position) {
        User_Position = user_Position;
    }

    public void setCommittee_position(String committee_position) {
        Committee_position = committee_position;
    }

    public String getCommittee_position() {
        return Committee_position;
    }

    public String getUser_Committee() {
        return User_Committee;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public String getUser_Position() {
        return User_Position;
    }


}
