package com.groupin.florianmalapel.groupin.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.groupin.florianmalapel.groupin.R;

/**
 * Created by florianmalapel on 17/01/2017.
 */

public class GIProgressIndicator extends LinearLayout implements Animation.AnimationListener {

    private ImageView imageViewLogo = null;
    private Context context = null;
    private int currentNbRotateAnim = 0;


    public GIProgressIndicator(Context context) {
        super(context);
        this.context = context;
        initializeView();
    }

    public GIProgressIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeView();
    }

    public GIProgressIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initializeView();
    }

    public void initializeView(){

        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(params);

        imageViewLogo = new ImageView(context);
        imageViewLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gilogo_loader));
        this.addView(imageViewLogo);
    }

    public void startRotating(){
        Animation rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.loader_rotation);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setAnimationListener(this);
        imageViewLogo.setAnimation(rotateAnimation);
        imageViewLogo.startAnimation(rotateAnimation);
    }

    public void stopRotate(){
        imageViewLogo.setAnimation(null);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        this.setVisibility(VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
