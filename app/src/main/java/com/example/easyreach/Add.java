package com.example.easyreach;

import com.google.firebase.firestore.Exclude;

public class Add {
    private String documentId;
    private String Name;
    private String Skills;
    private String email;
    private String Location;
    private String photoUrl;


    public Add() {
        //public no-arg constructor needed
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Add(String Name, String Skills, String Location,String email,String photoUrl) {
        this.Name = Name;
        this.Skills = Skills;
        this.Location = Location;
        this.email=email;
        this.photoUrl = photoUrl;

    }

    public String getSkills() {
        return Skills;
    }
    public String getPhotoUrl(){

        return photoUrl;
    }


    public String getName() {
        return Name;
    }


    public String getLocation() {
        return Location;
    }
    public String getEmail(){

        return email;
    }
}

