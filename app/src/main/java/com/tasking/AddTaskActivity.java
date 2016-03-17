package com.tasking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {

    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        Bundle userParams = getIntent().getExtras();
        int taskId = userParams.getInt("taskId");
        final Spinner spinner = (Spinner) findViewById(R.id.spn_add_member);
        ArrayList<Employee> employees = TaskDAO.getInstance(this).getMembers();
        ArrayList<String> employeeNames = new ArrayList<>();
        for(Employee e: employees){
            employeeNames.add(e.getUserName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeeNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        if(taskId != 0){
            isUpdate = true;
            Task task = TaskDAO.getInstance(this).getTask(taskId);
            EditText name = (EditText) findViewById(R.id.edit_task_name);
            EditText category = (EditText) findViewById(R.id.edit_category);
            EditText priority = (EditText) findViewById(R.id.edit_priority);
            EditText date = (EditText) findViewById(R.id.edit_date);
            EditText time = (EditText) findViewById(R.id.edit_time);
            EditText location = (EditText) findViewById(R.id.edit_location);
            name.setText(task.getName());
            category.setText(task.getCategory());
            priority.setText(task.getPriority());
            date.setText(task.getDateString());
            time.setText(task.getTimeString());
            location.setText(task.getLocation());
        }
    }

    public void done(View view){
        EditText name = (EditText) findViewById(R.id.edit_task_name);
        EditText category = (EditText) findViewById(R.id.edit_category);
        EditText priority = (EditText) findViewById(R.id.edit_priority);
        EditText date = (EditText) findViewById(R.id.edit_date);
        EditText time = (EditText) findViewById(R.id.edit_time);
        EditText location = (EditText) findViewById(R.id.edit_location);
        String taskName = name.getText().toString();
        String taskCategory = category.getText().toString();
        String taskPriority = priority.getText().toString();
        String taskLocation = location.getText().toString();
        String taskDate = date.getText().toString();
        String taskTime = time.getText().toString();
        Bundle userParams = getIntent().getExtras();
        Spinner spinner = (Spinner) findViewById(R.id.spn_add_member);
        String employeeName = spinner.getSelectedItem().toString();
        if(!taskName.equals("") && !taskCategory.equals("") && !taskPriority.equals("") && !taskDate.equals("") && !taskTime.equals("") && !taskLocation.equals("")) {
            Task task = new Task();
            task.setName(taskName);
            task.setDateFromString(taskTime, taskDate);
            task.setCategory(taskCategory);
            task.setPriority(taskPriority);
            task.setStatus("WAITING");
            task.setAssignee(employeeName);
            task.setLocation(taskLocation);
            if(isUpdate){
                TaskDAO.getInstance(this).updateTask(task);
                isUpdate = false;
                userParams.remove("taskId");
            }
            else {
                TaskDAO.getInstance(this).addTask(task);
            }
            //TODO setId(); from return value from addTask(task)(not implemented)
            //TODO: add task to firebaseDB & setFirebaseDBId();
            Intent intent = new Intent(this, TasksActivity.class);
            intent.putExtras(userParams);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
