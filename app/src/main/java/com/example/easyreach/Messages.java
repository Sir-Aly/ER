package com.example.easyreach;

import com.google.firebase.Timestamp;

public class Messages {

    private String senderID;
    private String recipientID;
    private String content;
    private String senderEmail;
    private String recipientEmail;
    private boolean isAccepted;
    private Timestamp timestamp;

    public Messages(){
    }

    public Messages(String senderID, String recipientID, String content, String senderEmail,String recipientEmail, boolean isAccepted, Timestamp timestamp){

        this.senderID = senderID;
        this.recipientID = senderID;
        this.content = content;
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.isAccepted = false;
        this.timestamp = timestamp;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
