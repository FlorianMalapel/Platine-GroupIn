package com.groupin.florianmalapel.groupin.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.checker.GILogInApiChecker;
import com.groupin.florianmalapel.groupin.helpers.GICommunicationsHelper;
import com.groupin.florianmalapel.groupin.helpers.GIFacebookLoginHelper;
import com.groupin.florianmalapel.groupin.helpers.GIGoogleLoginHelper;
import com.groupin.florianmalapel.groupin.helpers.GISharedPreferencesHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.tools.DLog;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONObject;

public class GIActivitySocialSign extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        GIGoogleLoginHelper.LoginCallback,
        GICommunicationsHelper.FirebaseGoogleLoginSuccess,
        GIFacebookLoginHelper.FacebookLoginCallback,
        GICommunicationsHelper.FirebaseFacebookLoginSuccess,
        GIVolleyRequest.RequestCallback {

    public  static String           TAG_EMAIL_BUNDLE            = GIActivitySocialSign.class.getSimpleName();
    private static String           TAG                         = GIActivitySocialSign.class.getSimpleName();
    private GICommunicationsHelper  commsHelper                 = null;
    private GIGoogleLoginHelper     googleLoginHelper           = null;
    private GIFacebookLoginHelper   facebookLoginHelper         = null;
    private Button                  buttonGoogleSignIn          = null;
    private Button                  buttonFacebookSignIn        = null;
    private Button                  buttonContinue              = null;
    private EditText                editTextEmail               = null;
    private GISharedPreferencesHelper prefsHelper               = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_gisign);
        findViewById();
        checkIfUserLoggedIn();
        setListeners();
        initialize();
    }

    private void checkIfUserLoggedIn(){
        GILogInApiChecker logInApiChecker = new GILogInApiChecker(this);
        if(logInApiChecker.isUserLoggedIn() != GILogInApiChecker.STATE.NOT_CONNECTED) {
            Log.wtf("∂∂∂∂∂∂∂∂∂", "CONNECTED");
            goToActivityMain();
        }
        else {
            Log.wtf("∂∂∂∂∂∂∂∂∂", "NOT CONNECTED");
        }
    }

    private void initialize() {
        prefsHelper = new GISharedPreferencesHelper(this);
        commsHelper = new GICommunicationsHelper(this);
        buttonGoogleSignIn.setOnClickListener(this);
        googleLoginHelper = new GIGoogleLoginHelper(this, this, this);
        facebookLoginHelper = new GIFacebookLoginHelper(this);
    }

    private void setListeners(){
        buttonFacebookSignIn.setOnClickListener(this);
        buttonGoogleSignIn.setOnClickListener(this);
        buttonContinue.setOnClickListener(this);
    }

    private void findViewById(){
        buttonGoogleSignIn = (Button) findViewById(R.id.button_sign_in_google);
        buttonFacebookSignIn = (Button) findViewById(R.id.button_facebook_login);
        buttonContinue = (Button) findViewById(R.id.button_continue);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
    }


    private void goToActivityMain(){
        Intent intent = new Intent(this, GIActivityMain.class);
        startActivity(intent);
        finish();
    }

    private void goToActivityMailSign(){
        Intent intent = new Intent(this, GIActivityMailSign.class);
        Bundle bundle = getEmailAndPutItInBundle();
        if(bundle != null)
            intent.putExtra(TAG_EMAIL_BUNDLE, bundle);
        startActivity(intent);
        finish();
    }

    private Bundle getEmailAndPutItInBundle(){
        String email = editTextEmail.getText().toString();

        if(email == null)
            return null;

        Bundle bundle = new Bundle();
        bundle.putString(TAG_EMAIL_BUNDLE, email);
        return bundle;
    }

    private void saveAccessTokenInPreference(String accessToken){
        prefsHelper.storeUserAccessTokenFb(accessToken);
    }

    @Override
    public void onClick(View view) {
        if( view == buttonGoogleSignIn ){
            googleLoginHelper.initialize();
        }

        else if(view == buttonFacebookSignIn){
            facebookLoginHelper.initialize(this);
        }

        else if(view == buttonContinue){
            goToActivityMailSign();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        commsHelper.stopAuthListerner();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DLog.logwtf(TAG, "∆∆onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    @Override
    public void loginSuccess(Intent data) {
        Log.wtf(" ~~~~~~~  ", "facebook firebase success");
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            commsHelper.firebaseAuthWithGoogle(account.getIdToken(), this, this, this);
            prefsHelper.storeUserIdTokenGoogle(account.getIdToken());
        }
    }

    @Override
    public void loginFailed() {
            DLog.logwtf(TAG, "google connection failed");
            // Google Sign In failed, update UI appropriately
    }

    @Override
    public void firebaseGoogleLoginSuccess() {
//        goToActivityMain();
    }

    @Override
    public void facebookLoginSuccess(LoginResult loginResult) {
        saveAccessTokenInPreference(loginResult.getAccessToken().toString());
        commsHelper.firebaseAuthWithFacebook(loginResult.getAccessToken(), GIActivitySocialSign.this, this, this);
    }

    @Override
    public void firebaseFacebookLoginSuccess() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == googleLoginHelper.GOOGLE_LOGIN_ID)
            googleLoginHelper.handleResponse(data, requestCode);
        else facebookLoginHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        if(request_code == GIRequestData.POST_USER_CODE) {
            GIApplicationDelegate.getInstance().onRequestFinishWithSuccess(request_code, object);
            goToActivityMain();
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
