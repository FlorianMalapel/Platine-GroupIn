package com.groupin.florianmalapel.groupin.model;

import android.content.Context;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.groupin.florianmalapel.groupin.helpers.GISharedPreferencesHelper;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
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
    public HashMap<String, GIGroup>     userGroupsList      = null;
    private GISharedPreferencesHelper   prefsHelper         = null;

    public GIDataCache(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.start();
        this.prefsHelper = new GISharedPreferencesHelper(context);
        this.userFriendList = new HashMap<>();
        this.userGroupsList = new HashMap<>();
        this.user = getUserFromPrefs();
    }

    private GIUser getUserFromPrefs(){
        if(prefsHelper.getUserUid() == null)
            return null;

        GIUser user = new GIUser(
                prefsHelper.getUserLogin(),
                prefsHelper.getUserDisplayName()
        );
        user.uid = prefsHelper.getUserUid();
        user.providerId = prefsHelper.getUserProviderId();
        user.photoURL = prefsHelper.getUserPhotoUrl();

        return user;
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
        if(allUsersList != null && user != null && user.friendsUids != null){
            for(String uid: user.friendsUids){
                userFriendList.put(uid, allUsersList.get(uid));
            }
        }
        Log.v("~~ ~~ ~~ ~~ ", userFriendList.toString());
    }

    public String getUserUid(){
        return prefsHelper.getUserUid();
    }

    public String isUserLoggedIn(){
        if(this.getUserUid() != null)
            return this.getUserUid();
        return null;
    }

    public GIUser findUserFromAll(String email){
        if(allUsersList == null)
            return null;

        for(String key : allUsersList.keySet()){
            if(allUsersList.get(key).email.contains(email)){
                return allUsersList.get(key);
            }
        }

        return null;
    }

    public ArrayList<GIUser> getUserFriendsList(){
        ArrayList<GIUser> friendList = new ArrayList<>();
        for(String key : userFriendList.keySet()){
            friendList.add(userFriendList.get(key));
        }

        sortFriendsListAlphabetically(friendList);
        return friendList;
    }

    private void sortFriendsListAlphabetically(ArrayList<GIUser> friendsList){
        Collections.sort(friendsList, new SortedListAdapterCallback<GIUser>(null) {
            // TODO Usually sorted by lastname, but for now it will be the display name
            @Override
            public int compare(GIUser o1, GIUser o2) {
                return o1.display_name.compareTo(o2.display_name);
            }

            @Override
            public boolean areContentsTheSame(GIUser oldItem, GIUser newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(GIUser item1, GIUser item2) {
                return false;
            }
        });
    }

    public void setUserGroupsList(HashMap<String, GIGroup> list){
        this.userGroupsList = list;

        if(this.userGroupsList == null)
            return;

        if(this.userGroupsList.size() != this.user.groupsUids.size()){
            for(String key : this.userGroupsList.keySet()){
                if(!this.user.groupsUids.contains(key)){
                    this.user.groupsUids.add(key);
                }
            }
        }
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
