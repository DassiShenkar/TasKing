package com.tasking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class AsyncUpdateTasks extends AsyncTask<Void, Void, ArrayList<String>> {

    private Context context;
    private boolean isManager;
    private String uid;
    private ArrayList<String> uids;
    private Bundle userParams;
    private DataSnapshot snapshot;

    public AsyncUpdateTasks(Context context, Bundle userParams, DataSnapshot snapshot) {
        this.context = context;
        this.userParams = userParams;
        this.isManager = this.userParams.getBoolean("isManager");
        this.uid = this.userParams.getString("uid");
        this.snapshot = snapshot;
        uids = new ArrayList<>();
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        if(snapshot != null) {
            ArrayList<Task> localTasks = TaskDAO.getInstance(context).getTasks(uid, isManager);
            ArrayList<Task> remoteTasks = new ArrayList<>();
            Date localDate = null;
            Date firebaseDate = null;
            String managerUid;
            if (isManager) {
                managerUid = uid;
            } else {
                managerUid = snapshot.child("member-manager").child(uid).getValue().toString();
            }
            for (DataSnapshot task : snapshot.child("managers").child(managerUid).child("tasks").getChildren()) {
                Task taskToAdd = task.getValue(Task.class);
                remoteTasks.add(taskToAdd);
                if (taskToAdd.getAssigneeUid().equals(uid)) {
                    for (Task localTask : localTasks) {
                        if (localTask.getFirebaseId().equals(taskToAdd.getFirebaseId())) {
                            try {
                                localDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).parse(localTask.getTimeStamp());
                                firebaseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).parse(taskToAdd.getTimeStamp());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (localDate != null && firebaseDate != null) {
                                if (localDate.before(firebaseDate)) {
                                    TaskDAO.getInstance(context).updateTask(taskToAdd);
                                    uids.add(taskToAdd.getFirebaseId());
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
            if (isManager) {
                if (addList.size() > 0) {
                    addList.removeAll(local);
                    for (Task t : addList) {
                        if (t.getManagerUid().equals(uid)) {
                            TaskDAO.getInstance(context).addTask(t);
                            uids.add(t.getFirebaseId());
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
            } else {
                if (addList.size() > 0) {
                    addList.removeAll(local);
                    for (Task t : addList) {
                        if (t.getAssigneeUid().equals(uid)) {
                            TaskDAO.getInstance(context).addTask(t);
                            uids.add(t.getFirebaseId());
                        }
                    }
                }
                if (removeList.size() > 0) {
                    removeList.removeAll(remote);
                    for (Task t : removeList) {
                        if (t.getAssigneeUid().equals(uid)) {
                            TaskDAO.getInstance(context).removeTask(t.getFirebaseId());
                        }
                    }
                }
            }
        }
        return uids;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (snapshot != null) {
            if (uids.size() > 0) {
                String msg = "You have " + uids.size() + " New Tasks\nView/Cancel";
                new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                        .setTitle("New Task/s found")
                        .setMessage(msg)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent;
                                if (uids.size() == 1) {
                                    if (isManager) {
                                        userParams.putString("taskUid", uids.get(0));
                                        intent = new Intent(context, AddTaskActivity.class);
                                    } else {
                                        userParams.putString("taskUid", uids.get(0));
                                        intent = new Intent(context, ViewTaskActivity.class);
                                    }
                                } else {
                                    userParams.putStringArrayList("taskUids", uids);
                                    intent = new Intent(context, TasksActivity.class);
                                }
                                intent.putExtras(userParams);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            } else {
                Toast.makeText(context, "No new Tasks", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
