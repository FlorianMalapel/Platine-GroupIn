package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityCreatePoll;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityDisplayGroup;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewPolls;
import com.groupin.florianmalapel.groupin.helpers.GIJsonToObjectHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIPoll;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIFragmentGroupMenuPolls extends Fragment
        implements GIVolleyRequest.RequestCallback, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private GIProgressIndicator progressIndicator = null;
    private GIVolleyHandler volleyHandler = null;
    private RecyclerView recyclerViewPolls = null;
    private FloatingActionButton fabAddPoll = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private GIAdapterRecyclerViewPolls adapterRecyclerViewPolls = null;
    private ArrayList<GIPoll> arrayPolls = null;
    private TextView textViewGroupPolls = null;
    private String groupId = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_menu_polls, container, false);
        findViewById(view);
        initialize();
        startRequestGetPolls();
        return view;
    }

    private void findViewById(View view){
        progressIndicator = (GIProgressIndicator) view.findViewById(R.id.progressIndicator);
        recyclerViewPolls = (RecyclerView) view.findViewById(R.id.recyclerViewPolls);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        fabAddPoll = (FloatingActionButton) view.findViewById(R.id.fabAddPoll);
        textViewGroupPolls = (TextView) view.findViewById(R.id.textViewGroupPolls);
    }

    private void initialize(){
        groupId = getArguments().getString(GIActivityDisplayGroup.GROUP_ID);
        volleyHandler = new GIVolleyHandler();
        swipeRefreshLayout.setOnRefreshListener(this);
        fabAddPoll.setOnClickListener(this);
        textViewGroupPolls.setTypeface(GIDesign.getBoldFont(getContext()));
    }

    private void startRequestGetPolls(){
        GIDesign.startRotatingProgressIndicator(progressIndicator);
        volleyHandler.getPollsFromGroup(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid(), groupId);
    }

    private void onRequestGetPollsComeBack(JSONObject object){
        GIDesign.stopRotatingProgressIndicator(progressIndicator);
        swipeRefreshLayout.setRefreshing(false);
        try {
            arrayPolls = GIJsonToObjectHelper.getArrayPollsSortedFromJSON(object, groupId);
        } catch (JSONException e) {
            arrayPolls = new ArrayList<>();
        } catch (Exception e) {
            arrayPolls = new ArrayList<>();
        }
        initRecyclerViewPolls();
    }

    private void initRecyclerViewPolls(){
        adapterRecyclerViewPolls = new GIAdapterRecyclerViewPolls(getContext(), arrayPolls, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPolls.setLayoutManager(layoutManager);
        recyclerViewPolls.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPolls.setAdapter(adapterRecyclerViewPolls);
    }

    @Override
    public void onResume() {
        super.onResume();
        volleyHandler.getPollsFromGroup(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid(), groupId);
    }

    @Override
    public void onClick(View view) {
        if(view == fabAddPoll){
            Intent intent = new Intent(getContext(), GIActivityCreatePoll.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(GIActivityCreatePoll.GROUP_ID, groupId);
            intent.putExtra(GIActivityCreatePoll.BUNDLE_ID, bundle);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        volleyHandler.getPollsFromGroup(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid(), groupId);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        if(request_code == GIRequestData.GET_POLLS_GROUP_CODE)
            onRequestGetPollsComeBack(object);
        else if(request_code == GIRequestData.POST_POLLS_ANSWER_CODE)
            onRequestGetPollsComeBack(object);
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
