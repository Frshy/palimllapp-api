package com.example.myapplication.model;

import java.util.Date;

public class AgendaModel {
    private int id;
    private String activity;
    private String description;
    private String address;
    private Date date;
    private boolean active;
    private int createdByUserId;
    private Date updatedAt;
    private Date createdAt;
    private CreatedByUser createdByUser;

    // Getters for all the fields, no setters

    public int getId() {
        return id;
    }

    public String getActivity() {
        return activity;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public Date getDate() {
        return date;
    }

    public boolean isActive() {
        return active;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public CreatedByUser getCreatedByUser() {
        return createdByUser;
    }

    // Nested class to represent the "createdByUser" object
    public static class CreatedByUser {
        private String username;

        // Getter for "username"
        public String getUsername() {
            return username;
        }
    }
}