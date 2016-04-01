package com.tasking;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.client.Firebase;

public class AsyncAddTask extends AsyncTask<Task, Void, String> {

    private Context context;
    private Firebase firebase;


    public AsyncAddTask(Context context, boolean isManager) {
        this.context = context;
        firebase = new Firebase("https://tasking-android.firebaseio.com/");
    }

    @Override
    protected String doInBackground(Task... params) {

        return null;
    }

    @Override
    protected void onPostExecute(String result){
        // invoked on the UI thread after the background computation finishes
    }
}
