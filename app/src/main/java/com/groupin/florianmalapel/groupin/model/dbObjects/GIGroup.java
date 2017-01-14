package com.groupin.florianmalapel.groupin.model.dbObjects;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 06/01/2017.
 */

public class GIGroup {

    public String id = null;
    public String name = null;
    public String url_image = null;
    public String description = null;
    public ArrayList<String> membersUids = null;
    public ArrayList<String> eventsIds = null;
    public ArrayList<String> votesIds = null;
    public ArrayList<String> depensesIds = null;

    public GIGroup(String id, String name, String url_image, String description) {
        this.id = id;
        this.name = name;
        this.url_image = url_image;
        this.description = description;
    }
}
