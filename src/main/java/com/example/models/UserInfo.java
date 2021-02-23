package com.example.models;



public class UserInfo {
    private String email;
    private String username;
    private boolean is_admin;

    public UserInfo() {
    }

    public UserInfo(String email, String username, boolean is_admin) {
        this.email = email;
        this.username = username;
        this.is_admin = is_admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }
}
