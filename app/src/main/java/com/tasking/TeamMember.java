package com.tasking;


public class TeamMember extends Employee{


    public TeamMember() {
    }

    public TeamMember(String userName, String password, int isManager) {
        super(userName, password, isManager);
    }

    public TeamMember(String userName, String password, int isManager, int id) {
        super(userName, password, isManager, id);
    }



}
