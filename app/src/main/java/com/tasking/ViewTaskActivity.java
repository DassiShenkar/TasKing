package com.tasking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class ViewTaskActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String imageFile;
    private String currentPhotoPath;
    private Uri imageUri;
    static final int REQUEST_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Bundle userParams = getIntent().getExtras();
//        if(savedInstanceState != null) {
//            if (savedInstanceState.getParcelable("imageURI") != null) {
//                imageUri = savedInstanceState.getParcelable("imageURI");
//            }
//            if (savedInstanceState.getString("image") != null) {
//                String byteString = savedInstanceState.getString("image");
//                ImageView imageview = (ImageView) findViewById(R.id.btn_img_save);
//                if (byteString != null) {
//                    byte[] bitmapdata = byteString.getBytes(StandardCharsets.UTF_8);
//                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
//                    imageview.setImageBitmap(imageBitmap);
//                }
//                imageview.setClickable(false);
//            }
//        }
        Task task = TaskDAO.getInstance(this).getTask(userParams.getInt("taskId"));
        TextView category = (TextView) findViewById(R.id.txt_view_curr_category);
        TextView priority = (TextView) findViewById(R.id.txt_view_curr_priority);
        TextView location = (TextView) findViewById(R.id.txt_view_curr_location);
        TextView dueDate = (TextView) findViewById(R.id.txt_curr_dueTime);
        RelativeLayout taskStatus = (RelativeLayout) findViewById(R.id.view_status_layout);
        RelativeLayout addPhoto = (RelativeLayout) findViewById(R.id.view_photo_layout);
        RadioGroup accept = (RadioGroup) findViewById(R.id.radGrp_accept);
        RadioGroup status = (RadioGroup) findViewById(R.id.radGrp_status);
        if(task.getAcceptStatus() == null || !task.getAcceptStatus().equals(getResources().getString(R.string.accept))) {
            taskStatus.setVisibility(View.GONE);
            accept.check(R.id.radio_waiting);
        }
        else{
            accept.check(R.id.radio_accept);
        }
        if(task.getStatus() == null || !task.getStatus().equals(getResources().getString(R.string.status_done))) {
            addPhoto.setVisibility(View.GONE);
            accept.check(R.id.radio_status_waiting);
        }
        else{
            status.check(R.id.radio_done);
        }
        if(task.getPicture() != null){
            ImageView imageview = (ImageView) findViewById(R.id.btn_img_save);
            byte[] bitmapdata = task.getPicture().getBytes(StandardCharsets.UTF_8);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            imageview.setImageBitmap(imageBitmap);
        }
        category.setText(task.getCategory());
        priority.setText(task.getPriority());
        location.setText(task.getLocation());
        String date = task.getDateString() + " " + task.getTimeString();
        dueDate.setText(date);
        accept.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Bundle userParams = getIntent().getExtras();
                int taskId = userParams.getInt("taskId");
                Task task = TaskDAO.getInstance(getApplicationContext()).getTask(taskId);
                RadioButton selected = (RadioButton) findViewById(checkedId);
                if(selected.getText().toString().equals(getResources().getString(R.string.accept))) {
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
                int taskId = userParams.getInt("taskId");
                Task task = TaskDAO.getInstance(getApplicationContext()).getTask(taskId);
                RadioButton selected = (RadioButton) findViewById(checkedId);
                if(selected.getText().toString().equals(getResources().getString(R.string.status_done))) {
                    RelativeLayout addPhoto = (RelativeLayout) findViewById(R.id.view_photo_layout);
                    addPhoto.setVisibility(View.VISIBLE);
                }
                task.setStatus(selected.getText().toString());
                TaskDAO.getInstance(getApplicationContext()).updateTask(task);
            }
        });

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putString("image", imageFile);
//        outState.putString("filePath", currentPhotoPath);
//        outState.putParcelable("imageURI", imageUri);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle userParams = getIntent().getExtras();
        Task task = TaskDAO.getInstance(this).getTask(userParams.getInt("taskId"));
        ImageView imageview = (ImageView) findViewById(R.id.btn_img_save);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageview.setImageBitmap(imageBitmap);
            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
            byte[] byteArray = bYtE.toByteArray();
            imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
            task.setPicture(imageFile);
            TaskDAO.getInstance(this).updateTask(task);
            imageview.setClickable(false);
        }
    }

    public void takePhoto(View view){
        Bundle userParams = getIntent().getExtras();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    public void cancel(View view){
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }

    public void done(View view){
        //TODO: update backend
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);

    }

}
