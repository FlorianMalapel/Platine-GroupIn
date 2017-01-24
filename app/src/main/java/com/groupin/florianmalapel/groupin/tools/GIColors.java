package com.groupin.florianmalapel.groupin.tools;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIColors {

    public static final String GREEN = "#99cc00";

    public static int getColorFromXml(Context context, int id){
        return ContextCompat.getColor(context, id);
    }

}
