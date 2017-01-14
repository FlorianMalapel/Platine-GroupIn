package com.groupin.florianmalapel.groupin.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by florianmalapel on 23/11/2016.
 */

public class GIVolleyRequest implements Response.Listener<JSONObject>, Response.ErrorListener {

    private int                 request_code    = -1;
    private int                 method_type     = -1;
    private String              url             = null;
    private JsonObjectRequest   request         = null;
    private RequestCallback     callback        = null;

    public interface RequestCallback {
        void onRequestStart();
        void onRequestFinishWithSuccess(int request_code,  JSONObject object);
        void onRequestFinishWithFailure();
    }

    public GIVolleyRequest(int request_code, int method_type, String url, RequestCallback callback) {
        this.request_code = request_code;
        this.method_type = method_type;
        this.url = url;
        this.callback = callback;
    }

    public void initGetJSONRequest(final Map<String, String> header_params){
        request = new JsonObjectRequest(method_type, url, null, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(header_params == null)
                    return super.getHeaders();
                else return header_params;
            }
        };
    }

    public void initPostJSONRequest(final Map<String, String> header_params, JSONObject objectToSend){
        request = new JsonObjectRequest(method_type, url, objectToSend, this, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(header_params == null)
                    return super.getHeaders();
                else return header_params;
            }
        };
    }

    public void initDeleteJSONRequest(final Map<String, String> header_params) {
        request = new JsonObjectRequest(method_type, url, null, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (header_params == null)
                    return super.getHeaders();
                else return header_params;
            }
        };
    }

    public void startRequest(){
        GIApplicationDelegate.getInstance().addToRequestQueue(request);
        if(callback != null)
            callback.onRequestStart();
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.wtf("~~~~~~~ ", response.toString());
        if(callback != null)
            callback.onRequestFinishWithSuccess(request_code, response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.wtf("~~~~~~~ ", error.toString());
        if(callback != null)
            callback.onRequestFinishWithFailure();
    }


}
