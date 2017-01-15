package com.groupin.florianmalapel.groupin.helpers;

import android.util.Log;

import com.groupin.florianmalapel.groupin.model.GIDataCache;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by florianmalapel on 12/01/2017.
 */

public class GIDataCacheHelper {

    // int.class.isAssignableFrom(int.class)

    public GIDataCacheHelper() {
    }

    public static void handleResponseRequest(int requestCode, JSONObject response, GIDataCache cache){
        ArrayList<GIJsonToObjectHelper.ItemReceived> listObjects = null;
        try {
            listObjects = GIJsonToObjectHelper.mapJSON(requestCode, response);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.wtf("GIDataCacheHelper", e.getLocalizedMessage());
        }

        if(listObjects == null)
            return;

        for(GIJsonToObjectHelper.ItemReceived item : listObjects){
            handleObjectToCache(item, cache);
        }
    }

    public static void handleObjectToCache(GIJsonToObjectHelper.ItemReceived itemReceived, GIDataCache cache){

        if(itemReceived == null) {
            Log.w("GIDataCacheHelper", "~~~~~~~~ null ~~~~~~~ñ");
            return;
        }

        if(itemReceived.request_code == GIRequestData.USER){
            cache.setUser((GIUser) itemReceived.object);
        }

        else if(itemReceived.request_code == GIRequestData.ALL_USERS){
            cache.setAllUsersList((HashMap<String, GIUser>) itemReceived.object);
        }

        // TODO NEED TO FINISH

    }
}
