package com.geniuskid.haveitback.activities.loginSignup;

import android.os.Bundle;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.activities.main.MainActivity;
import com.geniuskid.haveitback.utils.BaseActivity;
import com.geniuskid.haveitback.views.MaterialRippleLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginOrSignUpActivity extends BaseActivity {

    @BindView(R.id.fb_login)
    MaterialRippleLayout fbLogin;
    @BindView(R.id.google_login)
    MaterialRippleLayout googleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);
    }

    @OnClick(R.id.fb_login)
    public void onFbLogin() {
        goTo(this, MainActivity.class, true);
    }

    @OnClick(R.id.google_login)
    public void onGoogleLogin() {
        goTo(this, MainActivity.class, true);
    }
}
