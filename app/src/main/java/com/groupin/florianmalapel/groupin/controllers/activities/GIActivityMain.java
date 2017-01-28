package com.groupin.florianmalapel.groupin.controllers.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.fragments.GIFragmentHomeMenuEvents;
import com.groupin.florianmalapel.groupin.controllers.fragments.GIFragmentHomeMenuFriends;
import com.groupin.florianmalapel.groupin.controllers.fragments.GIFragmentHomeMenuGroups;
import com.groupin.florianmalapel.groupin.controllers.fragments.GIFragmentHomeMenuHome;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;
import com.squareup.picasso.Picasso;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import org.json.JSONObject;

public class GIActivityMain extends AppCompatActivity
        implements  /*NavigationView.OnNavigationItemSelectedListener,*/
                    OnSelectedItemChangeListener,
                    ViewPager.OnPageChangeListener,
                    View.OnClickListener,
                    GIVolleyRequest.RequestCallback {

    private static final int NB_TAB_BOTTOM_MENU = 4;

//    private DrawerLayout drawer = null;
//    private ActionBarDrawerToggle toggle = null;
//    private NavigationView navigationView = null;
    private Toolbar toolbar = null;
    private ViewPager viewPagerBottomMenu = null;
    private BottomNavigation bottomNavigation = null;
    private CustomPagerAdapter customPagerAdapter = null;
    private ImageView imageViewMenu = null;
    private ImageButton imageButtonProfile = null;
    private TextView textViewTitle = null;
    private GIVolleyHandler volleyHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByID();
        setTitleToolbar("Accueil");
        initialize();
        initializeViews();
        setListeners();
        volleyHandler.getUser(GIApplicationDelegate.getInstance().getDataCache().getUserUid(), this);
//        volleyHandler.getAllUsers(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_credits) {
//
//        } else if (id == R.id.nav_help) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    private void findViewByID(){
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPagerBottomMenu = (ViewPager) findViewById(R.id.viewPagerBottomMenu);
        bottomNavigation = (BottomNavigation) findViewById(R.id.bottomNavigation);
//        imageViewMenu = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewMenu);
        imageButtonProfile = (ImageButton) findViewById(R.id.imageButtonProfile);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
    }

    private void initialize(){
        volleyHandler = new GIVolleyHandler();
        setSupportActionBar(toolbar);
//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        viewPagerBottomMenu.setAdapter(customPagerAdapter);
    }

    private void initializeViews(){
        imageButtonProfile.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//        textViewTitle.setX(textViewTitle.getX() - (toggle.getDrawerArrowDrawable().getMinimumWidth() * 2));
    }

    private void setUserPhotoInMenu(){
        if(GIApplicationDelegate.getInstance().getDataCache().user.photoURL == null)
            return;

        Picasso.with(this)
                .load(GIApplicationDelegate.getInstance().getDataCache().user.photoURL)
                .transform(new CircleTransform()).into(imageViewMenu);
    }

    private void setListeners(){
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigation.setOnSelectedItemChangeListener(this);
        viewPagerBottomMenu.addOnPageChangeListener(this);
        imageButtonProfile.setOnClickListener(this);
    }

    private void setTitleToolbar(String title){
        textViewTitle.setText(title);
    }

    private void launchActivityProfile(){
        Intent intent = new Intent(this, GIActivityProfile.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if( view == imageButtonProfile ){
            launchActivityProfile();
        }
    }

    @Override
    public void onSelectedItemChanged(int i) {
        if( i == R.id.tab_home ){
            viewPagerBottomMenu.setCurrentItem(0);
        }

        else if( i == R.id.tab_groups ){
            viewPagerBottomMenu.setCurrentItem(1);
        }

        else if( i == R.id.tab_events ){
            viewPagerBottomMenu.setCurrentItem(2);
        }

        else if( i == R.id.tab_friends ){
            viewPagerBottomMenu.setCurrentItem(3);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigation.getChildAt(position).performClick();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class CustomPagerAdapter extends FragmentStatePagerAdapter {

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if( position == 0 ) {
                return new GIFragmentHomeMenuHome();
            }

            else if( position == 1 ) {
                return new GIFragmentHomeMenuGroups();
            }

            else if( position == 2 ) {
                return new GIFragmentHomeMenuEvents();
            }

            else if( position == 3 ) {
                return new GIFragmentHomeMenuFriends();
            }


            return new GIFragmentHomeMenuHome();
        }

        @Override
        public int getCount() {
            return NB_TAB_BOTTOM_MENU;
        }
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        GIApplicationDelegate.getInstance().onRequestFinishWithSuccess(request_code, object);

        if(request_code == GIRequestData.GET_USER_MY_CODE)
            setUserPhotoInMenu();
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
