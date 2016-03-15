package com.tasking;


public class Manager extends Employee{

    public Manager() {
    }

    public Manager(String userName, String password, int isManager) {
        super(userName, password, isManager);
    }
    public Manager(String userName, String password, int isManager,String uid) {
        super(userName, password, isManager);

    }

}