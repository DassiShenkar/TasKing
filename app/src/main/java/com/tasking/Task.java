package com.tasking;

import java.util.ArrayList;


public class Task {
    private int id;
    private String name;
    private String dueDate;
    private String category;
    private String priority;
    private String location;
    private String status;
    private ArrayList<TeamMember> assignees;

    public Task() {
    }

    public Task(String name, String dueDate, String category, String priority, String location, String status, ArrayList<TeamMember> assignees) {
        this.name = name;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.location = location;
        this.status = status;
        if(assignees == null){
            this.assignees = new ArrayList<>();
        }
        else{
            this.assignees = assignees;
        }
        //TODO: check this!!!
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<TeamMember> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<TeamMember> assignees) {
        this.assignees = assignees;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", dueTime=" + getDueDate() +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", location='" + location + '\'' +
                ", assignees=" + assignees +
                '}';
    }
}
