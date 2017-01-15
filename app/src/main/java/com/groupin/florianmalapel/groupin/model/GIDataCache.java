package com.groupin.florianmalapel.groupin.model;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.groupin.florianmalapel.groupin.helpers.GISharedPreferencesHelper;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by florianmalapel on 05/01/2017.
 */

public class GIDataCache implements GIVolleyRequest.RequestCallback {

    // RequestQueue used by the API Volley which allow HTTP requests
    public RequestQueue                 requestQueue        = null;
    public GIUser                       user                = null;
    public HashMap<String, GIUser>      userFriendList      = null;
    public HashMap<String, GIUser>      allUsersList        = null;
    private GISharedPreferencesHelper   prefsHelper         = null;

    public GIDataCache(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.start();
        this.prefsHelper = new GISharedPreferencesHelper(context);
        this.userFriendList = new HashMap<>();
    }

    public void setUser(GIUser user){
        this.user = user;
        this.prefsHelper.storeUserUid(this.user.uid);
    }

    public void getFriendsUser(){
        GIVolleyHandler volleyHandler = new GIVolleyHandler();
        for(String friendUid : user.friendsUids){

        }
    }

    public void setAllUsersList(HashMap<String, GIUser> allUsersList) {
        this.allUsersList = allUsersList;
    }

    public String getUserUid(){
        return prefsHelper.getUserUid();
    }

    public String isUserLoggedIn(){
        if(this.getUserUid() != null)
            return this.getUserUid();
        return null;
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {

    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
