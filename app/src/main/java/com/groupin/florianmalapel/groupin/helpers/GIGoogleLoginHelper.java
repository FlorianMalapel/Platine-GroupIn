package com.groupin.florianmalapel.groupin.helpers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.groupin.florianmalapel.groupin.R;

/**
 * Created by florianmalapel on 30/10/2016.
 */

public class GIGoogleLoginHelper {

    public static final int GOOGLE_LOGIN_ID = 1232;
    private AppCompatActivity activity = null;
    private GoogleApiClient googleApiClient = null;
    private LoginCallback loginCallback = null;
    private GoogleApiClient.OnConnectionFailedListener  onConnectionFailedListener = null;

    public interface LoginCallback {
        void loginSuccess(Intent data);
        void loginFailed();
    }

    public GIGoogleLoginHelper(AppCompatActivity activity, LoginCallback loginCallback,
                               GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.activity = activity;
        this.loginCallback = loginCallback;
        this.onConnectionFailedListener = onConnectionFailedListener;
    }

    public void initialize(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.google_default_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, onConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, GOOGLE_LOGIN_ID);
    }

    public void handleResponse(Intent data, int requestCode){
        // If google sign in result
        if (requestCode == GOOGLE_LOGIN_ID) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                loginCallback.loginSuccess(data);
            } else {
                loginCallback.loginFailed();
            }
        }
    }
}
