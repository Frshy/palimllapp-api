package com.example.myapplication.model;

public class NewsModel {
    private int id;
    private String title;
    private String content;
    private boolean active;
    private int createdByUserId;
    private String updatedAt;
    private String createdAt;
    private CreatedByUser createdByUser;

    // Getters for all the fields

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isActive() {
        return active;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
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
