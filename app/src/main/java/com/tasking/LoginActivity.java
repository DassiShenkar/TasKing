package com.tasking;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.firebase.client.Firebase;

public class LoginActivity extends Activity {

    private ProgressDialog progress;
    private Bundle userParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tasking.R.layout.activity_login);
        Firebase.setAndroidContext(this);
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
        progress = new ProgressDialog(this, R.style.ProgressCustomTheme);
        progress.setProgressStyle(android.R.style.Widget_ProgressBar_Large);

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
        if(!username.equals("")) {
            FireBaseDB.getInstance(this).resetPassword(username, new MyCallback<String>() {
                @Override
                public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                    if(error == null){
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
    }

    public void submit(View view) {
        Button signUp = (Button) findViewById(R.id.btn_sign);
        EditText editUsername = (EditText) findViewById(R.id.txt_user_name);
        EditText editPassword = (EditText) findViewById(R.id.txt_password);
        userParams = new Bundle();
        final String username = editUsername.getText().toString();
        final String password = editPassword.getText().toString();
        if (signUp.getText().toString().equals((getResources().getString(R.string.sign_up)))) {
            if (!username.equals("")) {
                if (!password.equals("")) {
                    userParams.putString("password", password);
                    progress.show();
                    FireBaseDB.getInstance(this).createManager(username, password, new MyCallback<String>() {
                        @Override
                        public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                            myStartActivity(result, error, managerUid, isManager, hasTeam);
                            progress.dismiss();
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
                    progress.show();
                    FireBaseDB.getInstance(this).authenticate(username, password, new MyCallback<String>() {
                        @Override
                        public void done(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
                            myStartActivity(result, error, managerUid, isManager,  hasTeam);
                            progress.dismiss();
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

    private void myStartActivity(String result, String error, String managerUid, boolean isManager, boolean hasTeam) {
        if(error == null) {
            SaveSharedPreference.setUid(LoginActivity.this, result);
            SaveSharedPreference.setIsManager(LoginActivity.this, isManager);
            SaveSharedPreference.setManagerId(LoginActivity.this, managerUid);
            userParams.putString("managerUid", managerUid);
            userParams.putString("uid", result);
            userParams.putBoolean("isManager", isManager);
            Intent intent;
            if(isManager) {
                if (hasTeam) {
                    intent = new Intent(getApplication(), TasksActivity.class);
                } else {
                    intent = new Intent(getApplication(), TeamActivity.class);
                }
            }
            else {
                intent = new Intent(getApplication(), TasksActivity.class);
            }
            intent.putExtras(userParams);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }
}
