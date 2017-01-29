package com.groupin.florianmalapel.groupin.model.dbObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by florianmalapel on 28/01/2017.
 */

public class GIPoll implements Serializable {

    public String pollId = null;
    public String creatorUid = null;
    public String groupId = null;
    public String question = null;
    public boolean hasAlreadyVote = false;
    public boolean isQcm = false;
    public ArrayList<GIChoice> listChoice = new ArrayList();

    public GIPoll() {
    }

    public GIPoll(String pollId, String creatorUid, String groupId, String question, ArrayList<GIChoice> listChoice) {
        this.pollId = pollId;
        this.creatorUid = creatorUid;
        this.question = question;
        this.listChoice = listChoice;
        this.groupId = groupId;
    }

    public GIPoll(String creatorUid, String question, ArrayList<GIChoice> listChoice) {
        this.creatorUid = creatorUid;
        this.question = question;
        this.listChoice = listChoice;
    }

    public GIChoice getCurrentTopChoice(){
        if(listChoice.isEmpty())
            return null;

        GIChoice currentTopChoice = listChoice.get(0);

        for(GIChoice choice : listChoice){
            if(choice.percentage > currentTopChoice.percentage){
                currentTopChoice = choice;
            }
        }

        return currentTopChoice;
    }

    public JSONObject getCreatePollJSON(String userId) throws JSONException {
        JSONObject poll = new JSONObject();
        poll.put("question", question);
        poll.put("qcm", isQcm);
        poll.put("createur", creatorUid);
        poll.put("group", groupId);
        JSONArray array = new JSONArray();
        for(GIChoice choice : listChoice){
            JSONObject choiceJSON = new JSONObject();
            choiceJSON.put("choix", choice.choice);
            array.put(choiceJSON);
        }
        poll.put("choix", array);
        return poll;
    }

    @Override
    public String toString() {
        return "GIPoll{" +
                "pollId='" + pollId + '\'' +
                ", creatorUid='" + creatorUid + '\'' +
                ", groupId='" + groupId + '\'' +
                ", question='" + question + '\'' +
                ", hasAlreadyVote=" + hasAlreadyVote +
                ", isQcm=" + isQcm +
                ", listChoice=" + listChoice +
                '}';
    }
}
