package com.tasking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class LoginActivity extends Activity {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(com.tasking.R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        EditText username = (EditText) findViewById(com.tasking.R.id.txt_user_name);
        EditText password = (EditText) findViewById(com.tasking.R.id.txt_password);
        TextView forgotMsg = (TextView) findViewById(com.tasking.R.id.txt_forgot);
        TextView toggleSignUp = (TextView) findViewById(R.id.txt_toggle_sign);
        TextView signUpMsg = (TextView) findViewById(com.tasking.R.id.txt_sign_msg);
        Button button = (Button) findViewById(com.tasking.R.id.btn_sign);
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

    public void signUp(View view) {
        TextView toggleSignUp = (TextView) findViewById(R.id.txt_toggle_sign);
        TextView signMsg = (TextView) findViewById(R.id.txt_sign_msg);
        Button signUp = (Button) findViewById(R.id.btn_sign);
        if (toggleSignUp.getText().toString().equals((getResources().getString(R.string.sign_up)))) {
            toggleSignUp.setText(getResources().getString(R.string.log_in));
            signUp.setText(getResources().getString(R.string.sign_up));
            signMsg.setText(getResources().getString(R.string.sign_up_msg));
        } else {
            toggleSignUp.setText(getResources().getString(R.string.sign_up));
            signUp.setText(getResources().getString(R.string.log_in));
            signMsg.setText(getResources().getString(R.string.log_in_msg));
        }
    }

    public void forgotPass(View view){
        EditText editUsername = (EditText) findViewById(R.id.txt_user_name);
        final String username = editUsername.getText().toString();
        Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
        if(!username.equals("")) {
            firebase.resetPassword(username, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "New password was sent to your e-mail", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
    }

    public void submit(View view) {
        final Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
        Button signUp = (Button) findViewById(R.id.btn_sign);
        EditText editUsername = (EditText) findViewById(R.id.txt_user_name);
        EditText editPassword = (EditText) findViewById(R.id.txt_password);
        final String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        if (signUp.getText().toString().equals((getResources().getString(R.string.sign_up)))) {
            if (!username.equals("")) {
                if (!password.equals("")) {
                    progress = ProgressDialog.show(this, "Authentication",
                            "Creating new TasKing Manager", true);
                    //TODO: design this?
                    firebase.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            String uid = result.get("uid").toString();
                            firebase.child("managers").child(uid).child("username").setValue(username);
                            Bundle userParams = new Bundle();
                            userParams.putString("uid", uid);
                            userParams.putBoolean("isManager", true);
                            Intent intent = new Intent(getApplication(), TeamActivity.class);
                            intent.putExtras(userParams);
                            progress.dismiss();
                            startActivity(intent);
                        }
                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(getApplication(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!username.equals("")) {
                if (!password.equals("")) {
                    progress = ProgressDialog.show(this, "Authentication",
                            "Checking User name & Password", true);
                    //TODO: design this?
                    firebase.authWithPassword(username, password,
                            new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    final String uid = authData.getUid();
                                    final Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
                                    SharedPreferences settings = getSharedPreferences("user_pref", MODE_PRIVATE);
                                    SharedPreferences.Editor prefEditor = settings.edit();
                                    prefEditor.putBoolean("isAuthenticated", true);
                                    prefEditor.putString("userName", username);
                                    if (uid != null) {
                                        firebase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                Bundle activityCheck = getIntent().getExtras();
                                                if(activityCheck != null) {
                                                    if (activityCheck.getBoolean("isLoginActivity")) {
                                                        Bundle userParams = new Bundle();
                                                        if (snapshot.child("managers").child(uid).child("username").getValue() != null) {
                                                            SharedPreferences settings = getSharedPreferences("user_pref", MODE_PRIVATE);
                                                            SharedPreferences.Editor prefEditor = settings.edit();
                                                            prefEditor.putBoolean("isManager", true);
                                                            prefEditor.putBoolean("isAuthenticated", true);
                                                            prefEditor.apply();
                                                            if (snapshot.child("managers").child(uid).getChildrenCount() > 1) {
                                                                ArrayList<Employee> localTeam = TaskDAO.getInstance(getApplicationContext()).getMembers(uid);
                                                                if (localTeam.size() == 0) {
                                                                    for (DataSnapshot teamMember : snapshot.child("managers").child(uid).child("team").getChildren()) {
                                                                        String memberUid = teamMember.getKey();
                                                                        Employee employee = teamMember.getValue(Employee.class);
                                                                        TaskDAO.getInstance(getApplicationContext()).addMember(employee, memberUid, uid);
                                                                    }
                                                                }
                                                                ArrayList<Task> localTasks = TaskDAO.getInstance(getApplicationContext()).getTasks(uid, true);
                                                                ArrayList<Task> remoteTasks = new ArrayList<>();
                                                                Date localDate = null;
                                                                Date firebaseDate = null;
                                                                for (DataSnapshot task : snapshot.child("managers").child(uid).child("tasks").getChildren()) {
                                                                    Task taskToAdd = task.getValue(Task.class);
                                                                    remoteTasks.add(taskToAdd);
                                                                    for (Task localTask : localTasks) {
                                                                        if (localTask.getFirebaseId().equals(taskToAdd.getFirebaseId())) {
                                                                            try {
                                                                                localDate = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(localTask.getTimeStamp());
                                                                                firebaseDate = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(taskToAdd.getTimeStamp());
                                                                            } catch (ParseException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            if (localDate != null && firebaseDate != null) {
                                                                                if (localDate.before(firebaseDate)) {
                                                                                    TaskDAO.getInstance(getApplicationContext()).updateTask(taskToAdd);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                Collection<Task> remote = new ArrayList<>(remoteTasks);
                                                                Collection<Task> local = new ArrayList<>(localTasks);
                                                                ArrayList<Task> addList = new ArrayList<>(remote);
                                                                ArrayList<Task> removeList = new ArrayList<>(local);
                                                                if (addList.size() > 0) {
                                                                    addList.removeAll(local);
                                                                    for (Task t : addList) {
                                                                        if(t.getManagerUid().equals(uid)) {
                                                                            TaskDAO.getInstance(getApplicationContext()).addTask(t);
                                                                        }
                                                                    }
                                                                }
                                                                if (removeList.size() > 0) {
                                                                    removeList.removeAll(remote);
                                                                    for (Task t : removeList) {
                                                                        if(t.getManagerUid().equals(uid)) {
                                                                            TaskDAO.getInstance(getApplicationContext()).removeTask(t.getFirebaseId());
                                                                        }
                                                                    }
                                                                }
                                                                userParams.putString("uid", uid);
                                                                userParams.putBoolean("isManager", true);
                                                                Intent intent = new Intent(getApplication(), TasksActivity.class);
                                                                intent.putExtras(userParams);
                                                                progress.dismiss();
                                                                startActivity(intent);
                                                            } else {
                                                                userParams.putString("uid", uid);
                                                                userParams.putBoolean("isManager", true);
                                                                Intent intent = new Intent(getApplication(), TeamActivity.class);
                                                                intent.putExtras(userParams);
                                                                progress.dismiss();
                                                                startActivity(intent);
                                                            }
                                                        } else {
                                                            ArrayList<Task> localTasks = TaskDAO.getInstance(getApplicationContext()).getTasks(uid, false);
                                                            ArrayList<Task> remoteTasks = new ArrayList<>();
                                                            Date localDate = null;
                                                            Date firebaseDate = null;
                                                            String managerUid = snapshot.child("member-manager").child(uid).getValue().toString();
                                                            for (DataSnapshot task : snapshot.child("managers").child(managerUid).child("tasks").getChildren()) {
                                                                Task taskToAdd = task.getValue(Task.class);
                                                                remoteTasks.add(taskToAdd);
                                                                if (taskToAdd.getAssigneeUid().equals(uid)) {
                                                                    for (Task localTask : localTasks) {
                                                                        if (localTask.getFirebaseId().equals(taskToAdd.getFirebaseId())) {
                                                                            try {
                                                                                localDate = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(localTask.getTimeStamp());
                                                                                firebaseDate = DateFormat.getDateInstance(DateFormat.DEFAULT).parse(taskToAdd.getTimeStamp());
                                                                            } catch (ParseException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            if (localDate != null && firebaseDate != null) {
                                                                                if (localDate.before(firebaseDate)) {
                                                                                    TaskDAO.getInstance(getApplicationContext()).updateTask(taskToAdd);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            Collection<Task> remote = new ArrayList<>(remoteTasks);
                                                            Collection<Task> local = new ArrayList<>(localTasks);
                                                            ArrayList<Task> addList = new ArrayList<>(remote);
                                                            ArrayList<Task> removeList = new ArrayList<>(local);
                                                            if (addList.size() > 0) {
                                                                addList.removeAll(local);
                                                                for (Task task : addList) {
                                                                    if(task.getAssigneeUid().equals(uid)) {
                                                                        TaskDAO.getInstance(getApplicationContext()).addTask(task);
                                                                    }
                                                                }
                                                            }
                                                            if (removeList.size() > 0) {
                                                                removeList.removeAll(remote);
                                                                for (Task task : removeList) {
                                                                    if(task.getAssigneeUid().equals(uid)) {
                                                                        TaskDAO.getInstance(getApplicationContext()).removeTask(task.getFirebaseId());
                                                                    }
                                                                }
                                                            }
                                                            userParams.putString("managerUid", managerUid);
                                                            userParams.putString("uid", uid);
                                                            userParams.putBoolean("isManager", false);
                                                            Intent intent = new Intent(getApplication(), TasksActivity.class);
                                                            intent.putExtras(userParams);
                                                            progress.dismiss();
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {
                                                Toast.makeText(getApplication(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    prefEditor.apply();
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError error) {
                                    Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
