package com.tasking.tasking;

import java.util.ArrayList;

/**
 * Created by Grisha on 1/2/2016.
 */
public interface ITaskDAO {

    void addTeamMember(Employee member);
    void removeTeamMember(String userName);
    Employee getTeamMember(String userName);
    ArrayList<Employee> getTeamMembers();
    int updateTeamMember(Employee member);
    int getMemberCount();
    void addTask(Task task, int userId);
    void removeTask(String userName);
    Task getTask(String name);
    ArrayList<Task> getTasks(String name);
    int getTaskCount(int userID);
    int updateTask(Task task);
    ArrayList<Task> getMemberTasks(int userID);

}
