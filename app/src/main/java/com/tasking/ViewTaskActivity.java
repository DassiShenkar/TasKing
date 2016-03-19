package com.tasking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ViewTaskActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
    }

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
            String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
            task.setPicture(imageFile);
            TaskDAO.getInstance(this).updateTask(task);
            imageview.setClickable(false);
        }
    }

    public void takePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void done(View view){
        //TODO: implement logic
        //TODO: update backend
        Bundle userParams = getIntent().getExtras();
        Task task = TaskDAO.getInstance(this).getTask(userParams.getInt("taskId"));
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);

    }

}
