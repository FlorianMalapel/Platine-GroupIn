package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.groupin.florianmalapel.groupin.R;

/**
 * Created by florianmalapel on 04/12/2016.
 */

public class GIFragmentHomeMenuHome extends Fragment {

    private RelativeLayout relativeLayoutContainer = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu_home, container, false);

        findViewById(view);
        initialize();

        return view;

    }


    private void findViewById(View view){
        relativeLayoutContainer = (RelativeLayout) view.findViewById(R.id.relativeLayoutContainer);
    }

    private void initialize(){
    }

}
