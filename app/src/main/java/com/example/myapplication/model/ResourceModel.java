package com.example.myapplication.model;

public class ResourceModel {
    private int id;
    private String name;
    private String address;
    private String attendingHours;
    private String description;
    private boolean isActive;
    private int createdByUserId;
    private String updatedAt;
    private String createdAt;
    private CreatedByUser createdByUser;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getAttendingHours() {
        return attendingHours;
    }

    public boolean isActive() {
        return isActive;
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

    public class CreatedByUser {
        private String username;

        public String getUsername() {
            return username;
        }
    }
}
