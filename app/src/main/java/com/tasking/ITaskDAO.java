package com.tasking;

import java.util.ArrayList;

/**
 * Created by Grisha on 1/2/2016.
 */
public interface ITaskDAO {

    void addTeamMember(Employee member);
    void removeTeamMember(String userName);
    Employee getTeamMember(String userName);
    ArrayList<Employee> getTeamMembers();
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
