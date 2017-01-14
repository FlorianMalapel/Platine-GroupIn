package com.groupin.florianmalapel.groupin.helpers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.groupin.florianmalapel.groupin.tools.DLog;

import java.util.Arrays;

/**
 * Created by florianmalapel on 30/10/2016.
 */

public class GIFacebookLoginHelper {

    private final String TAG = "FacebookLogin";

    private CallbackManager callbackManager = null;
    private FacebookLoginCallback loginCallback = null;

    public interface FacebookLoginCallback {
        void facebookLoginSuccess(LoginResult loginResult);
    }

    public GIFacebookLoginHelper(FacebookLoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public void initialize(AppCompatActivity activity){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        DLog.logwtf(TAG, "onSuccess " + loginResult.getAccessToken());
                        loginCallback.facebookLoginSuccess(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        DLog.logwtf(TAG, "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        DLog.logwtf(TAG, "onError");
                    }
                });
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
