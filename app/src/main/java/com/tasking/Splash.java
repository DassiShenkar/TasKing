package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class Splash extends Activity {

    private static final int SPLASH_TIMEOUT = 3000;
    private SharedPreferences prefs = null;
    private boolean isManager;
    public final static String EXTRA_MESSAGE = "com.tasking.MESSAGE";

    // TODO: 1/7/2016 handle data while splash screen is on 

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        prefs = getSharedPreferences("com.tasking.TasKing", MODE_PRIVATE);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(Splash.this,LoginActivity.class);
                mainIntent.putExtra(EXTRA_MESSAGE, isManager);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            int count = TaskDAO.getInstance(this).getMemberCount();
            if (count == 0) {
                TextView forgotMsg = (TextView) findViewById(com.tasking.R.id.txt_forgot);
                forgotMsg.setVisibility(TextView.GONE);
                Button button = (Button) findViewById(com.tasking.R.id.btn_sign);
                button.setText(com.tasking.R.string.sign_up);
                isManager = true;
            }
            else{
                isManager = false;
            }
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }
}