package com.groupin.florianmalapel.groupin.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.checker.GILogInApiChecker;
import com.groupin.florianmalapel.groupin.helpers.GICommunicationsHelper;
import com.groupin.florianmalapel.groupin.helpers.GISharedPreferencesHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONObject;

/**
 * Created by florianmalapel on 14/01/2017.
 */

public class GIActivityLauncher extends AppCompatActivity
        implements  GIVolleyRequest.RequestCallback,
                    GICommunicationsHelper.FirebaseFacebookLoginSuccess,
                    GICommunicationsHelper.FirebaseGoogleLoginSuccess,
                    GICommunicationsHelper.FirebaseCreateUserCallback {

    private ImageView imageViewGroupInLogo = null;
    private static final int NB_ROTATE_ANIM = 2;
    private int currentNbRotateAnim = 0;
    private GIVolleyHandler volleyHandler = null;
    private GILogInApiChecker logInApiChecker = null;
    private GICommunicationsHelper commsHelper = null;
    private GISharedPreferencesHelper prefsHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initialize();
    }

    private void initialize() {
        findViewById();
        initializeObjects();
        initializeViews();
        checkIfUserLogged();
    }

    private void findViewById() {
        imageViewGroupInLogo = (ImageView) findViewById(R.id.imageViewGroupInLogo);
    }

    private void initializeObjects() {
        volleyHandler = new GIVolleyHandler();
        logInApiChecker = new GILogInApiChecker(this);
        commsHelper = new GICommunicationsHelper(this);
        prefsHelper = new GISharedPreferencesHelper(this);
    }

    private void checkIfUserLogged(){
        if (logInApiChecker.isUserLoggedIn() == GILogInApiChecker.STATE.CONNECTED_FB){
            commsHelper.firebaseAuthWithFacebook(prefsHelper.getUserAccessTokenFb(), this, this, this);
        }

        else if(logInApiChecker.isUserLoggedIn() == GILogInApiChecker.STATE.CONNECTED_GOOGLE){
            commsHelper.firebaseAuthWithGoogle(prefsHelper.getUserIdTokenGoogle(), this, this, this);
        }

        else if(logInApiChecker.isUserLoggedIn() == GILogInApiChecker.STATE.CONNECTED_MAIL){
            commsHelper.signInWithEmailAndPassword(prefsHelper.getUserLogin(), prefsHelper.getUserPassword(), this, this);
        }

        else if (logInApiChecker.isUserLoggedIn() == GILogInApiChecker.STATE.NOT_CONNECTED) {
            goToLoginActivity();
        }
    }


    private void initializeViews() {
        startAnimationLoader();
    }

    private void startAnimationLoader(){
        imageViewGroupInLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loader_rotation));
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, GIActivityMain.class);
        startActivity(intent);
        finish();
    }

    private void goToLoginActivity(){
        Intent intent = new Intent(this, GIActivitySocialSign.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        GIApplicationDelegate.getInstance().onRequestFinishWithSuccess(request_code, object);
        if(request_code == GIRequestData.POST_USER_CODE) {
            GIApplicationDelegate.getInstance().getDataCache().storeCurrentUserInPref();
            volleyHandler.getGroups(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid());
        }

        if(request_code == GIRequestData.GET_GROUPS_CODE) {
            volleyHandler.getEventsOfUser(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid());
        }

        if(request_code == GIRequestData.GET_EVENTS_USER_CODE){
            volleyHandler.getNotifications(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid());
        }

        if(request_code == GIRequestData.GET_NOTIFICATIONS_CODE){
            goToMainActivity();
        }
    }

    @Override
    public void onRequestFinishWithFailure() {
//        volleyHandler.postUser(GIApplicationDelegate.getInstance().getDataCache().user, this);
    }

    @Override
    public void firebaseFacebookLoginSuccess() {

    }

    @Override
    public void firebaseGoogleLoginSuccess() {

    }

    @Override
    public void firebaseCreateUserSuccess(Task task) {

    }

    @Override
    public void firebaseCreateUserFailed(Task task) {

    }
}
