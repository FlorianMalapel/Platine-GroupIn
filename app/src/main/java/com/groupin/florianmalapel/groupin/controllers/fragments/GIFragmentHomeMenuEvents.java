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

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityCreateEvent;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewEventsList;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuEvents extends Fragment implements View.OnClickListener {

    private ArrayList<GIEvent> list_events = null;
    private RecyclerView recyclerViewEvents = null;
    private GIAdapterRecyclerViewEventsList eventsAdapter = null;
    private FloatingActionButton fabAddEvent = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu_events, container, false);
        findViewById(view);
        fixture();
        initViews();
        setListeners();
        return view;
    }

    private void findViewById(View view) {
        recyclerViewEvents = (RecyclerView) view.findViewById(R.id.recyclerViewEvents);
        fabAddEvent = (FloatingActionButton) view.findViewById(R.id.fabAddEvent);
    }

    private void initViews(){
        initRecyclerView();
        fabAddEvent.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }

    private void setListeners(){
        fabAddEvent.setOnClickListener(this);
    }

    private void initRecyclerView(){
        eventsAdapter = new GIAdapterRecyclerViewEventsList(list_events, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewEvents.setLayoutManager(layoutManager);
        recyclerViewEvents.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEvents.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewEvents.setAdapter(eventsAdapter);
    }

    private void fixture(){
        ArrayList<String> participants = new ArrayList<>();
        list_events = new ArrayList<>();
        participants.add("Dany");
        participants.add("John");
        participants.add("Bon");

        GIEvent event = new GIEvent("id", "id_group", "Bon Jovi concert", "Hell yeah", participants, "https://highape.com/wp-content/uploads/2016/12/party.jpg");
        event.date_start = "12/02/2017";
        event.participantsUids = participants;

        GIEvent event2 = new GIEvent("id", "id_group", "Soirée d'anniversaire", "Hell yeah", participants, "http://img.cinemablend.com/cb/b/5/b/b/b/4/b5bbb40234e36535bbe8ccb687ccdbe0de3d5641e7e44b61b1a5bb5973069a5e.jpg");
        event2.date_start = "14/02/2017";
        event2.participantsUids = participants;

        GIEvent event3 = new GIEvent("id", "id_group", "Foot en salle", "Hell yeah", participants, "http://www.aixfootindoor.fr/public/img/big/5193b82b45459.jpg");
        event3.date_start = "14/02/2017";
        event3.participantsUids = participants;

        list_events.add(event);
        list_events.add(event2);
        list_events.add(event3);
        list_events.add(event);
        list_events.add(event2);
        list_events.add(event3);
    }

    private void createNewEvent(){
        Intent intent = new Intent(getContext(), GIActivityCreateEvent.class);
        getContext().startActivity(intent);
    }

    private void onClickFabAddEvent(){
        createNewEvent();
    }

    @Override
    public void onClick(View view) {
        if( view == fabAddEvent ){
            onClickFabAddEvent();
        }
    }
}
