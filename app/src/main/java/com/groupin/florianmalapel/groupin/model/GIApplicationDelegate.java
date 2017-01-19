package com.groupin.florianmalapel.groupin.model;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.storage.FirebaseStorage;
import com.groupin.florianmalapel.groupin.helpers.GIDataCacheHelper;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by florianmalapel on 23/11/2016.
 */

public class GIApplicationDelegate extends Application implements GIVolleyRequest.RequestCallback {


    private static GIApplicationDelegate app_instance        = null;

    // Contains all the data needed in a session
    private static GIDataCache dataCache            = null;

    private FirebaseStorage firebaseStorage = null;

    private GIVolleyHandler volleyHandler = null;

    @Override
    public void onCreate() {
        app_instance = this;
        dataCache = new GIDataCache(this);
        firebaseStorage = FirebaseStorage.getInstance();
        volleyHandler = new GIVolleyHandler();
        Picasso.with(this).setLoggingEnabled(true);
    }

    public static synchronized GIApplicationDelegate getInstance(){
        return app_instance;
    }

    /**
     * Init the request queue if it is null or, just return an instance of the current queue
     * @return
     */
    public RequestQueue getRequestQueue(){
        if(dataCache.requestQueue == null) {
            dataCache.requestQueue = Volley.newRequestQueue(getApplicationContext());
            dataCache.requestQueue.start();
        }
        return dataCache.requestQueue;
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public GIDataCache getDataCache(){
        if(dataCache == null) {
            dataCache = new GIDataCache(this);
        }
        return dataCache;
    }

    /**
     * Add a request to the request queue without tag
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("default");
        getRequestQueue().add(req);
    }


    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        GIDataCacheHelper.handleResponseRequest(request_code, object, dataCache);
        if(request_code == GIRequestData.POST_USER_CODE){
            volleyHandler.getAllUsers(this);
            volleyHandler.getGroups(this, dataCache.user.uid);
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
