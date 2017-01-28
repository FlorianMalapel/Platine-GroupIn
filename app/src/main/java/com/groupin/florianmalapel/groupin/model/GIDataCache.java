package com.groupin.florianmalapel.groupin.model;

import android.content.Context;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.groupin.florianmalapel.groupin.helpers.GISharedPreferencesHelper;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationFriend;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationGroup;
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

    private Context                             context             = null;
    // RequestQueue used by the API Volley which allow HTTP requests
    public RequestQueue                         requestQueue        = null;
    public GIUser                               user                = null;
    public HashMap<String, GIUser>              userFriendList      = null;
    public HashMap<String, GIUser>              allUsersList        = null;
    public HashMap<String, GIGroup>             userGroupsList      = null;
    public HashMap<String, GIEvent>             eventsList          = null;
    public ArrayList<GINotificationFriend>      notifsFriendList    = null;
    public ArrayList<GINotificationGroup>       notifsGroupList     = null;
    private GISharedPreferencesHelper           prefsHelper         = null;

    public GIDataCache(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.start();
        this.prefsHelper = new GISharedPreferencesHelper(context);
        this.userFriendList = new HashMap<>();
        this.userGroupsList = new HashMap<>();
        this.eventsList = new HashMap<>();
        this.notifsFriendList = new ArrayList<>();
        this.notifsGroupList = new ArrayList<>();
        this.user = getCurrentUserFromPrefs();
    }

    private GIUser getCurrentUserFromPrefs(){
        if(prefsHelper.getUserUid() == null)
            return new GIUser();

        GIUser user = new GIUser(
                prefsHelper.getUserLogin(),
                prefsHelper.getUserDisplayName()
        );
        user.uid = prefsHelper.getUserUid();
        user.providerId = prefsHelper.getUserProviderId();
        user.photoURL = prefsHelper.getUserPhotoUrl();

        Log.v("|| GIDATACACHE ||", " GET user : " + user.toString());
        return user;
    }

    public void logOut(){
        prefsHelper.reset();
        this.user = new GIUser();
    }

    public void storeCurrentUserInPref(){
        Log.v("|| GIDATACACHE ||", " POST user : " + user.toString());
        prefsHelper.storeUserUid(user.uid);
        prefsHelper.storeUserLogin(user.email);
        prefsHelper.storePhotoURL(user.photoURL);
        prefsHelper.storeDisplayName(user.display_name);
        prefsHelper.storeProviderId(user.providerId);
    }

    public void setUser(GIUser user){
        this.user = user;
        this.prefsHelper.storeUserUid(this.user.uid);
    }

    public void addOrUpdateUserFriend(GIUser user){
        if(allUsersList.containsKey(user.uid)){
            allUsersList.remove(user.uid);
            allUsersList.put(user.uid, user);
        }

        else allUsersList.put(user.uid, user);
    }

    public void getFriendsUser(){
        GIVolleyHandler volleyHandler = new GIVolleyHandler();
        for(String friendUid : user.friendsUids){

        }
    }

    public void setNotificationsFriend(ArrayList<GINotificationFriend> notifsFriendList){
        this.notifsFriendList = notifsFriendList;
    }


    public void setNotificationsGroup(ArrayList<GINotificationGroup> notifsGroupList){
        this.notifsGroupList = notifsGroupList;
    }

    public void setAllUsersList(HashMap<String, GIUser> allUsersList) {
        this.allUsersList = allUsersList;
        if(allUsersList != null && user != null && user.friendsUids != null){
            for(String uid: user.friendsUids){
                userFriendList.put(uid, allUsersList.get(uid));
            }
        }
        Log.v("~~ ~~ ~~ ~~ ", "GIDataCache : " + userFriendList.toString());
    }

    public void setEventsList(HashMap<String, GIEvent> eventsList){
        if(this.eventsList.isEmpty())
            this.eventsList = eventsList;
        else {
            for(String eventId : eventsList.keySet()){
                if(this.eventsList.containsKey(eventId)){
                    this.eventsList.remove(eventId);
                    this.eventsList.put(eventId, eventsList.get(eventId));
                }
                else this.eventsList.put(eventId, eventsList.get(eventId));
            }
        }
    }

    public void addGroupOrUpdate(GIGroup group){
        if(userGroupsList.containsKey(group.id)){
            userGroupsList.remove(group.id);
            userGroupsList.put(group.id, group);
        }

        else userGroupsList.put(group.id, group);
    }

    public GIGroup getGroupByName(String name){
        for(String key : userGroupsList.keySet()){
            if(userGroupsList.get(key).name.equals(name))
                return userGroupsList.get(key);
        }
        return null;
    }

    public String getUserUid(){
        return prefsHelper.getUserUid();
    }

    public ArrayList<GIUser> getArrayMembersGroup(String groupId){
        ArrayList<GIUser> groupMembers = new ArrayList<>();
        for(String userId : userGroupsList.get(groupId).membersUids){
            groupMembers.add(allUsersList.get(userId));
        }
        return groupMembers;
    }

    public ArrayList<GIEvent> getArrayEventFromGroup(String groupId){
        ArrayList<GIEvent> eventsArray = new ArrayList<>();
        GIGroup group = userGroupsList.get(groupId);
        if(group != null && group.eventsIds != null) {
            for (String eventId : group.eventsIds){
                eventsArray.add(eventsList.get(eventId));
            }
        }
        return eventsArray;
    }

    public ArrayList<GIUser> getArrayUserParticipatingToTheEvent(String eventId){
        ArrayList<GIUser> membersPaticipating = new ArrayList<>();

        for(String uid : eventsList.get(eventId).participantsUids){
            membersPaticipating.add(allUsersList.get(uid));
        }

        return membersPaticipating;
    }

    public ArrayList<GIEvent> getArrayEvent(){
        ArrayList<GIEvent> eventsArray = new ArrayList<>();
        for(String key : eventsList.keySet()){
            eventsArray.add(eventsList.get(key));
        }
        Log.v("∆∆ ∆∆ GIDataCache", " -->  events array : " + eventsArray.toString());
        return eventsArray;
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
                if(o1.display_name != null && o2.display_name != null)
                    return o1.display_name.compareTo(o2.display_name);
                else return o1.email.compareTo(o2.email);
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

    public String getGroupIdByName(String name){
        for(String key : userGroupsList.keySet()){
            if(name == userGroupsList.get(key).id){
                return key;
            }
        }
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
