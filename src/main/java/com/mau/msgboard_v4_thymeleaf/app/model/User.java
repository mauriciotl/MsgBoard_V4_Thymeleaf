package com.mau.msgboard_v4_thymeleaf.app.model;

public class User {
    private int userId;
    private String name;
    private String password;

    // Constructor with all fields
    public User(int userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }


    // Getters and Setters (Existing code remains here)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}