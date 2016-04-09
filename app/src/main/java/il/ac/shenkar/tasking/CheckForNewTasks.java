package il.ac.shenkar.tasking;

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
        startSchedule();
    }

    public void stopSchedule() {
       if(timer !=null){
           this.timer.cancel();
           this.timer.purge();
       }

    }

    public void startSchedule() {
        timer = new Timer();
        timer.schedule(new firstTask(), 1, 1000 * 60 * this.time);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        if(time>0) {
            this.time = time;
        }
    }

    class firstTask extends TimerTask {

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
    }
}

