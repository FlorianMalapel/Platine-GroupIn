package com.groupin.florianmalapel.groupin.helpers;

import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
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
                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.USER));
                listObjects.add(new ItemReceived(getNotifsFriendsFromJSON(object), GIRequestData.NOTIFS_FRIENDS));
                listObjects.add(new ItemReceived(getNotifsGroupsFromJSON(object), GIRequestData.NOTIFS_GROUP));
                break;

            case GIRequestData.GET_USER_CODE:
                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.USER));
                break;

            case GIRequestData.GET_USERS_CODE:
                listObjects.add(new ItemReceived(getUsersFromJSON(object), GIRequestData.ALL_USERS));
                break;
            case GIRequestData.NOTIFS_GROUP:
                listObjects.add(new ItemReceived(getUsersFromJSON(object), GIRequestData.USER));
                break;
            case GIRequestData.POST_GROUP_CODE:
                listObjects.add(new ItemReceived(getGroupFromJSON(object), GIRequestData.GROUP));
                break;
            case GIRequestData.GET_GROUPS_CODE:
                listObjects.add(new ItemReceived(getGroupsFromJSON(object), GIRequestData.ALL_GROUPS));
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
