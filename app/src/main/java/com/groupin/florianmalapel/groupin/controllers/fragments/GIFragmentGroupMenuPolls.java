package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groupin.florianmalapel.groupin.R;
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

public class GIFragmentGroupMenuPolls extends Fragment implements GIVolleyRequest.RequestCallback {

    private GIProgressIndicator progressIndicator = null;
    private GIVolleyHandler volleyHandler = null;
    private RecyclerView recyclerViewPolls = null;
    private GIAdapterRecyclerViewPolls adapterRecyclerViewPolls = null;
    private ArrayList<GIPoll> arrayPolls = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_menu_polls, container, false);
        findViewById(view);
        initialize();
        startRequestGetPolls();
//        initializePollView();
        return view;
    }

    private void findViewById(View view){
        progressIndicator = (GIProgressIndicator) view.findViewById(R.id.progressIndicator);
        recyclerViewPolls = (RecyclerView) view.findViewById(R.id.recyclerViewPolls);
    }

    private void initialize(){
        volleyHandler = new GIVolleyHandler();
    }

    private void startRequestGetPolls(){
        GIDesign.startRotatingProgressIndicator(progressIndicator);
        volleyHandler.getPollsFromGroup(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid(), ((GIActivityDisplayGroup)getContext()).groupId);
    }

    private void onRequestGetPollsComeBack(JSONObject object){
        GIDesign.stopRotatingProgressIndicator(progressIndicator);
        try {
            arrayPolls = GIJsonToObjectHelper.getArrayPollsFromJSON(object, ((GIActivityDisplayGroup)getContext()).groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("-- °°-- °°--", arrayPolls.toString());
        initRecyclerViewPolls();
    }

    private void initRecyclerViewPolls(){
        adapterRecyclerViewPolls = new GIAdapterRecyclerViewPolls(getContext(), arrayPolls);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPolls.setLayoutManager(layoutManager);
        recyclerViewPolls.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPolls.setAdapter(adapterRecyclerViewPolls);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        if(request_code == GIRequestData.GET_POLLS_GROUP_CODE)
            onRequestGetPollsComeBack(object);
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
