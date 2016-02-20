package com.tasking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    private static final int SPLASH_TIMEOUT = 3000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
        //String appVersion = "v1"; // TODO: 1/7/2016 handle data while splash screen is on
        //Backendless.initApp(this, "D7BF45EE-A9D1-E36C-FF67-2A98F5D50400", "CABFEA4F-DD79-9153-FF91-C25BB6A26400", appVersion);
        //Backendless.Persistence.of("Employee").find();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Splash.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}