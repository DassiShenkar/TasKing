package com.tasking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewTaskActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Bundle userParams = getIntent().getExtras();
        task = TaskDAO.getInstance(this).getTask(userParams.getString("taskUid"));
        TextView category = (TextView) findViewById(R.id.txt_view_curr_category);
        TextView priority = (TextView) findViewById(R.id.txt_view_curr_priority);
        TextView location = (TextView) findViewById(R.id.txt_view_curr_location);
        TextView dueDate = (TextView) findViewById(R.id.txt_curr_dueTime);
        RelativeLayout taskStatus = (RelativeLayout) findViewById(R.id.view_status_layout);
        RelativeLayout addPhoto = (RelativeLayout) findViewById(R.id.view_photo_layout);
        RadioGroup accept = (RadioGroup) findViewById(R.id.radGrp_accept);
        RadioGroup status = (RadioGroup) findViewById(R.id.radGrp_status);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        RadioButton waiting = (RadioButton) findViewById(R.id.radio_status_waiting);
        RadioButton inProgress = (RadioButton) findViewById(R.id.radio_inProgress);
        RadioButton done = (RadioButton) findViewById(R.id.radio_done);
        RadioButton acceptWaiting = (RadioButton) findViewById(R.id.radio_waiting);
        RadioButton acceptRad = (RadioButton) findViewById(R.id.radio_accept);
        RadioButton reject = (RadioButton) findViewById(R.id.radio_reject);
        waiting.setTypeface(typeFace);
        inProgress.setTypeface(typeFace);
        done.setTypeface(typeFace);
        acceptWaiting.setTypeface(typeFace);
        acceptRad.setTypeface(typeFace);
        reject.setTypeface(typeFace);
        if (task.getAcceptStatus() == null || !task.getAcceptStatus().equals(getResources().getString(R.string.accept))) {
            taskStatus.setVisibility(View.GONE);
            acceptWaiting.setChecked(true);
        } else {
            accept.check(R.id.radio_accept);
            acceptRad.setChecked(true);
        }
        if (task.getStatus() == null || !task.getStatus().equals(getResources().getString(R.string.status_done))) {
            addPhoto.setVisibility(View.GONE);
            waiting.setChecked(true);
        } else {
            done.setChecked(true);
        }
        ImageView imageView = (ImageView) findViewById(R.id.btn_img_save);
        if (task.getPicture() != null) {
            byte[] encodeByte = Base64.decode(task.getPicture(), Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imageView.setImageBitmap(imageBitmap);
        } else {
            imageView.setImageDrawable(getDrawable(R.drawable.ic_action_camera));
            imageView.setClickable(true);
        }
        category.setText(task.getCategory());
        priority.setText(task.getPriority());
        location.setText(task.getLocation());
        String date = task.convertDateString() + " " + task.convertTimeString();
        dueDate.setText(date);
        //TODO: If ReportAcceptStatus == “ACCEPT” Then TOAST: “Task <ReportAcceptStatus> && Task is <TaskStatus>”
        //TODO: If ReportAcceptStatus == “REJECT” Then TOAST: “Task <ReportAcceptStatus>

        accept.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Bundle userParams = getIntent().getExtras();
                String taskUid = userParams.getString("taskUid");
                task = TaskDAO.getInstance(getApplicationContext()).getTask(taskUid);
                RadioButton selected = (RadioButton) findViewById(checkedId);
                if (selected.getText().toString().equals(getResources().getString(R.string.accept))) {
                    RelativeLayout status = (RelativeLayout) findViewById(R.id.view_status_layout);
                    status.setVisibility(View.VISIBLE);
                }
                task.setAcceptStatus(selected.getText().toString());
                TaskDAO.getInstance(getApplicationContext()).updateTask(task);
            }
        });
        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Bundle userParams = getIntent().getExtras();
                String taskUid = userParams.getString("taskUid");
                task = TaskDAO.getInstance(getApplicationContext()).getTask(taskUid);
                RadioButton selected = (RadioButton) findViewById(checkedId);
                if (selected.getText().toString().equals(getResources().getString(R.string.status_done))) {
                    RelativeLayout addPhoto = (RelativeLayout) findViewById(R.id.view_photo_layout);
                    addPhoto.setVisibility(View.VISIBLE);
                }
                task.setStatus(selected.getText().toString());
                TaskDAO.getInstance(getApplicationContext()).updateTask(task);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle userParams = getIntent().getExtras();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            task = TaskDAO.getInstance(this).getTask(userParams.getString("taskUid"));
            ImageView imageView = (ImageView) findViewById(R.id.btn_img_save);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            imageView.setClickable(false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (imageBitmap != null) {
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            }
            byte[] byteArray = stream.toByteArray();
            String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
            task.setPicture(imageFile);
            TaskDAO.getInstance(this).updateTask(task);
        }
    }

    public void takePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    public void cancel(View view){
        Bundle userParams = getIntent().getExtras();
        userParams.remove("taskId");
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }

    public void done(View view){
        Bundle userParams = getIntent().getExtras();
        task = TaskDAO.getInstance(this).getTask(userParams.getString("taskUid"));
        final Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
        String managerUid = userParams.getString("managerUid");
        Map<String, Object> update = new HashMap<>();
        update.put("acceptStatus", task.getAcceptStatus());
        update.put("status", task.getStatus());
        update.put("picture", task.getPicture());
        update.put("timeStamp", new Date().toString());
        if (managerUid != null) {
            //TODO: check callback
            firebase.child("managers").child(managerUid).child("tasks").child(task.getFirebaseId()).updateChildren(update);
            //TODO: check if working
            Toast.makeText(getApplication(), "Task was updated", Toast.LENGTH_SHORT).show();
        }
        //TODO: update backend
        //TODO: If Error / Time Out: TOAST: “Error Saving Task Status: Please try again”
        userParams.remove("taskId");
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);

    }
}
