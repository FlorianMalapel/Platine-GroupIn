package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityCreateEvent;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityDisplayGroup;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewEventsList;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewFriends;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.views.GIHorizontalBubbleList;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIFragmentGroupMenuEvents extends Fragment
        implements  View.OnClickListener,
                    GIHorizontalBubbleList.OnClick, GIVolleyRequest.RequestCallback,
                    SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<GIEvent> list_events = null;
    private RecyclerView recyclerViewEvents = null;
    private GIAdapterRecyclerViewEventsList eventsAdapter = null;
    private FloatingActionButton fabAddEvent = null;
    private GIHorizontalBubbleList horizontalBubbleList_members = null;
    private RelativeLayout relativeLayoutFriendsPopUp = null;
    private RecyclerView recyclerViewFriends = null;
    private GIAdapterRecyclerViewFriends adapterRecyclerViewFriends = null;
    private TextView textViewCancel = null;
    private TextView textViewOk = null;
    private TextView textViewMyEvents = null;
    private ArrayList<GIUser> friendsListChosen = null;
    private GIVolleyHandler volleyHandler = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private String groupId = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_menu_events, container, false);
        findViewById(view);
        initialize();
        initViews();
        setListeners();
        return view;
    }

    private void initialize(){
        groupId = getArguments().getString(GIActivityDisplayGroup.GROUP_ID);
        volleyHandler = new GIVolleyHandler();
    }

    private void findViewById(View view) {
        recyclerViewEvents = (RecyclerView) view.findViewById(R.id.recyclerViewEvents);
        recyclerViewFriends = (RecyclerView) view.findViewById(R.id.recyclerViewFriends);
        textViewOk = (TextView) view.findViewById(R.id.textViewOk);
        relativeLayoutFriendsPopUp = (RelativeLayout) view.findViewById(R.id.relativeLayoutFriendsPopUp);
        textViewCancel = (TextView) view.findViewById(R.id.textViewCancel);
        textViewMyEvents = (TextView) view.findViewById(R.id.textViewMyEvents);
        fabAddEvent = (FloatingActionButton) view.findViewById(R.id.fabAddEvent);
        horizontalBubbleList_members = (GIHorizontalBubbleList) view.findViewById(R.id.horizontalBubbleList_members);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    }

    private void initViews(){
        textViewOk.setTypeface(GIDesign.getRegularFont(getContext()));
        textViewCancel.setTypeface(GIDesign.getRegularFont(getContext()));
        textViewMyEvents.setTypeface(GIDesign.getBoldFont(getContext()));
        fabAddEvent.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initRecyclerView();
        initTopBubbleFriendList();
        initRecyclerViewFriends();
    }

    private void initTopBubbleFriendList(){
        if(GIApplicationDelegate.getInstance().getDataCache().userFriendList == null)
            return;

        horizontalBubbleList_members.createContainerScrollView(this);
        horizontalBubbleList_members.addImageViewAddMoreIcon(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        for(GIUser user : GIApplicationDelegate.getInstance().getDataCache().getArrayMembersGroup(getCurrentGroupId())){
            horizontalBubbleList_members.addBubbleInViewWithPicasso(user);
        }
    }

    private void setListeners(){
        fabAddEvent.setOnClickListener(this);
        textViewCancel.setOnClickListener(this);
        textViewOk.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerViewFriends(){
        adapterRecyclerViewFriends = new GIAdapterRecyclerViewFriends(GIApplicationDelegate.getInstance().getDataCache().getUserFriendsList(), true, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewFriends.setLayoutManager(layoutManager);
        recyclerViewFriends.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFriends.setAdapter(adapterRecyclerViewFriends);
    }

    private boolean isEventListEmpty(ArrayList<GIEvent> eventArrayList){
        if(eventArrayList == null || eventArrayList.isEmpty())
            return true;
        else {
            boolean isNull = true;
            for(GIEvent event : eventArrayList){
                if(event != null)
                    isNull = false;
            }
            return isNull;
        }
    }

    private void initRecyclerView(){
        if(isEventListEmpty(GIApplicationDelegate.getInstance().getDataCache().getArrayEventFromGroup(getCurrentGroupId())))
            return;

        eventsAdapter = new GIAdapterRecyclerViewEventsList(GIApplicationDelegate.getInstance().getDataCache().getArrayEventFromGroup(getCurrentGroupId()), getContext()); // TODO SHOULD NOT BE NULL
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManager);
        recyclerViewEvents.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEvents.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewEvents.setAdapter(eventsAdapter);
    }

    private String getCurrentGroupId(){
        return groupId;
    }


    private void onClickFabAddEvent(){
        createNewEvent();
    }

    private void sendInvitationToJoinGroup(){
        for(GIUser user : friendsListChosen){
            Log.v("∆∆ GIFragmentGroupMenu", "-------  : " + groupId + "  " + user.uid);
            volleyHandler.postNotifAddUserInGroup(this, groupId, user.uid);
        }
    }

    private void onClickOnTextViewCancel(){
        relativeLayoutFriendsPopUp.setVisibility(View.GONE);
    }

    private void onClickOnTextViewOk(){
        friendsListChosen = getUserChosenFromAdapter();
        relativeLayoutFriendsPopUp.setVisibility(View.GONE);
        sendInvitationToJoinGroup();
        Log.v("∆∆ GIFragmentGroupMenu", "onClickOnTextViewOk : " + friendsListChosen );
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

    private void createNewEvent(){
        Intent intent = new Intent(getContext(), GIActivityCreateEvent.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GIActivityCreateEvent.GROUP_ID,
                GIApplicationDelegate.getInstance().getDataCache().userGroupsList.get(groupId)
        );
        intent.putExtra(GIActivityCreateEvent.BUNDLE_ID, bundle);
        getContext().startActivity(intent);
    }

    @Override
    public void onRefresh() {
        volleyHandler.getEventsOfUser(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid());
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(eventsAdapter != null)
            eventsAdapter.refreshList(GIApplicationDelegate.getInstance().getDataCache().getArrayEventFromGroup(getCurrentGroupId()));
        else */initRecyclerView();
    }


    @Override
    public void onClick(View view) {
        if( view == fabAddEvent ){
            onClickFabAddEvent();
        }

        else if( view == textViewCancel ){
            onClickOnTextViewCancel();
        }

        else if( view == textViewOk ){
            onClickOnTextViewOk();
        }

    }

    @Override
    public void onClickBubble(int position) {

    }

    @Override
    public void onClickAdd() {
        relativeLayoutFriendsPopUp.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        GIApplicationDelegate.getInstance().onRequestFinishWithSuccess(request_code, object);
        if(request_code == GIRequestData.GET_EVENTS_USER_CODE){
            swipeRefreshLayout.setRefreshing(false);
            initRecyclerView();
//            if(eventsAdapter != null)
//                eventsAdapter.refreshList(GIApplicationDelegate.getInstance().getDataCache().getArrayEventFromGroup(getCurrentGroupId()));
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
