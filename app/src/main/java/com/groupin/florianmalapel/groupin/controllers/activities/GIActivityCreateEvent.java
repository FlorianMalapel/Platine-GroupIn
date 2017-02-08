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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewDeletableItem;
import com.groupin.florianmalapel.groupin.helpers.GICommunicationsHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;
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
                    GIVolleyRequest.RequestCallback,
                    GIAdapterRecyclerViewDeletableItem.ItemClickedCallback {

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final String BUNDLE_ID = "GROUP_BUNDLE";
    public static final String GROUP_ID = "GROUP_OBJECT";

    private ImageView imageButtonBack = null;
    private ImageView imageViewAddPhotoEvent = null;
    private ImageView imageViewLocationIcon = null;
    private ImageView imageViewEuroIcon = null;
    private TextView textViewValidateCreate = null;
    private TextView textViewCreateEvent = null;
    private TextView textViewEventStartDate = null;
    private TextView textViewEventName = null;
    private TextView textViewEventEndDate = null;
    private TextView textViewStartDate = null;
    private TextView textViewEndDate = null;
    private TextView textViewTheme = null;
    private TextView textViewSecondTheme = null;
    private TextView textViewEventBringBack = null;
    private TextView textViewEventPrice = null;
    private TextView textViewEventPlace = null;
    private TextView textViewEventDesc = null;
    private RelativeLayout relativeLayoutPhotoEvent = null;
    private RecyclerView recyclerViewDeletableItems = null;
    private EditText editTextEventName = null;
    private EditText editTextEventDesc = null;
    private EditText editTextEventBringBack = null;
    private EditText editTextEventTheme = null;
    private EditText editTextEventSecondTheme = null;
    private EditText editTextEventPlace = null;
    private EditText editTextEventPrice = null;
    private Spinner spinnerGroupList = null;
    private GIProgressIndicator progressIndicator = null;
    private Bitmap bitmapImageEvent = null;
    private GIEvent eventToCreate = null;
    private GIGroup groupParent = null;
    private String urlGroupPhoto = null;
    private long eventDateStart = 0;
    private long eventDateEnd = 0;
    private GIVolleyHandler volleyHandler = null;
    private SwitchDateTimeDialogFragment startDatePicker = null;
    private SwitchDateTimeDialogFragment endDatePicker = null;
    private ArrayList<String> stringItemListChosen = new ArrayList<>();
    private GIAdapterRecyclerViewDeletableItem adapterDeleteList = null;
    private ImageButton imageButtonAddObject = null;

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
        textViewEventBringBack = (TextView) findViewById(R.id.textViewEventBringBack);
        textViewEventDesc = (TextView) findViewById(R.id.textViewEventDesc);
        textViewEventStartDate = (TextView) findViewById(R.id.textViewEventStartDate);
        textViewEventEndDate = (TextView) findViewById(R.id.textViewEventEndDate);
        textViewStartDate = (TextView) findViewById(R.id.textViewStartDate);
        textViewEventName = (TextView) findViewById(R.id.textViewStartDate);
        textViewEndDate = (TextView) findViewById(R.id.textViewEndDate);
        textViewEventBringBack = (TextView) findViewById(R.id.textViewEventBringBack);
        textViewCreateEvent = (TextView) findViewById(R.id.textViewCreateEvent);
        textViewTheme = (TextView) findViewById(R.id.textViewTheme);
        textViewSecondTheme = (TextView) findViewById(R.id.textViewSecondTheme);
        textViewEventPrice = (TextView) findViewById(R.id.textViewEventPrice);
        textViewEventPlace = (TextView) findViewById(R.id.textViewEventPlace);
        relativeLayoutPhotoEvent = (RelativeLayout) findViewById(R.id.relativeLayoutPhotoEvent);
        editTextEventName = (EditText) findViewById(R.id.editTextBillPrice);
        editTextEventDesc = (EditText) findViewById(R.id.editTextPollQuestion);
        editTextEventBringBack = (EditText) findViewById(R.id.editTextEventBringBack);
        editTextEventTheme = (EditText) findViewById(R.id.editTextEventTheme);
        editTextEventSecondTheme = (EditText) findViewById(R.id.editTextEventSecondTheme);
        editTextEventPlace = (EditText) findViewById(R.id.editTextEventPlace);
        editTextEventPrice = (EditText) findViewById(R.id.editTextEventPrice);
        progressIndicator = (GIProgressIndicator) findViewById(R.id.progressIndicator);
        recyclerViewDeletableItems = (RecyclerView) findViewById(R.id.recyclerViewDeletableItems);
        imageButtonAddObject = (ImageButton) findViewById(R.id.imageButtonAddObject);
        spinnerGroupList = (Spinner) findViewById(R.id.spinnerGroupList);
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
            spinnerGroupList.setVisibility(View.GONE);
            return;
        }
        final List<String> groupsNames = new ArrayList<>();
        for(String key : GIApplicationDelegate.getInstance().getDataCache().userGroupsList.keySet()){
            groupsNames.add(GIApplicationDelegate.getInstance().getDataCache().userGroupsList.get(key).name);
        }
        if(groupsNames.isEmpty())
            return;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupsNames);
        spinnerGroupList.setAdapter(dataAdapter);
        spinnerGroupList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groupParent = GIApplicationDelegate.getInstance().getDataCache().getGroupByName(groupsNames.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initializeViews() {
        textViewCreateEvent.setTypeface(GIDesign.getBoldFont(this));
        textViewValidateCreate.setTypeface(GIDesign.getBoldFont(this));
        textViewEventStartDate.setTypeface(GIDesign.getRegularFont(this));
        textViewEventEndDate.setTypeface(GIDesign.getRegularFont(this));
        textViewStartDate.setTypeface(GIDesign.getRegularFont(this));
        textViewEndDate.setTypeface(GIDesign.getRegularFont(this));
        textViewTheme.setTypeface(GIDesign.getRegularFont(this));
        textViewEventName.setTypeface(GIDesign.getRegularFont(this));
        textViewEventDesc.setTypeface(GIDesign.getRegularFont(this));
        textViewEventBringBack.setTypeface(GIDesign.getRegularFont(this));
        textViewSecondTheme.setTypeface(GIDesign.getRegularFont(this));
        textViewEventPrice.setTypeface(GIDesign.getRegularFont(this));
        textViewEventPlace.setTypeface(GIDesign.getRegularFont(this));
        editTextEventName.setTypeface(GIDesign.getRegularFont(this));
        editTextEventDesc.setTypeface(GIDesign.getRegularFont(this));
        editTextEventBringBack.setTypeface(GIDesign.getRegularFont(this));
        editTextEventTheme.setTypeface(GIDesign.getRegularFont(this));
        editTextEventSecondTheme.setTypeface(GIDesign.getRegularFont(this));
        editTextEventPlace.setTypeface(GIDesign.getRegularFont(this));
        editTextEventPrice.setTypeface(GIDesign.getRegularFont(this));
        imageButtonBack.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initializeMaterialSpinnerWithGroups();
        initRecyclerViewDeletableItemList();
    }

    private void initRecyclerViewDeletableItemList(){
        adapterDeleteList = new GIAdapterRecyclerViewDeletableItem(stringItemListChosen, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewDeletableItems.setLayoutManager(layoutManager);
        recyclerViewDeletableItems.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDeletableItems.setAdapter(adapterDeleteList);
    }


    private void setListeners() {
        imageButtonBack.setOnClickListener(this);
        imageButtonAddObject.setOnClickListener(this);
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
    }

    private void setDateInTextView(Date date, TextView textView){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        textView.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    }

    private void confirmEventCreation(){
        if(!areFieldsFill())
            return;
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

    private boolean areFieldsFill(){
        boolean isFill = true;
        if(bitmapImageEvent == null){
            imageViewAddPhotoEvent.getDrawable().mutate().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            isFill = false;
        }
        if(editTextEventName.getText() == null || editTextEventName.getText().toString().isEmpty()){
            editTextEventName.setHintTextColor(Color.RED);
            isFill = false;
        }
        if(eventDateEnd == 0){
            textViewEventEndDate.setHintTextColor(Color.RED);
            isFill = false;
        }
        if(eventDateStart == 0){
            textViewEventStartDate.setHintTextColor(Color.RED);
            isFill = false;
        }
        if(editTextEventDesc.getText() == null || editTextEventDesc.getText().toString().isEmpty()){
            editTextEventDesc.setHintTextColor(Color.RED);
            isFill = false;
        }
        if(editTextEventTheme.getText() == null || editTextEventTheme.getText().toString().isEmpty()){
            editTextEventTheme.setHintTextColor(Color.RED);
            isFill = false;
        }

        return isFill;
    }

    private void sendEventToAPI(){
        if(groupParent == null)
            return;

        float price = (editTextEventPrice.getText().toString().isEmpty()) ? 0 : Float.valueOf(editTextEventPrice.getText().toString());

        // TODO check if nothing is null
        eventToCreate = new GIEvent(groupParent.id, editTextEventName.getText().toString(),
            editTextEventDesc.getText().toString(), editTextEventTheme.getText().toString(),
            editTextEventPlace.getText().toString(), urlGroupPhoto,
            String.valueOf(eventDateStart), String.valueOf(eventDateEnd),
            price, stringItemListChosen);
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

    private void onClickOnAddObject(){
        String answer = editTextEventBringBack.getText().toString();
        stringItemListChosen.add(answer);
        adapterDeleteList.refreshList(stringItemListChosen);
        editTextEventBringBack.setText("");
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

        else if(view == imageButtonAddObject){
            onClickOnAddObject();
        }
    }

    @Override
    public void itemDeletedAddPosition(int position) {
        if(stringItemListChosen.isEmpty())
            return;
        stringItemListChosen.remove(position);
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
        if(request_code == GIRequestData.POST_EVENT_CODE){
            volleyHandler.getGroups(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid());
        }
        else if(request_code == GIRequestData.GET_GROUPS_CODE){
            progressIndicator.stopRotate();
            finish();
        }
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
