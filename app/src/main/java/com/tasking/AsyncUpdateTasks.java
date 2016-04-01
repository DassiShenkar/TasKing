package com.tasking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class AsyncUpdateTasks extends AsyncTask<Void, Void, ArrayList<String>> {

    private Context context;
    private RecyclerView.Adapter adapter;
    private boolean isManager;
    private String uid;
    private ArrayList<String> uids;
    private Bundle userParams;
    private DataSnapshot snapshot;

    public AsyncUpdateTasks(Context context, RecyclerView.Adapter adapter, Bundle userParams, DataSnapshot snapshot) {
        this.context = context;
        this.adapter = adapter;
        this.userParams = userParams;
        this.isManager = this.userParams.getBoolean("isManager");
        this.uid = this.userParams.getString("uid");
        this.snapshot = snapshot;
        uids = new ArrayList<>();
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
                uids.add("Data");
                ArrayList<Task> localTasks = TaskDAO.getInstance(context).getTasks(uid, isManager);
                ArrayList<Task> remoteTasks = new ArrayList<>();
                Date localDate = null;
                Date firebaseDate = null;
                String managerUid;
                if(isManager){
                    managerUid = uid;
                }
                else{
                    managerUid = snapshot.child("member-manager").child(uid).getValue().toString();
                }
                for (DataSnapshot task : snapshot.child(managerUid).child("tasks").getChildren()) {
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
                if(isManager) {
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
                }
                else{
                    if (addList.size() > 0) {
                        addList.removeAll(local);
                        for (Task t : addList) {
                            if (t.getAssigneeUid().equals(uid)) {
                                TaskDAO.getInstance(context).addTask(t);
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
                if(adapter != null) {
                    adapter.notifyDataSetChanged();
                }
        return uids;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result){
        if(uids.size() > 0){
            String msg = "You have " + uids.size() + "New Tasks\nView/Cancel";
            new AlertDialog.Builder(context)
                    .setTitle("New Task/s found")
                    .setMessage(msg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(context, TasksActivity.class);
                            intent.putExtras(userParams);
                            context.startActivity(intent);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        else{
            Toast.makeText(context, "No new Tasks", Toast.LENGTH_SHORT).show();

        }
    }
}
