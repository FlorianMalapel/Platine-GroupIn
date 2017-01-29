package com.groupin.florianmalapel.groupin.controllers.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.groupin.florianmalapel.groupin.views.GIHorizontalBubbleList;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by florianmalapel on 21/01/2017.
 */

public class GIActivityDisplayEvent extends AppCompatActivity implements View.OnClickListener {

    public static final String BUNDLE_ID = "bundleEvent";
    public static final String EVENT_ID = "event";

    private GIEvent eventToDisplay = null;

    private ImageButton imageButtonBack = null;
    private TextView textViewEventName = null;
    private TextView textViewEventDate = null;
    private TextView textViewEventBringBack = null;
    private TextView textViewEventLocation = null;
    private TextView textViewEventPrice = null;
    private TextView textViewEventDesc = null;
    private ImageView imageViewEventPhoto = null;
    private Button buttonParticipate = null;
    private Button buttonParticipateMaybe = null;
    private Button buttonNotParticipating = null;
    private GIHorizontalBubbleList horizontalBubbleListParticipants = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event);
        initialize();
    }

    private void initialize() {
        findViewById();
        initializeObjects();
        initializeViews();
        setListeners();
    }

    private void findViewById() {
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        textViewEventName = (TextView) findViewById(R.id.textViewEventName);
        textViewEventDate = (TextView) findViewById(R.id.textViewEventDate);
        textViewEventLocation = (TextView) findViewById(R.id.textViewEventLocation);
        textViewEventBringBack = (TextView) findViewById(R.id.textViewEventBringBack);
        textViewEventPrice = (TextView) findViewById(R.id.textViewEventPrice);
        textViewEventDesc = (TextView) findViewById(R.id.textViewEventDesc);
        imageViewEventPhoto = (ImageView) findViewById(R.id.imageViewEventPhoto);
        buttonParticipate = (Button) findViewById(R.id.buttonParticipate);
        buttonParticipateMaybe = (Button) findViewById(R.id.buttonParticipateMaybe);
        buttonNotParticipating = (Button) findViewById(R.id.buttonNotParticipating);
        horizontalBubbleListParticipants = (GIHorizontalBubbleList) findViewById(R.id.horizontalBubbleList_members);
    }

    private void initializeObjects() {
        tryToGetEventFromBundle();
    }

    private void tryToGetEventFromBundle(){
        try {
            eventToDisplay = (GIEvent) getIntent().getBundleExtra(BUNDLE_ID).get(EVENT_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeViews() {
        initTopBubbleFriendList();
        initializeViewsWithEvent();
        imageButtonBack.getDrawable().mutate().setColorFilter(GIDesign.getColorFromXml(this, R.color.GIYellow), PorterDuff.Mode.SRC_ATOP);
        buttonParticipate.getCompoundDrawables()[0].mutate().setColorFilter(GIDesign.getColorFromXml(this, R.color.GIBlue), PorterDuff.Mode.SRC_ATOP);
        buttonNotParticipating.getCompoundDrawables()[0].mutate().setColorFilter(GIDesign.getColorFromXml(this, R.color.GIBlue), PorterDuff.Mode.SRC_ATOP);
        buttonParticipateMaybe.getCompoundDrawables()[0].mutate().setColorFilter(GIDesign.getColorFromXml(this, R.color.GIBlue), PorterDuff.Mode.SRC_ATOP);
        textViewEventLocation.getCompoundDrawables()[0].mutate().setColorFilter(GIDesign.getColorFromXml(this, R.color.GIBlue), PorterDuff.Mode.SRC_ATOP);
        textViewEventDate.getCompoundDrawables()[0].mutate().setColorFilter(GIDesign.getColorFromXml(this, R.color.GIBlue), PorterDuff.Mode.SRC_ATOP);
        textViewEventPrice.getCompoundDrawables()[2].mutate().setColorFilter(GIDesign.getColorFromXml(this, R.color.GIBlue), PorterDuff.Mode.SRC_ATOP);
    }

    private void initializeViewsWithEvent(){
        Picasso.with(this).load(eventToDisplay.url_image).transform(new CircleTransform()).into(imageViewEventPhoto);
        textViewEventName.setText(eventToDisplay.name);
        textViewEventDate.setText(getTimeFormatted(eventToDisplay.date_start, eventToDisplay.date_end));
        textViewEventPrice.setText(String.valueOf(eventToDisplay.price));
        textViewEventLocation.setText(eventToDisplay.address);
        textViewEventDesc.setText(eventToDisplay.description);
        textViewEventBringBack.setText(eventToDisplay.bring_back);
    }

    private String getTimeFormatted(String start, String end){
        String finalDate = null;
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sfd.format(new Date(Long.valueOf(start)));
        finalDate = sfd.format(new Date(Long.valueOf(start))) + " au " +  sfd.format(new Date(Long.valueOf(end)));
        return finalDate;
    }

    private void initTopBubbleFriendList(){
        if(GIApplicationDelegate.getInstance().getDataCache().userFriendList == null)
            return;

        horizontalBubbleListParticipants.createContainerScrollView(null);

        for(GIUser user : GIApplicationDelegate.getInstance().getDataCache().getArrayUserParticipatingToTheEvent(eventToDisplay.id)){
            horizontalBubbleListParticipants.addBubbleInViewWithPicasso(user);
        }
    }

    private void setListeners() {
        imageButtonBack.setOnClickListener(this);
        buttonParticipate.setOnClickListener(this);
        buttonParticipateMaybe.setOnClickListener(this);
        buttonNotParticipating.setOnClickListener(this);
    }

    private void selectButton(Button button, boolean isSelected, int color){
        int colorDefault = GIDesign.getColorFromXml(this, R.color.GIBlue);
        button.getCompoundDrawables()[0].mutate().setColorFilter((isSelected) ? color : colorDefault, PorterDuff.Mode.SRC_ATOP);
        button.setTextColor((isSelected) ? color : colorDefault);
    }

    @Override
    public void onClick(View view) {
        if(view == imageButtonBack){
            finish();
        }

        else if(view == buttonParticipate){
            selectButton(buttonParticipate, true, GIDesign.getColorFromXml(this, R.color.holoGreen));
            selectButton(buttonNotParticipating, false, 0);
            selectButton(buttonParticipateMaybe, false, 0);
        }

        else if(view == buttonNotParticipating){
            selectButton(buttonParticipate, false, 0);
            selectButton(buttonNotParticipating, true, GIDesign.getColorFromXml(this, R.color.googleRed));
            selectButton(buttonParticipateMaybe, false, 0);
        }

        else if(view == buttonParticipateMaybe){
            selectButton(buttonParticipate, false, 0);
            selectButton(buttonNotParticipating, false, 0);
            selectButton(buttonParticipateMaybe, true, GIDesign.getColorFromXml(this, R.color.GIYellow));
        }
    }
}
