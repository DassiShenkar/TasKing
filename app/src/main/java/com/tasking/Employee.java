package com.tasking;


public class Employee {
    private String userName;
    private String password;
    private String uid;
    private String managerId;

    public Employee() {

    }

    public Employee(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.uid = null;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
//
//    public int getIsManager() {
//        return isManager;
//    }

//    public void setIsManager(int iSmanager) {
//        this.isManager = iSmanager;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'';
    }
}
