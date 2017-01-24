package com.groupin.florianmalapel.groupin.model.dbObjects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIEvent implements Serializable {

    public String id = null;
    public String id_group = null;
    public String uid_creator = null;
    public String name = null;
    public String description = null;
    public String theme = null;
    public String sub_theme = null;
    public String address = null;
    public String url_image = null;
    public String date_start = null;
    public String date_end = null;
    public float price = 0;
    public String bring_back = null;
    public ArrayList<String> participantsUids = null;
    public ArrayList<String> votesIds = null;

    public GIEvent() {
    }

    public GIEvent(String id, String id_group, String name, String description, ArrayList<String> participantsUids, String url_image) {
        this.id = id;
        this.id_group = id_group;
        this.name = name;
        this.description = description;
        this.participantsUids = participantsUids;
        this.url_image = url_image;
    }

    public GIEvent(String id_group, String name, String description, String theme, String address, String url_image, String date_start, String date_end, float price, String bring_back) {
        this.id_group = id_group;
        this.name = name;
        this.description = description;
        this.theme = theme;
        this.address = address;
        this.url_image = url_image;
        this.date_start = date_start;
        this.date_end = date_end;
        this.price = price;
        this.bring_back = bring_back;
    }

    public JSONObject getCreateEventJSON(String userId) throws JSONException {
        JSONObject event = new JSONObject();
        event.put("groupId", id_group);
        event.put("userId", userId);
        event.put("nom", name);
        event.put("photoURL", url_image);
        event.put("description", description);
        event.put("dateDebut", date_start);
        event.put("dateFin", date_end);
        event.put("obj", bring_back);
        event.put("theme", theme);
        event.put("prix", String.valueOf(price));
        Log.v("ŒŒŒŒ == ∆∆ ||" , event.toString());
        return event;
    }

    @Override
    public String toString() {
        return "GIEvent{" +
                "id='" + id + '\'' +
                ", id_group='" + id_group + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", theme='" + theme + '\'' +
                ", sub_theme='" + sub_theme + '\'' +
                ", address='" + address + '\'' +
                ", url_image='" + url_image + '\'' +
                ", date_start='" + date_start + '\'' +
                ", date_end='" + date_end + '\'' +
                ", price=" + price +
                ", bring_back='" + bring_back + '\'' +
                '}';
    }
}
