package com.tasking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private  NumberPicker np;
    private CheckForNewTasks checkForNewTasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Bundle userParams = getIntent().getExtras();
        checkForNewTasks = CheckForNewTasks.getInstance(SettingsActivity.this,userParams);
        np = (NumberPicker) findViewById(R.id.number_curr_interval);
        np.setMinValue(5);
        np.setMaxValue(60);
        np.setValue(checkForNewTasks.getTime());
        np.setWrapSelectorWheel(false);
    }
    public void save(View view){
        if(np.getValue() != checkForNewTasks.getTime() ){ // time interval in setting changed
            checkForNewTasks.setTime(np.getValue());
            checkForNewTasks.stopSchedule();
            checkForNewTasks.startSchedule(np.getValue());
        }
        Toast.makeText(getApplication(), "Check for new Tasks every "+np.getValue()+" minutes.", Toast.LENGTH_SHORT).show();
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }
}
