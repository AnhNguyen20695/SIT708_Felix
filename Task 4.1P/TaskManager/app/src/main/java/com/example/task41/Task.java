package com.example.task41;

import java.util.ArrayList;

public class Task {
    private int ID;
    private ArrayList<Person> persons;
    private String taskTitle;
    private String taskDescription;
    private String date;

    public Task() {
        persons = new ArrayList<>();
    }

    public int getID(){
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public String getTitle() {
        return taskTitle;
    }

    public void setTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDescription() {
        return taskDescription;
    }

    public void setDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
