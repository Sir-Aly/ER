package com.example.easyreach;

import com.google.firebase.firestore.Exclude;

public class ADD_Message {
    private String documentId;
    private String Message;
    private String From;


    public ADD_Message() {
        //public no-arg constructor needed
    }

    public ADD_Message(String Message, String From) {
        this.Message = Message;
        this.From = From;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getMessage() {
        return Message;
    }

    public String getFrom() {
        return From;
    }
}

