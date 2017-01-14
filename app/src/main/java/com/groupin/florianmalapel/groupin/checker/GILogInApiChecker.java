package com.groupin.florianmalapel.groupin.checker;

import android.content.Context;

import com.groupin.florianmalapel.groupin.helpers.GISharedPreferencesHelper;

/**
 * Created by florianmalapel on 12/01/2017.
 */

public class GILogInApiChecker {

    private Context context = null;
    private GISharedPreferencesHelper prefsHelper = null;

    public enum STATE {
        CONNECTED_FB, CONNECTED_GOOGLE, CONNECTED_MAIL, NOT_CONNECTED
    }

    public GILogInApiChecker(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        prefsHelper = new GISharedPreferencesHelper(context);
    }

    public STATE isUserLoggedIn(){
        if(prefsHelper.getUserAccessTokenFb() != null)
            return STATE.CONNECTED_FB;
        else if(prefsHelper.getUserIdTokenGoogle() != null)
            return STATE.CONNECTED_GOOGLE;
        else if(prefsHelper.getUserLogin() != null && prefsHelper.getUserPassword() != null)
            return STATE.CONNECTED_MAIL;
        else return STATE.NOT_CONNECTED;
    }
}
