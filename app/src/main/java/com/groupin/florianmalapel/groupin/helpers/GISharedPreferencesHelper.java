package com.groupin.florianmalapel.groupin.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by florianmalapel on 05/01/2017.
 */

public class GISharedPreferencesHelper {

    private static final int    PRIVATE_MODE = 0;
    private static final String PREFS_FILE_NAME                 = "prefs_groupin";
    private static final String KEY_USER_LOGIN                  = "login";
    private static final String KEY_USER_PASSWORD               = "password";
    private static final String KEY_USER_ACCESS_TOKEN_FB        = "access_token_fb";
    private static final String KEY_USER_ACCESS_TOKEN_GOOGLE    = "access_token_google";
    private static final String KEY_USER_DISPLAY_NAME           = "display_name";
    private static final String KEY_USER_PHOTO_URL              = "photo_url";
    private static final String KEY_USER_PROVIDER_ID            = "provider_id";
    private static final String KEY_USER_UID                    = "user_uid";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public GISharedPreferencesHelper(Context context) {
        this.context = context;
        try {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_FILE_NAME, PRIVATE_MODE);
        } catch(Exception e) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }
        editor = sharedPreferences.edit();
    }

    public void storeUserLogin(String login){
        editor.putString(KEY_USER_LOGIN, login);
        editor.commit();
    }

    public String getUserLogin(){
        return sharedPreferences.getString(KEY_USER_LOGIN, null);
    }

    public String getUserPassword(){
        return sharedPreferences.getString(KEY_USER_PASSWORD, null);
    }

    public void storeUserPassword(String password){
        editor.putString(KEY_USER_PASSWORD, password);
        editor.commit();
    }

    public void storeUserAccessTokenFb(String access_token){
        editor.putString(KEY_USER_ACCESS_TOKEN_FB, access_token);
        editor.commit();
    }

    public String getUserAccessTokenFb(){
        return sharedPreferences.getString(KEY_USER_ACCESS_TOKEN_FB, null);
    }

    public void storeUserIdTokenGoogle(String access_token){
        editor.putString(KEY_USER_ACCESS_TOKEN_GOOGLE, access_token);
        editor.commit();
    }

    public void storeUserUid(String userUid){
        editor.putString(KEY_USER_UID, userUid);
        editor.commit();
    }

    public void storeProviderId(String providerId){
        editor.putString(KEY_USER_PROVIDER_ID, providerId);
        editor.commit();
    }

    public void storeDisplayName(String displayName){
        editor.putString(KEY_USER_DISPLAY_NAME, displayName);
        editor.commit();
    }

    public void storePhotoURL(String photoURL){
        editor.putString(KEY_USER_PHOTO_URL, photoURL);
        editor.commit();
    }

    public String getUserProviderId(){
        return sharedPreferences.getString(KEY_USER_PROVIDER_ID, null);
    }

    public String getUserDisplayName(){
        return sharedPreferences.getString(KEY_USER_DISPLAY_NAME, null);
    }

    public String getUserPhotoUrl(){
        return sharedPreferences.getString(KEY_USER_PHOTO_URL, null);
    }

    public String getUserIdTokenGoogle(){
        return sharedPreferences.getString(KEY_USER_ACCESS_TOKEN_GOOGLE, null);
    }

    public String getUserUid(){
        return sharedPreferences.getString(KEY_USER_UID, null);
    }


}
