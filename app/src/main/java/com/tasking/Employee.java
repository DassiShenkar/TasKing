package com.tasking;


public class Employee {
    private String userName;
    private String password;
    private int isManager;
    private String uid;

    public Employee() {

    }

    public Employee(String userName, String password, int isManager) {
        this.userName = userName;
        this.password = password;
        this.isManager = isManager;
        this.uid = null;
    }
    public Employee(String userName, String password, int isManager,String uid) {
        this.userName = userName;
        this.password = password;
        this.isManager = isManager;
        this.uid = uid;
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

    public int getIsManager() {
        return isManager;
    }

    public void setIsManager(int iSmanager) {
        this.isManager = iSmanager;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Employee{" +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isManager=" + isManager +
                '}';
    }
}
