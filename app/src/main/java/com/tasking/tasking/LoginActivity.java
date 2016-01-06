package com.tasking.tasking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs = null;
    private boolean isManager;

    public final static String EXTRA_MESSAGE = "com.tasking.taskking.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("com.tasking.tasking.TasKing", MODE_PRIVATE);
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

    @Override
    public void onResume(){
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            SQLiteDatabase db = null;
            TasKingDBHelper DBHelper = new TasKingDBHelper(this);
            int count = TaskDAO.getInstance(this).getMemberCount();
                if (count == 0) {
                    TextView forgotMsg = (TextView) findViewById(R.id.txt_forgot);
                    forgotMsg.setVisibility(TextView.GONE);
                    Button button = (Button) findViewById(R.id.btn_sign);
                    button.setText(R.string.sign_up);
                    isManager = true;
                }
            else{
                    isManager = false;
                }
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    public void signUp(View view){
        Employee employee = null;
        EditText username  = (EditText)findViewById(R.id.txt_user_name);
        EditText password  = (EditText)findViewById(R.id.txt_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(isManager){
            Manager manager = new Manager(user, pass, 1);
            TaskDAO.getInstance(this).addTeamMember(manager);
            Intent intent = new Intent(this, TeamActivity.class);
            intent.putExtra(EXTRA_MESSAGE, isManager);
            startActivity(intent);
        }
        else{
            employee = TaskDAO.getInstance(this).getTeamMember(user);
            if(employee == null){
                Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_LONG).show();
            }
            else if(employee.getPassword().equals(pass)) {
                Intent intent = new Intent(this, TasksActivity.class);
                intent.putExtra(EXTRA_MESSAGE, isManager);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_LONG).show();
            }
        }
        //todo: get team from DB if exists set hasTeam (text + arrow)
        //todo: intent.putExtra(EXTRA_MESSAGE, boolean isManager);
        //todo: check if manager or user
        //todo: if managerFirstTime => User manger = TaskDAO.getInstance(this).addTeamMember(userName);
        //todo: check if user not exists!!
        //todo: if member => User member = TaskDAO.getInstance(this).getTeamMember(userName);
        //todo: check if exists? login : toast error
    }
}
