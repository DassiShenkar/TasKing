package com.tasking;

/**
 * Created by Grisha on 1/2/2016.
 */
public class TeamMember extends Employee{
    private int id;


    public TeamMember() {
    }

    public TeamMember(String userName, String password, int isManager) {
        super(userName, password, isManager);
    }

    public TeamMember(String userName, String password, int isManager, int id) {
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
