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
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.adapters.GIAdapterRecyclerViewFriends;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuFriends extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerViewFriends = null;
    private FloatingActionButton fabAddFriend = null;
    private RelativeLayout relativeLayoutFormAddFiend = null;
    private RelativeLayout relativeLayoutFriendInfo = null;
    private EditText editTextMailAddress = null;
    private CircleImageView circularImageViewProfilPic = null;
    private TextView textViewNameFriend = null;
    private Button buttonAddFriendCancel = null;
    private Button buttonAddFriendOk = null;
    private ArrayList<GIUser> friendsList = null;
    private GIAdapterRecyclerViewFriends friendsAdapter = null;

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
        circularImageViewProfilPic = (CircleImageView) view.findViewById(R.id.circularImageViewProfilPic);
    }

    private void initialize(){
        friendsList = new ArrayList<>();
        for(String key : GIApplicationDelegate.getInstance().getDataCache().allUsersList.keySet()){
            friendsList.add(GIApplicationDelegate.getInstance().getDataCache().allUsersList.get(key));
            Log.w("∆∆∆ ∆∆∆ ∆∆∆", friendsList.get(friendsList.size()-1).toString());
        }
    }

    private void initViews(){
        fabAddFriend.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initRecyclerView();
    }

    private void setListeners(){
        fabAddFriend.setOnClickListener(this);
        buttonAddFriendCancel.setOnClickListener(this);
        buttonAddFriendOk.setOnClickListener(this);
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
    }

    private void onClickToCancelFriendship(){
        relativeLayoutFormAddFiend.setVisibility(View.GONE);
    }

    private void sortFriendsListAlphabetically(ArrayList<GIUser> friendsList){
        Collections.sort(friendsList, new SortedListAdapterCallback<GIUser>(null) {
            @Override
            public int compare(GIUser o1, GIUser o2) {
                return o1.lastName.compareTo(o2.lastName);
            }

            @Override
            public boolean areContentsTheSame(GIUser oldItem, GIUser newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(GIUser item1, GIUser item2) {
                return false;
            }
        });
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
}
