package com.talk.demo.types;

public class Candidate implements TalkType {

    public String userName;
    public String phoneMobile;
    public String avatar;
    public String description;
    
    public Candidate() {
    }
    
    public Candidate(String name, String phone, String fAvatar,
            String fDescription) {
        userName = name;
        phoneMobile = phone;
        avatar = fAvatar;
        description = fDescription;
    }
    
    public void setUsername(String v) {
        userName = v;
    }
    public String getUserName() {
        return userName;
    }
    
    public void setPhoneMobile(String v) {
        phoneMobile = v;
    }
    public String getPhoneMobile() {
        return phoneMobile;
    }
    
    public void setAvatar(String v) {
        avatar = v;
    }
    public String getAvatar() {
        return avatar;
    }
    
    public void setDescription(String v) {
        description = v;
    }
    public String getDescription() {
        return description;
    }       

}