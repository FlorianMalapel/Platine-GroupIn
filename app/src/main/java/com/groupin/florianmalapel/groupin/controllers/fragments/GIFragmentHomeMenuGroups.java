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

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityCreateGroup;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewGroupsList;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuGroups extends Fragment implements View.OnClickListener {

    private ArrayList<GIGroup> list_groups = null;
    private RecyclerView recyclerViewGroups = null;
    private FloatingActionButton fabAddGroup = null;
    private GIAdapterRecyclerViewGroupsList groupsAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu_groups, container, false);
        findViewById(view);
        fixture();
        initViews();
        setListeners();
        return view;
    }

    private void findViewById(View view){
        recyclerViewGroups = (RecyclerView) view.findViewById(R.id.recyclerViewGroups);
        fabAddGroup = (FloatingActionButton) view.findViewById(R.id.fabAddGroup);
    }

    private void initRecyclerView(){
        groupsAdapter = new GIAdapterRecyclerViewGroupsList(list_groups, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewGroups.setLayoutManager(layoutManager);
        recyclerViewGroups.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGroups.setAdapter(groupsAdapter);
    }

    private void initViews(){
        fabAddGroup.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
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

    private void fixture(){
        ArrayList<String> uids = new ArrayList<>();
        uids.add("ole");
        uids.add("daniel");
        uids.add("sandoval");
        uids.add("west");
        uids.add("world");

        list_groups = new ArrayList<>();
        GIGroup group = new GIGroup("#az", "Douai street", "http://fantasynscifi.com/wp-content/uploads/2016/05/yoda.jpg", "description");
        group.membersUids = uids;

        GIGroup group2 = new GIGroup("#az", "Anniversaire Thomas", "http://img.over-blog-kiwi.com/0/99/19/27/20150325/ob_d7ff31_20150215-131050.jpg", "description");
        group2.membersUids = uids;

        GIGroup group3 = new GIGroup("#az", "Foot", "http://hildasjcr.org.uk/wp-content/uploads/2015/07/Football.png", "description");
        group3.membersUids = uids;

        GIGroup group4 = new GIGroup("#az", "Famille", "https://verbstomp.com/wp-content/uploads/2015/11/rick-and-morty.jpg", "description");
        group4.membersUids = uids;

        GIGroup group5 = new GIGroup("#az", "Coloc", "http://www.plan-immobilier.fr/images/thumbnail/thumb_programme/images/programmes/2575/15401_0.jpg", "description");
        group5.membersUids = uids;

        list_groups.add(group);
        list_groups.add(group2);
        list_groups.add(group3);
        list_groups.add(group4);
        list_groups.add(group5);


    }
}
