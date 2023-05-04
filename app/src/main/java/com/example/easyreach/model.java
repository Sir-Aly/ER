package com.example.easyreach;

public class model
{
    String name, email,skills,photoUrl;

    public model() {
    }

    public model(String name, String email, String skills,String photoUrl) {
        this.name = name;
        this.email = email;
        this.skills = skills;
        this.photoUrl=photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getSkills(){
        return skills;
    }
    public String getPhotoUrl(){
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}
