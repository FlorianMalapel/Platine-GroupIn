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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewFriends;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewFriendsListCreateGroup;
import com.groupin.florianmalapel.groupin.helpers.GICommunicationsHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by florianmalapel on 13/01/2017.
 */

public class GIActivityCreateGroup extends AppCompatActivity
        implements  View.OnClickListener,
                    GIAdapterRecyclerViewFriendsListCreateGroup.ItemClickedCallback,
                    GICommunicationsHelper.FirebaseUploadImageCallback,
                    GIVolleyRequest.RequestCallback {

    private int PICK_IMAGE_REQUEST = 1;

    private ImageButton buttonBack = null;
    private ImageView imageViewAddPhotoGroup = null;
    private ImageButton buttonAddFriend = null;

    private EditText editTextGroupDesc = null;
    private EditText editTextGroupName = null;

    private TextView textViewCancel = null;
    private TextView textViewOk = null;
    private TextView textViewValidateCreate = null;

    private RelativeLayout relativeLayoutFriends = null;
    private RelativeLayout relativeLayoutPhotoGroup = null;

    private RecyclerView recyclerViewFriends = null;
    private GIAdapterRecyclerViewFriends adapterRecyclerViewFriends = null;

    private ArrayList<GIUser> friendsListChosen = null;

    private RecyclerView recyclerViewFriendsDeleteList = null;
    private GIAdapterRecyclerViewFriendsListCreateGroup adapterRecyclerViewFriendsDeleteList = null;

    private String urlGroupPhoto = null;
    private Bitmap bitmapImageGroup = null;

    private GIVolleyHandler volleyHandler = null;

    private ArrayList<GIUser> friendsList = null;

    private GIGroup groupToCreate = null;
    private GIProgressIndicator progressIndicator = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        initialize();
    }

    private void initialize() {
        findViewById();
//        fixture();
        initializeObjects();
        initializeViews();
        setListeners();
    }

    private void findViewById() {
        buttonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        imageViewAddPhotoGroup = (ImageView) findViewById(R.id.imageButtonAddPhotoGroup);
        buttonAddFriend = (ImageButton) findViewById(R.id.imageButtonAddFriend);
        editTextGroupName = (EditText) findViewById(R.id.editTextGroupName);
        editTextGroupDesc = (EditText) findViewById(R.id.editTextEventDesc);
        textViewCancel = (TextView) findViewById(R.id.textViewCancel);
        textViewValidateCreate = (TextView) findViewById(R.id.textViewValidateCreate);
        textViewOk = (TextView) findViewById(R.id.textViewOk);
        recyclerViewFriends = (RecyclerView) findViewById(R.id.recyclerViewFriends);
        recyclerViewFriendsDeleteList = (RecyclerView) findViewById(R.id.recyclerViewFriendsDeleteList);
        relativeLayoutFriends = (RelativeLayout) findViewById(R.id.relativeLayoutFriendsPopUp);
        relativeLayoutPhotoGroup = (RelativeLayout) findViewById(R.id.relativeLayoutPhotoEvent);
        progressIndicator = (GIProgressIndicator) findViewById(R.id.progressIndicator);
    }

    private void initializeObjects() {
        friendsList = GIApplicationDelegate.getInstance().getDataCache().getUserFriendsList();
        volleyHandler = new GIVolleyHandler();
    }

    private void initRecyclerViewFriends(){
        adapterRecyclerViewFriends = new GIAdapterRecyclerViewFriends(friendsList, true, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewFriends.setLayoutManager(layoutManager);
        recyclerViewFriends.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFriends.setAdapter(adapterRecyclerViewFriends);
    }

    private void initRecyclerViewFriendDeleteList(){
        adapterRecyclerViewFriendsDeleteList = new GIAdapterRecyclerViewFriendsListCreateGroup(friendsListChosen, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewFriendsDeleteList.setLayoutManager(layoutManager);
        recyclerViewFriendsDeleteList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFriendsDeleteList.setAdapter(adapterRecyclerViewFriendsDeleteList);
    }

    private void initializeViews() {
        buttonBack.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initRecyclerViewFriends();
        initRecyclerViewFriendDeleteList();
    }

    private void setListeners() {
        textViewCancel.setOnClickListener(this);
        textViewOk.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        textViewValidateCreate.setOnClickListener(this);
        imageViewAddPhotoGroup.setOnClickListener(this);
        buttonAddFriend.setOnClickListener(this);
    }

    private void onClickOnTextViewCancel(){
        relativeLayoutFriends.setVisibility(View.GONE);
    }

    private void onClickOnTextViewOk(){
        friendsListChosen = getUserChosenFromAdapter();
        relativeLayoutFriends.setVisibility(View.GONE);
        adapterRecyclerViewFriendsDeleteList.refreshList(friendsListChosen);
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

    private void pickAPicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void confirmGroupCreation(){
        progressIndicator.startRotating();
        progressIndicator.setVisibility(View.VISIBLE);
        GICommunicationsHelper.firebaseUploadBitmap(GIApplicationDelegate.getInstance().getDataCache().getUserUid() + System.currentTimeMillis() + ".jpg", bitmapImageGroup, this);
    }

    private void sendGroupToAPI(){
        // TODO check if nothing is null
        groupToCreate = new GIGroup(editTextGroupName.getText().toString(), editTextGroupDesc.getText().toString(), urlGroupPhoto, friendsListChosen);
        try {
            volleyHandler.postNewGroup(this, groupToCreate.getCreateGroupJSON(GIApplicationDelegate.getInstance().getDataCache().getUserUid()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onClickOnButtonAddFriend(){
        relativeLayoutFriends.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonBack){
            finish();
        }

        else if(view == imageViewAddPhotoGroup){
            pickAPicture();
        }

        else if(view == textViewValidateCreate){
            confirmGroupCreation();
        }

        else if(view == buttonAddFriend){
            onClickOnButtonAddFriend();
        }

        else if(view == textViewCancel){
            onClickOnTextViewCancel();
        }

        else if(view == textViewOk){
            onClickOnTextViewOk();
        }
    }

    private void sendInvitationToTheGroupToFriendsSelected(){
        String idGroup = GIApplicationDelegate.getInstance().getDataCache().getGroupIdByName(editTextGroupName.getText().toString());
        for(GIUser friend : friendsListChosen){
            volleyHandler.postUserJoinGroup(null, friend.uid, idGroup);
        }
    }

    @Override
    public void itemDeletedAddPosition(int position) {
        if(this.friendsListChosen != null)
            this.friendsListChosen.remove(position);
    }

    @Override
    public void firebaseUploadSuccess(String url) {
        this.urlGroupPhoto = url;
        sendGroupToAPI();
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
        if(request_code == GIRequestData.POST_GROUP_CODE){
            sendInvitationToTheGroupToFriendsSelected();
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
                bitmapImageGroup = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewAddPhotoGroup.setImageBitmap(bitmapImageGroup);
                relativeLayoutPhotoGroup.setBackgroundColor(Color.BLACK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
