package com.tasking;

import java.util.ArrayList;


public interface ITaskDAO {

    void addTask(Task task);
    void updateTask(Task task);
    Task getTask(String uid);
    ArrayList<Task> getTasks(String uid, boolean isManager);
    void addMember(Employee employee, String uid, String mUid);
    void removeMember(Employee employee);
    Employee getMember(String uid);
    ArrayList<Employee> getMembers(String uid);
    void removeTask(String taskUid);
}
