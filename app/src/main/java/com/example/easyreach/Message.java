package com.example.easyreach;

public class Message {

    private String messageContent, receiverID, adminUID, senderID, senderEmail, timestamp;

    public Message() {
        // Empty constructor needed for Firestore
    }

    public Message(String messageContent, String receiverID, String adminUID, String senderID, String senderEmail, String timestamp) {
        this.messageContent = messageContent;
        this.receiverID = receiverID;
        this.adminUID = adminUID;
        this.senderID = senderID;
        this.senderEmail = senderEmail;
        this.timestamp =timestamp;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getAdminUID() {
        return adminUID;
    }

    public void setAdminUID(String adminUID) {
        this.adminUID = adminUID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
