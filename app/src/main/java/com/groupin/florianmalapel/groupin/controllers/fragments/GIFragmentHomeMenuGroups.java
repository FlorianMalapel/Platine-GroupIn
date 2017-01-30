package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityCreateGroup;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewGroupsList;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.tools.GIDesign;

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuGroups extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerViewGroups = null;
    private FloatingActionButton fabAddGroup = null;
    private TextView textViewMyGroups = null;
    private GIAdapterRecyclerViewGroupsList groupsAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu_groups, container, false);
        findViewById(view);
        initViews();
        setListeners();
        return view;
    }

    private void findViewById(View view){
        recyclerViewGroups = (RecyclerView) view.findViewById(R.id.recyclerViewGroups);
        fabAddGroup = (FloatingActionButton) view.findViewById(R.id.fabAddGroup);
        textViewMyGroups = (TextView) view.findViewById(R.id.textViewMyEvents);
    }

    private void initRecyclerView(){
        groupsAdapter = new GIAdapterRecyclerViewGroupsList(GIApplicationDelegate.getInstance().getDataCache().userGroupsList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewGroups.setLayoutManager(layoutManager);
        recyclerViewGroups.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGroups.setAdapter(groupsAdapter);
    }

    private void initViews(){
        fabAddGroup.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        textViewMyGroups.setTypeface(GIDesign.getBoldFont(getContext()));
        initRecyclerView();
    }

    private void setListeners(){
        fabAddGroup.setOnClickListener(this);
    }

    private void createNewGroup(){
        Intent intent = new Intent(getContext(), GIActivityCreateGroup.class);
        getContext().startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if(view == fabAddGroup){
            createNewGroup();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
    }
}
