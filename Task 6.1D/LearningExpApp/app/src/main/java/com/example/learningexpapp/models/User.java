package com.example.learningexpapp.models;

public class User {
    private int ID;
    private String fullname;
    private String username;
    private String password;
    private String records;
    private String recommendedTopic;
    private String taskQuestions;

    public User() {
        this.ID = -1;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User(int ID, String username, String password) {
        this.ID = ID;
        this.username = username;
        this.password = password;
    }

    public User(int ID, String username, String password, String records) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.records = records;
    }
    public User(int ID, String username, String fullname, String password, String records) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.records = records;
        this.fullname = fullname;
    }
    public User(int ID, String username, String fullname, String password, String records, String recommendedTopic, String taskQuestions) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.records = records;
        this.fullname = fullname;
        this.recommendedTopic = recommendedTopic;
        this.taskQuestions = taskQuestions;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getRecommendedTopic() {
        return recommendedTopic;
    }

    public void setRecommendedTopic(String recommendedTopic) { this.recommendedTopic = recommendedTopic;    }
    public String getTaskQuestions() {
        return taskQuestions;
    }

    public void setTaskQuestions(String taskQuestions) {
        this.taskQuestions = taskQuestions;
    }

}
