package com.tasking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewTaskActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private ImageView imageView;
    private Uri fileUri;
    static final int REQUEST_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            fileUri = savedInstanceState.getParcelable("file_uri");
            currentPhotoPath = savedInstanceState.getString("path");
        }
        setContentView(R.layout.activity_view_task);
        Bundle userParams = getIntent().getExtras();
        Task task = TaskDAO.getInstance(this).getTask(userParams.getInt("taskId"));
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
        RadioButton toCheck;
        if(task.getAcceptStatus() == null || !task.getAcceptStatus().equals(getResources().getString(R.string.accept))) {
            taskStatus.setVisibility(View.GONE);
            toCheck = (RadioButton) findViewById(R.id.radio_waiting);
            toCheck.setChecked(true);
        }
        else{
            accept.check(R.id.radio_accept);
            toCheck = (RadioButton) findViewById(R.id.radio_accept);
            toCheck.setChecked(true);
        }
        if(task.getStatus() == null || !task.getStatus().equals(getResources().getString(R.string.status_done))) {
            addPhoto.setVisibility(View.GONE);
            toCheck = (RadioButton) findViewById(R.id.radio_status_waiting);
            toCheck.setChecked(true);
        }
        else{
            toCheck = (RadioButton) findViewById(R.id.radio_done);
            toCheck.setChecked(true);
        }
        if(task.getPicture() != null){
            imageView = (ImageView) findViewById(R.id.btn_img_save);
            byte[] encodeByte = Base64.decode(task.getPicture(), Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imageView.setImageBitmap(imageBitmap);
        }
        category.setText(task.getCategory());
        priority.setText(task.getPriority());
        location.setText(task.getLocation());
        String date = task.getDateString() + " " + task.getTimeString();
        dueDate.setText(date);
        //TODO: If ReportAcceptStatus == “ACCEPT” Then TOAST: “Task <ReportAcceptStatus> && Task is <TaskStatus>”
        //TODO: If ReportAcceptStatus == “REJECT” Then TOAST: “Task <ReportAcceptStatus>

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
        outState.putString("path", currentPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
        currentPhotoPath = savedInstanceState.getString("path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }

    public void takePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //TODO
            }
            if (photoFile != null) {
                fileUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
        imageView.setClickable(false);
    }

    public void cancel(View view){
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
    }

    public void done(View view){
        //TODO: update backend
        //TODO: If Error / Time Out: TOAST: “Error Saving Task Status: Please try again”
        Bundle userParams = getIntent().getExtras();
        Task task = TaskDAO.getInstance(this).getTask(userParams.getInt("taskId"));
        Bitmap imageBitmap = null;
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (imageBitmap != null) {
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
        }
        byte[] byteArray = stream.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        task.setPicture(imageFile);
        TaskDAO.getInstance(this).updateTask(task);
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);

    }

}
