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
    private static int id;
    private String name;
    private Date dueTime;
    private String category;
    private String priority;
    private String location;
    private ArrayList<TeamMember> assignees;

    public Task() {
    }

    public Task(String name, Date dueTime, String category, String priority, String location, ArrayList<TeamMember> assignees) {
        this.name = name;
        this.dueTime = dueTime;
        this.category = category;
        this.priority = priority;
        this.location = location;
        this.assignees = assignees;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDueTime() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        return df.format(dueTime);
    }

    public void setDueTime(String dateString) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            this.dueTime = format.parse(dateString);
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

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", dueTime=" + dueTime +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", location='" + location + '\'' +
                ", assignees=" + assignees +
                '}';
    }
}
