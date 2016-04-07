package com.tasking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        Bundle userParams = getIntent().getExtras();
        Bitmap bitmap = userParams.getParcelable("bitmap");
        ImageView imageView = (ImageView)findViewById(R.id.image_done);
        imageView.setImageBitmap(bitmap);
    }

    public void back(View view){
        Bundle userParams = getIntent().getExtras();
        Intent intent = new Intent(this, TasksActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);
        finish();
    }
}
