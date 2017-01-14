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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.helpers.GICommunicationsHelper;
import com.groupin.florianmalapel.groupin.helpers.GISharedPreferencesHelper;

/**
 * Created by florianmalapel on 31/10/2016.
 */

public class GIActivityMailSign extends AppCompatActivity implements
        View.OnClickListener,
        View.OnTouchListener,
        GICommunicationsHelper.FirebaseCreateUserCallback {

    private EditText editTextEmail = null;
    private EditText editTextPassword = null;
    private ImageButton imageButtonEye = null;
    private TextView textView_emailInvalid = null;
    private TextView textView_passwordInvalid = null;
    private Button buttonConnect = null;
    private Button buttonSignIn  = null;
    private String stringEmail   = null;
    private GICommunicationsHelper commsHelper = null;



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
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        textView_emailInvalid = (TextView) findViewById(R.id.textView_emailInvalid);
        textView_passwordInvalid = (TextView) findViewById(R.id.textView_passwordInvalid);
    }

    private void initialize(){
        commsHelper = new GICommunicationsHelper(this);
        editTextEmail.setText(stringEmail);
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

    @Override
    public void onClick(View view) {
        if(view == buttonConnect){
            displayErrorMessage();
            commsHelper.createFirebaseUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString(), this);
        }

        else if(view == buttonSignIn){
            displayErrorMessage();
            commsHelper.createFirebaseUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString(), this);
        }

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
        saveLoginInfoInPreferences(editTextEmail.getText().toString(), editTextPassword.getText().toString());
        goToActivityMain();
    }

    @Override
    public void firebaseCreateUserFailed(Task task) {

    }
}
