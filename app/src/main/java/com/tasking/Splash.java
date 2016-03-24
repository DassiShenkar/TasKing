package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity {

    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
        final Bundle userParams = new Bundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//                boolean isAuthenticated = preferences.getBoolean("isAuthenticated", true);
//                if(isAuthenticated) {
//                    Bundle userParams = getIntent().getExtras();
//                    ArrayList<Employee> teamMembers = TaskDAO.getInstance(getApplication()).getMembers();
//                    boolean isManager = teamMembers.size() > 0;
//                    userParams.putBoolean("isManager", isManager);
//                    Intent intent = new Intent(getApplication(), TasksActivity.class);
//                    userParams.putString("workMode", "offline");
//                    intent.putExtras(userParams);
//                    startActivity(intent);
//                }
                Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
                mainIntent.putExtras(userParams);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}