package com.tasking.tasking;

/**
 * Created by Grisha on 1/2/2016.
 */
public class Manager extends Employee{
    private int id;

    public Manager() {
    }

    public Manager(String userName, String password, int isManager) {
        super(userName, password, isManager);
    }

    public Manager(String userName, String password, int isManager, int id) {
        super(userName, password, isManager);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}