package com.tasking;

public interface MyCallback<T> {
    void done(T result, T error, boolean isManager, boolean hasTeam);
}
