package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

    private boolean isManager;
    public final static String EXTRA_MESSAGE = "com.tasking.MESSAGE";
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tasking.R.layout.activity_login);
        prefs = getSharedPreferences("com.tasking.TasKing", MODE_PRIVATE);
        EditText username  = (EditText)findViewById(com.tasking.R.id.txt_user_name);
        EditText password  = (EditText)findViewById(com.tasking.R.id.txt_password);
        TextView forgotMsg = (TextView)findViewById(com.tasking.R.id.txt_forgot);
        TextView signUpMsg = (TextView)findViewById(com.tasking.R.id.txt_sign_msg);
        Button button = (Button)findViewById(com.tasking.R.id.btn_sign);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        Typeface boldTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        username.setTypeface(typeFace);
        password.setTypeface(typeFace);
        forgotMsg.setTypeface(typeFace);
        signUpMsg.setTypeface(typeFace);
        button.setTypeface(boldTypeFace);
        button.setTransformationMethod(null);
    }

    public void signUp(View view) {
        Employee employee;
        EditText username = (EditText) findViewById(com.tasking.R.id.txt_user_name);
        EditText password = (EditText) findViewById(com.tasking.R.id.txt_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();//TODO: encrypt using MD5
        if (isManager) {
            Manager manager = new Manager(user, pass, 1);
            TaskDAO.getInstance(this).addTeamMember(manager);
            Intent intent = new Intent(this, TeamActivity.class);
            intent.putExtra(EXTRA_MESSAGE, isManager);
            startActivity(intent);
        } else {
            employee = TaskDAO.getInstance(this).getTeamMember(user);
            if (employee == null) {
                Toast.makeText(this, com.tasking.R.string.user_not_found, Toast.LENGTH_LONG).show();
            } else if (employee.getPassword().equals(pass)) {
                Intent intent;
                if(TaskDAO.getInstance(this).getTeamMembers() == null) {
                    intent = new Intent(this, TeamActivity.class);
                }
                else{
                    intent = new Intent(this, TasksActivity.class);
                }
                intent.putExtra(EXTRA_MESSAGE, isManager);
                startActivity(intent);
            } else {
                Toast.makeText(this, com.tasking.R.string.user_not_found, Toast.LENGTH_LONG).show();
            }
        }
    }
        @Override
        protected void onResume() {
            super.onResume();
            int count = 0;// TODO: get from splash
            if (prefs.getBoolean("firstrun", true)) {
                if (count == 0) {
                    TextView forgotMsg = (TextView) findViewById(com.tasking.R.id.txt_forgot);
                    forgotMsg.setTextColor(Color.parseColor("#0e2635"));
                    Button button = (Button) findViewById(com.tasking.R.id.btn_sign);
                    button.setText(com.tasking.R.string.sign_up);
                    isManager = true;
                }
                else{
                    isManager = false;//TODO: check is_manager
                }
                prefs.edit().putBoolean("firstrun", false).apply();
            }
        }
        // TODO: authenticate
        //todo: get team from DB if exists set hasTeam (text + arrow)
        //todo: intent.putExtra(EXTRA_MESSAGE, boolean isManager);
        //todo: check if manager or user
        //todo: if managerFirstTime => User manger = TaskDAO.getInstance(this).addTeamMember(userName);
        //todo: check if user not exists!!
        //todo: if member => User member = TaskDAO.getInstance(this).getTeamMember(userName);
        //todo: check if exists? login : toast error
}
