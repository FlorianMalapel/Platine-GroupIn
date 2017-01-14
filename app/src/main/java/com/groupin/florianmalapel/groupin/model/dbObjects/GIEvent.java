package com.groupin.florianmalapel.groupin.model.dbObjects;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIEvent {

    public String id = null;
    public String id_group = null;
    public String name = null;
    public String description = null;
    public String theme = null;
    public String sub_theme = null;
    public String address = null;
    public String url_image = null;
    public String date_start = null;
    public String date_end = null;
    public int price = 0;
    public ArrayList<String> objectsToBring = null;
    public ArrayList<String> participantsUids = null;
    public ArrayList<String> votesIds = null;

    public GIEvent(String id, String id_group, String name, String description, ArrayList<String> participantsUids, String url_image) {
        this.id = id;
        this.id_group = id_group;
        this.name = name;
        this.description = description;
        this.participantsUids = participantsUids;
        this.url_image = url_image;
    }
}
