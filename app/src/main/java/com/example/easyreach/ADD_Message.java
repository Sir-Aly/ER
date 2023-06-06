package com.example.easyreach;

import com.google.firebase.firestore.Exclude;

public class ADD_Message {
    private String documentId;
    private String Message;
    private String From;
    private String FUid;


    public ADD_Message() {
        //public no-arg constructor needed
    }

    public ADD_Message(String Message, String From,String FUid) {
        this.Message = Message;
        this.From = From;
        this.FUid = FUid;

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

    public String getFUid() {
        return FUid;
    }

    public void setUid(String Uid) {
        this.FUid = Uid;
    }
}

