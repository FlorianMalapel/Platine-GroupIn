package com.groupin.florianmalapel.groupin.controllers.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewDeletableItem;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIChoice;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIPoll;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 29/01/2017.
 */

public class GIActivityCreatePoll extends AppCompatActivity
        implements  View.OnClickListener,
                    GIAdapterRecyclerViewDeletableItem.ItemClickedCallback,
                    GIVolleyRequest.RequestCallback {

    public static final String BUNDLE_ID = "bundle";
    public static final String GROUP_ID = "groupId";

    private GIProgressIndicator progressIndicator = null;
    private ImageButton imageButtonBack = null;
    private ImageButton imageButtonAddAnswer = null;
    private Switch switchQCM = null;
    private TextView textViewQCM = null;
    private TextView textViewCreateGroup = null;
    private TextView textViewValidateCreate = null;
    private TextView textViewPollQuestion = null;
    private TextView textViewAnswers = null;
    private EditText editTextPollQuestion = null;
    private EditText editTextAnswer = null;
    private RecyclerView recyclerViewDeletableItems = null;
    private GIVolleyHandler volleyHandler = null;
    private GIAdapterRecyclerViewDeletableItem adapterDeleteList = null;
    private ArrayList<String> stringItemListChosen = new ArrayList<>();
    private String groupId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        initialize();
    }

    private void initialize() {
        findViewById();
        initializeObjects();
        initializeViews();
        setListeners();
    }

    private void findViewById() {
        progressIndicator = (GIProgressIndicator) findViewById(R.id.progressIndicator);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        imageButtonAddAnswer = (ImageButton) findViewById(R.id.imageButtonAddAFriend);
        switchQCM = (Switch) findViewById(R.id.switchQCM);
        textViewQCM = (TextView) findViewById(R.id.textViewQCM);
        textViewCreateGroup = (TextView) findViewById(R.id.textViewCreateBill);
        textViewValidateCreate = (TextView) findViewById(R.id.textViewValidateCreate);
        textViewPollQuestion = (TextView) findViewById(R.id.textViewEventDesc);
        textViewAnswers = (TextView) findViewById(R.id.textViewPaidFor);
        editTextPollQuestion = (EditText) findViewById(R.id.editTextPollQuestion);
        editTextAnswer = (EditText) findViewById(R.id.editTextAnswer);
        recyclerViewDeletableItems = (RecyclerView) findViewById(R.id.recyclerViewDeletableItems);
    }

    private void initializeObjects() {
        volleyHandler = new GIVolleyHandler();
        groupId = getIntent().getBundleExtra(BUNDLE_ID).getString(GROUP_ID);
    }

    private void initializeViews() {
        imageButtonBack.getDrawable().mutate().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
//        imageButtonBack.getDrawable().mutate().setColorFilter(GIDesign.getColorFromXml(this, GIDesign.getColorFromXml(this, R.color.toolbarTextViewTextColor)), PorterDuff.Mode.SRC_ATOP);
        textViewQCM.setTypeface(GIDesign.getRegularFont(this));
        textViewCreateGroup.setTypeface(GIDesign.getBoldFont(this));
        textViewValidateCreate.setTypeface(GIDesign.getBoldFont(this));
        textViewPollQuestion.setTypeface(GIDesign.getRegularFont(this));
        textViewAnswers.setTypeface(GIDesign.getRegularFont(this));
        initRecyclerViewDeletableItemList();
    }

    private void initRecyclerViewDeletableItemList(){
        adapterDeleteList = new GIAdapterRecyclerViewDeletableItem(stringItemListChosen, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewDeletableItems.setLayoutManager(layoutManager);
        recyclerViewDeletableItems.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDeletableItems.setAdapter(adapterDeleteList);
    }

    private void setListeners() {
        imageButtonAddAnswer.setOnClickListener(this);
        imageButtonBack.setOnClickListener(this);
        textViewValidateCreate.setOnClickListener(this);
    }

    private void onClickOnValidateTextView(){
        if(!areFieldsFill())
            return;

        GIPoll poll = new GIPoll();
        poll.creatorUid = GIApplicationDelegate.getInstance().getDataCache().getUserUid();
        poll.groupId = groupId;
        poll.question = editTextPollQuestion.getText().toString();
        poll.isQcm = switchQCM.isChecked();
        ArrayList<GIChoice> choicesList = new ArrayList<>();
        for(String string : stringItemListChosen){
            GIChoice choice = new GIChoice(string);
            choicesList.add(choice);
        }
        poll.listChoice = choicesList;
        try {
            volleyHandler.postPoll(this, poll.getCreatePollJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GIDesign.startRotatingProgressIndicator(progressIndicator);
    }

    private void onClickOnButtonAddAnswer(){
        String answer = editTextAnswer.getText().toString();
        stringItemListChosen.add(answer);
        adapterDeleteList.refreshList(stringItemListChosen);
        editTextAnswer.setText("");
    }

    private boolean areFieldsFill(){
        boolean isFill = true;

        if(editTextPollQuestion.getText() == null || editTextPollQuestion.getText().toString().isEmpty()){
            editTextPollQuestion.setHintTextColor(Color.RED);
            isFill = false;
        }

        if(stringItemListChosen == null || stringItemListChosen.isEmpty()){
            editTextAnswer.setHintTextColor(Color.RED);
            isFill = false;
        }

        return isFill;
    }

    @Override
    public void onClick(View view) {
        if(view == textViewValidateCreate){
            onClickOnValidateTextView();
        }

        else if(view == imageButtonAddAnswer){
            onClickOnButtonAddAnswer();
        }

        else if(view == imageButtonBack){
            finish();
        }
    }

    @Override
    public void itemDeletedAddPosition(int position) {
        if(stringItemListChosen.isEmpty())
            return;
        stringItemListChosen.remove(position);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        if(request_code == GIRequestData.POST_POLLS_GROUP_CODE){
            GIDesign.stopRotatingProgressIndicator(progressIndicator);
            finish();
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
