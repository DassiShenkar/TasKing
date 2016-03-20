package com.tasking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private boolean isUpdate;
    private Task taskToUpdate;


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
            taskToUpdate = TaskDAO.getInstance(this).getTask(taskId);
            EditText name = (EditText) findViewById(R.id.edit_task_name);
            EditText category = (EditText) findViewById(R.id.edit_category);
           // EditText priority = (EditText) findViewById(R.id.edit_priority);
            EditText date = (EditText) findViewById(R.id.edit_date);
            EditText time = (EditText) findViewById(R.id.edit_time);
            EditText location = (EditText) findViewById(R.id.edit_location);
            name.setText(taskToUpdate.getName());
            category.setText(taskToUpdate.getCategory());
          //  priority.setText(taskToUpdate.getPriority());
            date.setText(taskToUpdate.getDateString());
            time.setText(taskToUpdate.getTimeString());
            location.setText(taskToUpdate.getLocation());
        }
    }

    public void done(View view){
        EditText name = (EditText) findViewById(R.id.edit_task_name);
        EditText category = (EditText) findViewById(R.id.edit_category);
       // EditText priority = (EditText) findViewById(R.id.edit_priority);
        EditText date = (EditText) findViewById(R.id.edit_date);
        EditText time = (EditText) findViewById(R.id.edit_time);
        EditText location = (EditText) findViewById(R.id.edit_location);
        String taskName = name.getText().toString();
        String taskCategory = category.getText().toString();
        String taskPriority = "Normal";
        String taskLocation = location.getText().toString();
        String taskDate = date.getText().toString();
        String taskTime = time.getText().toString();
        Bundle userParams = getIntent().getExtras();
        Spinner spinner = (Spinner) findViewById(R.id.spn_add_member);
        String employeeName = spinner.getSelectedItem().toString();
        if(!taskName.equals("") && !taskCategory.equals("") && !taskPriority.equals("") && !taskDate.equals("") && !taskTime.equals("") && !taskLocation.equals("")) {
            if(isUpdate){
                taskToUpdate.setName(taskName);
                taskToUpdate.setDateFromString(taskTime, taskDate);
                taskToUpdate.setCategory(taskCategory);
                taskToUpdate.setPriority(taskPriority);
                taskToUpdate.setAssignee(employeeName);
                taskToUpdate.setLocation(taskLocation);
                TaskDAO.getInstance(this).updateTask(taskToUpdate);
                isUpdate = false;
                userParams.remove("taskId");
            }
            else {
                Task task = new Task();
                task.setName(taskName);
                task.setDateFromString(taskTime, taskDate);
                task.setCategory(taskCategory);
                task.setPriority(taskPriority);
                task.setStatus("WAITING");
                task.setAssignee(employeeName);
                task.setLocation(taskLocation);
                TaskDAO.getInstance(this).addTask(task);
            }
            //TODO: add task to firebaseDB & setFirebaseDBId();
            Intent intent = new Intent(this, TasksActivity.class);
            intent.putExtras(userParams);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeString = hourOfDay + ":" + minute; //
            EditText b = (EditText)getActivity().findViewById(R.id.edit_time);
            b.setText(timeString);
        }
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dateString = year + "-" + month + "-" + day; //
            EditText b = (EditText)getActivity().findViewById(R.id.edit_date);
            b.setText(dateString);
        }
    }
    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
