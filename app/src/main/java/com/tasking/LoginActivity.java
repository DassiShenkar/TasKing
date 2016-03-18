package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;


public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {


    //  Gindos DB CHECK - DO NOT TOUCH
        Firebase.setAndroidContext(this);
        FireBaseDB db = FireBaseDB.getInstance();


        Manager manager = new Manager("new13anager@gmail.com","15243w",1);
        TeamMember e4  = new TeamMember("arel10@gmail.com","1232214w345",1);
        TeamMember e5  = new TeamMember("arel11@gmail.com","12322454514w345",0);
        TeamMember e6  = new TeamMember("arel12@gmail.com","12322454514ew345",0);
      //  db.createNewEmployee(e);
//        Task task = new Task("Endroid1","11/09/1990","homework","urgent","room 247","waiting",e6);
       // String mangerUid = db.createNewManager("new12anager@gmail.com","15243w");
       // AuthData ad = db.ge
        db.createNewManager(manager);
        db.authenticationEmployee(manager);
        System.out.println("login="+db.getManagerUid());
       // db.addNewTask(task);
      //  db.setTaskAssignees(task,e4);
      //  db.setManagerEmployees(e4,teamMember);




        super.onCreate(savedInstanceState);
        setContentView(com.tasking.R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
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
//        Button signUp = (Button) findViewById(R.id.btn_sign);
//        EditText username = (EditText) findViewById(R.id.txt_user_name);
//        EditText password = (EditText) findViewById(R.id.txt_password);
//        String user = username.getText().toString();
//        String pass = password.getText().toString();
        Bundle userParams = new Bundle();
        Intent intent = new Intent(this, TeamActivity.class);
        userParams.putBoolean("isManager", true);
        intent.putExtras(userParams);
        startActivity(intent);
//        if(signUp.getText().toString().equals((getResources().getString(R.string.sign_up)))){
//            if(!user.equals("")) {
//                if (!pass.equals("")) {
//                    Employee employee = new Manager(user, pass, 1);
//                    TaskDAO.getInstance(this).addTeamMember(employee);
//                    Intent intent = new Intent(this, TeamActivity.class);
//                    userParams.putString("userName", employee.getUserName());
//                    userParams.putBoolean("isManager", true);
//                    intent.putExtras(userParams);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else{
//                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else{
//            if(!user.equals("")){
//                if(!pass.equals("")){
//                    Employee member = TaskDAO.getInstance(this).getTeamMember(user);
//                    if(member != null){
//                        if(pass.equals(member.getPassword())){
//                            if(member.getIsManager() == 1){
//                                if(TaskDAO.getInstance(this).hasMembers(member.getUserName())){
//                                    Intent intent = new Intent(this, TasksActivity.class);
//                                    userParams.putString("userName", member.getUserName());
//                                    userParams.putBoolean("isManager", true);
//                                    intent.putExtras(userParams);
//                                    startActivity(intent);
//                                }
//                                else{
//                                    Intent intent = new Intent(this, TeamActivity.class);
//                                    userParams.putString("userName", member.getUserName());
//                                    userParams.putBoolean("isManager", true);
//                                    intent.putExtras(userParams);
//                                    startActivity(intent);
//                                }
//                            }
//                            else{
//                                Intent intent = new Intent(this, TasksActivity.class);
//                                userParams.putString("userName", member.getUserName());
//                                userParams.putBoolean("isManager", false);
//                                intent.putExtras(userParams);
//                                startActivity(intent);
//                            }
//                        }
//                        else{
//                            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else{
//                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else{
//                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
