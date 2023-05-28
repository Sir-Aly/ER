package com.example.easyreach;

public class JobSeeker {
    private String name;
    private String skills;
    private String location;
    private String profileUrl;
    private String email;
    private String UID;

    public JobSeeker() {
        // Default constructor required for Firebase
    }

    public JobSeeker(String name, String skills, String location, String profileUrl, String email, String UID) {
        this.name = name;
        this.skills = skills;
        this.location = location;
        this.profileUrl = profileUrl;
        this.email = email;
        this.UID = UID;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}