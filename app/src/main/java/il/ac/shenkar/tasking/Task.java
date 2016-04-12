package il.ac.shenkar.tasking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private String name;
    private Date date;
    private String category;
    private String priority;
    private String location;
    private String acceptStatus;
    private String status;
    private String firebaseId;
    private String assignee;
    private String picture;
    private String timeStamp;
    private String managerUid;
    private String assigneeUid;

    public Task(){}

    public Task(String name, Date date, String category, String priority, String location, String acceptStatus, String status, String firebaseId, String assignee, String picture, String timeStamp, String managerUid, String assigneeUid) {
        this.name = name;
        this.date = date;
        this.category = category;
        this.priority = priority;
        this.location = location;
        this.acceptStatus = acceptStatus;
        this.status = status;
        this.firebaseId = firebaseId;
        this.assignee = assignee;
        this.picture = picture;
        this.timeStamp = timeStamp;
        this.managerUid = managerUid;
        this.assigneeUid = assigneeUid;
    }

    public String convertTimeString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        return simpleDateFormat.format(date);
    }

    public String convertDateString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return simpleDateFormat.format(date);
    }

    public void convertDateFromString(String time, String date){
        String fullDate = date + " " + time;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        try{
            this.date = dateFormat.parse(fullDate);
        } catch (ParseException e){
            e.printStackTrace();
        }
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

    public String getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(String acceptStatus) {
        this.acceptStatus = acceptStatus;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getManagerUid() {
        return managerUid;
    }

    public void setManagerUid(String managerUid) {
        this.managerUid = managerUid;
    }

    public String getAssigneeUid() {
        return assigneeUid;
    }

    public void setAssigneeUid(String assigneeUid) {
        this.assigneeUid = assigneeUid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return !(getFirebaseId() != null ? !getFirebaseId().equals(task.getFirebaseId()) : task.getFirebaseId() != null);
    }

    @Override
    public int hashCode() {
        return getFirebaseId() != null ? getFirebaseId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Task{" +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", category='" + category + '\'' +
                ", priority='" + priority + '\'' +
                ", location='" + location + '\'' +
                ", acceptStatus='" + acceptStatus + '\'' +
                ", status='" + status + '\'' +
                ", firebaseId='" + firebaseId + '\'' +
                ", assignee='" + assignee + '\'' +
                ", picture='" + picture + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", managerUid='" + managerUid + '\'' +
                ", assigneeUid='" + assigneeUid + '\'' +
                '}';
    }
}


