package com.example.easyreach;

public class User {
    private String name;
    private String email;
    private boolean isJobProvider;

    public User(String name, String email, boolean isJobProvider) {
        this.name = name;
        this.email = email;
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
