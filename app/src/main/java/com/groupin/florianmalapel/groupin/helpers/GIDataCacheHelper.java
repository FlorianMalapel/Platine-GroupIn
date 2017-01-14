package com.groupin.florianmalapel.groupin.helpers;

import android.util.Log;

import com.groupin.florianmalapel.groupin.model.GIDataCache;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 12/01/2017.
 */

public class GIDataCacheHelper {

    // int.class.isAssignableFrom(int.class)

    public GIDataCacheHelper() {
    }

    public static void handleResponseRequest(int requestCode, JSONObject response, GIDataCache cache){
        ArrayList<Object> listObjects = null;
        try {
            listObjects = GIJsonToObjectHelper.mapJSON(requestCode, response);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.wtf("GIDataCacheHelper", e.getLocalizedMessage());
        }

        if(listObjects == null)
            return;

        for(Object object : listObjects){
            handleObjectToCache(object, cache);
        }
    }

    public static void handleObjectToCache(Object object, GIDataCache cache){

        if(object == null) {
            Log.w("GIDataCacheHelper", "~~~~~~~~ null ~~~~~~~ñ");
            return;
        }

        if(object.getClass().isAssignableFrom(GIUser.class)){
            cache.setUser((GIUser) object);
        }

        // TODO NEED TO FINISH

    }
}
