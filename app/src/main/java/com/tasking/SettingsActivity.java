package com.tasking;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private CheckForNewTasks checkForNewTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Typeface coolTypeface = Typeface.createFromAsset(getAssets(), "fonts/Scriptorama-Tradeshow-JF.ttf");
        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(coolTypeface);
        Bundle userParams = getIntent().getExtras();
        checkForNewTasks = CheckForNewTasks.getInstance(SettingsActivity.this,userParams);
    }

    public void save(View view){
        EditText np = (EditText) findViewById(R.id.number_curr_interval);
        if(np.getText() != null) {
            int picked = Integer.valueOf(np.getText().toString());
            if (picked != checkForNewTasks.getTime()) {
                checkForNewTasks.setTime(picked);
                checkForNewTasks.stopSchedule();
                checkForNewTasks.startSchedule();
            }
            Toast.makeText(getApplication(), "Check for new Tasks every " + picked + " minutes.", Toast.LENGTH_SHORT).show();
            Bundle userParams = getIntent().getExtras();
            Intent intent = new Intent(this, TasksActivity.class);
            intent.putExtras(userParams);
            startActivity(intent);
        }
    }
}
