package com.groupin.florianmalapel.groupin.helpers;

import android.util.Log;

import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationFriend;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by florianmalapel on 12/01/2017.
 */

public class GIJsonToObjectHelper {

    public static ArrayList<ItemReceived> mapJSON(int requestCode, JSONObject object) throws JSONException {
        ArrayList<ItemReceived> listObjects = new ArrayList<>();

        switch (requestCode) {
            case GIRequestData.POST_USER_CODE:
                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.MY_USER));
                listObjects.add(new ItemReceived(getNotifsFriendsFromJSON(object), GIRequestData.NOTIFS_FRIENDS));
                listObjects.add(new ItemReceived(getNotifsGroupsFromJSON(object), GIRequestData.NOTIFS_GROUP));
                break;

            case GIRequestData.GET_USER_CODE:
                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.USER));
                break;

            case GIRequestData.GET_USER_MY_CODE:
                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.MY_USER));
                break;

            case GIRequestData.GET_USERS_CODE:
                listObjects.add(new ItemReceived(getUsersFromJSON(object), GIRequestData.ALL_USERS));
                break;
            case GIRequestData.NOTIFS_GROUP:
                // TODO strange .. getUSers and MY_USER .. to see
                listObjects.add(new ItemReceived(getUsersFromJSON(object), GIRequestData.MY_USER));
                break;
            case GIRequestData.POST_GROUP_CODE:
                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.MY_USER));
                listObjects.add(new ItemReceived(getGroupsFromJSON(object), GIRequestData.ALL_GROUPS));
                break;
            case GIRequestData.GET_GROUPS_CODE:
                listObjects.add(new ItemReceived(getGroupsFromJSON(object), GIRequestData.ALL_GROUPS));
                break;
            case GIRequestData.POST_EVENT_CODE:
                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.MY_USER));
                listObjects.add(new ItemReceived(getEventsFromJSONArray(object), GIRequestData.EVENTS));
                break;
            case GIRequestData.GET_EVENTS_USER_CODE:
                listObjects.add(new ItemReceived(getEventsFromJSON(object), GIRequestData.EVENTS));
                break;
            case GIRequestData.GET_NOTIFICATIONS_CODE:
                listObjects.add(new ItemReceived(getArrayNotificationFriendFromJSON(object), GIRequestData.NOTIFS_FRIENDS));
                listObjects.add(new ItemReceived(getArrayNotificationGroupFromJSON(object), GIRequestData.NOTIFS_GROUP));
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
            else user.friendsUids = new ArrayList<>();
        }
        if(object.has("groups")){
            JSONObject groups = object.getJSONObject("groups");
            ArrayList<String> groupsIds = new ArrayList<>();
            for(int indexId = 0; indexId < groups.names().length(); indexId++){
                if(groups.names().get(indexId) != null && !((String)groups.names().get(indexId)).isEmpty()){
                    groupsIds.add((String) groups.names().get(indexId));
                }
            }

            if(!groupsIds.isEmpty())
                user.groupsUids = groupsIds;
            else user.groupsUids = new ArrayList<>();
        }


        if(user.email != null && !user.email.isEmpty())
            return user;

        return null;
    }

    public static HashMap<String, GIGroup> getGroupsFromJSON(JSONObject object) throws JSONException {
        HashMap<String, GIGroup> groupsList = new HashMap<>();
        JSONArray array = null;
        if(object.has("groups")){
            array = object.getJSONArray("groups");
        }
        else return null;

        for(int i=0; i<array.length(); i++){
            GIGroup group = getGroupFromJSON(array.getJSONObject(i));
            if(group != null)
                groupsList.put(group.id, group);
        }

        return groupsList;
    }

    public static GIGroup getGroupFromJSON(JSONObject object) throws JSONException {
        GIGroup group = new GIGroup();
        if(object.has("id")){
            group.id = object.getString("id");
        }

        if(object.has("nom")){
            group.name = object.getString("nom");
        }

        if(object.has("description")){
            group.description = object.getString("description");
        }

        if(object.has("photoURL")){
            group.url_image = object.getString("photoURL");
        }

        if(object.has("membres")){
            JSONObject members = object.getJSONObject("membres");
            ArrayList<String> membersUids = new ArrayList<>();
            for(int indexUid = 0; indexUid < members.names().length(); indexUid++){
                if(members.names().get(indexUid) != null && !((String)members.names().get(indexUid)).isEmpty()){
                    membersUids.add((String) members.names().get(indexUid));
                }
            }
            group.membersUids = membersUids;
        }

        if(object.has("events")){
            JSONObject events = object.getJSONObject("events");
            ArrayList<String> eventsIds = new ArrayList<>();
            for(int indexId = 0; indexId < events.names().length(); indexId++){
                if(events.names().get(indexId) != null && !((String)events.names().get(indexId)).isEmpty()){
                    eventsIds.add((String) events.names().get(indexId));
                }
            }
            group.eventsIds = eventsIds;
        }

        return group;
    }

    public static HashMap<String,GIUser> getUsersFromJSON(JSONObject object) throws JSONException {
        if(object.has("users")){
            object = object.getJSONObject("users");
        }
        else return null;

        HashMap<String, GIUser> usersList = new HashMap<>();

        for(int i=0; i<object.names().length(); i++){
            String uid = object.names().getString(i);
            GIUser user = getUserFromJSON(object.getJSONObject(uid));
            if(user != null)
                usersList.put(uid, user);
        }

        return usersList;
    }

    public static HashMap<String, GIEvent> getEventsFromJSON(JSONObject object) throws JSONException {
        HashMap<String, GIEvent> eventsList = new HashMap<>();

        if(object.names() == null)
            return eventsList;

        for(int index = 0; index < object.names().length(); index ++){
            GIEvent event = getEventFromJSON(object.getJSONObject((String)object.names().get(index)));
            if(event != null)
                eventsList.put(event.id, event);
        }

        return eventsList;
    }

    public static HashMap<String, GIEvent> getEventsFromJSONArray(JSONObject object) throws JSONException {
        HashMap<String, GIEvent> eventsList = new HashMap<>();
        JSONArray array = null;
        if(object.has("events")){
            array = object.getJSONArray("events");
        }
        else return null;

        for(int i=0; i<array.length(); i++){
            GIEvent event = getEventFromJSON(array.getJSONObject(i));
            if(event != null)
                eventsList.put(event.id, event);
        }

        return eventsList;
    }

    public static GIEvent getEventFromJSON(JSONObject object) throws JSONException {
        GIEvent event = new GIEvent();
        if(object.has("id")){
            event.id = object.getString("id");
        }

        if(object.has("nom")){
            event.name = object.getString("nom");
        }

        if(object.has("description")){
            event.description = object.getString("description");
        }

        if(object.has("photoURL")){
            event.url_image = object.getString("photoURL");
        }

        if(object.has("dateDebut")){
            event.date_start = object.getString("dateDebut");
        }

        if(object.has("dateFin")){
            event.date_end = object.getString("dateFin");
        }

        if(object.has("obj")){
            event.bring_back = object.getString("obj");
        }

        if(object.has("theme")){
            event.theme = object.getString("theme");
        }

        if(object.has("price")){
            event.price = Float.valueOf(object.getString("price"));
        }

        if(object.has("createur")){
            event.uid_creator = object.getString("createur");
        }

        if(object.has("participants")){
            JSONObject participants = object.getJSONObject("participants");
            ArrayList<String> participantsUids = new ArrayList<>();
            for(int indexUid = 0; indexUid < participants.names().length(); indexUid++){
                participantsUids.add((String) participants.names().get(indexUid));
            }

            if(!participantsUids.isEmpty())
                event.participantsUids = participantsUids;
            else event.participantsUids = new ArrayList<>();
        }

        return event;
    }

    public static ArrayList<GINotificationGroup> getArrayNotificationGroupFromJSON(JSONObject object) throws JSONException {
        ArrayList<GINotificationGroup> arrayNotifsGroup = new ArrayList<>();
        JSONArray arrayNotifsGroupJSON = null;
        if(object.has("notifsGroupes")){
            arrayNotifsGroupJSON = object.getJSONArray("notifsGroupes");
        }
        else return arrayNotifsGroup;

        for(int index=0; index<arrayNotifsGroupJSON.length(); index++){
            arrayNotifsGroup.add(new GINotificationGroup(getGroupFromJSON(arrayNotifsGroupJSON.getJSONObject(index))));
        }

        Log.v("~~~ = GIJsonToObj", " --> -->  " + arrayNotifsGroup.toString());

        return arrayNotifsGroup;
    }

    public static ArrayList<GINotificationFriend> getArrayNotificationFriendFromJSON(JSONObject object) throws JSONException {
        ArrayList<GINotificationFriend> arrayNotifsFriend = new ArrayList<>();
        JSONArray arrayNotifsFriendJSON = null;
        if(object.has("notifsAmis")){
            arrayNotifsFriendJSON = object.getJSONArray("notifsAmis");
        }
        else return arrayNotifsFriend;

        for(int index=0; index<arrayNotifsFriendJSON.length(); index++){
            arrayNotifsFriend.add(new GINotificationFriend(getUserFromJSON(arrayNotifsFriendJSON.getJSONObject(index))));
        }

        Log.v("~~~ = GIJsonToObj", " --> -->  " + arrayNotifsFriend.toString());

        return arrayNotifsFriend;
    }

    public static Object getNotifsFriendsFromJSON(JSONObject object){
        // TODO NEED TO FINISH
        return null;
    }

    public static Object getNotifsGroupsFromJSON(JSONObject object){
        return null;
    }

    public static class ItemReceived {
        public Object object = null;
        public int request_code = 0;

        public ItemReceived(Object object, int request_code) {
            this.object = object;
            this.request_code = request_code;
        }
    }


}
