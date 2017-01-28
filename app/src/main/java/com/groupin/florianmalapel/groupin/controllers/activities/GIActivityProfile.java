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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.helpers.GICommunicationsHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by florianmalapel on 22/01/2017.
 */

public class GIActivityProfile extends AppCompatActivity
        implements View.OnClickListener, GIVolleyRequest.RequestCallback, GICommunicationsHelper.FirebaseUploadImageCallback {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewUserPhoto = null;
    private ImageButton imageButtonBack = null;
    private Button buttonLogOut = null;
    private RelativeLayout relativeLayoutPhoto = null;
    private EditText editTextUserDisplayName = null;
    private TextView textViewValidateUpdate = null;
    private GIVolleyHandler volleyHandler = null;
    private GIProgressIndicator progressIndicator = null;
    private Bitmap bitmapProfilePicture = null;
    private String urlBitmapProfilePicture = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
    }

    private void initialize() {
        findViewById();
        initializeObjects();
        initializeViews();
        setListeners();
    }

    private void findViewById() {
        buttonLogOut = (Button) findViewById(R.id.logOut);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        relativeLayoutPhoto = (RelativeLayout) findViewById(R.id.relativeLayoutPhoto);
        imageViewUserPhoto = (ImageView) findViewById(R.id.imageViewUserPhoto);
        editTextUserDisplayName = (EditText) findViewById(R.id.editTextUserDisplayName);
        textViewValidateUpdate = (TextView) findViewById(R.id.textViewValidateUpdate);
        progressIndicator = (GIProgressIndicator) findViewById(R.id.progressIndicator);
    }

    private void initializeObjects() {
        volleyHandler = new GIVolleyHandler();
    }

    private void initializeViews() {
        imageButtonBack.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        String urlPhoto = GIApplicationDelegate.getInstance().getDataCache().user.photoURL;
        if( urlPhoto != null && !urlPhoto.isEmpty())
            Picasso.with(this).load(GIApplicationDelegate.getInstance().getDataCache().user.photoURL).transform(new CircleTransform()).into(imageViewUserPhoto);
        editTextUserDisplayName.setText(GIApplicationDelegate.getInstance().getDataCache().user.display_name);
    }

    private void setListeners() {
        imageButtonBack.setOnClickListener(this);
        imageViewUserPhoto.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);
        relativeLayoutPhoto.setOnClickListener(this);
        textViewValidateUpdate.setOnClickListener(this);
    }

    private void onClickOnConfirm(){
        progressIndicator.startRotating();
        progressIndicator.setVisibility(View.VISIBLE);
        if(bitmapProfilePicture != null)
            GICommunicationsHelper.firebaseUploadBitmap(GIApplicationDelegate.getInstance().getDataCache().getUserUid() + System.currentTimeMillis() + ".jpg", bitmapProfilePicture, this);
        else sendUserUpdatedToApi();
    }

    private void onClickOnRelativeLayoutProfilePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void sendUserUpdatedToApi(){
        String displayName = editTextUserDisplayName.getText().toString();

        GIUser user = GIApplicationDelegate.getInstance().getDataCache().user;

        if(displayName != null && !displayName.isEmpty())
            user.display_name = displayName;
        if(urlBitmapProfilePicture != null)
            user.photoURL = urlBitmapProfilePicture;

        volleyHandler.postUser(user, this);
    }

    private void onClickOnButtonLogOut(){
        GIApplicationDelegate.getInstance().getDataCache().logOut();
        Intent intent = new Intent(this, GIActivitySocialSign.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view == textViewValidateUpdate){
            onClickOnConfirm();
        }

        else if(view == imageButtonBack){
            finish();
        }

        else if(view == relativeLayoutPhoto){
            onClickOnRelativeLayoutProfilePicture();
        }

        else if(view == buttonLogOut){
            onClickOnButtonLogOut();
        }
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        GIApplicationDelegate.getInstance().onRequestFinishWithSuccess(request_code, object);
        if(request_code == GIRequestData.POST_USER_CODE){
            GIApplicationDelegate.getInstance().getDataCache().storeCurrentUserInPref();
            progressIndicator.stopRotate();
            finish();
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }

    @Override
    public void firebaseUploadSuccess(String url) {
        this.urlBitmapProfilePicture = url;
        Picasso.with(this).load(urlBitmapProfilePicture).transform(new CircleTransform()).into(imageViewUserPhoto);
        sendUserUpdatedToApi();
    }

    @Override
    public void firebaseUploadFailed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                bitmapProfilePicture = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewUserPhoto.setImageBitmap(bitmapProfilePicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
