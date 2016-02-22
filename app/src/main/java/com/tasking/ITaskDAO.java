package com.tasking;

import java.util.ArrayList;


public interface ITaskDAO {

    void addTeamMember(Employee member);
    void removeTeamMember(String userName);
    Employee getTeamMember(String userName);
    Employee getTeamMember(String userName, String managerName);
    ArrayList<Employee> getTeamMembers(String userName);
    void updateTeamMember(Employee member);
    int getMemberCount();
    void addTask(Task task, ArrayList<Integer> userId);
    void removeTask(String userName);
    Task getTask(String name);
    ArrayList<Task> getTasks(String name);
    int getTaskCount(int userID);
    void updateTask(Task task);
    boolean hasMembers(String name);

}
