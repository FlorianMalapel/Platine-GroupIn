package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewFriends;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuFriends extends Fragment implements View.OnClickListener, TextWatcher {

    private RecyclerView recyclerViewFriends = null;
    private FloatingActionButton fabAddFriend = null;
    private RelativeLayout relativeLayoutFormAddFiend = null;
    private RelativeLayout relativeLayoutFriendInfo = null;
    private EditText editTextMailAddress = null;
    private ImageView circularImageViewProfilPic = null;
    private TextView textViewNameFriend = null;
    private Button buttonAddFriendCancel = null;
    private Button buttonAddFriendOk = null;
    private ArrayList<GIUser> friendsList = null;
    private GIAdapterRecyclerViewFriends friendsAdapter = null;
    private GIUser userSelected = null;
    private GIVolleyHandler volleyHandler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu_friends, container, false);
//        fixture();
        findViewById(view);
        initialize();
        initViews();
        setListeners();
        return view;
    }

    private void findViewById(View view){
        recyclerViewFriends = (RecyclerView) view.findViewById(R.id.recyclerViewFriends);
        fabAddFriend = (FloatingActionButton) view.findViewById(R.id.fabAddFriend);
        buttonAddFriendCancel = (Button) view.findViewById(R.id.buttonAddFriendCancel);
        buttonAddFriendOk = (Button) view.findViewById(R.id.buttonAddFriendOk);
        relativeLayoutFormAddFiend = (RelativeLayout) view.findViewById(R.id.relativeLayoutFormAddFiend);
        relativeLayoutFriendInfo = (RelativeLayout) view.findViewById(R.id.relativeLayoutFriendInfo);
        editTextMailAddress = (EditText) view.findViewById(R.id.editTextMailAddress);
        textViewNameFriend = (TextView) view.findViewById(R.id.textViewNameFriend);
        circularImageViewProfilPic = (ImageView) view.findViewById(R.id.circularImageViewProfilPic);
    }

    private void initialize(){
        friendsList = new ArrayList<>();

        if(GIApplicationDelegate.getInstance().getDataCache().userFriendList == null)
            return;

        for(String key : GIApplicationDelegate.getInstance().getDataCache().userFriendList.keySet()){
            friendsList.add(GIApplicationDelegate.getInstance().getDataCache().userFriendList.get(key));
        }

        volleyHandler = new GIVolleyHandler();
    }

    private void initViews(){
        fabAddFriend.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initRecyclerView();
    }

    private void setListeners(){
        fabAddFriend.setOnClickListener(this);
        buttonAddFriendCancel.setOnClickListener(this);
        buttonAddFriendOk.setOnClickListener(this);
        editTextMailAddress.addTextChangedListener(this);
    }

    private void initRecyclerView(){
        friendsAdapter = new GIAdapterRecyclerViewFriends(friendsList, false, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewFriends.setLayoutManager(mLayoutManager);
        recyclerViewFriends.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFriends.setAdapter(friendsAdapter);
    }

//    private void fixture(){
//        friendsList = new ArrayList<>();
//        friendsList.add(new GIUser("marty@mc.fly", "01", "McFly", "McFly", "Marty"));
//        friendsList.add(new GIUser("george@mc.fly", "02", "McFly", "McFly", "George"));
//        friendsList.add(new GIUser("daniel@gmail.com", "03", "Daniel", "Sandoval", "Daniel"));
//        friendsList.add(new GIUser("biff@tanon.ws", "04", "Biff", "Tanon", "Biff"));
//        friendsList.add(new GIUser("zoé@dubois.ws", "05", "Zoé", "Dubois", "Zoé"));
//        friendsList.add(new GIUser("edouard@delbove.sj", "06", "Edouard", "Delbove", "Edouard"));
//        friendsList.add(new GIUser("guitou@blank.sj", "07", "Guitou", "Blank", "Guillaume"));
//        friendsList.add(new GIUser("elodie@girot.sj", "08", "Elodie", "Girot", "Elodie"));
//        friendsList.add(new GIUser("alan@turing.ws", "09", "Alan", "Turing", "Alan"));
//        friendsList.add(new GIUser("shledon@cooper.tbbt", "10", "Sheldon", "Cooper", "Sheldon"));
//        sortFriendsListAlphabetically(friendsList);
//    }

    private void onClickToValidateFriendship(){
        relativeLayoutFormAddFiend.setVisibility(View.GONE);
        if(userSelected == null)
            return;
        volleyHandler.postFriendShip(GIApplicationDelegate.getInstance(), GIApplicationDelegate.getInstance().getDataCache().getUserUid(), userSelected.uid);
    }

    private void onClickToCancelFriendship(){
        relativeLayoutFormAddFiend.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if( view == fabAddFriend ) {
            relativeLayoutFormAddFiend.setVisibility(View.VISIBLE);
        }

        else if( view == buttonAddFriendCancel ) {
            onClickToCancelFriendship();
        }

        else if( view == buttonAddFriendOk ) {
            onClickToValidateFriendship();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        userSelected = GIApplicationDelegate.getInstance().getDataCache().findUserFromAll(charSequence.toString());
        if(userSelected != null){
            textViewNameFriend.setText(userSelected.display_name);
            Picasso.with(getContext()).load(userSelected.photoURL).transform(new CircleTransform()).into(circularImageViewProfilPic);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
