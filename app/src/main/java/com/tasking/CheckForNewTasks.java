package com.tasking;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class CheckForNewTasks {

    private static CheckForNewTasks instance = null;
    private int time;
    private Timer timer;
    private Context context;
    private Bundle userParams;

    public static CheckForNewTasks getInstance(Context context, Bundle userParams) {
        if (instance == null) {
            instance = new CheckForNewTasks(context, userParams);
        }
        return instance;
    }

    public CheckForNewTasks(Context context, Bundle userParams) {
        this.context = context;
        this.userParams = userParams;
        this.time = 5;
        timer = new Timer();
        startSchedule(time);
    }

    public void stopSchedule() {
        timer.cancel();
        timer.purge();
    }

    public void startSchedule(int time) {
        if(time > 0) {
            timer.schedule(checkNewTasks, 1, 1000 * 60 * time);
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    TimerTask checkNewTasks = new TimerTask() {
        @Override
        public void run() {
            FireBaseDB remote = new FireBaseDB(context);
            remote.refresh(userParams, new MyCallback<String>() {
            @Override
            public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                if (error != null) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
    };
}

