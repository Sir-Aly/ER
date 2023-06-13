package com.example.easyreach;

public class Users {
    private String senderName;
    private String email;
    private String id;
    private int messageCount;

    public Users() {
        // Default constructor required for Firestore
    }

    public Users(String senderName, String email, String id) {
        this.senderName = senderName;
        this.email = email;
        this.id = id;
        this.messageCount = 0; // Initialize message count to 0
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
}
