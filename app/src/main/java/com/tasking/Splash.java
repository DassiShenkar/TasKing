package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.client.Firebase;

public class Splash extends Activity {

    private static final int SPLASH_TIMEOUT = 1000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        if (SaveSharedPreference.getUid(Splash.this).length() == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_TIMEOUT);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bundle userParams = new Bundle();
                    userParams.putString("uid", SaveSharedPreference.getUid(Splash.this));
                    userParams.putString("uidmanagerUid", SaveSharedPreference.getManagerId(Splash.this));
                    userParams.putBoolean("isManager", SaveSharedPreference.getIsManager(Splash.this));
                    Intent mainIntent = new Intent(Splash.this, TasksActivity.class);
                    mainIntent.putExtras(userParams);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_TIMEOUT);
        }
    }
}