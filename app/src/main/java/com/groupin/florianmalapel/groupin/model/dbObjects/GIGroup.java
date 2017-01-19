package com.groupin.florianmalapel.groupin.model.dbObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by florianmalapel on 06/01/2017.
 */

public class GIGroup implements Serializable{

    public String id = null;
    public String name = null;
    public String url_image = null;
    public String description = null;
    public ArrayList<String> membersUids = null;
    public ArrayList<String> eventsIds = null;
    public ArrayList<String> votesIds = null;
    public ArrayList<String> depensesIds = null;

    public GIGroup() {
    }

    public GIGroup(String id, String name, String url_image, String description) {
        this.id = id;
        this.name = name;
        this.url_image = url_image;
        this.description = description;
    }

    public GIGroup(String name, String description, String url_image) {
        this.name = name;
        this.url_image = url_image;
        this.description = description;
    }

    public JSONObject getCreateGroupJSON(String userUid) throws JSONException {
        JSONObject group = new JSONObject();
        group.put("uid", userUid);
        group.put("nom", this.name);
        group.put("description", this.description);
        group.put("photoURL", this.url_image);
        return group;
    }

    @Override
    public String toString() {
        return "GIGroup{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url_image='" + url_image + '\'' +
                ", description='" + description + '\'' +
                ", membersUids=" + membersUids +
                ", eventsIds=" + eventsIds +
                ", votesIds=" + votesIds +
                ", depensesIds=" + depensesIds +
                '}';
    }
}
