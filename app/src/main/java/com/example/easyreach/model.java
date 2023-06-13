package com.example.easyreach;

public class model
{
    String sName, sEmail, sAge, sField, sImageUrl,sLocation ,UID, sYoE, documentId;

    public model() {
    }

    public model(String documentId, String sName, String sEmail,String sAge, String sField, String sImageUrl,String sLocation, String UID, String sYoE) {
        this.documentId = documentId;
        this.sName = sName;
        this.sEmail = sEmail;
        this.sAge = sAge;
        this.sField = sField;
        this.sImageUrl = sImageUrl;
        this.sLocation = sLocation;
        this.sYoE = sYoE;
        this.UID = UID;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsAge() {
        return sAge;
    }

    public void setsAge(String sAge) {
        this.sAge = sAge;
    }

    public String getsField() {
        return sField;
    }

    public void setsField(String sField) {
        this.sField = sField;
    }

    public String getsImageUrl() {
        return sImageUrl;
    }

    public void setsImageUrl(String sImageUrl) {
        this.sImageUrl = sImageUrl;
    }

    public String getsLocation() {
        return sLocation;
    }

    public void setsLocation(String sLocation) {
        this.sLocation = sLocation;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getsYoE() {
        return sYoE;
    }

    public void setsYoE(String sYoE) {
        this.sYoE = sYoE;
    }
}
