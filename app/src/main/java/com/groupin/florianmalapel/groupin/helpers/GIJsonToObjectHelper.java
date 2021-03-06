package com.groupin.florianmalapel.groupin.helpers;

import com.groupin.florianmalapel.groupin.model.dbObjects.GIBill;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIChatMessage;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIChoice;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationFriend;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIPoll;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
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
                listObjects.add(new ItemReceived(getArrayNotificationFriendFromJSON(object), GIRequestData.NOTIFS_FRIENDS));
                listObjects.add(new ItemReceived(getArrayNotificationGroupFromJSON(object), GIRequestData.NOTIFS_GROUP));
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
//                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.MY_USER));
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
//            case GIRequestData.GET_BILLS_GROUP_CODE:
//                listObjects.add(new ItemReceived(getBillsArrayFromJSON(object), GIRequestData.BILLS));
//            case GIRequestData.CHATS:
//                listObjects.add(new ItemReceived(getUserFromJSON(object), GIRequestData.MY_USER));
//                listObjects.add(new ItemReceived(getArrayMessagesFromJSON(object), GIRequestData.CHATS));
//                break;
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
        if(object.has("balance"))
            user.balance = object.getDouble("balance");
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
            ArrayList<String> objectsBringBack = new ArrayList<>();
            JSONArray arrayObj = object.optJSONArray("obj");
            if(arrayObj != null){
                for(int i=0; i<arrayObj.length(); i++){
                    objectsBringBack.add(arrayObj.getString(i));
                }
                event.bring_back_list = objectsBringBack;
            }
            else {
                JSONObject obj = object.getJSONObject("obj");
                if (obj.names() != null) {
                    for (int i = 0; i < obj.names().length(); i++) {
                        objectsBringBack.add((String) obj.names().get(i));
                    }
                    event.bring_back_list = objectsBringBack;
                }
            }

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

        return arrayNotifsFriend;
    }

    public static GIChatMessage getMessageFromJSON(JSONObject object) throws JSONException {

        GIChatMessage message = new GIChatMessage();

        if(object.has("auteur")){
            GIUser author = getUserFromJSON(object.getJSONObject("auteur"));
            message.authorUid = author.uid;
        }

        if(object.has("date")){
            message.date = object.getLong("date");
        }

        if(object.has("id")){
            message.messageId = object.getString("id");
        }

        if(object.has("message")){
            message.messageContent = object.getString("message");
        }

        return message;
    }


    public static ArrayList<GIChatMessage> getArrayMessagesFromJSON(JSONObject object) throws JSONException {
        ArrayList<GIChatMessage> arrayMessages = new ArrayList<>();
        JSONArray jsonArrayMessages = null;
        if(object.has("messages")){
            jsonArrayMessages = object.getJSONArray("messages");
        }
        else return arrayMessages;

        for(int index=0; index<jsonArrayMessages.length(); index++){
            arrayMessages.add(getMessageFromJSON(jsonArrayMessages.getJSONObject(index)));
        }

        Collections.sort(arrayMessages);

        return arrayMessages;
    }

    public static ArrayList<GIPoll> getArrayPollsSortedFromJSON(JSONObject object, String groupId) throws JSONException {
        ArrayList<GIPoll> arrayPolls = new ArrayList<>();
        JSONArray jsonArrayPolls = null;

        if(object.has("votes")){
            jsonArrayPolls = object.getJSONArray("votes");
        }
        else return arrayPolls;

        for(int index=0; index<jsonArrayPolls.length(); index++){
            arrayPolls.add(getPollFromJSON(jsonArrayPolls.getJSONObject(index), groupId));
        }

        ArrayList<GIPoll> arrayPollsSorted = new ArrayList<>();
        for(GIPoll poll : arrayPolls){
            if(!poll.hasAlreadyVote)
                arrayPollsSorted.add(poll);
        }

        for(GIPoll poll : arrayPolls){
            if(poll.hasAlreadyVote)
                arrayPollsSorted.add(poll);
        }

        return arrayPollsSorted;
    }

    public static ArrayList<GIChoice> getChoicesFromJSON(JSONObject pollJSON) throws JSONException {
        ArrayList<GIChoice> arrayChoices = new ArrayList<>();
        JSONArray arrayChoicesJSON = pollJSON.getJSONArray("choix");
        for(int i=0; i<arrayChoicesJSON.length(); i++){
            JSONObject object = arrayChoicesJSON.getJSONObject(i);
            arrayChoices.add(new GIChoice(object.getString("choix"), (object.has("pourcentage")) ? object.getInt("pourcentage") : 0));
        }
        return arrayChoices;
    }

    public static GIPoll getPollFromJSON(JSONObject pollJSON, String groupId) throws JSONException {
        GIPoll poll = new GIPoll();

        if(pollJSON.has("QCM")){
            poll.isQcm = pollJSON.getBoolean("QCM");
        }

        if(pollJSON.has("id")){
            poll.pollId = pollJSON.getString("id");
        }

        if(pollJSON.has("createur")){
            poll.creatorUid = pollJSON.getString("createur");
        }

        if(pollJSON.has("date")){
            poll.date = pollJSON.getLong("date");
        }


        if(pollJSON.has("question")){
            poll.question = pollJSON.getString("question");
        }

        if(pollJSON.has("hasalreadyvote")){
            poll.hasAlreadyVote = pollJSON.getBoolean("hasalreadyvote");
        }

        if(pollJSON.has("choix")){
            poll.listChoice = getChoicesFromJSON(pollJSON);
        }

        poll.groupId = groupId;

        return poll;
    }

    public static ArrayList<GIBill> getBillsArrayFromJSON(JSONObject object) throws JSONException {
        ArrayList<GIBill> billsArray = new ArrayList<>();
        JSONArray jsonArrayBills = null;
        if(object.has("depenses")){
            jsonArrayBills = object.getJSONArray("depenses");
        }

        for(int i=0; i<jsonArrayBills.length(); i++){
            billsArray.add(getBillFromJSON(jsonArrayBills.getJSONObject(i)));
        }

        return billsArray;
    }

    public static GIBill getBillFromJSON(JSONObject billJSON) throws JSONException {
        GIBill bill = new GIBill();

        if(billJSON.has("what"))
            bill.objectBought = billJSON.getString("what");

        if(billJSON.has("payer_id"))
            bill.uidBuyer = getUserFromJSON(billJSON.getJSONObject("payer_id")).uid;

        if(billJSON.has("owers")){
            JSONArray arrayOwners = billJSON.getJSONArray("owers");
            for(int i=0; i<arrayOwners.length(); i++){
                bill.paidForList.add(getUserFromJSON(arrayOwners.getJSONObject(i)).uid);
            }
        }

        if(billJSON.has("amount")){
            bill.price = billJSON.getDouble("amount");
        }

        if(billJSON.has("id")){
            bill.billId = billJSON.getString("id");
        }

        return bill;
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
