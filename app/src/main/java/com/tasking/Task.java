package com.tasking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Grisha on 1/2/2016.
 */
public class Task {
    private int id;
    private String name;
    private Date dueDate;
    private String category;
    private String priority;
    private String location;
    private ArrayList<TeamMember> assignees;

    public Task() {
    }

    public Task(String name, Date dueDate, String category, String priority, String location, ArrayList<TeamMember> assignees) {
        this.name = name;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.location = location;
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
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        return df.format(dueDate);
    }

    public void setDueDate(String dateString) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            this.dueDate = format.parse(dateString);
        } catch (ParseException e) {
            // todo: throw our Exception
        }
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

    public ArrayList<TeamMember> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<TeamMember> assignees) {
        this.assignees = assignees;
    }

    public void addAssignee(TeamMember member){

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
