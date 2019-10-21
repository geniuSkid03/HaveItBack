package com.geniuskid.haveitback.activities.splash;

import android.os.Bundle;
import android.os.Handler;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.activities.loginSignup.LoginOrSignUpActivity;
import com.geniuskid.haveitback.activities.main.MainActivity;
import com.geniuskid.haveitback.utils.BaseActivity;
import com.geniuskid.haveitback.utils.Keys;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkAndOpen();
    }

    private void checkAndOpen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dataStorage.getBoolean("is-logged-in")) {
                    goTo(SplashActivity.this, MainActivity.class, true);
                } else {
                    goTo(SplashActivity.this, LoginOrSignUpActivity.class, true);
                }
            }
        }, 3000);
    }
}
