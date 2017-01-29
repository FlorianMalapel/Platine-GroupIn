package com.groupin.florianmalapel.groupin.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIDesign {

    public static final String GREEN = "#99cc00";

    private static final String REGULAR_FONT_PATH = "Dosis-Medium.ttf";
    private static final String LIGHT_FONT_PATH = "Dosis-Light.ttf";
    private static final String BOLD_FONT_PATH = "Dosis-Bold.ttf";

    public static int getColorFromXml(Context context, int id){
        return ContextCompat.getColor(context, id);
    }

    public static Typeface getRegularFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), REGULAR_FONT_PATH);
    }

    public static Typeface getLightFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), LIGHT_FONT_PATH);
    }

    public static Typeface getBoldFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), BOLD_FONT_PATH);
    }

    public static void startRotatingProgressIndicator(GIProgressIndicator progressIndicator){
        progressIndicator.setVisibility(View.VISIBLE);
        progressIndicator.startRotating();
    }

    public static void stopRotatingProgressIndicator(GIProgressIndicator progressIndicator){
        progressIndicator.stopRotate();
        progressIndicator.setVisibility(View.GONE);
    }
}
