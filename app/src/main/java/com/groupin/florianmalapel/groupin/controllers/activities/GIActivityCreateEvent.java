package com.groupin.florianmalapel.groupin.controllers.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.helpers.GICommunicationsHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by florianmalapel on 18/01/2017.
 */

public class GIActivityCreateEvent extends AppCompatActivity
        implements  View.OnClickListener, TextWatcher,
                    GICommunicationsHelper.FirebaseUploadImageCallback,
                    GIVolleyRequest.RequestCallback{

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final String BUNDLE_ID = "GROUP_BUNDLE";
    public static final String GROUP_ID = "GROUP_OBJECT";

    private ImageView imageButtonBack = null;
    private ImageView imageViewAddPhotoEvent = null;
    private ImageView imageViewLocationIcon = null;
    private ImageView imageViewEuroIcon = null;
    private TextView textViewValidateCreate = null;
    private TextView textViewEventStartDate = null;
    private TextView textViewEventEndDate = null;
    private RelativeLayout relativeLayoutPhotoEvent = null;
    private EditText editTextEventName = null;
    private EditText editTextEventDesc = null;
    private EditText editTextEventBringBack = null;
    private EditText editTextEventTheme = null;
    private EditText editTextEventSecondTheme = null;
    private EditText editTextEventPlace = null;
    private EditText editTextEventPrice = null;
    private GIProgressIndicator progressIndicator = null;
    private MaterialSpinner spinnerGroups = null;
    private Bitmap bitmapImageEvent = null;
    private GIEvent eventToCreate = null;
    private GIGroup groupParent = null;
    private String urlGroupPhoto = null;
    private long eventDateStart = 0;
    private long eventDateEnd = 0;
    private GIVolleyHandler volleyHandler = null;
    private SwitchDateTimeDialogFragment startDatePicker = null;
    private SwitchDateTimeDialogFragment endDatePicker = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        initialize();
    }

    private void initialize() {
        findViewById();
        initializeObjects();
        initializeViews();
        setListeners();
    }

    private void findViewById() {
        imageButtonBack = (ImageView) findViewById(R.id.imageButtonBack);
        imageViewAddPhotoEvent = (ImageView) findViewById(R.id.imageViewAddPhotoEvent);
        imageViewLocationIcon = (ImageView) findViewById(R.id.imageViewLocationIcon);
        imageViewEuroIcon = (ImageView) findViewById(R.id.imageViewEuroIcon);
        textViewValidateCreate = (TextView) findViewById(R.id.textViewValidateCreate);
        textViewEventStartDate = (TextView) findViewById(R.id.textViewEventStartDate);
        textViewEventEndDate = (TextView) findViewById(R.id.textViewEventEndDate);
        relativeLayoutPhotoEvent = (RelativeLayout) findViewById(R.id.relativeLayoutPhotoEvent);
        editTextEventName = (EditText) findViewById(R.id.editTextEventName);
        editTextEventDesc = (EditText) findViewById(R.id.editTextEventDesc);
        editTextEventBringBack = (EditText) findViewById(R.id.editTextEventBringBack);
        editTextEventTheme = (EditText) findViewById(R.id.editTextEventTheme);
        editTextEventSecondTheme = (EditText) findViewById(R.id.editTextEventSecondTheme);
        editTextEventPlace = (EditText) findViewById(R.id.editTextEventPlace);
        editTextEventPrice = (EditText) findViewById(R.id.editTextEventPrice);
        progressIndicator = (GIProgressIndicator) findViewById(R.id.progressIndicator);
        spinnerGroups = (MaterialSpinner) findViewById(R.id.spinnerGroups);
    }

    private void initializeObjects() {
        volleyHandler = new GIVolleyHandler();
        try {
            groupParent = (GIGroup) getIntent().getBundleExtra(BUNDLE_ID).getSerializable(GROUP_ID);
        } catch (Exception e) {
            groupParent = null;
        }
        startDatePicker = SwitchDateTimeDialogFragment.newInstance(
                "Date début",
                "OK",
                "Cancel"
        );
        endDatePicker = SwitchDateTimeDialogFragment.newInstance(
                "Date fin",
                "OK",
                "Cancel"
        );
    }

    private void initializeMaterialSpinnerWithGroups(){
        if(groupParent != null) {
            spinnerGroups.setVisibility(View.GONE);
            return;
        }
        List<String> groupsNames = new ArrayList<>();
        for(String key : GIApplicationDelegate.getInstance().getDataCache().userGroupsList.keySet()){
            groupsNames.add(GIApplicationDelegate.getInstance().getDataCache().userGroupsList.get(key).name);
        }
        if(groupsNames.isEmpty())
            return;
        spinnerGroups.setItems(groupsNames);
    }

    private void initializeViews() {
        imageButtonBack.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initializeMaterialSpinnerWithGroups();
    }

    private void setListeners() {
        imageButtonBack.setOnClickListener(this);
        relativeLayoutPhotoEvent.setOnClickListener(this);
        textViewValidateCreate.setOnClickListener(this);
        textViewEventStartDate.setOnClickListener(this);
        textViewEventEndDate.setOnClickListener(this);
        imageViewAddPhotoEvent.setOnClickListener(this);

        editTextEventName.addTextChangedListener(this);
        editTextEventDesc.addTextChangedListener(this);
        editTextEventTheme.addTextChangedListener(this);
        editTextEventBringBack.addTextChangedListener(this);
        editTextEventSecondTheme.addTextChangedListener(this);
        editTextEventPlace.addTextChangedListener(this);
        editTextEventPrice.addTextChangedListener(this);
        startDatePicker.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                eventDateStart = date.getTime();
                setDateInTextView(date, textViewEventStartDate);
            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });
        endDatePicker.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                eventDateEnd = date.getTime();
                setDateInTextView(date, textViewEventEndDate);
            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });
        spinnerGroups.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                groupParent = GIApplicationDelegate.getInstance().getDataCache().getGroupByName(item);
            }
        });
    }

    private void setDateInTextView(Date date, TextView textView){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        textView.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    }

    private void confirmEventCreation(){
        progressIndicator.startRotating();
        progressIndicator.setVisibility(View.VISIBLE);
        GICommunicationsHelper.firebaseUploadBitmap(GIApplicationDelegate.getInstance().getDataCache().getUserUid() + System.currentTimeMillis() + ".jpg", bitmapImageEvent, this);
    }

    private void onClickOnButtonBack(){
        finish();
    }

    private void onClickRelativeLayoutPhotoEvent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void sendEventToAPI(){
        if(groupParent == null)
            groupParent = GIApplicationDelegate.getInstance().getDataCache().getGroupByName((String) spinnerGroups.getItems().get(0));

        // TODO check if nothing is null
        eventToCreate = new GIEvent(groupParent.id, editTextEventName.getText().toString(),
            editTextEventDesc.getText().toString(), editTextEventTheme.getText().toString(),
            editTextEventPlace.getText().toString(), urlGroupPhoto,
            String.valueOf(eventDateStart), String.valueOf(eventDateEnd),
            Float.valueOf(editTextEventPrice.getText().toString()), editTextEventBringBack.getText().toString());
        try {
            volleyHandler.postEvent(this, eventToCreate.getCreateEventJSON(GIApplicationDelegate.getInstance().getDataCache().getUserUid()));
            Log.v("∆∆ ∆∆ || ∆∆", "start post /Event " + eventToCreate.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onClickOnTextViewStartDate(){
        startDatePicker.show(getSupportFragmentManager(), "dialog_time");
    }

    private void onClickOnTextViewEndDate(){
        endDatePicker.show(getSupportFragmentManager(), "dialog_time");
    }

    @Override
    public void onClick(View view) {
        if(view == imageButtonBack){
            onClickOnButtonBack();
        }

        else if(view == relativeLayoutPhotoEvent || view == imageViewAddPhotoEvent){
            onClickRelativeLayoutPhotoEvent();
        }

        else if(view == textViewEventStartDate){
            onClickOnTextViewStartDate();
        }

        else if(view == textViewEventEndDate){
            onClickOnTextViewEndDate();
        }

        else if(view == textViewValidateCreate){
            confirmEventCreation();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void firebaseUploadSuccess(String url) {
        this.urlGroupPhoto = url;
        sendEventToAPI();
    }

    @Override
    public void firebaseUploadFailed() {

    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        GIApplicationDelegate.getInstance().onRequestFinishWithSuccess(request_code, object);
        progressIndicator.stopRotate();
        finish();
    }

    @Override
    public void onRequestFinishWithFailure() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                bitmapImageEvent = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewAddPhotoEvent.setImageBitmap(bitmapImageEvent);
                relativeLayoutPhotoEvent.setBackgroundColor(Color.BLACK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
