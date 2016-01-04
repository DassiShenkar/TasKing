package com.tasking.tasking;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.tasking.taskking.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText username  = (EditText)findViewById(R.id.txt_user_name);
        EditText password  = (EditText)findViewById(R.id.txt_password);
        TextView forgotMsg = (TextView)findViewById(R.id.txt_forgot);
        TextView signUpMsg = (TextView)findViewById(R.id.txt_sign_msg);
        Button button = (Button)findViewById(R.id.btn_sign);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        username.setTypeface(typeFace);
        password.setTypeface(typeFace);
        forgotMsg.setTypeface(typeFace);
        signUpMsg.setTypeface(typeFace);
        button.setTypeface(boldTypeFace);
        button.setTransformationMethod(null);
    }

    public void signUp(View view){
        Intent intent = new Intent(this, TeamActivity.class);
        //todo: get team from DB if exists set hasTeam (text + arrow)
        //todo: intent.putExtra(EXTRA_MESSAGE, boolean hasTeam);
        startActivity(intent);
    }
}
