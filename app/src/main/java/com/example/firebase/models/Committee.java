package com.example.firebase.models;

import java.util.List;

public class Committee {
    private String Committee_Name;//
    private List<String> members_IDs;
    private List<String> Committee_Heads_IDs;
    private String Committee_Vice_ID;
    private String Committee_Image_Url;//
    private List<String> announcemnts_IDs;//

    public Committee(String Committename , List<String> Heads , String vice , List<String> members)
    {
        setCommittee_Name(Committename);
        setCommittee_Heads_IDs(Heads);
        setCommittee_Vice_ID(vice);
        setMembers_IDs(members);
    }
    public Committee(String Committename , List<String> Heads , String vice , List<String> members,String Committee_Image_Url)
    {
        setCommittee_Name(Committename);
        setCommittee_Heads_IDs(Heads);
        setCommittee_Vice_ID(vice);
        setMembers_IDs(members);
        setCommittee_Image_Url(Committee_Image_Url);

    }
    public Committee(){}
    public void setCommittee_Name(String committee_Name) {
        Committee_Name = committee_Name;
    }



    public void setCommittee_Image_Url(String committee_Image_Url) {
        Committee_Image_Url = committee_Image_Url;
    }

    public String getCommittee_Image_Url() {
        return Committee_Image_Url;
    }


    public String getCommittee_Name() {
        return Committee_Name;
    }

    public void setAnnouncemnts_IDs(List<String> announcemnts_IDs) {
        this.announcemnts_IDs = announcemnts_IDs;
    }

    public void setCommittee_Heads_IDs(List<String> committee_Heads_IDs) {
        Committee_Heads_IDs = committee_Heads_IDs;
    }

    public void setCommittee_Vice_ID(String committee_Vice_ID) {
        Committee_Vice_ID = committee_Vice_ID;
    }

    public void setMembers_IDs(List<String> members_IDs) {
        this.members_IDs = members_IDs;
    }

    public String getCommittee_Vice_ID() {
        return Committee_Vice_ID;
    }

    public List<String> getAnnouncemnts_IDs() {
        return announcemnts_IDs;
    }

    public List<String> getCommittee_Heads_IDs() {
        return Committee_Heads_IDs;
    }

    public List<String> getMembers_IDs() {
        return members_IDs;
    }

}
