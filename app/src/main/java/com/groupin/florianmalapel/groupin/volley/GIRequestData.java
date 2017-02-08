package com.groupin.florianmalapel.groupin.volley;

/**
 * Created by florianmalapel on 23/11/2016.
 */

public class GIRequestData {

//    public static String API_URL = "http://192.168.43.150:8080";
    public static String API_URL = "https://platine-groupin.herokuapp.com";
    public static String USER_ENDPOINT = "/users";
    public static String FRIENDS_ENDPOINT = "/friends";
    public static String AMIS_ENDPOINT = "/amis";
    public static String GROUP_ENDPOINT = "/groups";
    public static String ANDROID_ENDPOINT = "/android";
    public static String EVENT_ENDPOINT = "/events";
    public static String NOTIF_ENDPOINT = "/notifications";
    public static String CHAT_ENDPOINT = "/chats";
    public static String POLLS_ENDPOINT = "/votes";
    public static String EUROS_ENDPOINT = "/euros";
    public static String BILLS_ENDPOINT = "/bills";
    public static String PARTICIP_ENDPOINT = "/participants";


    // Volley requests code
    public final static int POST_USER_CODE = 111;
    public final static int GET_USER_MY_CODE = 112;
    public final static int GET_USER_CODE = 110;
    public final static int GET_USERS_CODE = 113;
    public final static int DELETE_GROUP_CODE = 114;
    public final static int POST_GROUP_CODE = 115;
    public final static int GET_GROUPS_CODE = 116;
    public final static int POST_FRIENDSHIP_CODE = 117;
    public final static int POST_EVENT_CODE = 118;
    public final static int GET_EVENTS_USER_CODE = 119;
    public final static int POST_NOTIF_ADD_GROUP_CODE = 120;
    public final static int GET_NOTIFICATIONS_CODE = 121;
    public final static int DELETE_NOTIFICATION_FRIEND_CODE = 122;
    public final static int DELETE_NOTIFICATION_GROUP_CODE = 123;
    public final static int GET_CHAT_GROUP_CODE = 124;
    public final static int POST_CHAT_MESSAG_GROUP_CODE = 125;
    public final static int GET_POLLS_GROUP_CODE = 126;
    public final static int POST_POLLS_GROUP_CODE = 127;
    public final static int POST_POLLS_ANSWER_CODE = 128;
    public final static int POST_BILL_CODE = 129;
    public final static int GET_BILLS_GROUP_CODE = 130;
    public final static int DELETE_BILLS_GROUP_CODE = 131;
    public final static int POST_PARTICIP_EVENT_CODE = 132;

    // Volley receive item type
    public final static int USER = 221;
    public final static int MY_USER = 222;
    public final static int GROUP = 223;
    public final static int ALL_USERS = 224;
    public final static int NOTIFS_GROUP = 225;
    public final static int NOTIFS_FRIENDS = 226;
    public final static int ALL_GROUPS = 227;
    public final static int EVENTS = 228;
    public final static int EVENT = 231;
    public final static int CHATS = 229;
    public final static int POLLS = 230;
    public final static int BILLS = 232;


}
