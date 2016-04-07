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
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddTaskActivity extends AppCompatActivity {

    private boolean isUpdate;
    private Task taskToUpdate;
    private String selectedRadio;
    private Employee employee;

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
        final Bundle userParams = getIntent().getExtras();
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
        String taskUid = userParams.getString("taskUid");
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
        MyEmployeeArrayAdapter assigneeSpinnerAdapter = null;
        if(userParams.getBoolean("isManager")) {
            final Spinner assigneeSpinner = (Spinner) findViewById(R.id.spn_add_member);
            ArrayList<Employee> employees = TaskDAO.getInstance(this).getMembers(userParams.getString("uid"));
            assigneeSpinnerAdapter = new MyEmployeeArrayAdapter(this, R.layout.spinner_item, employees);
            assigneeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            assigneeSpinner.setAdapter(assigneeSpinnerAdapter);
            assigneeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    employee = (Employee) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if(taskUid != null){
            isUpdate = true;
            taskToUpdate = TaskDAO.getInstance(this).getTask(taskUid);
            if(taskToUpdate.getPicture() != null){
                photo.setVisibility(View.VISIBLE);
                ImageView imageview = (ImageView) findViewById(R.id.img_show_photo);
                byte[] encodeByte= Base64.decode(taskToUpdate.getPicture(), Base64.DEFAULT);
                final Bitmap imageBitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                imageview.setImageBitmap(imageBitmap);
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ShowImageActivity.class);
                        userParams.putParcelable("bitmap", imageBitmap);
                        intent.putExtras(userParams);
                        startActivity(intent);
                    }
                });
            }
            EditText name = (EditText) findViewById(R.id.edit_task_name);
            TextView date = (TextView) findViewById(R.id.edit_date);
            TextView time = (TextView) findViewById(R.id.edit_time);
            name.setText(taskToUpdate.getName());
            date.setText(taskToUpdate.convertDateString());
            time.setText(taskToUpdate.convertTimeString());
            String categoryValue = taskToUpdate.getCategory();
            String locationValue = taskToUpdate.getLocation();
            Spinner assigneeSpinner = (Spinner) findViewById(R.id.spn_add_member);
            if (employee != null) {
                int spinnerPosition = 0;
                if (assigneeSpinnerAdapter != null) {
                    spinnerPosition = assigneeSpinnerAdapter.getPosition(employee);
                }
                assigneeSpinner.setSelection(spinnerPosition);
            }
            if (categoryValue != null) {
                int spinnerPosition = categorySpinnerAdapter.getPosition(categoryValue);
                categorySpinner.setSelection(spinnerPosition);
            }
            if (locationValue != null) {
                int spinnerPosition = locationSpinnerAdapter.getPosition(locationValue);
                locationSpinner.setSelection(spinnerPosition);
            }
        }
    }

    public void scanQR(View view){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(intent != null) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String[] locationItems = getResources().getStringArray(R.array.location_array);
                Spinner locationSpinner = (Spinner) findViewById(R.id.spn_location);

                String[] newLocationItems = new String[locationItems.length + 1];
                System.arraycopy(locationItems, 0, newLocationItems, 0, newLocationItems.length - 1);
                newLocationItems[newLocationItems.length - 1] = scanContent;
                MyArrayAdapter<String> locationSpinnerAdapter = new MyArrayAdapter<>(this, R.layout.spinner_item, newLocationItems);
                locationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locationSpinner.setAdapter(locationSpinnerAdapter);
                int spinnerPosition = locationSpinnerAdapter.getPosition(scanContent);
                locationSpinner.setSelection(spinnerPosition);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
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
        String taskDate = date.getText().toString();
        String taskTime = time.getText().toString();
        Bundle userParams = getIntent().getExtras();
        String employeeName = null;
        if(userParams.getBoolean("isManager")) {
            Spinner spinner = (Spinner) findViewById(R.id.spn_add_member);
            Employee employee = (Employee)spinner.getSelectedItem();
            employeeName = employee.getUsername();
        }
        Task task = new Task();
        if(!taskName.equals("") && !taskCategory.equals("") && !taskDate.equals("") && !taskTime.equals("") && !taskLocation.equals("")) {
            String managerUid;
            if(userParams.getBoolean("isManager")){
                managerUid = userParams.getString("uid");
            }
            else {
                managerUid = userParams.getString("managerUid");
            }
            if(isUpdate){
                Map<String, Object> current = new HashMap<>();
                current.put("name", taskName);
                current.put("date", taskToUpdate.getDate().getTime());
                current.put("category", taskCategory);
                current.put("priority", taskToUpdate.getPriority());
                current.put("assignee", employeeName);
                if(employee != null) {
                    current.put("assigneeUid", employee.getUid());
                }
                current.put("location", taskLocation);
                taskToUpdate.setName(taskName);
                taskToUpdate.convertDateFromString(taskTime, taskDate);
                taskToUpdate.setCategory(taskCategory);
                taskToUpdate.setPriority(selectedRadio);
                if(userParams.getBoolean("isManager")){
                    taskToUpdate.setAssignee(employeeName);
                    taskToUpdate.setManagerUid(userParams.getString("uid"));
                    taskToUpdate.setAssigneeUid(employee.getUid());
                }
                else{
                    taskToUpdate.setAssignee("self");
                    taskToUpdate.setManagerUid(managerUid);
                    taskToUpdate.setAssigneeUid(userParams.getString("uid"));
                }
                taskToUpdate.setLocation(taskLocation);
                taskToUpdate.setTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date()));
                TaskDAO.getInstance(this).updateTask(taskToUpdate);
                isUpdate = false;
                userParams.remove("taskUid");
                Map<String, Object> update = new HashMap<>();
                update.put("name", taskName);
                update.put("date", taskToUpdate.getDate().getTime());
                update.put("category", taskCategory);
                update.put("priority", selectedRadio);
                update.put("assignee", employeeName);
                if(employee != null) {
                    update.put("assigneeUid", employee.getUid());
                }
                update.put("location", taskLocation);
                MapDifference<String, Object> diff = Maps.difference(update, current);
                if(!diff.entriesDiffering().isEmpty()) {
                    update.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date()));
                    if (managerUid != null) {
                        FireBaseDB remote = new FireBaseDB(AddTaskActivity.this);
                        remote.updateTask(taskToUpdate, update, new MyCallback<String>() {
                            @Override
                            public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                                if (error != null) {
                                    Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            }
            else {
                FireBaseDB remote = new FireBaseDB(AddTaskActivity.this);
                String postId = remote.getTaskKey(managerUid);
                task.setName(taskName);
                task.convertDateFromString(taskTime, taskDate);
                task.setCategory(taskCategory);
                task.setPriority(selectedRadio);
                task.setAcceptStatus("Waiting");
                task.setStatus("Waiting");
                task.setTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date()));
                if(userParams.getBoolean("isManager")){
                    task.setAssignee(employeeName);
                    task.setAssigneeUid(employee.getUid());

                }
                else{
                    task.setAssignee("Self");
                    task.setAssigneeUid(userParams.getString("uid"));

                }
                task.setLocation(taskLocation);
                task.setFirebaseId(postId);
                task.setManagerUid(managerUid);
                task.setPicture(null);
                TaskDAO.getInstance(this).addTask(task);
                remote.addTask(task, new MyCallback<String>() {
                    @Override
                    public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                        if (error != null) {
                            Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            Intent intent = new Intent(this, TasksActivity.class);
            intent.putExtras(userParams);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
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

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
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
