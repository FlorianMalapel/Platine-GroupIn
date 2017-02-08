package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityCreateBill;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityDisplayGroup;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewBillsList;
import com.groupin.florianmalapel.groupin.helpers.GIJsonToObjectHelper;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIBill;
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

public class GIFragmentGroupMenuMoney extends Fragment
        implements GIVolleyRequest.RequestCallback,
                    SwipeRefreshLayout.OnRefreshListener,
                    View.OnClickListener {

    private GIVolleyHandler volleyHandler = null;
    private FloatingActionButton fabAddBill = null;
    private RecyclerView recyclerViewBills = null;
    private GIAdapterRecyclerViewBillsList adapterBillList = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private GIProgressIndicator progressIndicator = null;
    private ArrayList<GIBill> billsArray = null;
    private String groupId = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_menu_money, container, false);

        initialize(view);
        startRequestGetBills();
        return view;
    }

    private void initialize(View view) {
        findViewById(view);
        initializeObjects();
        initializeViews();
        setListeners();
    }

    private void findViewById(View view) {
        fabAddBill = (FloatingActionButton) view.findViewById(R.id.fabAddBill);
        progressIndicator = (GIProgressIndicator) view.findViewById(R.id.progressIndicator);
        recyclerViewBills = (RecyclerView) view.findViewById(R.id.recyclerViewBills);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    }

    private void initializeObjects() {
        groupId = getArguments().getString(GIActivityDisplayGroup.GROUP_ID);
        volleyHandler = new GIVolleyHandler();
    }

    private void initializeViews() {
        fabAddBill.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }

    private void initRecyclerView(){
        adapterBillList = new GIAdapterRecyclerViewBillsList(billsArray, getContext(), this, groupId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewBills.setLayoutManager(layoutManager);
        recyclerViewBills.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBills.setAdapter(adapterBillList);
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        fabAddBill.setOnClickListener(this);
    }

    private void startRequestGetBills(){
        volleyHandler.getBills(this, groupId);
        GIDesign.startRotatingProgressIndicator(progressIndicator);
    }

    private void onGetBillsRequestComeBack(JSONObject object){
        try {
            billsArray = GIJsonToObjectHelper.getBillsArrayFromJSON(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initRecyclerView();
        GIDesign.stopRotatingProgressIndicator(progressIndicator);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        volleyHandler.getBills(this, groupId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapterBillList != null)
            adapterBillList.refreshList(billsArray);
        else initRecyclerView();
    }

    @Override
    public void onClick(View view) {
        if(view == fabAddBill){
            Intent intent = new Intent(getContext(), GIActivityCreateBill.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("groupId", groupId);
            intent.putExtra("bundle", bundle);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        if(request_code == GIRequestData.GET_BILLS_GROUP_CODE || request_code == GIRequestData.DELETE_BILLS_GROUP_CODE){
            onGetBillsRequestComeBack(object);
        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
