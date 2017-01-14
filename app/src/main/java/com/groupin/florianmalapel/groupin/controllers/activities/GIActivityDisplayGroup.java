package com.groupin.florianmalapel.groupin.controllers.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.fragments.GIFragmentGroupMenuChat;
import com.groupin.florianmalapel.groupin.controllers.fragments.GIFragmentGroupMenuEvents;
import com.groupin.florianmalapel.groupin.controllers.fragments.GIFragmentGroupMenuMoney;
import com.groupin.florianmalapel.groupin.controllers.fragments.GIFragmentGroupMenuPolls;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIActivityDisplayGroup extends AppCompatActivity
        implements  OnSelectedItemChangeListener,
                    ViewPager.OnPageChangeListener {

    private static final int NB_TAB_BOTTOM_MENU = 4;


    private BottomNavigation bottomNavigationMenu = null;
    private ViewPager viewPagerBottomMenuGroup = null;
    private CustomPagerAdapter customPagerAdapter = null;
    private TextView textView_groupName = null;
    private GIGroup group = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_group);
        findViewsByIds();
        initialize();
        setListeners();
    }

    private void findViewsByIds(){
        bottomNavigationMenu = (BottomNavigation) findViewById(R.id.bottomNavigation);
        viewPagerBottomMenuGroup = (ViewPager) findViewById(R.id.viewPagerBottomMenuGroup);
        textView_groupName = (TextView) findViewById(R.id.textView_groupName);
    }

    private void initialize(){
        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        viewPagerBottomMenuGroup.setAdapter(customPagerAdapter);
    }

    private void setListeners(){
        bottomNavigationMenu.setOnSelectedItemChangeListener(this);
        viewPagerBottomMenuGroup.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationMenu.getChildAt(position).performClick();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSelectedItemChanged(int i) {
        if( i == R.id.tab_homeGroup ){
            viewPagerBottomMenuGroup.setCurrentItem(0);
        }

        else if( i == R.id.tab_chat ){
            viewPagerBottomMenuGroup.setCurrentItem(1);
        }

        else if( i == R.id.tab_money ){
            viewPagerBottomMenuGroup.setCurrentItem(2);
        }

        else if( i == R.id.tab_polls ){
            viewPagerBottomMenuGroup.setCurrentItem(3);
        }

    }

    private class CustomPagerAdapter extends FragmentStatePagerAdapter {

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if( position == 0 ) {
                return new GIFragmentGroupMenuEvents();
            }

            else if( position == 1 ) {
                return new GIFragmentGroupMenuChat();
            }

            else if( position == 2 ) {
                return new GIFragmentGroupMenuPolls();
            }

            else if( position == 3 ) {
                return new GIFragmentGroupMenuMoney();
            }


            return new GIFragmentGroupMenuEvents();
        }

        @Override
        public int getCount() {
            return NB_TAB_BOTTOM_MENU;
        }
    }

}
