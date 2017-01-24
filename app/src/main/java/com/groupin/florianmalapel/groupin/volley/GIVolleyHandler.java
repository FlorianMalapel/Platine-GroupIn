package com.groupin.florianmalapel.groupin.volley;

import android.util.Log;

import com.android.volley.Request;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by florianmalapel on 23/11/2016.
 */

public class GIVolleyHandler {

    public GIVolleyHandler() {

    }

    public void postUser(GIUser user, GIVolleyRequest.RequestCallback callback){
        String url = GIRequestData.API_URL + GIRequestData.USER_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_USER_CODE, Request.Method.POST, url, callback);

        try {
            request.initPostJSONRequest(null, user.getJSONUser());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.startRequest();
    }

    public void getUser(String uid, GIVolleyRequest.RequestCallback callback){
        String url = GIRequestData.API_URL + GIRequestData.USER_ENDPOINT + "/" + uid;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_USER_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }

    public void getMyUser(String uid, GIVolleyRequest.RequestCallback callback){
        String url = GIRequestData.API_URL + GIRequestData.USER_ENDPOINT + "/" + uid;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_USER_MY_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }

    public void getAllUsers(GIVolleyRequest.RequestCallback callback){
        String url = GIRequestData.API_URL + GIRequestData.USER_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_USERS_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }

    public void deleteGroup(GIVolleyRequest.RequestCallback callback, String userUid, String groupId){
        String url = GIRequestData.API_URL + GIRequestData.USER_ENDPOINT + userUid + GIRequestData.GROUP_ENDPOINT + "/" + groupId;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.DELETE_GROUP_CODE, Request.Method.DELETE, url, callback);
        request.initDeleteJSONRequest(null);
        request.startRequest();
    }

    public void postNewGroup(GIVolleyRequest.RequestCallback callback, JSONObject groupObject) {
        Log.v("∆∆ volleyHandler ∆∆", groupObject.toString());
        String  url = GIRequestData.API_URL + GIRequestData.GROUP_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_GROUP_CODE, Request.Method.POST, url, callback);
        request.initPostJSONRequest(null, groupObject);
        request.startRequest();
    }

    public void getGroups(GIVolleyRequest.RequestCallback callback, String uid){
        String url = GIRequestData.API_URL + GIRequestData.GROUP_ENDPOINT + "/" + uid;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_GROUPS_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }


    public void postFriendShip(GIVolleyRequest.RequestCallback callback, String userUid, String friendUid){
        String url = GIRequestData.API_URL + GIRequestData.USER_ENDPOINT + GIRequestData.FRIENDS_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_FRIENDSHIP_CODE, Request.Method.POST, url, callback);
        JSONObject object = new JSONObject();
        try {
            object.put("uidR", userUid); // Ask for friendship
            object.put("uidD", friendUid); // Asked for friendship
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.initPostJSONRequest(null, object);
        request.startRequest();
    }

    public void postEvent(GIVolleyRequest.RequestCallback callback, JSONObject eventObject){
        String  url = GIRequestData.API_URL + GIRequestData.EVENT_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_EVENT_CODE, Request.Method.POST, url, callback);
        request.initPostJSONRequest(null, eventObject);
        request.startRequest();
    }

    public void getEventsOfUser(GIVolleyRequest.RequestCallback callback, String uid){
        String url = GIRequestData.API_URL + GIRequestData.EVENT_ENDPOINT + GIRequestData.USER_ENDPOINT + "/" + uid + GIRequestData.ANDROID_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_EVENTS_USER_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }

    public void getNotifications(GIVolleyRequest.RequestCallback callback, String uid){
        String url = GIRequestData.API_URL + GIRequestData.NOTIF_ENDPOINT + "/" + uid;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_NOTIFICATIONS_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }

    public void postAddUserInGroup(GIVolleyRequest.RequestCallback callback, String id_group, String uid_userToAdd){
        String url = GIRequestData.API_URL + GIRequestData.NOTIF_ENDPOINT + GIRequestData.GROUP_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_NOTIF_ADD_GROUP_CODE, Request.Method.POST, url, callback);

        JSONObject object = new JSONObject();
        try {
            object.put("idG", id_group);
            object.put("uidR", uid_userToAdd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.initPostJSONRequest(null, object);
        request.startRequest();
    }

    public void postSendNotifFriendShip(GIVolleyRequest.RequestCallback callback, String userUid, String friendUid){
        String url = GIRequestData.API_URL + GIRequestData.NOTIF_ENDPOINT + GIRequestData.FRIENDS_ENDPOINT + GIRequestData.FRIENDS_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_FRIENDSHIP_CODE, Request.Method.POST, url, callback);
        JSONObject object = new JSONObject();
        try {
            object.put("uidD", userUid); // Ask for friendship
            object.put("uidR", friendUid); // Asked for friendship
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.initPostJSONRequest(null, object);
        request.startRequest();
    }
}
