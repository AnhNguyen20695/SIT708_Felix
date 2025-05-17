package com.example.lostandfoundv2.models;

public class LostModel {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String description;
    private String reportedDate;
    private String location;
    private String locationLatitude;
    private String locationLongitude;

    public LostModel(String name, String phoneNumber, String description, String reportedDate, String location) {
        this.id = 0;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.reportedDate = reportedDate;
        this.location = location;
    }

    public LostModel(Integer id, String name, String phoneNumber, String description, String reportedDate, String location, String locationLatitude, String locationLongitude) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.reportedDate = reportedDate;
        this.location = location;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
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
    public String getLocationLat() {
        return locationLatitude;
    }
    public void setLocationLat(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }
    public String getLocationLng() {
        return locationLongitude;
    }
    public void setLocationLng(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

}
