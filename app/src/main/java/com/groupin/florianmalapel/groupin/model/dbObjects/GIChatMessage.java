package com.groupin.florianmalapel.groupin.model.dbObjects;

import java.io.Serializable;

/**
 * Created by florianmalapel on 28/01/2017.
 */

public class GIChatMessage implements Serializable, Comparable<GIChatMessage> {

    public String messageId = null;
    public String authorUid = null;
    public String messageContent = null;
    public long date = 0;

    public GIChatMessage() {
    }

    public GIChatMessage(String messageId, String authorUid, String messageContent, long date) {
        this.messageId = messageId;
        this.authorUid = authorUid;
        this.messageContent = messageContent;
        this.date = date;
    }

    @Override
    public int compareTo(GIChatMessage o) {
        return (date < o.date) ? -1 : 1;
    }

    @Override
    public String toString() {
        return "GIChatMessage{" +
                "messageId='" + messageId + '\'' +
                ", authorUid='" + authorUid + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", date=" + date +
                '}';
    }
}
