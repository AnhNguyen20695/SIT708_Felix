package com.example.lostandfound.models;

public class FoundModel {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String description;
    private String reportedDate;
    private String location;
    public FoundModel(String name, String phoneNumber, String description, String reportedDate, String location) {
        this.id = 0;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.reportedDate = reportedDate;
        this.location = location;
    }

    public FoundModel(Integer id, String name, String phoneNumber, String description, String reportedDate, String location) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.reportedDate = reportedDate;
        this.location = location;
    }

    public Integer getID() {
        return id;
    }
    public void setID(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedDate() {
        return reportedDate;
    }
    public void setReportedDate(String reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
