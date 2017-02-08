package com.groupin.florianmalapel.groupin.volley;

import com.android.volley.Request;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
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

    public void postParticipateEvent(GIVolleyRequest.RequestCallback callback, String eventId, String groupId, boolean isParticipating){
        String  url = GIRequestData.API_URL + GIRequestData.EVENT_ENDPOINT + GIRequestData.PARTICIP_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_PARTICIP_EVENT_CODE, Request.Method.POST, url, callback);

        JSONObject object = new JSONObject();
        try {
            object.put("uid", GIApplicationDelegate.getInstance().getDataCache().getUserUid());
            object.put("participe", isParticipating);
            object.put("event", eventId);
            object.put("groupe", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.initPostJSONRequest(null, object);
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

    public void postNotifAddUserInGroup(GIVolleyRequest.RequestCallback callback, String id_group, String uid_userToAdd){
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
        String url = GIRequestData.API_URL + GIRequestData.NOTIF_ENDPOINT + GIRequestData.AMIS_ENDPOINT;
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

    public void postUserJoinGroup(GIVolleyRequest.RequestCallback callback, String userUid, String groupId){
        String url = GIRequestData.API_URL + GIRequestData.USER_ENDPOINT + GIRequestData.GROUP_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_FRIENDSHIP_CODE, Request.Method.POST, url, callback);
        JSONObject object = new JSONObject();
        try {
            object.put("idG", groupId); // Ask for friendship
            object.put("uidR", userUid); // Asked for friendship
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.initPostJSONRequest(null, object);
        request.startRequest();
    }

    public void deleteNotifFriend(GIVolleyRequest.RequestCallback callback, String userUid, String friendUid){
        String url = GIRequestData.API_URL + GIRequestData.NOTIF_ENDPOINT + GIRequestData.USER_ENDPOINT + "/" + userUid + GIRequestData.FRIENDS_ENDPOINT + "/" + friendUid;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.DELETE_GROUP_CODE, Request.Method.DELETE, url, callback);
        request.initDeleteJSONRequest(null);
        request.startRequest();
    }

    public void deleteNotifGroup(GIVolleyRequest.RequestCallback callback, String userUid, String idGroup){
        String url = GIRequestData.API_URL + GIRequestData.NOTIF_ENDPOINT + GIRequestData.USER_ENDPOINT + "/" + userUid + GIRequestData.GROUP_ENDPOINT + "/" + idGroup;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.DELETE_GROUP_CODE, Request.Method.DELETE, url, callback);
        request.initDeleteJSONRequest(null);
        request.startRequest();
    }

    public void getMessagesChatFromGroup(GIVolleyRequest.RequestCallback callback, String userUid, String idGroup){
        String url = GIRequestData.API_URL + GIRequestData.CHAT_ENDPOINT + GIRequestData.USER_ENDPOINT + "/" + userUid + GIRequestData.GROUP_ENDPOINT + "/" + idGroup;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_CHAT_GROUP_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }

    public void postChatMessage(GIVolleyRequest.RequestCallback callback, String userUid, String groupId, String message){
        String url = GIRequestData.API_URL + GIRequestData.CHAT_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_FRIENDSHIP_CODE, Request.Method.POST, url, callback);
        JSONObject object = new JSONObject();
        try {
            object.put("uid", userUid);
            object.put("groupId", groupId);
            object.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.initPostJSONRequest(null, object);
        request.startRequest();
    }

    public void getPollsFromGroup(GIVolleyRequest.RequestCallback callback, String userUid, String idGroup){
        String url = GIRequestData.API_URL + GIRequestData.POLLS_ENDPOINT + GIRequestData.USER_ENDPOINT + "/" + userUid + GIRequestData.GROUP_ENDPOINT + "/" + idGroup;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_POLLS_GROUP_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }

    public void postPoll(GIVolleyRequest.RequestCallback callback, JSONObject pollObject){
        String  url = GIRequestData.API_URL + GIRequestData.POLLS_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_POLLS_GROUP_CODE, Request.Method.POST, url, callback);
        request.initPostJSONRequest(null, pollObject);
        request.startRequest();
    }

    public void postPollAnswer(GIVolleyRequest.RequestCallback callback, JSONObject pollAnswerObject){
        String  url = GIRequestData.API_URL + GIRequestData.POLLS_ENDPOINT + GIRequestData.USER_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_POLLS_ANSWER_CODE, Request.Method.POST, url, callback);
        request.initPostJSONRequest(null, pollAnswerObject);
        request.startRequest();
    }

    public void postBill(GIVolleyRequest.RequestCallback callback, JSONObject billJSON){
        String  url = GIRequestData.API_URL + GIRequestData.EUROS_ENDPOINT + GIRequestData.BILLS_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.POST_BILL_CODE, Request.Method.POST, url, callback);
        request.initPostJSONRequest(null, billJSON);
        request.startRequest();
    }

    public void getBills(GIVolleyRequest.RequestCallback callback, String groupId){
        String  url = GIRequestData.API_URL + GIRequestData.EUROS_ENDPOINT + "/" + groupId + GIRequestData.BILLS_ENDPOINT;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.GET_BILLS_GROUP_CODE, Request.Method.GET, url, callback);
        request.initGetJSONRequest(null);
        request.startRequest();
    }

    public void deleteBills(GIVolleyRequest.RequestCallback callback, String groupId, String billId){
        String url = GIRequestData.API_URL + GIRequestData.EUROS_ENDPOINT + "/" + groupId + GIRequestData.BILLS_ENDPOINT + "/" + billId;
        GIVolleyRequest request = new GIVolleyRequest(GIRequestData.DELETE_BILLS_GROUP_CODE, Request.Method.DELETE, url, callback);
        request.initDeleteJSONRequest(null);
        request.startRequest();
    }
}
