package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tasking.R.layout.activity_login);
        EditText username  = (EditText)findViewById(com.tasking.R.id.txt_user_name);
        EditText password  = (EditText)findViewById(com.tasking.R.id.txt_password);
        TextView forgotMsg = (TextView)findViewById(com.tasking.R.id.txt_forgot);
        TextView toggleSignUp = (TextView) findViewById(R.id.txt_toggle_sign);
        TextView signUpMsg = (TextView)findViewById(com.tasking.R.id.txt_sign_msg);
        Button button = (Button)findViewById(com.tasking.R.id.btn_sign);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        username.setTypeface(typeFace);
        toggleSignUp.setTypeface(typeFace);
        password.setTypeface(typeFace);
        forgotMsg.setTypeface(typeFace);
        signUpMsg.setTypeface(typeFace);
        button.setTypeface(boldTypeFace);
        button.setTransformationMethod(null);
    }


    public void signUp(View view){
        TextView toggleSignUp = (TextView) findViewById(R.id.txt_toggle_sign);
        TextView signMsg = (TextView) findViewById(R.id.txt_sign_msg);
        Button signUp = (Button) findViewById(R.id.btn_sign);
        if(toggleSignUp.getText().toString().equals((getResources().getString(R.string.sign_up)))){
            toggleSignUp.setText(getResources().getString(R.string.log_in));
            signUp.setText(getResources().getString(R.string.sign_up));
            signMsg.setText(getResources().getString(R.string.sign_up_msg));
        }
        else{
            toggleSignUp.setText(getResources().getString(R.string.sign_up));
            signUp.setText(getResources().getString(R.string.log_in));
            signMsg.setText(getResources().getString(R.string.log_in_msg));

        }
    }


    public void submit(View view) {
        Button signUp = (Button) findViewById(R.id.btn_sign);
        EditText username = (EditText) findViewById(R.id.txt_user_name);
        EditText password = (EditText) findViewById(R.id.txt_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(signUp.getText().toString().equals((getResources().getString(R.string.sign_up)))){
            Employee employee = new Manager(user, pass, 1);
            TaskDAO.getInstance(this).addTeamMember(employee);
            Intent intent = new Intent(this, TeamActivity.class);
            startActivity(intent);
        }
        else{

        }
    }
}
