package com.tasking;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AsyncUpdateTasks extends AsyncTask<Void, Void, ArrayList<String>> {

    private Context context;
    private Firebase firebase;
    private ArrayList<String> uids;
    private String uid;

    public AsyncUpdateTasks(Context context, String uid, boolean isManager) {
        this.context = context;
        firebase = new Firebase("https://tasking-android.firebaseio.com/");
        uids = new ArrayList<>();
        this.uid = uid;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

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
                if (remoteTasks.size() > 0) {
                    remoteTasks.removeAll(localTasks);
                    for (Task task : remoteTasks) {
                        if (task.getAssigneeUid().equals(uid)) {
                            TaskDAO.getInstance(context).addTask(task);
                        }
                    }
                }
                if (localTasks.size() > 0) {
                    localTasks.removeAll(remoteTasks);
                    for (Task task : localTasks) {
                        if (task.getAssigneeUid().equals(uid)) {
                            TaskDAO.getInstance(context).removeTask(task.getFirebaseId());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return uids;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result){
        // invoked on the UI thread after the background computation finishes
    }
}
