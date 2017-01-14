package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groupin.florianmalapel.groupin.R;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIFragmentGroupMenuPolls extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_menu_polls, container, false);
        return view;
    }
}
