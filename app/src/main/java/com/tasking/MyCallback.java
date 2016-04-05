package com.tasking;

public interface MyCallback<T> {
    void done(T result, T error, String managerUid, boolean isManager, boolean hasTeam);
}
