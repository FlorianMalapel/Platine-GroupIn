package com.groupin.florianmalapel.groupin.tools;

import android.util.Log;

import com.groupin.florianmalapel.groupin.BuildConfig;

/**
 * Created by florianmalapel on 29/10/2016.
 */

public class DLog {

    public DLog() {
    }


    public static void logd(String tag, String msg){

        if(BuildConfig.DEBUG)
            Log.d(tag, msg);

    }

    public static void logw(String tag, String msg){

        if(BuildConfig.DEBUG)
            Log.w(tag, msg);

    }

    public static void logwtf(String tag, String msg){

        if(BuildConfig.DEBUG)
            Log.wtf(tag, msg);

    }

    public static void loge(String tag, String msg){

        if(BuildConfig.DEBUG)
            Log.e(tag, msg);

    }
}
