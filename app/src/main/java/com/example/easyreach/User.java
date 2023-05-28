package com.example.easyreach;

public class User {
    private String name;
    private String email;
    private boolean isJobProvider;

    public User(String pName, String pEmail, boolean isJobProvider) {
        this.name = pName;
        this.email = pEmail;
        this.isJobProvider = isJobProvider;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isJobProvider() {
        return isJobProvider;
    }
}
