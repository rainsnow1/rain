package com.example.order2;

public class UserManager {
    private static UserManager instance;
    private String currentUsername = null; // null 表示没有用户登录

    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public boolean isUserLoggedIn() {
        return currentUsername != null;
    }

    public void logout() {
        currentUsername = null;
    }
} 