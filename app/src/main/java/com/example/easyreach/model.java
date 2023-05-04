package com.example.easyreach;

public class model
{
    String name, email,skills;

    public model() {
    }

    public model(String name, String email, String skills) {
        this.name = name;
        this.email = email;
        this.skills = skills;
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
}
