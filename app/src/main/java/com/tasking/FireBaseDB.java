package com.tasking;

import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


public class FireBaseDB {

    private static FireBaseDB instance = null;
    private Firebase firebaseConnection;
    private Context context;

    public static FireBaseDB getInstance(Context context) {
        if(instance == null) {
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
            instance = new FireBaseDB(context);
        }
        return instance;
    }

    private FireBaseDB(Context context) {
        firebaseConnection = new Firebase("https://tasking-android.firebaseio.com/");
        this.context = context;
    }


    public  void createUser(final String username, String password, final MyCallback<String> callback){
        firebaseConnection.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public synchronized void onSuccess(Map<String, Object> result) {
                String uid = result.get("uid").toString();
                firebaseConnection.child("managers").child(uid).child("username").setValue(username);
                if(callback != null){
                    callback.done(uid, null, true, false);
                }
            }

            @Override
            public synchronized void onError(FirebaseError firebaseError) {
                if(callback != null){
                    callback.done(null, firebaseError.getMessage(), false, false);
                }
            }
        });
    }

    public void authtenticate(final String username, String password, final MyCallback<String> callback){
        firebaseConnection.authWithPassword(username, password,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        final String uid = authData.getUid();
                        if (uid != null) {
                            firebaseConnection.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.child("managers").child(uid).child("username").getValue() != null) {
                                        if (snapshot.child("managers").child(uid).getChildrenCount() > 1) {
                                            ArrayList<Employee> localTeam = TaskDAO.getInstance(context).getMembers(uid);
                                            if (localTeam.size() == 0) {
                                                for (DataSnapshot teamMember : snapshot.child("managers").child(uid).child("team").getChildren()) {
                                                    String memberUid = teamMember.getKey();
                                                    Employee employee = teamMember.getValue(Employee.class);
                                                    TaskDAO.getInstance(context).addMember(employee, memberUid, uid);
                                                }
                                            }
                                            ArrayList<Task> localTasks = TaskDAO.getInstance(context).getTasks(uid, true);
                                            ArrayList<Task> remoteTasks = new ArrayList<>();
                                            Date localDate = null;
                                            Date firebaseDate = null;
                                            for (DataSnapshot task : snapshot.child("managers").child(uid).child("tasks").getChildren()) {
                                                Task taskToAdd = task.getValue(Task.class);
                                                remoteTasks.add(taskToAdd);
                                                for (Task localTask : localTasks) {
                                                    if (localTask.getFirebaseId().equals(taskToAdd.getFirebaseId())) {
                                                        try {
                                                            localDate = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(localTask.getTimeStamp());
                                                            firebaseDate = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(taskToAdd.getTimeStamp());
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (localDate != null && firebaseDate != null) {
                                                            if (localDate.before(firebaseDate)) {
                                                                TaskDAO.getInstance(context).updateTask(taskToAdd);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            Collection<Task> remote = new ArrayList<>(remoteTasks);
                                            Collection<Task> local = new ArrayList<>(localTasks);
                                            ArrayList<Task> addList = new ArrayList<>(remote);
                                            ArrayList<Task> removeList = new ArrayList<>(local);
                                            if (addList.size() > 0) {
                                                addList.removeAll(local);
                                                for (Task t : addList) {
                                                    if (t.getManagerUid().equals(uid)) {
                                                        TaskDAO.getInstance(context).addTask(t);
                                                    }
                                                }
                                            }
                                            if (removeList.size() > 0) {
                                                removeList.removeAll(remote);
                                                for (Task t : removeList) {
                                                    if (t.getManagerUid().equals(uid)) {
                                                        TaskDAO.getInstance(context).removeTask(t.getFirebaseId());
                                                    }
                                                }
                                            }
                                            if (callback != null) {
                                                callback.done(uid, null, true, true);
                                            }
                                        } else {
                                            if (callback != null) {
                                                callback.done(uid, null, true, false);
                                            }
                                        }
                                    } else {
                                        ArrayList<Task> localTasks = TaskDAO.getInstance(context).getTasks(uid, false);
                                        ArrayList<Task> remoteTasks = new ArrayList<>();
                                        Date localDate = null;
                                        Date firebaseDate = null;
                                        String managerUid = snapshot.child("member-manager").child(uid).getValue().toString();
                                        for (DataSnapshot task : snapshot.child("managers").child(managerUid).child("tasks").getChildren()) {
                                            Task taskToAdd = task.getValue(Task.class);
                                            remoteTasks.add(taskToAdd);
                                            if (taskToAdd.getAssigneeUid().equals(uid)) {
                                                for (Task localTask : localTasks) {
                                                    if (localTask.getFirebaseId().equals(taskToAdd.getFirebaseId())) {
                                                        try {
                                                            localDate = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(localTask.getTimeStamp());
                                                            firebaseDate = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(taskToAdd.getTimeStamp());
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (localDate != null && firebaseDate != null) {
                                                            if (localDate.before(firebaseDate)) {
                                                                TaskDAO.getInstance(context).updateTask(taskToAdd);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Collection<Task> remote = new ArrayList<>(remoteTasks);
                                        Collection<Task> local = new ArrayList<>(localTasks);
                                        ArrayList<Task> addList = new ArrayList<>(remote);
                                        ArrayList<Task> removeList = new ArrayList<>(local);
                                        if (addList.size() > 0) {
                                            addList.removeAll(local);
                                            for (Task task : addList) {
                                                if (task.getAssigneeUid().equals(uid)) {
                                                    TaskDAO.getInstance(context).addTask(task);
                                                }
                                            }
                                        }
                                        if (removeList.size() > 0) {
                                            removeList.removeAll(remote);
                                            for (Task task : removeList) {
                                                if (task.getAssigneeUid().equals(uid)) {
                                                    TaskDAO.getInstance(context).removeTask(task.getFirebaseId());
                                                }
                                            }
                                        }
                                        if (callback != null) {
                                            callback.done(uid, null, false, false);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    if (callback != null) {
                                        callback.done(null, firebaseError.getMessage(), false, false);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        if (callback != null) {
                            callback.done(null, firebaseError.getMessage(), false, false);
                        }
                    }
                });

    }

    public void updateTask(Task task, final Map<String, Object> update, final MyCallback<String> callback){
        firebaseConnection.child("managers").child(task.getManagerUid()).child("tasks").child(task.getFirebaseId()).updateChildren(update, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    if (callback != null) {
                        callback.done(null, firebaseError.getMessage(), false, false);
                    }
                } else {
                    if (callback != null) {
                        callback.done(null, "Task was updated", false, false);
                    }
                }
            }
        });
    }

    public void addTask(Task task, final MyCallback<String> callback){
        firebaseConnection.child("managers").child(task.getManagerUid()).child("tasks").child(task.getFirebaseId()).setValue(task, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    if (callback != null) {
                        callback.done(null, firebaseError.getMessage(), false, false);
                    }
                } else {
                    if (callback != null) {
                        callback.done(null, "New Task created and sent", false, false);
                    }
                }
            }
        });
    }

    public void removeUser(String username, String password, final MyCallback<String> callback){
        firebaseConnection.removeUser(username, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.done(null, "User removed", false, false);
                }
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                if (callback != null) {
                    callback.done(null, firebaseError.getMessage(), false, false);
                }
            }
        });
    }

    public void resetPassword(String username, final MyCallback<String> callback){
        firebaseConnection.resetPassword(username, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.done(null, "New password was sent to your e-mail", false, false);
                }
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                if (callback != null) {
                    callback.done(null, firebaseError.getMessage(), false, false);
                }
            }
        });
    }
}
