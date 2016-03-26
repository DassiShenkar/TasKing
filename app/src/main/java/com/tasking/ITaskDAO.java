package com.tasking;

import java.util.ArrayList;


public interface ITaskDAO {


    void addTask(Task task);
    void updateTask(Task task);
    Task getTask(int id);
    ArrayList<Task> getTasks();
    void addMember(Employee employee);
    void removeMember(Employee employee);
    ArrayList<Employee> getMembers(String uid);

//    void addTeamMember(Employee member);
//    void removeTeamMember(String userName);
//    Employee getTeamMember(String userName);
//    Employee getTeamMember(String userName, String managerName);
//    ArrayList<String> getTeamMembers(String userName);
//    void updateTeamMember(Employee member);
//    int getMemberCount();
//    void addTask(Task task, String managerName, String userName);
//    void removeTask(int taskId);
//    Task getTask(int taskId);
//    ArrayList<Task> getTasks(String name);
//    int getTaskCount(int userID);
//    void updateTask(Task task, ArrayList<Employee> employees);
//    boolean hasMembers(String name);
//    void addMemberManager(String member, String manager);
//    void addMemberTask(Employee employee, int taskId);

}
