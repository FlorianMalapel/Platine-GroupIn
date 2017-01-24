package com.groupin.florianmalapel.groupin.helpers;

import android.util.Log;

import com.groupin.florianmalapel.groupin.model.GIDataCache;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationFriend;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by florianmalapel on 12/01/2017.
 */

public class GIDataCacheHelper {

    // int.class.isAssignableFrom(int.class)

    public GIDataCacheHelper() {
    }

    public static void handleResponseRequest(int requestCode, JSONObject response, GIDataCache cache){
        ArrayList<GIJsonToObjectHelper.ItemReceived> listObjects = null;
        try {
            listObjects = GIJsonToObjectHelper.mapJSON(requestCode, response);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.wtf("GIDataCacheHelper", e.getLocalizedMessage());
        }

        if(listObjects == null)
            return;

        for(GIJsonToObjectHelper.ItemReceived item : listObjects){
            handleObjectToCache(item, cache);
        }
    }

    public static void handleObjectToCache(GIJsonToObjectHelper.ItemReceived itemReceived, GIDataCache cache){

        if(itemReceived == null) {
            return;
        }

        if(itemReceived.request_code == GIRequestData.MY_USER){
            cache.setUser((GIUser) itemReceived.object);
        }

        if(itemReceived.request_code == GIRequestData.USER){
            cache.setUser((GIUser) itemReceived.object);
        }

        else if(itemReceived.request_code == GIRequestData.ALL_USERS){
            cache.setAllUsersList((HashMap<String, GIUser>) itemReceived.object);
        }

        else if(itemReceived.request_code == GIRequestData.ALL_GROUPS){
            cache.setUserGroupsList((HashMap<String, GIGroup>) itemReceived.object);
        }

        else if(itemReceived.request_code == GIRequestData.GROUP){
            cache.addGroupOrUpdate((GIGroup) itemReceived.object);
        }

        else if(itemReceived.request_code == GIRequestData.EVENTS) {
            cache.setEventsList((HashMap<String,GIEvent>) itemReceived.object);
        }

        else if(itemReceived.request_code == GIRequestData.NOTIFS_FRIENDS) {
            cache.setNotificationsFriend((ArrayList<GINotificationFriend>) itemReceived.object);
        }

        else if(itemReceived.request_code == GIRequestData.NOTIFS_GROUP) {
            cache.setNotificationsGroup((ArrayList<GINotificationGroup>) itemReceived.object);
        }
        // TODO NEED TO FINISH

    }
}
