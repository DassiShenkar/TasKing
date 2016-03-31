package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity {

    private static final int SPLASH_TIMEOUT = 1000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                boolean isAuthenticated = preferences.getBoolean("isAuthenticated", false);
                boolean isManager = preferences.getBoolean("isManager", false);
                if(isAuthenticated) {
                    Bundle userParams = new Bundle();
                    userParams.putBoolean("isManager", isManager);
                    Intent intent = new Intent(getApplication(), TasksActivity.class);
                    intent.putExtras(userParams);
                    startActivity(intent);
                }
                Bundle activityCheck = new Bundle();
                activityCheck.putBoolean("isLoginActivity", true);
                Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
                mainIntent.putExtras(activityCheck);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}