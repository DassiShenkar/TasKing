package com.tasking;

/**
 * Created by Grisha on 1/6/2016.
 */
public class Employee {
    private int id;
    private String name;
    private String userName;
    private String password;
    private int isManager;

    public Employee() {

    }

    public Employee(String userName, String password, int isManager) {
        this.userName = userName;
        this.password = password;
        this.isManager = isManager;
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

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", isManager=" + isManager +
                '}';
    }
}
