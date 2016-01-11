package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    private static final int SPLASH_TIMEOUT = 3000;
    public final static String EXTRA_MESSAGE = "com.tasking.MESSAGE";

    // TODO: 1/7/2016 handle data while splash screen is on 

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
        final int count = TaskDAO.getInstance(this).getMemberCount();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
                mainIntent.putExtra(EXTRA_MESSAGE, count);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}