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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewFriends;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewFriendsListCreateGroup;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIBill;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 31/01/2017.
 */

public class GIActivityCreateBill extends AppCompatActivity
        implements  View.OnClickListener,
                    GIVolleyRequest.RequestCallback,
                    GIAdapterRecyclerViewFriendsListCreateGroup.ItemClickedCallback {

    private ImageButton imageButtonBack = null;
    private ImageButton imageButtonAddAFriend = null;
    private TextView textViewCreateBill = null;
    private TextView textViewValidateCreate = null;
    private TextView textViewEventName = null;
    private TextView textViewBillPrice = null;
    private TextView textViewPaidFor = null;
    private TextView textViewCancel = null;
    private TextView textViewOk = null;
    private EditText editTextBillName = null;
    private EditText editTextBillPrice = null;
    private RelativeLayout relativeLayoutFriendsPopUp = null;
    private RecyclerView recyclerViewDeletableItems = null;
    private RecyclerView recyclerViewFriends = null;
    private GIAdapterRecyclerViewFriendsListCreateGroup adapterDeleteList = null;
    private ArrayList<GIUser> friendsListChosen = null;
    private GIAdapterRecyclerViewFriends adapterRecyclerViewFriends = null;
    private ArrayList<GIUser> friendsList = null;
    private String groupId = null;
    private GIVolleyHandler volleyHandler = null;
    private GIProgressIndicator progressIndicator = null;
    private GIBill billToCreate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
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
        imageButtonAddAFriend = (ImageButton) findViewById(R.id.imageButtonAddAFriend);
        textViewCreateBill = (TextView) findViewById(R.id.textViewCreateBill);
        textViewValidateCreate = (TextView) findViewById(R.id.textViewValidateCreate);
        textViewEventName = (TextView) findViewById(R.id.textViewEventName);
        textViewBillPrice = (TextView) findViewById(R.id.textViewBillPrice);
        textViewPaidFor = (TextView) findViewById(R.id.textViewPaidFor);
        textViewCancel = (TextView) findViewById(R.id.textViewCancel);
        textViewOk = (TextView) findViewById(R.id.textViewOk);
        editTextBillName = (EditText) findViewById(R.id.editTextBillName);
        editTextBillPrice = (EditText) findViewById(R.id.editTextBillPrice);
        recyclerViewFriends = (RecyclerView) findViewById(R.id.recyclerViewFriends);
        recyclerViewDeletableItems = (RecyclerView) findViewById(R.id.recyclerViewDeletableItems);
        relativeLayoutFriendsPopUp = (RelativeLayout) findViewById(R.id.relativeLayoutFriendsPopUp);
        progressIndicator = (GIProgressIndicator) findViewById(R.id.progressIndicator);
    }

    private void initializeObjects() {
        groupId = getIntent().getBundleExtra("bundle").getString("groupId");
        friendsList = GIApplicationDelegate.getInstance().getDataCache().getArrayMembersGroup(groupId);
        volleyHandler = new GIVolleyHandler();
    }

    private void initializeViews() {
        initRecyclerViewFriends();
        initRecyclerViewFriendDeleteList();
        textViewCreateBill.setTypeface(GIDesign.getBoldFont(this));
        textViewValidateCreate.setTypeface(GIDesign.getBoldFont(this));
        textViewEventName.setTypeface(GIDesign.getRegularFont(this));
        textViewBillPrice.setTypeface(GIDesign.getRegularFont(this));
        textViewPaidFor.setTypeface(GIDesign.getRegularFont(this));
        textViewCancel.setTypeface(GIDesign.getRegularFont(this));
        textViewOk.setTypeface(GIDesign.getRegularFont(this));
        imageButtonBack.getDrawable().mutate().setColorFilter(GIDesign.getColorFromXml(this, R.color.textViewToolbarTextColor), PorterDuff.Mode.SRC_ATOP);
    }

    private void initRecyclerViewFriendDeleteList(){
        adapterDeleteList = new GIAdapterRecyclerViewFriendsListCreateGroup(friendsListChosen, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewDeletableItems.setLayoutManager(layoutManager);
        recyclerViewDeletableItems.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDeletableItems.setAdapter(adapterDeleteList);
    }

    private void initRecyclerViewFriends(){
        adapterRecyclerViewFriends = new GIAdapterRecyclerViewFriends(friendsList, true, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewFriends.setLayoutManager(layoutManager);
        recyclerViewFriends.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFriends.setAdapter(adapterRecyclerViewFriends);
    }

    private void setListeners() {
        textViewCancel.setOnClickListener(this);
        textViewOk.setOnClickListener(this);
        imageButtonBack.setOnClickListener(this);
        textViewValidateCreate.setOnClickListener(this);
        imageButtonAddAFriend.setOnClickListener(this);
    }

    private void onClickOnTextViewCancel(){
        relativeLayoutFriendsPopUp.setVisibility(View.GONE);
    }

    private void onClickOnTextViewOk(){
        friendsListChosen = getUserChosenFromAdapter();
        relativeLayoutFriendsPopUp.setVisibility(View.GONE);
        adapterDeleteList.refreshList(friendsListChosen);

    }

    private void confirmBillCreation(){
        if(!areFieldsFill())
            return;
        progressIndicator.startRotating();
        progressIndicator.setVisibility(View.VISIBLE);
        sendGroupToAPI();
    }

    private ArrayList<GIUser> getUserChosenFromAdapter(){
        this.friendsListChosen = null;
        ArrayList<GIUser> friendsChosen = new ArrayList<>();
        for(int i=0; i<adapterRecyclerViewFriends.getFriendsSelected().size(); i++){
            if(adapterRecyclerViewFriends.getFriendsSelected().get(i)){
                friendsChosen.add(adapterRecyclerViewFriends.getGIUserAtPosition(i));
            }
        }
        return friendsChosen;
    }

    private boolean areFieldsFill(){
        boolean isFill = true;

        if(editTextBillName == null || editTextBillName.getText().toString().isEmpty()){
            editTextBillName.setHintTextColor(Color.RED);
            isFill = false;
        }

        if(editTextBillPrice.getText() == null || editTextBillPrice.getText().toString().isEmpty()){
            editTextBillPrice.setHintTextColor(Color.RED);
            isFill = false;
        }

        if(friendsListChosen == null && friendsListChosen.isEmpty()){
            textViewPaidFor.setTextColor(Color.RED);
            isFill = false;
        }

        return isFill;
    }

    private void sendGroupToAPI(){
        ArrayList<String> uidsSelected = new ArrayList<>();
        for(GIUser user : friendsListChosen){
            uidsSelected.add(user.uid);
        }
        billToCreate = new GIBill(groupId,
                uidsSelected,
                Double.valueOf(editTextBillPrice.getText().toString()),
                editTextBillName.getText().toString(),
                GIApplicationDelegate.getInstance().getDataCache().getUserUid());
        try {
            JSONObject billJSON = billToCreate.getCreateBillJSON();
            volleyHandler.postBill(this, billJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if(view == imageButtonBack){
            finish();
        }

        else if(view == textViewValidateCreate){
            confirmBillCreation();
        }

        else if(view == imageButtonAddAFriend){
            relativeLayoutFriendsPopUp.setVisibility(View.VISIBLE);
        }

        else if(view == textViewCancel){
            onClickOnTextViewCancel();
        }

        else if(view == textViewOk){
            onClickOnTextViewOk();
        }
    }

    @Override
    public void itemDeletedAddPosition(int position) {
        if(this.friendsListChosen != null)
            this.friendsListChosen.remove(position);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        if(request_code == GIRequestData.POST_BILL_CODE){
            finish();
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
