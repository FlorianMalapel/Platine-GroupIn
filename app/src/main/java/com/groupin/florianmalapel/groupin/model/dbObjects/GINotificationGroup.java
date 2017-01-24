package com.groupin.florianmalapel.groupin.model.dbObjects;

import java.io.Serializable;

/**
 * Created by florianmalapel on 24/01/2017.
 */

public class GINotificationGroup implements Serializable {

    public GIGroup group = null;

    public GINotificationGroup(GIGroup group) {
        this.group = group;
    }
}
