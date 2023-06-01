package com.example.easyreach;

public class JobSeeker {
    private String sName;
    private String sField;
    private String sAge;
    private String sYoE;
    private String sLocation;
    private String sImageUrl;
    private String sEmail;
    private String UID;

    public JobSeeker() {
        // Default constructor required for Firebase
    }

    public JobSeeker(String sName, String sField,String sAge, String sYoE,  String sLocation, String sImageUrl, String sEmail, String UID) {
        this.sName = sName;
        this.sField = sField;
        this.sAge = sAge;
        this.sYoE = sYoE;
        this.sLocation = sLocation;
        this.sImageUrl = sImageUrl;
        this.sEmail = sEmail;
        this.UID = UID;
    }

    // Getters and setters


    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsField() {
        return sField;
    }

    public void setsField(String sField) {
        this.sField = sField;
    }

    public String getsAge() {
        return sAge;
    }

    public void setsAge(String sAge) {
        this.sAge = sAge;
    }

    public String getsYoE() {
        return sYoE;
    }

    public void setsYoE(String sYoE) {
        this.sYoE = sYoE;
    }

    public String getsLocation() {
        return sLocation;
    }

    public void setsLocation(String sLocation) {
        this.sLocation = sLocation;
    }

    public String getsImageUrl() {
        return sImageUrl;
    }

    public void setsImageUrl(String sImageUrl) {
        this.sImageUrl = sImageUrl;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}