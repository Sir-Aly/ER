package com.example.easyreach;

public class User {
    private String name;
    private String pEmail;
    private boolean isJobProvider;

    public User(String name, String pEmail, boolean isJobProvider) {
        this.name = name;
        this.pEmail = pEmail;
        this.isJobProvider = isJobProvider;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return pEmail;
    }

    public boolean isJobProvider() {
        return isJobProvider;
    }
}
