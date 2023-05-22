package com.example.easyreach;

import com.google.firebase.firestore.Exclude;

public class ADD_Massage {
    private String documentId;
    private String massage;
    private String from;


    public ADD_Massage() {
        //public no-arg constructor needed
    }

    public ADD_Massage(String massage, String from) {
        this.massage = massage;
        this.from = from;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getMassage() {
        return massage;
    }

    public String getFrom() {
        return from;
    }
}

