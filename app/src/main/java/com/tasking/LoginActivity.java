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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //  Gindos DB CHECK - DO NOT TOUCH
        Firebase.setAndroidContext(this);
        FireBaseDB db = FireBaseDB.getInstance();


        Manager manager = new Manager("manager17@gmail.com", "15243w");
        Firebase ref = new Firebase("https://tasking-android.firebaseio.com/");
//        manager.setUid(ref.child("users").child("uid").getKey().toString());

      /*  Map users = (Map<String, String>)ref.child("users");
        for(Object key :
            users.keySet()){
                System.out.println(key.toString());
        }*/
        System.out.println("uid=" + manager.getUid());
        TeamMember e4 = new TeamMember("arel10@gmail.com", "1232214w345");
        TeamMember e5 = new TeamMember("arel11@gmail.com", "12322454514w345");
        TeamMember e6 = new TeamMember("arel12@gmail.com", "12322454514ew345");
        //  db.createNewEmployee(e);
//        Task task = new Task("Endroid1","11/09/1990","homework","urgent","room 247","waiting",e6);
        // String mangerUid = db.createNewManager("new12anager@gmail.com","15243w");
        // AuthData ad = db.ge
//        db.createNewManager(manager);
//        db.authenticationEmployee(manager);
        System.out.println("login=" + db.getManagerUid());
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
        Button signUp = (Button) findViewById(R.id.btn_sign);
        EditText username = (EditText) findViewById(R.id.txt_user_name);
        EditText password = (EditText) findViewById(R.id.txt_password);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        Bundle userParams = getIntent().getExtras();
        if (signUp.getText().toString().equals((getResources().getString(R.string.sign_up)))) {
            if (!user.equals("")) {
                if (!pass.equals("")) {
                    final Firebase ref = new Firebase("https://tasking-android.firebaseio.com/");
                    ref.createUser(user, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            String uid = result.get("uid").toString();
                            Bundle userParams = getIntent().getExtras();
                            userParams.putString("uid", uid);
                            Intent intent = new Intent(getApplication(), TeamActivity.class);
                            userParams.putBoolean("isManager", true); // after we check if isManager
                            intent.putExtras(userParams);
                            startActivity(intent);
                            System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            System.out.println("error on create manager");
                            // there was an error
                        }
                    });
                } else {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!user.equals("")) {
                if (!pass.equals("")) {
                    final Firebase ref = new Firebase("https://tasking-android.firebaseio.com/");
                    ref.authWithPassword(user, pass,
                            new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    // save bullean auth true to shared prefference;

                                    SharedPreferences settings = getSharedPreferences("user_pref", MODE_PRIVATE);
                                    SharedPreferences.Editor prefEditor = settings.edit();
                                    prefEditor.putBoolean("isAuthenticated", true);
                                    prefEditor.apply();

                                    final String uid = authData.getUid();
                                    if (uid != null) {
                                        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                Bundle userParams = getIntent().getExtras();
                                                if (snapshot.child("Managers").child(uid).getKey().equals(uid)) {
                                                    if (snapshot.child("Managers").child("Team").getKey() != null) {
                                                        Intent intent = new Intent(getApplication(), TasksActivity.class);
                                                        userParams.putString("uid", uid);
                                                        userParams.putBoolean("isManager", true);
                                                        intent.putExtras(userParams);
                                                        startActivity(intent);
                                                        JSONObject teamMemebers = (JSONObject)snapshot.child("Managers").child("Team").getValue();
                                                        Iterator<String> keys = teamMemebers.keys();
                                                        while (keys.hasNext()) {

                                                        }
                                                        //save localy users + tasks
                                                    } else {
                                                        Intent intent = new Intent(getApplication(), TeamActivity.class);
                                                        userParams.putString("uid", uid);
                                                        userParams.putBoolean("isManager", true);
                                                        intent.putExtras(userParams);
                                                        startActivity(intent);
                                                    }
                                                } else {
                                                    Intent intent = new Intent(getApplication(), TasksActivity.class);
                                                    userParams.putString("uid", uid);
                                                    userParams.putBoolean("isManager", false);
                                                    intent.putExtras(userParams);
                                                    startActivity(intent);
                                                    //save localy tasks
                                                }
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {
                                                Toast.makeText(getApplication(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError error) {
                                    //get authenticated localy if(auth) if has team localy goto task else goto team
                                    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                                    boolean isAuthenticated = preferences.getBoolean("isAuthenticated", true);
                                    if(isAuthenticated) {
                                        Bundle userParams = getIntent().getExtras();
                                        ArrayList<Employee> teamMembres = TaskDAO.getInstance(getApplication()).getMembers();
                                        boolean isManager = teamMembres.size() > 0;
                                        userParams.putBoolean("isManager", isManager);
                                        Intent intent = new Intent(getApplication(), TasksActivity.class);
                                        userParams.putString("workMode", "offline");
                                        intent.putExtras(userParams);
                                        startActivity(intent);
                                    }
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
