package com.groupin.florianmalapel.groupin.model.dbObjects;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 14/11/2016.
 */

@IgnoreExtraProperties
public class GIUser {

    public String email = null;
    public String uid = null;
    public String display_name = null;
    public String providerId = null;
    public String photoURL = null;
    public String firstName = null;
    public String lastName = null;
    public double balance = 0;
    public ArrayList<String> friendsUids = null;
    public ArrayList<String> groupsUids = null;

    public GIUser() {
        this.groupsUids = new ArrayList<>();
        this.friendsUids = new ArrayList<>();
    }

    public void setDataFromFirebase(FirebaseUser user) {
        this.email = user.getEmail();
        this.uid = user.getUid();
        this.providerId = user.getProviders().get(0);
        if(this.photoURL == null && user.getPhotoUrl() != null)
            this.photoURL = user.getPhotoUrl().toString();
    }

    public GIUser(String email, String display_name) {
        this.email = email;
        this.display_name = display_name;
        this.groupsUids = new ArrayList<>();
        this.friendsUids = new ArrayList<>();
    }

    public GIUser(String email, String uid, String display_name, String firstName, String lastName) {
        this.email = email;
        this.uid = uid;
        this.display_name = display_name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupsUids = new ArrayList<>();
        this.friendsUids = new ArrayList<>();
    }

    public GIUser(String email, String uid, String display_name, String firstName, String lastName, String photoURL) {
        this.email = email;
        this.uid = uid;
        this.display_name = display_name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoURL = photoURL;
        this.groupsUids = new ArrayList<>();
        this.friendsUids = new ArrayList<>();
    }

    public void writeUserInDB(DatabaseReference databaseReference) {
        databaseReference.child("users").setValue(this);
    }

    public JSONObject getJSONUser() throws JSONException {
        JSONObject userJSON = new JSONObject();
        userJSON.put("displayName", this.display_name);
        userJSON.put("email", this.email);
        userJSON.put("photoURL", (this.photoURL == null) ? "" : this.photoURL);
        userJSON.put("providerId", this.providerId);
        userJSON.put("uid", this.uid);
        return userJSON;
    }

    @Override
    public String toString() {
        return "GIUser{" +
                "email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                ", display_name='" + display_name + '\'' +
                ", providerId='" + providerId + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friendsUids=" + friendsUids +
                ", groupsUids=" + groupsUids +
                '}';
    }
}
