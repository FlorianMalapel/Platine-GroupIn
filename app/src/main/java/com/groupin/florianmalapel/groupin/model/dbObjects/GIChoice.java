package com.groupin.florianmalapel.groupin.model.dbObjects;

import java.io.Serializable;

/**
 * Created by florianmalapel on 28/01/2017.
 */

public class GIChoice implements Serializable {
    public String choice = null;
    public float percentage = 0;

    public GIChoice(String choice, float percentage) {
        this.choice = choice;
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "GIChoice{" +
                "choice='" + choice + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}