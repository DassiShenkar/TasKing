package com.tasking.tasking;

/**
 * Created by Grisha on 1/2/2016.
 */
public interface ITaskDAO {
    boolean addTeamMember(Employee e);
    boolean removeTeamMember(int id);
    boolean addTask(Task t);
    boolean removeTask(int id);
}
