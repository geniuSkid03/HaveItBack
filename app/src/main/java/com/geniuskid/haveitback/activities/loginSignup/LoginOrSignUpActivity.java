package com.geniuskid.haveitback.activities.loginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.geniuskid.haveitback.R;
import com.geniuskid.haveitback.activities.main.MainActivity;
import com.geniuskid.haveitback.utils.BaseActivity;
import com.geniuskid.haveitback.views.MaterialRippleLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginOrSignUpActivity extends BaseActivity
    implements GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.google_login)
    MaterialRippleLayout googleLogin;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);

        initGoogleSignIn();
    }

    private void initGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @OnClick(R.id.google_login)
    public void onGoogleLogin(){
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), 101);
    }

    private String gId="", gEmail="", gName="", gPic="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result != null) {
                    handleSignInResult(result);
                }
            }
        }
    }

    void handleSignInResult(GoogleSignInResult result) {
        if (result == null) {
            return;
        }

        if (result.getStatus().isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                gId = account.getId();
                gEmail = account.getEmail();
                gPic = account.getPhotoUrl() + "";
                gName = account.getDisplayName();
            }

            dataStorage.saveString("username", gName);
            dataStorage.saveString("email", gEmail);
            dataStorage.saveString("img", gPic);
            dataStorage.saveString("id", gId);
            dataStorage.saveBoolean("is-logged-in", true);

            goTo(LoginOrSignUpActivity.this, MainActivity.class, true);
        }

        signOutGMail();
    }

    private void signOutGMail() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
