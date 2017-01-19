package com.groupin.florianmalapel.groupin.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;

/**
 * Created by florianmalapel on 14/01/2017.
 */

public class GIActivityLauncher extends AppCompatActivity implements Animation.AnimationListener {

    private ImageView imageViewGroupInLogo = null;
    private static final int NB_ROTATE_ANIM = 2;
    private int currentNbRotateAnim = 0;
    private boolean isUserConnected = false;
    private GIVolleyHandler volleyHandler = null;

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
    }

    private void findViewById() {
        imageViewGroupInLogo = (ImageView) findViewById(R.id.imageViewGroupInLogo);
    }

    private void initializeObjects() {
        volleyHandler = new GIVolleyHandler();
        if (GIApplicationDelegate.getInstance().getDataCache().isUserLoggedIn() != null) {
            isUserConnected = true;
            volleyHandler.postUser(GIApplicationDelegate.getInstance().getDataCache().user, GIApplicationDelegate.getInstance());
        } else {
            isUserConnected = false;
        }

    }


    private void initializeViews() {
        startAnimationOnLogo();
    }

    private void onAnimationTerminated(){
        if(currentNbRotateAnim == NB_ROTATE_ANIM) {

            if (isUserConnected) {
                goToMainActivity();
            } else {
                goToLoginActivity();
            }

        }

        else {
            currentNbRotateAnim++;
            startAnimationOnLogo();
        }
    }

    private void startAnimationOnLogo(){
        RotateAnimation rotateAnimation = new RotateAnimation((currentNbRotateAnim * 90), 90 + (currentNbRotateAnim * 90), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(800);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setAnimationListener(this);
        imageViewGroupInLogo.setAnimation(rotateAnimation);
        imageViewGroupInLogo.startAnimation(rotateAnimation);
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
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        onAnimationTerminated();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
