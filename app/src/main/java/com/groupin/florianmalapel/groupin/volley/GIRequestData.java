package com.groupin.florianmalapel.groupin.volley;

/**
 * Created by florianmalapel on 23/11/2016.
 */

public class GIRequestData {

    public static String API_URL = "https://platine-groupin.herokuapp.com";
    public static String USER_ENDPOINT = "/users";
    public static String FRIENDS_ENDPOINT = "/friends";
    public static String GROUP_ENDPOINT = "/groups";


    // Volley requests code
    public final static int POST_USER_CODE = 111;
    public final static int GET_USER_CODE = 112;
    public final static int GET_USERS_CODE = 113;
    public final static int DELETE_GROUP_CODE = 114;
    public final static int POST_GROUP_CODE = 115;
    public final static int GET_GROUP_CODE = 116;
    public final static int POST_FRIENDSHIP_CODE = 117;

    // Volley receive item type
    public final static int USER = 222;
    public final static int GROUP = 223;
    public final static int ALL_USERS = 224;
    public final static int NOTIFS_GROUP = 225;
    public final static int NOTIFS_FRIENDS = 226;


}
