package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewNotifsFriend;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewNotifsGroup;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONObject;

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuHome extends Fragment implements View.OnClickListener, GIVolleyRequest.RequestCallback{

    private RecyclerView recyclerViewNotifsFriends = null;
    private RecyclerView recyclerViewNotifsGroups = null;
    private RecyclerView recyclerViewNotifsEvent = null;
    private GIAdapterRecyclerViewNotifsFriend notifsFriendAdapter = null;
    private GIAdapterRecyclerViewNotifsGroup notifsGroupAdapter = null;
    private TextView textViewNotifsFriends = null;
    private TextView textViewNotifsGroups = null;
    private TextView textViewNotifsEvents = null;
    private FOCUS currentFocusState = FOCUS.NONE;
    private GIVolleyHandler volleyHandler = null;

    public enum FOCUS {
        NONE, NOTIF_FRIEND, NOTIF_EVENT, NOTIF_GROUP
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu_home, container, false);
        findViewById(view);
        initialize();
        initViews();
        setListeners();
        return view;

    }

    private void initViews(){
        textViewNotifsGroups.setTypeface(GIDesign.getBoldFont(getContext()));
        textViewNotifsFriends.setTypeface(GIDesign.getBoldFont(getContext()));
        textViewNotifsEvents.setTypeface(GIDesign.getBoldFont(getContext()));
        recyclerViewNotifsGroups.setVisibility(View.GONE);
        recyclerViewNotifsFriends.setVisibility(View.GONE);
        recyclerViewNotifsEvent.setVisibility(View.GONE);
        initViewNotificationFriends();
        initViewNotificationGroups();
    }

    private void setListeners(){
        textViewNotifsFriends.setOnClickListener(this);
        textViewNotifsGroups.setOnClickListener(this);
        textViewNotifsEvents.setOnClickListener(this);
    }

    private void initRecyclerViewNotifsFriend(){
        notifsFriendAdapter = new GIAdapterRecyclerViewNotifsFriend(GIApplicationDelegate.getInstance().getDataCache().notifsFriendList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewNotifsFriends.setLayoutManager(layoutManager);
        recyclerViewNotifsFriends.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNotifsFriends.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewNotifsFriends.setAdapter(notifsFriendAdapter);
    }

    private void initViewNotificationFriends(){
        initRecyclerViewNotifsFriend();
        if(GIApplicationDelegate.getInstance().getDataCache().notifsFriendList.isEmpty()) {
            recyclerViewNotifsFriends.setVisibility(View.GONE);
            textViewNotifsFriends.setText(textViewNotifsFriends.getText() + " (0)");
        }
        else {
            textViewNotifsFriends.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_arrow_up), null);
            textViewNotifsFriends.setText(textViewNotifsFriends.getText() + " (" + GIApplicationDelegate.getInstance().getDataCache().notifsFriendList.size() + ")" );
        }
        textViewNotifsFriends.getCompoundDrawables()[2].mutate().setColorFilter(GIDesign.getColorFromXml(getContext(), R.color.GIYellow), PorterDuff.Mode.SRC_ATOP);
    }

    private void initViewNotificationGroups(){
        initRecyclerViewNotifsGroup();
        if(GIApplicationDelegate.getInstance().getDataCache().notifsGroupList.isEmpty()) {
            recyclerViewNotifsGroups.setVisibility(View.GONE);
            textViewNotifsGroups.setText(textViewNotifsGroups.getText() + " (0)");
        }
        else {
            textViewNotifsGroups.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_arrow_up), null);
            textViewNotifsGroups.setText(textViewNotifsGroups.getText() + " (" + GIApplicationDelegate.getInstance().getDataCache().notifsGroupList.size() + ")" );
        }
        textViewNotifsGroups.getCompoundDrawables()[2].mutate().setColorFilter(GIDesign.getColorFromXml(getContext(), R.color.GIYellow), PorterDuff.Mode.SRC_ATOP);
    }

    private void initRecyclerViewNotifsGroup(){
        notifsGroupAdapter = new GIAdapterRecyclerViewNotifsGroup(GIApplicationDelegate.getInstance().getDataCache().notifsGroupList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewNotifsGroups.setLayoutManager(layoutManager);
        recyclerViewNotifsGroups.setItemAnimator(new DefaultItemAnimator());
        recyclerViewNotifsGroups.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewNotifsGroups.setAdapter(notifsGroupAdapter);
    }

    private void findViewById(View view){
        recyclerViewNotifsFriends = (RecyclerView) view.findViewById(R.id.recyclerViewNotifsFriends);
        recyclerViewNotifsGroups = (RecyclerView) view.findViewById(R.id.recyclerViewNotifsGroups);
        textViewNotifsGroups = (TextView) view.findViewById(R.id.textViewNotifsGroups);
        textViewNotifsFriends = (TextView) view.findViewById(R.id.textViewNotifsFriends);
        textViewNotifsEvents = (TextView) view.findViewById(R.id.textViewNotifsEvents);
        recyclerViewNotifsEvent = (RecyclerView) view.findViewById(R.id.recyclerViewNotifsEvent);
    }

    private void initialize(){
        volleyHandler = new GIVolleyHandler();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(GIApplicationDelegate.getInstance().getDataCache().notifsGroupList == null
                || GIApplicationDelegate.getInstance().getDataCache().notifsFriendList == null
                || GIApplicationDelegate.getInstance().getDataCache().notifsFriendList.isEmpty()
                || GIApplicationDelegate.getInstance().getDataCache().notifsGroupList.isEmpty()){
            volleyHandler.getNotifications(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid());
        }
    }

    private void selectTextViewCategory(TextView textView, RecyclerView recyclerView, boolean isSelected){
        if(isSelected) {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_arrow_up), null);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_arrow_down), null);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void onClickOnTextViewNotifsFriend(){
        if(currentFocusState == FOCUS.NONE) {
            selectTextViewCategory(textViewNotifsFriends, recyclerViewNotifsFriends, true);
        }

        else if(currentFocusState == FOCUS.NOTIF_EVENT){
            selectTextViewCategory(textViewNotifsFriends, recyclerViewNotifsFriends, true);
            selectTextViewCategory(textViewNotifsEvents, recyclerViewNotifsEvent, false);
        }

        else if(currentFocusState == FOCUS.NOTIF_FRIEND){
            selectTextViewCategory(textViewNotifsFriends, recyclerViewNotifsFriends, false);
            currentFocusState = FOCUS.NONE;
            return;
        }

        else if(currentFocusState == FOCUS.NOTIF_GROUP){
            selectTextViewCategory(textViewNotifsFriends, recyclerViewNotifsFriends, true);
            selectTextViewCategory(textViewNotifsGroups, recyclerViewNotifsGroups, false);
        }

        currentFocusState = FOCUS.NOTIF_FRIEND;
    }

    private void onClickOnTextViewNotifsEvent(){
        if(currentFocusState == FOCUS.NONE) {
            selectTextViewCategory(textViewNotifsEvents, recyclerViewNotifsEvent, true);
        }

        else if(currentFocusState == FOCUS.NOTIF_EVENT){
            selectTextViewCategory(textViewNotifsEvents, recyclerViewNotifsEvent, false);
            currentFocusState = FOCUS.NONE;
            return;
        }

        else if(currentFocusState == FOCUS.NOTIF_FRIEND){
            selectTextViewCategory(textViewNotifsEvents, recyclerViewNotifsEvent, true);
            selectTextViewCategory(textViewNotifsFriends, recyclerViewNotifsFriends, false);
        }

        else if(currentFocusState == FOCUS.NOTIF_GROUP){
            selectTextViewCategory(textViewNotifsEvents, recyclerViewNotifsEvent, true);
            selectTextViewCategory(textViewNotifsGroups, recyclerViewNotifsGroups, false);
        }
        currentFocusState = FOCUS.NOTIF_EVENT;
    }

    private void onClickOnTextViewNotifsGroup(){
        if(currentFocusState == FOCUS.NONE) {
            selectTextViewCategory(textViewNotifsGroups, recyclerViewNotifsGroups, true);
        }

        else if(currentFocusState == FOCUS.NOTIF_EVENT){
            selectTextViewCategory(textViewNotifsGroups, recyclerViewNotifsGroups, true);
            selectTextViewCategory(textViewNotifsEvents, recyclerViewNotifsEvent, false);
        }

        else if(currentFocusState == FOCUS.NOTIF_FRIEND){
            selectTextViewCategory(textViewNotifsGroups, recyclerViewNotifsGroups, true);
            selectTextViewCategory(textViewNotifsFriends, recyclerViewNotifsFriends, false);
        }

        else if(currentFocusState == FOCUS.NOTIF_GROUP){
            selectTextViewCategory(textViewNotifsGroups, recyclerViewNotifsGroups, false);
            currentFocusState = FOCUS.NONE;
            return;
        }

        currentFocusState = FOCUS.NOTIF_GROUP;
    }

    @Override
    public void onClick(View view) {
        if(view == textViewNotifsFriends){
            onClickOnTextViewNotifsFriend();
        }

        else if(view == textViewNotifsGroups){
            onClickOnTextViewNotifsGroup();
        }

        else if(view == textViewNotifsEvents){
            onClickOnTextViewNotifsEvent();
        }
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        GIApplicationDelegate.getInstance().onRequestFinishWithSuccess(request_code, object);
        if(request_code == GIRequestData.GET_NOTIFICATIONS_CODE){
            initRecyclerViewNotifsGroup();
            initRecyclerViewNotifsFriend();
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
