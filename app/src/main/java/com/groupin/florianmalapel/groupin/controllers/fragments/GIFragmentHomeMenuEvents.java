package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityCreateEvent;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewEventsList;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.tools.GIDesign;

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuEvents extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerViewEvents = null;
    private GIAdapterRecyclerViewEventsList eventsAdapter = null;
    private TextView textViewMyGroups = null;
    private FloatingActionButton fabAddEvent = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu_events, container, false);
        findViewById(view);
        initViews();
        setListeners();
        return view;
    }

    private void findViewById(View view) {
        recyclerViewEvents = (RecyclerView) view.findViewById(R.id.recyclerViewEvents);
        fabAddEvent = (FloatingActionButton) view.findViewById(R.id.fabAddEvent);
        textViewMyGroups = (TextView) view.findViewById(R.id.textViewMyEvents);
    }

    private void initViews(){
        fabAddEvent.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        textViewMyGroups.setTypeface(GIDesign.getBoldFont(getContext()));
        initRecyclerView();
    }

    private void setListeners(){
        fabAddEvent.setOnClickListener(this);
    }

    private void initRecyclerView(){
        eventsAdapter = new GIAdapterRecyclerViewEventsList(GIApplicationDelegate.getInstance().getDataCache().getArrayEvent(), getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManager);
        recyclerViewEvents.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEvents.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewEvents.setAdapter(eventsAdapter);
    }


    private void createNewEvent(){
        Intent intent = new Intent(getContext(), GIActivityCreateEvent.class);
        getContext().startActivity(intent);
    }

    private void onClickFabAddEvent(){
        createNewEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    public void onClick(View view) {
        if( view == fabAddEvent ){
            onClickFabAddEvent();
        }
    }
}
