package com.example.bloodprojectapplication.model;

public class Notification {
    private String id;
    private String userName;
    private String userUID;
    private String userEmail;
    private String bloodGroup;
    private String phoneNumber;
    private String recipient;
    private String timeStamp;

    public Notification() {

    }

    public Notification(String id, String userName, String userUID, String userEmail, String bloodGroup, String phoneNumber, String recipient, String timeStamp) {
        this.id = id;
        this.userName = userName;
        this.userUID = userUID;
        this.userEmail = userEmail;
        this.bloodGroup = bloodGroup;
        this.phoneNumber = phoneNumber;
        this.recipient = recipient;
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
