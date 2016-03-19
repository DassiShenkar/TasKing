package com.tasking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private int id;
    private String name;
    private Date date;
    private String category;
    private String priority;
    private String location;
    private String status;
    private String firebaseId;
    private String assignee;
    private String picture;

    public Task() {

    }

    public Task(int id, String name, Date date, String category, String priority, String location, String status, String firebaseId, String assignee) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.category = category;
        this.priority = priority;
        this.location = location;
        this.status = status;
        this.firebaseId = firebaseId;
        this.assignee = assignee;
    }

    public String getTimeString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        String strTime = simpleDateFormat.format(date);
        return strTime;
    }

    public String getDateString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String strDate = simpleDateFormat.format(date);
        return strDate;
    }

    public void setDateFromString(String time, String date){
        String fullDate = date + " " + time;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        try{
            this.date = dateFormat.parse(fullDate);
        } catch (ParseException e){
            e.printStackTrace();
        }
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
