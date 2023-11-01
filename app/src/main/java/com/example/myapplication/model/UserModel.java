package com.example.myapplication.model;

import java.util.Date;

public class UserModel {
    private int id;
    private String username;
    private String role;
    private Date updatedAt;
    private Date createdAt;

    // Constructors, getters, and setters

    public UserModel() {
    }

    public UserModel(int id, String username, String role, Date updatedAt, Date createdAt) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
