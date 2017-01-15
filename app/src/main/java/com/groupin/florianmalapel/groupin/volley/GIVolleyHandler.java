package com.groupin.florianmalapel.groupin.volley;

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
        String  url = GIRequestData.API_URL + GIRequestData.GROUP_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_GROUP_CODE, Request.Method.POST, url, callback);
        request.initPostJSONRequest(null, groupObject);
        request.startRequest();
    }

    public void getGroup(GIVolleyRequest.RequestCallback callback, String uid){
        String url = GIRequestData.API_URL + GIRequestData.GROUP_ENDPOINT + "/" + uid;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_GROUP_CODE, Request.Method.GET, url, callback);
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
}
