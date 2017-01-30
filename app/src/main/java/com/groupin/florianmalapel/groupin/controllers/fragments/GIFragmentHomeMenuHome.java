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

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuHome extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerViewNotifsFriends = null;
    private RecyclerView recyclerViewNotifsGroups = null;
    private GIAdapterRecyclerViewNotifsFriend notifsFriendAdapter = null;
    private GIAdapterRecyclerViewNotifsGroup notifsGroupAdapter = null;
    private TextView textViewNotifsFriends = null;
    private TextView textViewNotifsGroups = null;

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
        initViewNotificationFriends();
        initViewNotificationGroups();
    }

    private void setListeners(){
        textViewNotifsFriends.setOnClickListener(this);
        textViewNotifsGroups.setOnClickListener(this);
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
    }

    private void initialize(){

    }

    private void onClickOnTextViewNotifsFriend(){
        if(!GIApplicationDelegate.getInstance().getDataCache().notifsFriendList.isEmpty()
                && recyclerViewNotifsFriends.getVisibility() == View.GONE){
            recyclerViewNotifsFriends.setVisibility(View.VISIBLE);
            textViewNotifsFriends.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_arrow_up), null);
        }
        else if(!GIApplicationDelegate.getInstance().getDataCache().notifsFriendList.isEmpty()
                && recyclerViewNotifsFriends.getVisibility() == View.VISIBLE){
            recyclerViewNotifsFriends.setVisibility(View.GONE);
            textViewNotifsFriends.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_arrow_down), null);
        }
    }

    private void onClickOnTextViewNotifsGroup(){
        if(!GIApplicationDelegate.getInstance().getDataCache().notifsGroupList.isEmpty()
                && recyclerViewNotifsGroups.getVisibility() == View.GONE){
            recyclerViewNotifsGroups.setVisibility(View.VISIBLE);
            textViewNotifsGroups.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_arrow_up), null);
        }
        else if(!GIApplicationDelegate.getInstance().getDataCache().notifsGroupList.isEmpty()
                && recyclerViewNotifsGroups.getVisibility() == View.VISIBLE){
            recyclerViewNotifsGroups.setVisibility(View.GONE);
            textViewNotifsGroups.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_keyboard_arrow_down), null);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == textViewNotifsFriends){
            onClickOnTextViewNotifsFriend();
        }

        else if(view == textViewNotifsGroups){
            onClickOnTextViewNotifsGroup();
        }
    }
}
