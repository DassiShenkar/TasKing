package il.ac.shenkar.tasking.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

@Entity
public class Task {

    @Id
    private String id;
    private String descpription;
    private Date date;
    private String category;
    private String priority;
    private String location;
    private String acceptStatus;
    private String status;
    private String assignee;
    private String picture;
    private String timeStamp;
    private String managerUid;
    private String assigneeUid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescpription() {
        return descpription;
    }

    public void setDescpription(String descpription) {
        this.descpription = descpription;
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

    public String getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(String acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
