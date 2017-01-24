package com.groupin.florianmalapel.groupin.model.dbObjects;

import java.io.Serializable;

/**
 * Created by florianmalapel on 24/01/2017.
 */

public class GINotificationFriend implements Serializable {

    public GIUser user = null;

    public GINotificationFriend(GIUser user) {
        this.user = user;
    }
}
