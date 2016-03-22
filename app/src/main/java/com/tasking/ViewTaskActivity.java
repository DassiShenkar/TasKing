package com.tasking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
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
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        if(savedInstanceState != null) {
            if (savedInstanceState.getParcelable("imageURI") != null) {
                imageUri = savedInstanceState.getParcelable("imageURI");
            }
            if (savedInstanceState.getString("image") != null) {
                String byteString = savedInstanceState.getString("image");
                ImageView imageview = (ImageView) findViewById(R.id.btn_img_save);
                if (byteString != null) {
                    byte[] bitmapdata = byteString.getBytes(StandardCharsets.UTF_8);
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    imageview.setImageBitmap(imageBitmap);
                }
                imageview.setClickable(false);
            }
        }
        Task task = TaskDAO.getInstance(this).getTask(userParams.getInt("taskId"));
        TextView category = (TextView) findViewById(R.id.txt_view_curr_category);
        TextView priority = (TextView) findViewById(R.id.txt_view_curr_priority);
        TextView location = (TextView) findViewById(R.id.txt_view_curr_location);
        TextView dueDate = (TextView) findViewById(R.id.txt_curr_dueTime);
        RelativeLayout acceptStatus = (RelativeLayout) findViewById(R.id.view_accept_layout);
        RelativeLayout taskStatus = (RelativeLayout) findViewById(R.id.view_status_layout);
        RelativeLayout addPhoto = (RelativeLayout) findViewById(R.id.view_photo_layout);
        RadioGroup accept = (RadioGroup) findViewById(R.id.radGrp_accept);
        RadioGroup status = (RadioGroup) findViewById(R.id.radGrp_status);
        if(task.getStatus() == null || !task.getStatus().equals(getResources().getString(R.string.accept))) {
            taskStatus.setVisibility(View.GONE);
        }
        if(task.getPicture() == null) {
            addPhoto.setVisibility(View.GONE);
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
                if(selected.getText().toString().equals(getResources().getString(R.string.done))) {
                    RelativeLayout addPhoto = (RelativeLayout) findViewById(R.id.view_photo_layout);
                    addPhoto.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("image", imageFile);
        outState.putString("filePath", currentPhotoPath);
        outState.putParcelable("imageURI", imageUri);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle userParams = getIntent().getExtras();
        Task task = TaskDAO.getInstance(this).getTask(userParams.getInt("taskId"));
        ImageView imageview = (ImageView) findViewById(R.id.btn_img_save);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            if(data != null){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
            }
            else{
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Get the dimensions of the View
            int targetW = imageview.getWidth();
            int targetH = imageview.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            imageview.setImageBitmap(bitmap);
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            imageUri = Uri.fromFile(photoFile);
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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
