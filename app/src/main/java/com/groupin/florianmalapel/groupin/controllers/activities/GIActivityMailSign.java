package com.groupin.florianmalapel.groupin.controllers.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.helpers.GICommunicationsHelper;
import com.groupin.florianmalapel.groupin.helpers.GISharedPreferencesHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONObject;

/**
 * Created by florianmalapel on 31/10/2016.
 */

public class GIActivityMailSign extends AppCompatActivity implements
        View.OnClickListener,
        View.OnTouchListener,
        GICommunicationsHelper.FirebaseCreateUserCallback,
        GIVolleyRequest.RequestCallback {

    private EditText editTextEmail = null;
    private EditText editTextPassword = null;
    private EditText editTextDisplayName = null;
    private ImageButton imageButtonEye = null;
    private ImageView imageViewGroupIn = null;
    private TextView textView_emailInvalid = null;
    private TextView textView_passwordInvalid = null;
    private Button buttonConnect = null;
    private Button buttonSignIn  = null;
    private String stringEmail   = null;
    private GICommunicationsHelper commsHelper = null;
    private GIVolleyHandler volleyHandler = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_sign);

        Bundle bundleEmail = getIntent().getBundleExtra(GIActivitySocialSign.TAG_EMAIL_BUNDLE);
        getEmailIfExistsFromBundle(bundleEmail);

        findViewByID();
        initialize();
        initialize_views();
        setClickListeners();
        setEditTextSetTextWatchers();

    }

    private void findViewByID(){
        buttonConnect = (Button) findViewById(R.id.button_connect);
        buttonSignIn = (Button) findViewById(R.id.button_signIn);
        imageButtonEye = (ImageButton) findViewById(R.id.imageButton_eye);
        imageViewGroupIn = (ImageView) findViewById(R.id.imageViewGroupIn);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        textView_emailInvalid = (TextView) findViewById(R.id.textView_emailInvalid);
        textView_passwordInvalid = (TextView) findViewById(R.id.textView_passwordInvalid);
    }

    private void initialize(){
        commsHelper = new GICommunicationsHelper(this);
        editTextEmail.setText(stringEmail);
        volleyHandler = new GIVolleyHandler();
    }

    private void initialize_views(){
        imageButtonEye.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }

    private void setClickListeners(){
        buttonConnect.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);
        imageButtonEye.setOnTouchListener(this);
    }

    private void getEmailIfExistsFromBundle(Bundle bundle){
        if(bundle != null && bundle.getString(GIActivitySocialSign.TAG_EMAIL_BUNDLE) != null)
            stringEmail = bundle.getString(GIActivitySocialSign.TAG_EMAIL_BUNDLE);
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void displayErrorMessage(){
        if(!isEmailValid(editTextEmail.getText().toString())){
            textView_emailInvalid.setVisibility(View.VISIBLE);
        }
        else textView_emailInvalid.setVisibility(View.GONE);
        if(editTextPassword.getText().toString().length() < 6){
            textView_passwordInvalid.setVisibility(View.VISIBLE);
        }
        else textView_passwordInvalid.setVisibility(View.GONE);
    }

    /**
     * TODO: fill the text listener
     */
    private void setEditTextSetTextWatchers(){
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                displayErrorMessage();
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                displayErrorMessage();
            }
        });

    }

    private void goToActivityMain(){
        Intent intent = new Intent(this, GIActivityMain.class);
        startActivity(intent);
        finish();
    }

        private void saveLoginInfoInPreferences(String email, String pwd){
        GISharedPreferencesHelper prefsHelper = new GISharedPreferencesHelper(this);
        prefsHelper.storeUserLogin(email);
        prefsHelper.storeUserPassword(pwd);
    }

    private void startLoading(ImageView imageView){
        imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loader_rotation));
    }


    @Override
    public void onClick(View view) {
        if(view == buttonConnect){
            displayErrorMessage();
            commsHelper.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString(), this, this);
        }

        else if(view == buttonSignIn){
            displayErrorMessage();
            commsHelper.createFirebaseUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString(), this, this);
        }

        startLoading(imageViewGroupIn);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(view == imageButtonEye){
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }

        return true;
    }

    @Override
    public void firebaseCreateUserSuccess(Task task) {
        Log.v("))- GIActivityMailSign", task.toString());
        saveLoginInfoInPreferences(editTextEmail.getText().toString(), editTextPassword.getText().toString());
    }

    @Override
    public void firebaseCreateUserFailed(Task task) {
        Log.v("))- GIActivityMailSign", "-- )) create user failed");
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        Log.v("))- GIActivityMailSign", "On request succeed");
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
            goToActivityMain();
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
