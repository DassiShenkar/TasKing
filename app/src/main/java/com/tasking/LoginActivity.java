package com.tasking;

import android.app.Activity;
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

import java.util.Map;


public class LoginActivity extends Activity {

    private String uid;
    private boolean isManager;
    private boolean hasTeam;

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

    public void submit(View view) {
        final Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
        Button signUp = (Button) findViewById(R.id.btn_sign);
        EditText editUsername = (EditText) findViewById(R.id.txt_user_name);
        EditText editPassword = (EditText) findViewById(R.id.txt_password);
        final String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        /*-------------for debug - remove after debug--------------------
        Bundle userParams = getIntent().getExtras();
        userParams.putBoolean("isManager", true);
        Intent intent = new Intent(getApplication(), TeamActivity.class);
        intent.putExtras(userParams);
        startActivity(intent);*/

//-----------------------for debug backend logic------------------------------------------------------
        //sign up (managers only)
        if (signUp.getText().toString().equals((getResources().getString(R.string.sign_up)))) {
            if (!username.equals("")) {
                if (!password.equals("")) {
                    firebase.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            String uid = result.get("uid").toString();
                            firebase.child("managers").child(uid).child("username").setValue(username);
                            Bundle userParams = getIntent().getExtras();
                            userParams.putString("uid", uid);
                            userParams.putBoolean("isManager", true);
                            Intent intent = new Intent(getApplication(), TeamActivity.class);
                            intent.putExtras(userParams);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            //TODO: something
                        }
                    });
                } else {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
            }
        } else {
            //login
            if (!username.equals("")) {
                if (!password.equals("")) {
                    firebase.authWithPassword(username, password,
                            new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    final String uid = authData.getUid();
                                    Firebase firebase = new Firebase("https://tasking-android.firebaseio.com/");
                                    SharedPreferences settings = getSharedPreferences("user_pref", MODE_PRIVATE);
                                    SharedPreferences.Editor prefEditor = settings.edit();
                                    prefEditor.putBoolean("isAuthenticated", true);
                                    prefEditor.putString("userName", username);
                                    if (uid != null) {
                                        firebase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                Bundle userParams = getIntent().getExtras();
                                                //Manager login
                                                if (snapshot.child("managers").child(uid).child("username").getValue() != null) {
                                                    SharedPreferences settings = getSharedPreferences("user_pref", MODE_PRIVATE);
                                                    SharedPreferences.Editor prefEditor = settings.edit();
                                                    prefEditor.putBoolean("isManager", true);
                                                    prefEditor.apply();
                                                    //Manager with team
                                                    if (snapshot.child("managers").child(uid).getChildrenCount() > 1) {
//                                                        for (DataSnapshot teamMember : snapshot.child("managers").child(uid).child("team").getChildren()) {
//                                                            ArrayList<Employee> localTeam = TaskDAO.getInstance(getApplicationContext()).getMembers(userParams.getString("uid"));
//                                                            if (localTeam.size() == 0) {
//                                                                Employee employee = teamMember.getValue(Employee.class);
//                                                                TaskDAO.getInstance(getApplicationContext()).addMember(employee);
//                                                            }
//                                                        }//TODO: handle exeption: firebase: failed to bounce to type
                                                        userParams.putString("uid", uid);
                                                        userParams.putBoolean("isManager", true);
                                                        Intent intent = new Intent(getApplication(), TasksActivity.class);
                                                        intent.putExtras(userParams);
                                                        startActivity(intent);
                                                        //Manager with no team
                                                    } else {
                                                        userParams.putString("uid", uid);
                                                        userParams.putBoolean("isManager", true);
                                                        Intent intent = new Intent(getApplication(), TeamActivity.class);
                                                        intent.putExtras(userParams);
                                                        startActivity(intent);
                                                    }
                                                    // Team Member
                                                } else {
                                                    userParams.putString("uid", uid);
                                                    userParams.putBoolean("isManager", false);
                                                    Intent intent = new Intent(getApplication(), TasksActivity.class);
                                                    intent.putExtras(userParams);
                                                    startActivity(intent);                                                    //TODO: save tasks in local db
                                                    //TODO: get tasks from firebase
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
       // ------------------------------------------------------------------------------------------
    }
}
