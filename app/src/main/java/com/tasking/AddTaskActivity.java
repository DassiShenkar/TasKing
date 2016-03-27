package com.tasking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private boolean isUpdate;
    private Task taskToUpdate;
    private String selectedRadio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        RadioButton low = (RadioButton) findViewById(R.id.radio_low);
        RadioButton normal = (RadioButton) findViewById(R.id.radio_normal);
        RadioButton urgent = (RadioButton) findViewById(R.id.radio_urgent);
        low.setTypeface(typeFace);
        normal.setTypeface(typeFace);
        urgent.setTypeface(typeFace);
        RelativeLayout photo = (RelativeLayout) findViewById(R.id.photo_layout);
        photo.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        Bundle userParams = getIntent().getExtras();
        if(!userParams.getBoolean("isManager")){
            RelativeLayout assigne = (RelativeLayout) findViewById(R.id.assign_layout);
            assigne.setVisibility(View.GONE);
        }
        RadioGroup priority = (RadioGroup) findViewById(R.id.radGrp_priority);
        int checked = priority.getCheckedRadioButtonId();
        RadioButton selected = (RadioButton) findViewById(checked);
        selectedRadio = selected.getText().toString();
        priority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected = (RadioButton) findViewById(checkedId);
                selectedRadio = selected.getText().toString();
            }
        });
        int taskId = userParams.getInt("taskId");
        final Spinner categorySpinner = (Spinner) findViewById(R.id.spn_category);
        final Spinner locationSpinner = (Spinner) findViewById(R.id.spn_location);
        String[] categoryItems = getResources().getStringArray(R.array.category_array);
        MyArrayAdapter<String> categorySpinnerAdapter = new MyArrayAdapter<>(this, R.layout.spinner_item, categoryItems);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        String[] locationItems = getResources().getStringArray(R.array.location_array);
        MyArrayAdapter<String> locationSpinnerAdapter = new MyArrayAdapter<>(this, R.layout.spinner_item, locationItems);
        locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationSpinnerAdapter);
        if(userParams.getBoolean("isManager")) {
            final Spinner assigneeSpinner = (Spinner) findViewById(R.id.spn_add_member);
            ArrayList<Employee> employees = TaskDAO.getInstance(this).getMembers(userParams.getString("uid"));
            ArrayList<String> employeeNames = new ArrayList<>();
            for (Employee e : employees) {
                employeeNames.add(e.getUsername());
            }
            MyArrayAdapter<String> assigneeSpinnerAdapter = new MyArrayAdapter<>(this, R.layout.spinner_item, employeeNames);
            assigneeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            assigneeSpinner.setAdapter(assigneeSpinnerAdapter);
        }
        if(taskId != 0){
            isUpdate = true;
            taskToUpdate = TaskDAO.getInstance(this).getTask(taskId);
            if(taskToUpdate.getPicture() != null){
                photo.setVisibility(View.VISIBLE);
                ImageView imageview = (ImageView) findViewById(R.id.img_show_photo);
                byte[] encodeByte= Base64.decode(taskToUpdate.getPicture(), Base64.DEFAULT);
                Bitmap imageBitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                imageview.setImageBitmap(imageBitmap);
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            EditText name = (EditText) findViewById(R.id.edit_task_name);
            TextView date = (TextView) findViewById(R.id.edit_date);
            TextView time = (TextView) findViewById(R.id.edit_time);
            name.setText(taskToUpdate.getName());
            date.setText(taskToUpdate.getDateString());
            time.setText(taskToUpdate.getTimeString());
        }
    }

    public void done(View view){
        EditText name = (EditText) findViewById(R.id.edit_task_name);
        Spinner categorySpinner = (Spinner) findViewById(R.id.spn_category);
        String taskCategory = categorySpinner.getSelectedItem().toString();
        TextView date = (TextView) findViewById(R.id.edit_date);
        TextView time = (TextView) findViewById(R.id.edit_time);
        Spinner locationSpinner = (Spinner) findViewById(R.id.spn_location);
        String taskLocation = locationSpinner.getSelectedItem().toString();
        String taskName = name.getText().toString();
        //TODO: extra credit scan barcode created class
        String taskDate = date.getText().toString();
        String taskTime = time.getText().toString();
        Bundle userParams = getIntent().getExtras();
        String employeeName = null;
        if(userParams.getBoolean("isManger")) {
            Spinner spinner = (Spinner) findViewById(R.id.spn_add_member);
            employeeName = spinner.getSelectedItem().toString();
        }
        Task task = new Task();


        if(!taskName.equals("") && !taskCategory.equals("") && !taskDate.equals("") && !taskTime.equals("") && !taskLocation.equals("")) {
            if(isUpdate){
                taskToUpdate.setName(taskName);
                taskToUpdate.setDateFromString(taskTime, taskDate);
                taskToUpdate.setCategory(taskCategory);
                taskToUpdate.setPriority(selectedRadio);
                if(userParams.getBoolean("isManger")){
                    taskToUpdate.setAssignee(employeeName);
                }
                else{
                    taskToUpdate.setAssignee("Self");
                }
                taskToUpdate.setLocation(taskLocation);
                TaskDAO.getInstance(this).updateTask(taskToUpdate);
                isUpdate = false;
                userParams.remove("taskId");
            }
            else {
                final Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
                String managerUid;
                if(userParams.getBoolean("isManager")){
                    managerUid = userParams.getString("uid");
                }
                else {
                    Employee member = TaskDAO.getInstance(this).getMember(userParams.getString("uid"));
                    managerUid = member.getManagerId();
                }
                Firebase postRef = firebase.child("Managers").child(managerUid).child("tasks").push();
                String postId = postRef.getKey();  // create new uid for task
                task.setName(taskName);
                task.setDateFromString(taskTime, taskDate);
                task.setCategory(taskCategory);
                task.setPriority(selectedRadio);
                task.setAcceptStatus("Waiting");
                task.setStatus("Waiting");
                if(userParams.getBoolean("isManger")){
                    task.setAssignee(employeeName);
                }
                else{
                    task.setAssignee("Self");//TODO: check later
                }
                task.setLocation(taskLocation);
                task.setFirebaseId(postId);
                //TODO: add assignee uid
                task.setUserId(userParams.getString("uid"));
                task.setManagerUid(managerUid);
                TaskDAO.getInstance(this).addTask(task);
                firebase.child("managers").child(managerUid).child("tasks").child(postId).setValue(task);


                //TODO: Show toast: “New Task created and sent”
//                firebase.child("managers").child(uid).child("team").child(member.getUid()).child("tasks").child(postId).setValue(task);
//                //TODO: debug and see why member.getUid() returns a null
//                }
//                else {
//                    //TODO: get manager uid and then activate the code below
//
//
//                    DataSnapshot DS = new DataSnapshot(firebase, com.firebase.client.snapshot.IndexedNode node);
//                    String managerUid =  firebase.child("member-manager").child(uid).getKey();
//                    firebase.child("managers").child(managerUid).child("tasks").child(postId).setValue(task);
//                    firebase.child("managers").child(managerUid).child("team").child(uid).child("tasks").child(postId).setValue(task);
//                }
            }
            Intent intent = new Intent(this, TasksActivity.class);
            intent.putExtras(userParams);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view) {
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
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
            TextView b = (TextView) getActivity().findViewById(R.id.edit_time);
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
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ++month;
            String dateString = year + "-" + month + "-" + day; //
            TextView b = (TextView) getActivity().findViewById(R.id.edit_date);
            b.setText(dateString);
        }
    }
    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
