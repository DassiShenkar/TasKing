package com.tasking;

import android.os.Bundle;

import java.util.Map;

/**
 * Created by user on 05/04/2016.
 */
public interface IFireBaseDB {
    void createManager(final String username, String password, final MyCallback<String> callback);
    void createMember(final String username, String password, final String managerUid, final String teamName, final MyCallback<String> callback);
    void authenticate(final String username, String password, final MyCallback<String> callback);
    void updateTask(Task task, final Map<String, Object> update, final MyCallback<String> callback);
    void addTask(final Task task, final MyCallback<String> callback);
    String getTaskKey(String managerUid);
    void removeUser(String username, String password, final MyCallback<String> callback);
    void resetPassword(String username, final MyCallback<String> callback);
    void refresh(Bundle userParams, MyCallback<String> callback);
}
