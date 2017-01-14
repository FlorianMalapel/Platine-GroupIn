package com.groupin.florianmalapel.groupin.helpers;

import android.util.Log;

import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 12/01/2017.
 */

public class GIJsonToObjectHelper {

    public static ArrayList<Object> mapJSON(int requestCode, JSONObject object) throws JSONException {
        Log.w("~~~~~~~~~~~~~", "|| " + requestCode + " " + object.toString());
        ArrayList<Object> listObjects = new ArrayList<>();

        switch (requestCode) {
            case GIRequestData.POST_USER_CODE:
                listObjects.add(getUserFromJSON(object));
                listObjects.add(getNotifsFriendsFromJSON(object));
                listObjects.add(getNotifsGroupsFromJSON(object));
                break;

            case GIRequestData.GET_USER_CODE:
                listObjects.add(getUserFromJSON(object));
                break;
        }


        if(listObjects.isEmpty())
            return null;

        return listObjects;
    }

    public static GIUser getUserFromJSON(JSONObject object) throws JSONException {
        if(object.has("user")){
            object = object.getJSONObject("user");
        }

        GIUser user = new GIUser();
        if(object.has("displayName"))
            user.display_name = object.getString("displayName");
        if(object.has("email"))
            user.email = object.getString("email");
        if(object.has("photoURL"))
            user.photoURL = object.getString("photoURL");
        if(object.has("providerId"))
            user.providerId = object.getString("providerId");
        if(object.has("uid"))
            user.uid = object.getString("uid");
        if(object.has("friends")){
            JSONObject friends = object.getJSONObject("friends");
            ArrayList<String> friendsUids = new ArrayList<>();
            for(int indexUid = 0; indexUid < friends.names().length(); indexUid++){
                if(friends.names().get(indexUid) != null && !((String)friends.names().get(indexUid)).isEmpty()){
                    friendsUids.add((String) friends.names().get(indexUid));
                }
            }

            if(!friendsUids.isEmpty())
                user.friendsUids = friendsUids;

            Log.wtf("∂∂∂∂∂", "∂∂∂∆∆∆∆∆∆∆∂∂∂∂∂ " + friends.names());
        }

        Log.wtf("∂∂∂∂∂", "∂∂∂∆∆∆∆∆∆∆∂∂∂∂∂ " + user.toString());
        if(user.email != null && !user.email.isEmpty())
            return user;

        return null;
    }

    public static Object getNotifsFriendsFromJSON(JSONObject object){
        // TODO NEED TO FINISH
        return null;
    }

    public static Object getNotifsGroupsFromJSON(JSONObject object){
        return null;
    }

}
