package com.tasking.tasking;

import java.util.ArrayList;

/**
 * Created by Grisha on 1/2/2016.
 */
public interface ITaskDAO {

    void addTeamMember(TeamMember member);
    void removeTeamMember(String userName);
    TeamMember getTeamMember(String userName);
    ArrayList<TeamMember> getTeamMembers();
    int updateTeamMember(TeamMember member);
    void addTask(Task task, int userId);
    void removeTask(String userName);
    Task getTask(String name);
    ArrayList<Task> getTasks(String name);
    int getTaskCount(int userID);
    int updateTask(Task task);
    ArrayList<Task> getMemberTasks(int userID);

}
